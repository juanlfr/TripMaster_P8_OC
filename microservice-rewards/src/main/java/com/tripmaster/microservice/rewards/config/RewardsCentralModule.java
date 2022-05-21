package com.tripmaster.microservice.rewards.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import rewardCentral.RewardCentral;

@Configuration
public class RewardsCentralModule {
    @Bean
    RewardCentral getInstance() {
        return new RewardCentral();
    }
}
