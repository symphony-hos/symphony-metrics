package io.symphony.metrics;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.symphony.common.conversion.EnableUnitConversion;
import io.symphony.common.startup.EnableSymphonyStartup;

@SpringBootApplication
@EnableUnitConversion
@EnableSymphonyStartup
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
