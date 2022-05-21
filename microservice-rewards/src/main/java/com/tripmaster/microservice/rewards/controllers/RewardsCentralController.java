package com.tripmaster.microservice.rewards.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import rewardCentral.RewardCentral;

import java.util.UUID;

@RestController
public class RewardsCentralController {

    @Autowired
    RewardCentral rewardCentral;

    @GetMapping("/getAttractionRewardPoints")
    int getAttractionRewardPoints(@RequestParam("attractionId") UUID attractionId, @RequestParam("userId") UUID userId) {
        return rewardCentral.getAttractionRewardPoints(attractionId, userId);
    }
}
