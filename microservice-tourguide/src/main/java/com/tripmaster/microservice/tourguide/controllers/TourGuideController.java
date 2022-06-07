package com.tripmaster.microservice.tourguide.controllers;

import com.jsoniter.output.JsonStream;
import com.tripmaster.microservice.tourguide.beans.Provider;
import com.tripmaster.microservice.tourguide.beans.VisitedLocationBean;
import com.tripmaster.microservice.tourguide.beans.user.User;
import com.tripmaster.microservice.tourguide.dto.UserAllCurrentLocationsDTO;
import com.tripmaster.microservice.tourguide.services.TourGuideService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
public class TourGuideController
{

    private final TourGuideService tourGuideService;
    private Logger logger = LoggerFactory.getLogger(TourGuideController.class);
    @Autowired
    public TourGuideController(TourGuideService tourGuideService) {
        this.tourGuideService = tourGuideService;
    }



    @RequestMapping("/getUserLocation")
    public String getLocation(@RequestParam String userName) {
        VisitedLocationBean visitedLocation = null;
        try {
            visitedLocation = tourGuideService.getUserLocation(getUser(userName));
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return JsonStream.serialize(visitedLocation.location);
    }

    @RequestMapping("/getNearbyAttractions")
    public String getNearbyAttractions(@RequestParam String userName) {
        VisitedLocationBean visitedLocation = null;
        try {
            visitedLocation = tourGuideService.getUserLocation(getUser(userName));
        } catch (ExecutionException | InterruptedException e) {
            logger.error("Error while getting user location" + e);
        }
        return JsonStream.serialize(tourGuideService.getNearByAttractions(visitedLocation, getUser(userName)));
    }
    @RequestMapping("/getRewards")
    public String getRewards(@RequestParam String userName) {
        return JsonStream.serialize(tourGuideService.getUserRewards(getUser(userName)));
    }
    @RequestMapping("/getAllCurrentLocations")
    public String getAllCurrentLocations() {
        List<UserAllCurrentLocationsDTO> userAllCurrentLocationsDTOS = tourGuideService.getAllUsersCurrentLocation();
        return JsonStream.serialize(userAllCurrentLocationsDTOS);
    }

    @RequestMapping("/getTripDeals")
    public String getTripDeals(@RequestParam String userName) {
        List<Provider> providers = tourGuideService.getTripDeals(getUser(userName));
        return JsonStream.serialize(providers);
    }

    private User getUser(String userName) {
        return tourGuideService.getUser(userName);
    }

}
