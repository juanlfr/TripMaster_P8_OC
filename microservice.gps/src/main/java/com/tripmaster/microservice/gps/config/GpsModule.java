package com.tripmaster.microservice.gps.config;

import gpsUtil.GpsUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GpsModule {
    @Bean
    GpsUtil getInstance(){
        return new GpsUtil();
    }
}
