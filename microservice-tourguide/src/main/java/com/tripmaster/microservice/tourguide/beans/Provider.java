package com.tripmaster.microservice.tourguide.beans;

import java.util.Objects;
import java.util.UUID;

public class Provider {
    public final String name;
    public final double price;
    public final UUID tripId;

    public Provider(UUID tripId, String name, double price) {
        this.name = name;
        this.tripId = tripId;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public UUID getTripId() {
        return tripId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Provider provider = (Provider) o;
        return Double.compare(provider.getPrice(), getPrice()) == 0 && Objects.equals(getName(), provider.getName()) && Objects.equals(getTripId(), provider.getTripId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getPrice(), getTripId());
    }
}
