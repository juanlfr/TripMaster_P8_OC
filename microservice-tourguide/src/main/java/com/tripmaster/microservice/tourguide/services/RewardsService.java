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

    public void calculateRewards(User user) {

        List<VisitedLocationBean> userLocations = user.getVisitedLocations();
        List<AttractionBean> attractions = microserviceGpsProxy.getAttractions();
        //TODO Envoyer un future?
        System.out.println("calculateRewards before for loop in Rewards service" + "by" + Thread.currentThread().getName());
        for (VisitedLocationBean visitedLocation : userLocations) {
            for (AttractionBean attraction : attractions) {
                //To check by attraction name if the user's reward is already added, if not, it is added
                //TODO à tester sans if ci dessous
                if (user.getUserRewards().stream().noneMatch(reward -> reward.attraction.attractionName.equals(attraction.attractionName))) {
                    if (nearAttraction(visitedLocation, attraction)) {
                        user.addUserReward(new UserReward(visitedLocation, attraction, getRewardPoints(attraction, user)));
                        System.out.println("addUserReward Rewards service" + "by" + Thread.currentThread().getName());
                    }
                }
            }
        }

    }
    public boolean isWithinAttractionProximity(AttractionBean attraction, LocationBean location) {
        return getDistance(attraction, location) > attractionProximityRange ? false : true;
    }

    public boolean nearAttraction(VisitedLocationBean visitedLocation, AttractionBean attraction) {
        return getDistance(attraction, visitedLocation.location) > proximityBuffer ? false : true;
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
