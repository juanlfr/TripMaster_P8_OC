package com.tripmaster.microservice.tourguide;

import com.tripmaster.microservice.tourguide.beans.AttractionBean;
import com.tripmaster.microservice.tourguide.beans.VisitedLocationBean;
import com.tripmaster.microservice.tourguide.beans.user.User;
import com.tripmaster.microservice.tourguide.helpers.InternalTestHelper;
import com.tripmaster.microservice.tourguide.proxies.MicroserviceGpsProxy;
import com.tripmaster.microservice.tourguide.services.RewardsService;
import com.tripmaster.microservice.tourguide.services.TourGuideService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.StopWatch;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class TestPerformance {
    @Autowired
    TourGuideService tourGuideService;
    @Autowired
    RewardsService rewardsService;
    @Autowired
    MicroserviceGpsProxy microserviceGpsProxy;
    @Test
    public void highVolumeTrackLocation() throws ExecutionException, InterruptedException {
        InternalTestHelper.setInternalUserNumber(10000);
        InternalTestHelper.initializeInternalUsers();
        List<User> allUsers = tourGuideService.getAllUsers();

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        allUsers.stream().parallel().forEach(user -> {
            try {
                tourGuideService.trackUserLocation(user);
            } catch (ExecutionException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        stopWatch.stop();
        System.out.println("highVolumeGetRewards: Time Elapsed: " + stopWatch.getTotalTimeSeconds() + " seconds.");
        assertTrue(TimeUnit.MINUTES.toSeconds(15) >= stopWatch.getTotalTimeSeconds());

    }
    @Test
    public void highVolumeGetRewards() {
        InternalTestHelper.setInternalUserNumber(100);

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        AttractionBean attraction = microserviceGpsProxy.getAttractions().get(0);
        List<User> allUsers = tourGuideService.getAllUsers();
        allUsers.forEach(u -> {
            u.addToVisitedLocations(new VisitedLocationBean(u.getUserId(), attraction, new Date()));
            System.out.println("**************** u.addToVisitedLocations IN TEST THREAD *********** ");
        });
        allUsers.forEach(rewardsService::calculateRewards);

        for(User user : allUsers) {
            assertTrue(user.getUserRewards().size() > 0);
        }
        stopWatch.stop();
        assertTrue(TimeUnit.MINUTES.toSeconds(20) >= stopWatch.getTotalTimeSeconds());
    }
}
