package io.symphony.metrics;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.Meter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import io.symphony.common.point.data.Point;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MetricsService {

	private Logger logger = LoggerFactory.getLogger(getClass());

	private final MeterRegistry meterRegistry;
	
	private final MetricServiceProperties props;

	private Map<String, PointWrapper> cache = new HashMap<>();

	public void update(Point point) {
		// Check if wrapper already in cache
		String pointId = point.getId();
		PointWrapper cached = cache.get(pointId);
		
		// Replace the point in the wrapper to update the value
		if (cached != null) {
			logger.debug("Updating value for {}", point.getId());
			cached.setPoint(point);
			
		// Create the wrapper, cache it and register Meter
		} else {
			PointWrapper pointMeter = PointWrapper.from(point.getId(), point);
			// Don't register Meters that will return null
			if (pointMeter.getMetricValue() == null) {
				logger.debug("Not going to register point with null value: {}", point.getId());
				return;
			}
			logger.debug("Registering point {}", point.getId());
			register(pointMeter, meterRegistry);
			cache.put(pointId, pointMeter);
		}
	}
	
	public Meter register(PointWrapper pointMeter, MeterRegistry registry) {
		Set<String> labels = props.getLabels();
		
		// Build tags. Metrics with the same name, must have the same set of labels or
		// Prometheus will not display them. Currently, we hardcode the labels that will
		// be added and use empty strings when the label doesn't exist.
		Set<Tag> tags = new HashSet<>();
		tags.add(Tag.of("pointId", pointMeter.getPoint().getId()));
		for (String name: labels)
			tags.add(Tag.of(name, labelValueOrEmpty(name, pointMeter.getPoint())));
				
		return Gauge.builder(pointMeter.getMetricName(), pointMeter, PointWrapper::getMetricValue)
			.tags(tags)
			.register(registry);
	}
	
	private String labelValueOrEmpty(String name, Point point) {
		String val = null;
		if (point.getLabels() != null)
			val = point.getLabels().get(name);
		return val != null ? val : "";
	}
	
}
