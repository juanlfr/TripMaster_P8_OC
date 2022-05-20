package com.tripmaster.microservice.tourguide.beans.user;

import com.tripmaster.microservice.tourguide.beans.AttractionBean;
import com.tripmaster.microservice.tourguide.beans.VisitedLocationBean;

public class UserReward {

	public final VisitedLocationBean visitedLocation;
	public final AttractionBean attraction;
	private int rewardPoints;
	public UserReward(VisitedLocationBean visitedLocation, AttractionBean attraction, int rewardPoints) {
		this.visitedLocation = visitedLocation;
		this.attraction = attraction;
		this.rewardPoints = rewardPoints;
	}
	
	public UserReward(VisitedLocationBean visitedLocation, AttractionBean attraction) {
		this.visitedLocation = visitedLocation;
		this.attraction = attraction;
	}

	public void setRewardPoints(int rewardPoints) {
		this.rewardPoints = rewardPoints;
	}
	
	public int getRewardPoints() {
		return rewardPoints;
	}

	@Override
	public String toString() {
		return "UserReward{" +
				"visitedLocation=" + visitedLocation +
				", attraction=" + attraction +
				", rewardPoints=" + rewardPoints +
				'}';
	}
}
