package com.tripmaster.microservice.tourguide;

import com.tripmaster.microservice.tourguide.beans.VisitedLocationBean;
import com.tripmaster.microservice.tourguide.beans.user.User;

import com.tripmaster.microservice.tourguide.helpers.InternalTestHelper;
import com.tripmaster.microservice.tourguide.services.RewardsService;
import com.tripmaster.microservice.tourguide.services.TourGuideService;

import jdk.nashorn.internal.ir.annotations.Ignore;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
public class TourGuideServiceTest {

    @Autowired
    TourGuideService tourGuideService;

    @Test
    public void getUserLocation() throws ExecutionException, InterruptedException {

        User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
        VisitedLocationBean visitedLocation = tourGuideService.trackUserLocation(user);
        //tourGuideService.tracker.stopTracking();
        assertEquals(visitedLocation.userId, user.getUserId());
    }
    @Test
    public void addUser() {

        User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
        User user2 = new User(UUID.randomUUID(), "jon2", "000", "jon2@tourGuide.com");

        tourGuideService.addUser(user);
        tourGuideService.addUser(user2);

        User retrivedUser = tourGuideService.getUser(user.getUserName());
        User retrivedUser2 = tourGuideService.getUser(user2.getUserName());

//        tourGuideService.tracker.stopTracking();
        assertEquals(user, retrivedUser);
        assertEquals(user2, retrivedUser2);
    }
//    @Test
//    @Ignore
//    public void getAllUsers() {
//
//
//        User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
//        User user2 = new User(UUID.randomUUID(), "jon2", "000", "jon2@tourGuide.com");
//
//        tourGuideService.addUser(user);
//        tourGuideService.addUser(user2);
//
//        List<User> allUsers = tourGuideService.getAllUsers();
//
////        tourGuideService.tracker.stopTracking();
//
//        assertTrue(allUsers.contains(user));
//        assertTrue(allUsers.contains(user2));
//    }

    @Test
    public void getVisitedUserLocation() throws ExecutionException, InterruptedException {

        User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");

        VisitedLocationBean visitedLocationBean = tourGuideService.trackUserLocation(user);

        System.out.println(visitedLocationBean);

        assertNotNull(visitedLocationBean);

    }


}
