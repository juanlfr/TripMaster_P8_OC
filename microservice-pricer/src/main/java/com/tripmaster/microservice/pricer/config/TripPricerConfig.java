package com.tripmaster.microservice.pricer.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tripPricer.TripPricer;

@Configuration
public class TripPricerConfig {
    @Bean
    TripPricer getTripPricer() {
        return new TripPricer();
    }
}
