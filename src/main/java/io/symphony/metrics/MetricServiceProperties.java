package io.symphony.metrics;

import java.util.Set;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import io.symphony.common.selector.SelectorPropertiesLoader;
import lombok.Data;

@Configuration
@ConfigurationProperties(prefix = "metrics")
@Data
public class MetricServiceProperties {

	private SelectorPropertiesLoader selector;
	
	private Set<String> labels;
	
}
