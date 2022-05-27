package com.tripmaster.microservice.pricer.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tripPricer.Provider;
import tripPricer.TripPricer;

import java.util.List;
import java.util.UUID;

@RestController
public class TripPricerController {
    @Autowired
    TripPricer tripPricer;

    @GetMapping("/getPrice")
    List<Provider> getPrice(@RequestParam String apiKey, @RequestParam UUID attractionId, @RequestParam int adults, @RequestParam int children, @RequestParam int nightsStay, @RequestParam int rewardsPoints ) {
        return tripPricer.getPrice(apiKey,attractionId,adults,children,nightsStay,rewardsPoints);
    }
}
