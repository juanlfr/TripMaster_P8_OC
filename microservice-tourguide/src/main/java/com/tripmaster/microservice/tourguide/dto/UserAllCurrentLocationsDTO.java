package com.tripmaster.microservice.tourguide.dto;

import com.tripmaster.microservice.tourguide.beans.LocationBean;

import java.util.Objects;
import java.util.UUID;

public class UserAllCurrentLocationsDTO {

    private UUID userId;
    private LocationBean userLocation;

    public UserAllCurrentLocationsDTO() {
    }

    public UserAllCurrentLocationsDTO(UUID userId, LocationBean userLocation) {
        this.userId = userId;
        this.userLocation = userLocation;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public LocationBean getUserLocation() {
        return userLocation;
    }

    public void setUserLocation(LocationBean userLocation) {
        this.userLocation = userLocation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserAllCurrentLocationsDTO that = (UserAllCurrentLocationsDTO) o;
        return Objects.equals(getUserId(), that.getUserId()) && Objects.equals(getUserLocation(), that.getUserLocation());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUserId(), getUserLocation());
    }

    @Override
    public String toString() {
        return "UserAllCurrentLocationsDTO{" +
                "userId=" + userId +
                ", userLocation=" + userLocation +
                '}';
    }
}
