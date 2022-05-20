package com.tripmaster.microservice.tourguide.beans;

import java.util.Date;
import java.util.UUID;

public class VisitedLocationBean {

    public final UUID userId;
    public final LocationBean location;
    public final Date timeVisited;

    public VisitedLocationBean(UUID userId, LocationBean location, Date timeVisited) {
        this.userId = userId;
        this.location = location;
        this.timeVisited = timeVisited;
    }

    public UUID getUserId() {
        return userId;
    }

    public LocationBean getLocation() {
        return location;
    }

    public Date getTimeVisited() {
        return timeVisited;
    }

    @Override
    public String toString() {
        return "VisitedLocationBean{" +
                "userId=" + userId +
                ", location=" + location +
                ", timeVisited=" + timeVisited +
                '}';
    }
}
