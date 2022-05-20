package com.tripmaster.microservice.tourguide;

import com.tripmaster.microservice.tourguide.beans.VisitedLocationBean;
import com.tripmaster.microservice.tourguide.beans.user.User;

import com.tripmaster.microservice.tourguide.services.TourGuideService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertNotNull;


@SpringBootTest
public class TourGuideServiceTest {

    @Autowired
    TourGuideService tourGuideService;

    @Test
    public void getVisitedUserLocation() {

        User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");

        VisitedLocationBean visitedLocationBean = tourGuideService.trackUserLocation(user);

        System.out.println(visitedLocationBean.toString());

        assertNotNull(visitedLocationBean);

    }


}
