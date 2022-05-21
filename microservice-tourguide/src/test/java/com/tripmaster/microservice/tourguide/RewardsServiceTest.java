package com.tripmaster.microservice.tourguide;

import com.tripmaster.microservice.tourguide.beans.LocationBean;
import com.tripmaster.microservice.tourguide.beans.VisitedLocationBean;
import com.tripmaster.microservice.tourguide.beans.user.User;
import com.tripmaster.microservice.tourguide.beans.user.UserReward;
import com.tripmaster.microservice.tourguide.proxies.MicroserviceGpsProxy;
import com.tripmaster.microservice.tourguide.services.RewardsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


@SpringBootTest
public class RewardsServiceTest {
    @Autowired
    RewardsService rewardsService;
    @Autowired
    MicroserviceGpsProxy microserviceGpsProxy;

    @Test
    public void nearAllAttractions() {
        rewardsService.setProximityBuffer(Integer.MAX_VALUE);
        User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
        generateUserLocationHistory(user);
        rewardsService.calculateRewards(user);
        assertEquals(microserviceGpsProxy.getAttractions().size(), user.getUserRewards().size());
    }
    private void generateUserLocationHistory(User user) {
        IntStream.range(0, 3).forEach(i -> {
            user.addToVisitedLocations(new VisitedLocationBean(user.getUserId(), new LocationBean(generateRandomLatitude(), generateRandomLongitude()), getRandomTime()));
        });
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
