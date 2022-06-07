package com.tripmaster.microservice.tourguide.tracker;

import com.tripmaster.microservice.tourguide.helpers.InternalTestHelper;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
@Profile("!test")
@Component
public class TrackerLauncher {

    private Tracker tracker;

    public TrackerLauncher() {
    }

    @Autowired
    public TrackerLauncher(Tracker tracker) {
        this.tracker = tracker;
    }

    @EventListener
    public void onApplicationEvent(ApplicationReadyEvent event) {
        InternalTestHelper.initializeInternalUsers();
        System.out.println("***********scheduledExecutor*******");
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(tracker, 10, 15, TimeUnit.MINUTES);

    }

}
