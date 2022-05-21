package com.tripmaster.microservice.gps.controllers;

import gpsUtil.GpsUtil;
import gpsUtil.location.Attraction;
import gpsUtil.location.VisitedLocation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;



@RestController
public class GpsController {
    @Autowired
    GpsUtil gpsUtil;

    @GetMapping("/getLocation")
    VisitedLocation getUserLocation(@RequestParam("userId") UUID userId) {
        return gpsUtil.getUserLocation(userId);
    }
    @GetMapping("/getAttractions")
    List<Attraction> getAttractions() {
        return gpsUtil.getAttractions();
    }
}
