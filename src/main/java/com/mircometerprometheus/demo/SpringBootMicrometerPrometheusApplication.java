package com.mircometerprometheus.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import io.micrometer.core.aop.TimedAspect;
import io.micrometer.core.instrument.MeterRegistry;

@SpringBootApplication
public class SpringBootMicrometerPrometheusApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootMicrometerPrometheusApplication.class, args);
	}
	
	/**
	 * This is required so that we can use the @Timed annotation
	 * on methods that we want to time.
	 * See: https://micrometer.io/docs/concepts#_the_timed_annotation
	 */
	@Bean
	public TimedAspect timedAspect(MeterRegistry registry) {
		return new TimedAspect(registry);
	}

}
