package com.tripmaster.microservice.tourguide;

import com.tripmaster.microservice.tourguide.beans.AttractionBean;
import com.tripmaster.microservice.tourguide.beans.LocationBean;
import com.tripmaster.microservice.tourguide.beans.VisitedLocationBean;
import com.tripmaster.microservice.tourguide.beans.user.User;
import com.tripmaster.microservice.tourguide.beans.user.UserReward;
import com.tripmaster.microservice.tourguide.proxies.MicroserviceGpsProxy;
import com.tripmaster.microservice.tourguide.services.RewardsService;
import com.tripmaster.microservice.tourguide.services.TourGuideService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ActiveProfiles("test")
@SpringBootTest
public class RewardsServiceTest {
    @Autowired
    RewardsService rewardsService;
    @Autowired
    MicroserviceGpsProxy microserviceGpsProxy;
    @Autowired
    TourGuideService tourGuideService;

    @Test
    public void userGetRewards() throws ExecutionException, InterruptedException {


        User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
        AttractionBean attraction = microserviceGpsProxy.getAttractions().get(0);
        user.addToVisitedLocations(new VisitedLocationBean(user.getUserId(), attraction, new Date()));
        tourGuideService.trackUserLocation(user);
        rewardsService.calculateRewards(user).get();
        List<UserReward> userRewards = user.getUserRewards();
        assertEquals(1, userRewards.size());
    }

    @Test
    public void isWithinAttractionProximity() {
        AttractionBean attraction = microserviceGpsProxy.getAttractions().get(0);
        assertTrue(rewardsService.isWithinAttractionProximity(attraction, attraction));
    }
    @Test
    public void nearAllAttractions() throws ExecutionException, InterruptedException {
        rewardsService.setProximityBuffer(Integer.MAX_VALUE);
        User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
        generateUserLocationHistory(user);
        rewardsService.calculateRewards(user).get();
        assertEquals(microserviceGpsProxy.getAttractions().size(), user.getUserRewards().size());
    }
    private void generateUserLocationHistory(User user) {
        IntStream.range(0, 3).forEach(i -> user.addToVisitedLocations(new VisitedLocationBean(user.getUserId(), new LocationBean(generateRandomLatitude(), generateRandomLongitude()), getRandomTime())));
    }

    private double generateRandomLongitude() {
        double leftLimit = -180;
        double rightLimit = 180;
        return leftLimit + new Random().nextDouble() * (rightLimit - leftLimit);
    }

    private double generateRandomLatitude() {
        double leftLimit = -85.05112878;
        double rightLimit = 85.05112878;
        return leftLimit + new Random().nextDouble() * (rightLimit - leftLimit);
    }

    private Date getRandomTime() {
        LocalDateTime localDateTime = LocalDateTime.now().minusDays(new Random().nextInt(30));
        return Date.from(localDateTime.toInstant(ZoneOffset.UTC));
    }
}
