package com.tripmaster.microservice.rewards.controllers;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import rewardCentral.RewardCentral;

import java.util.UUID;

@RestController
public class RewardsCentralController {
    private Logger logger = LoggerFactory.getLogger(RewardsCentralController.class);

    @Autowired
    RewardCentral rewardCentral;

    @GetMapping("/getAttractionRewardPoints")
    int getAttractionRewardPoints(@RequestParam("attractionId") UUID attractionId, @RequestParam("userId") UUID userId) {
        logger.info("******rewardCentral.getAttractionRewardPoints call******");
        return rewardCentral.getAttractionRewardPoints(attractionId, userId);
    }
}
