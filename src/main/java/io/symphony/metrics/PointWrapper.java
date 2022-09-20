package io.symphony.metrics;

import io.symphony.common.point.data.Point;
import io.symphony.common.point.data.quantity.QuantityPoint;
import io.symphony.common.point.data.state.StatePoint;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Builder
public class PointWrapper {
	
	
	public static PointWrapper from(String id, Point point) {		
		return PointWrapper.builder()
			.id(id)
			.metricName("symphony.point")
			.point(point)
			.build();
	}
		

	private String id;
	
	private String metricName;
	 
	@EqualsAndHashCode.Exclude
	private Point point; 
	
	public Double getMetricValue() {
		if (point == null)
			return null;
		if (point instanceof QuantityPoint) {
			return ((QuantityPoint) point).getValue();
		} else if (point instanceof StatePoint<?>) {
			StatePoint<?> s = (StatePoint<?>) point;
			if (s.getState() == null)
				return null;
			return Double.valueOf(s.getState().asNumber());
		}
		return null;
	}
		
}
