package com.tripmaster.microservice.gps.controllers;

import gpsUtil.GpsUtil;
import gpsUtil.location.Attraction;
import gpsUtil.location.VisitedLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;



@RestController
public class GpsController {

    private Logger logger = LoggerFactory.getLogger(GpsController.class);
    @Autowired
    GpsUtil gpsUtil;
    @Autowired
    private ServerProperties serverProperties;

    @GetMapping("/getLocation")
    VisitedLocation getUserLocation(@RequestParam("userId") UUID userId) {
        logger.info("******MicroserviceGPS getUserLocation from port : " + serverProperties.getPort() );

        return gpsUtil.getUserLocation(userId);
    }
    @GetMapping("/getAttractions")
    List<Attraction> getAttractions() {
        return gpsUtil.getAttractions();
    }
}
