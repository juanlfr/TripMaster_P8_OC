package com.tripmaster.microservice.tourguide.services;

import com.tripmaster.microservice.tourguide.beans.AttractionBean;
import com.tripmaster.microservice.tourguide.beans.LocationBean;
import com.tripmaster.microservice.tourguide.beans.VisitedLocationBean;
import com.tripmaster.microservice.tourguide.beans.user.User;
import com.tripmaster.microservice.tourguide.beans.user.UserReward;
import com.tripmaster.microservice.tourguide.proxies.MicroserviceGpsProxy;
import com.tripmaster.microservice.tourguide.proxies.MicroserviceRewardsCentralProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class RewardsService {
    private static final double STATUTE_MILES_PER_NAUTICAL_MILE = 1.15077945;

    // proximity in miles
    private int defaultProximityBuffer = 10;
    private int proximityBuffer = defaultProximityBuffer;
    private int attractionProximityRange = 200;
    @Autowired
    private MicroserviceGpsProxy microserviceGpsProxy;
    @Autowired
    private MicroserviceRewardsCentralProxy microserviceRewardsCentralProxy;

    public void setProximityBuffer(int proximityBuffer) {
        this.proximityBuffer = proximityBuffer;
    }

    public void setDefaultProximityBuffer() {
        proximityBuffer = defaultProximityBuffer;
    }

    public CompletableFuture<Void> calculateRewards(User user) {

        List<VisitedLocationBean> userLocations = user.getVisitedLocations();
        List<AttractionBean> attractions = microserviceGpsProxy.getAttractions();
        ExecutorService executorService = Executors.newCachedThreadPool();
        return CompletableFuture.runAsync(() -> {

            for (VisitedLocationBean visitedLocation : userLocations) {
//            attractions.stream().parallel().filter(attractionBean ->
//                user.getUserRewards().stream().noneMatch(reward -> reward.attraction.attractionName.equals(attractionBean.attractionName))
//            ).filter(attractionBean -> nearAttraction(visitedLocation, attractionBean)).
//                    forEach(attractionBean -> user.addUserReward(new UserReward(visitedLocation, attractionBean, getRewardPoints(attractionBean, user))));
                for (AttractionBean attraction : attractions) {
                    //To check by attraction name if the user's reward is already added, if not, it is added
                    if (user.getUserRewards().stream().noneMatch(reward -> reward.attraction.attractionName.equals(attraction.attractionName))) {
                        if (nearAttraction(visitedLocation, attraction)) {
                            user.addUserReward(new UserReward(visitedLocation, attraction, getRewardPoints(attraction, user)));
                            System.out.println("addUserReward Rewards service" + "by" + Thread.currentThread().getName());
                        }
                    }
                }
            }
        }, executorService);
    }

    public boolean isWithinAttractionProximity(AttractionBean attraction, LocationBean location) {
        return !(getDistance(attraction, location) > attractionProximityRange);
    }

    public boolean nearAttraction(VisitedLocationBean visitedLocation, AttractionBean attraction) {
        return !(getDistance(attraction, visitedLocation.location) > proximityBuffer);
    }

    public int getRewardPoints(AttractionBean attraction, User user) {
        return microserviceRewardsCentralProxy.getAttractionRewardPoints(attraction.attractionId, user.getUserId());
    }

    public double getDistance(LocationBean loc1, LocationBean loc2) {
        double lat1 = Math.toRadians(loc1.latitude);
        double lon1 = Math.toRadians(loc1.longitude);
        double lat2 = Math.toRadians(loc2.latitude);
        double lon2 = Math.toRadians(loc2.longitude);

        double angle = Math.acos(Math.sin(lat1) * Math.sin(lat2)
                + Math.cos(lat1) * Math.cos(lat2) * Math.cos(lon1 - lon2));

        double nauticalMiles = 60 * Math.toDegrees(angle);
        double statuteMiles = STATUTE_MILES_PER_NAUTICAL_MILE * nauticalMiles;
        return statuteMiles;
    }

}
