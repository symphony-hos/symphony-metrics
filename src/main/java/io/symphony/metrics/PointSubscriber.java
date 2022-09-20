package io.symphony.metrics;

import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import io.symphony.common.messages.event.PointUpdate;
import io.symphony.common.selector.Selector;

@Service
public class PointSubscriber {

	private Logger logger = LoggerFactory.getLogger(getClass());

	
	private final MetricsService metrics;
	
	private final Selector selector;

	public PointSubscriber(@Autowired MetricsService metrics, @Autowired MetricServiceProperties props) {
		this.metrics = metrics;
		this.selector = props.getSelector().load();
	}

	@Bean
	public Consumer<PointUpdate> processPoint() {
		return (update) -> {
			if (!selector.select(update.getPoint()).isSelected()) {
				logger.debug("Selector filtered point {}. Ignoring.", update.getPoint().getId());
				return;
			}
			metrics.update(update.getPoint());
		};
	}

}