package com.tripmaster.microservice.tourguide.services;

import com.tripmaster.microservice.tourguide.beans.AttractionBean;
import com.tripmaster.microservice.tourguide.beans.LocationBean;
import com.tripmaster.microservice.tourguide.beans.Provider;
import com.tripmaster.microservice.tourguide.beans.VisitedLocationBean;
import com.tripmaster.microservice.tourguide.beans.user.User;
import com.tripmaster.microservice.tourguide.beans.user.UserReward;
import com.tripmaster.microservice.tourguide.dto.AttractionDTO;
import com.tripmaster.microservice.tourguide.helpers.InternalTestHelper;
import com.tripmaster.microservice.tourguide.proxies.MicroserviceGpsProxy;
import com.tripmaster.microservice.tourguide.proxies.TripPricerProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.IntStream;


@Service
public class TourGuideService {
    private final Logger logger = LoggerFactory.getLogger(TourGuideService.class);
    private MicroserviceGpsProxy microserviceGpsProxy;
    private RewardsService rewardsService;

    private TripPricerProxy tripPricerProxy;

    public TourGuideService() {
    }

    @Autowired
    public TourGuideService(MicroserviceGpsProxy microserviceGpsProxy, RewardsService rewardsService, TripPricerProxy tripPricerProxy) {
        this.microserviceGpsProxy = microserviceGpsProxy;
        this.rewardsService = rewardsService;
        this.tripPricerProxy = tripPricerProxy;
    }

    public List<UserReward> getUserRewards(User user) {
        return user.getUserRewards();
    }

    public VisitedLocationBean getUserLocation(User user) throws ExecutionException, InterruptedException {
        return (user.getVisitedLocations().size() > 0) ?
                user.getLastVisitedLocation() :
                trackUserLocation(user);
    }

    public User getUser(String userName) {
        return InternalTestHelper.internalUserMap.get(userName);
    }

    public List<User> getAllUsers() {
        return new ArrayList<>(InternalTestHelper.internalUserMap.values());
    }
    public void addUser(User user) {
        if (!InternalTestHelper.internalUserMap.containsKey(user.getUserName())) {
            InternalTestHelper.internalUserMap.put(user.getUserName(), user);
        }
    }
    public List<Provider> getTripDeals(User user) {
        int cumulatativeRewardPoints = user.getUserRewards().stream().mapToInt(i -> i.getRewardPoints()).sum();
        List<Provider> providers = tripPricerProxy.getPrice(tripPricerApiKey, user.getUserId(), user.getUserPreferences().getNumberOfAdults(),
                user.getUserPreferences().getNumberOfChildren(), user.getUserPreferences().getTripDuration(), cumulatativeRewardPoints);
        user.setTripDeals(providers);
        return providers;
    }

    public VisitedLocationBean trackUserLocation(User user) throws ExecutionException, InterruptedException {

        ExecutorService executorService = Executors.newFixedThreadPool(50);
        CompletableFuture<VisitedLocationBean> getUserLocationFuture = CompletableFuture.supplyAsync(() -> microserviceGpsProxy.getUserLocation(user.getUserId()));
        getUserLocationFuture.thenAccept(user::addToVisitedLocations).thenRunAsync(() ->  rewardsService.calculateRewards(user), executorService);
        return getUserLocationFuture.get();
    }
    public List<AttractionDTO> getNearByAttractions(VisitedLocationBean visitedLocation, User user) {

        List<AttractionDTO> nearbyAttractions = new ArrayList<>();
        Map<Double, AttractionBean> attractionDistance = new HashMap<>();

        for (AttractionBean attraction : microserviceGpsProxy.getAttractions()) {
            double distance = rewardsService.getDistance(attraction, visitedLocation.location);
            attractionDistance.put(distance, attraction);
        }
        attractionDistance.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .limit(5)
                .forEach(attraction -> attractionToAttractionDTO(attraction.getValue(), visitedLocation, user, attraction.getKey(), nearbyAttractions));

        return nearbyAttractions;
    }
    private void attractionToAttractionDTO(AttractionBean attraction, VisitedLocationBean visitedLocation, User user, double distance, List<AttractionDTO> nearbyAttractions) {
        AttractionDTO attractionDTO = new AttractionDTO();
        attractionDTO.setAttractionName(attraction.attractionName);
        attractionDTO.setAttractionLocation(attraction);
        attractionDTO.setUserLocation(visitedLocation.location);
        attractionDTO.setDistance(distance);
        attractionDTO.setRewardPoints(rewardsService.getRewardPoints(attraction, user));
        nearbyAttractions.add(attractionDTO);
    }
//    private void addShutDownHook() {
//        Runtime.getRuntime().addShutdownHook(new Thread() {
//            public void run() {
//                tracker.stopTracking();
//            }
//        });
//    }

    /**********************************************************************************
     *
     * Methods Below: For Internal Testing
     *
     **********************************************************************************/
    private static final String tripPricerApiKey = "test-server-api-key";
    // Database connection will be used for external users, but for testing purposes internal users are provided and stored in memory



}
