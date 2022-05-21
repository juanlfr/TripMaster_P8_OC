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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


@Service
public class TourGuideService {
    private final Logger logger = LoggerFactory.getLogger(TourGuideService.class);
    private MicroserviceGpsProxy microserviceGpsProxy;
    private RewardsService rewardsService;

    public TourGuideService() {
    }

    @Autowired
    public TourGuideService(MicroserviceGpsProxy microserviceGpsProxy, RewardsService rewardsService) {
        this.microserviceGpsProxy = microserviceGpsProxy;
        this.rewardsService = rewardsService;
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
        return internalUserMap.get(userName);
    }

    public List<User> getAllUsers() {
        return new ArrayList<>(internalUserMap.values());
    }
    public void addUser(User user) {
        if (!internalUserMap.containsKey(user.getUserName())) {
            internalUserMap.put(user.getUserName(), user);
        }
    }
//    public List<Provider> getTripDeals(User user) {
//        int cumulatativeRewardPoints = user.getUserRewards().stream().mapToInt(i -> i.getRewardPoints()).sum();
//        List<Provider> providers = tripPricer.getPrice(tripPricerApiKey, user.getUserId(), user.getUserPreferences().getNumberOfAdults(),
//                user.getUserPreferences().getNumberOfChildren(), user.getUserPreferences().getTripDuration(), cumulatativeRewardPoints);
//        user.setTripDeals(providers);
//        return providers;
//    }

    public VisitedLocationBean trackUserLocation(User user) throws ExecutionException, InterruptedException {

        CompletableFuture<VisitedLocationBean> getUserLocationFuture = CompletableFuture.supplyAsync(() -> microserviceGpsProxy.getUserLocation(user.getUserId()));
        getUserLocationFuture.thenAccept(user::addToVisitedLocations).thenRunAsync(() ->  rewardsService.calculateRewards(user));
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
    private final Map<String, User> internalUserMap = new HashMap<>();

    private void initializeInternalUsers() {
        IntStream.range(0, InternalTestHelper.getInternalUserNumber()).forEach(i -> {
            String userName = "internalUser" + i;
            String phone = "000";
            String email = userName + "@tourGuide.com";
            User user = new User(UUID.randomUUID(), userName, phone, email);
            generateUserLocationHistory(user);

            internalUserMap.put(userName, user);
        });
        logger.debug("Created " + InternalTestHelper.getInternalUserNumber() + " internal test users.");
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
