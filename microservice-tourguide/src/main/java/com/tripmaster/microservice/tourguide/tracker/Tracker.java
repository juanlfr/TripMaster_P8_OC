package com.tripmaster.microservice.tourguide.tracker;

import com.tripmaster.microservice.tourguide.beans.VisitedLocationBean;
import com.tripmaster.microservice.tourguide.beans.user.User;
import com.tripmaster.microservice.tourguide.services.TourGuideService;
import org.apache.commons.lang.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.*;

@Component
public class Tracker implements Runnable {
    private final Logger logger = LoggerFactory.getLogger(Tracker.class);
    //    private static final long trackingPollingInterval = TimeUnit.MINUTES.toSeconds(5);
//    private  ExecutorService executorService = Executors.newSingleThreadExecutor();
    private TourGuideService tourGuideService;
//    private boolean stop = false;



    @Autowired
    public Tracker(TourGuideService tourGuideService) {
        this.tourGuideService = tourGuideService;
    }

    /**
     * Assures to shut down the Tracker thread
     */
//    public void stopTracking() {
//        stop = true;
//        executorService.shutdownNow();
//    }
    @Override
    public void run() {
        System.out.println("*****************Tracker**********");


        System.out.println("***************** getting AllUsers **********");
        List<User> users = tourGuideService.getAllUsers();
        System.out.println(users);
        logger.info("Begin Tracker. Tracking " + users.size() + " users.");

        users.forEach(u -> {
            try {
                System.out.println("***************** trackUserLocation ***********");
                tourGuideService.trackUserLocation(u);
            } catch (ExecutionException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        });


    }

}
