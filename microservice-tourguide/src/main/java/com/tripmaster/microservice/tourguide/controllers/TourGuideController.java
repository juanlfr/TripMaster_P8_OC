package com.tripmaster.microservice.tourguide.controllers;

import com.jsoniter.output.JsonStream;
import com.tripmaster.microservice.tourguide.beans.VisitedLocationBean;
import com.tripmaster.microservice.tourguide.beans.user.User;
import com.tripmaster.microservice.tourguide.services.TourGuideService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;

@RestController
public class TourGuideController
{

    private final TourGuideService tourGuideService;

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

    private User getUser(String userName) {
        return tourGuideService.getUser(userName);
    }

}
