package com.tripmaster.microservice.tourguide;

import com.tripmaster.microservice.tourguide.beans.Provider;
import com.tripmaster.microservice.tourguide.beans.VisitedLocationBean;
import com.tripmaster.microservice.tourguide.beans.user.User;
import com.tripmaster.microservice.tourguide.dto.AttractionDTO;
import com.tripmaster.microservice.tourguide.services.TourGuideService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.UUID;
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

    @Test
    public void getAllUsers() {

        TourGuideService tourGuideService2 = new TourGuideService();

        User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
        User user2 = new User(UUID.randomUUID(), "jon2", "000", "jon2@tourGuide.com");

        tourGuideService2.addUser(user);
        tourGuideService2.addUser(user2);

        List<User> allUsers = tourGuideService2.getAllUsers();

//        tourGuideService.tracker.stopTracking();

        assertTrue(allUsers.contains(user));
        assertTrue(allUsers.contains(user2));
    }

    @Test
    public void getVisitedUserLocation() throws ExecutionException, InterruptedException {

        User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");

        VisitedLocationBean visitedLocationBean = tourGuideService.trackUserLocation(user);

        System.out.println(visitedLocationBean);

        assertNotNull(visitedLocationBean);

    }
    @Test
    public void trackUser() throws ExecutionException, InterruptedException {
        User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
        VisitedLocationBean visitedLocation = tourGuideService.trackUserLocation(user);
        //tourGuideService.tracker.stopTracking();
        assertEquals( user.getUserId(), visitedLocation.userId);

    }
    @Test
    public void getNearbyAttractions() throws ExecutionException, InterruptedException {

        User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
        VisitedLocationBean visitedLocation = tourGuideService.trackUserLocation(user);
        List<AttractionDTO> attractions = tourGuideService.getNearByAttractions(visitedLocation, user);
        //tourGuideService.tracker.stopTracking();
        assertEquals(5, attractions.size());
    }

    @Test
    public void getTripDeals() {


        User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");

        List<Provider> providers = tourGuideService.getTripDeals(user);

//        tourGuideService.tracker.stopTracking();

        assertEquals(5, providers.size());
    }


}
