package com.tripmaster.microservice.tourguide.beans.user;

import com.tripmaster.microservice.tourguide.beans.Provider;
import com.tripmaster.microservice.tourguide.beans.VisitedLocationBean;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class User {
	private final UUID userId;
	private final String userName;
	private String phoneNumber;
	private String emailAddress;
	private Date latestLocationTimestamp;
	private List<VisitedLocationBean> visitedLocations = new ArrayList<>();
	private List<UserReward> userRewards = new ArrayList<>();
	private UserPreferences userPreferences = new UserPreferences();
	private List<Provider> tripDeals = new ArrayList<>();
	public User(UUID userId, String userName, String phoneNumber, String emailAddress) {
		this.userId = userId;
		this.userName = userName;
		this.phoneNumber = phoneNumber;
		this.emailAddress = emailAddress;
	}
	
	public UUID getUserId() {
		return userId;
	}
	
	public String getUserName() {
		return userName;
	}
	
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	
	public String getEmailAddress() {
		return emailAddress;
	}
	
	public void setLatestLocationTimestamp(Date latestLocationTimestamp) {
		this.latestLocationTimestamp = latestLocationTimestamp;
	}
	
	public Date getLatestLocationTimestamp() {
		return latestLocationTimestamp;
	}
	
	public void addToVisitedLocations(VisitedLocationBean visitedLocation) {
		visitedLocations.add(visitedLocation);
	}
	
	public List<VisitedLocationBean> getVisitedLocations() {
		return visitedLocations;
	}
	
	public void clearVisitedLocations() {
		visitedLocations.clear();
	}
	
	public void addUserReward(UserReward userReward) {

		if(userRewards.stream().noneMatch(reward -> reward.attraction.attractionName.equals(userReward.attraction.attractionName))) {
			userRewards.add(userReward);
			System.out.println("adding user reward" + userReward.toString());
		}

	}
	
	public List<UserReward> getUserRewards() {
		return userRewards;
	}
	
	public UserPreferences getUserPreferences() {
		return userPreferences;
	}
	
	public void setUserPreferences(UserPreferences userPreferences) {
		this.userPreferences = userPreferences;
	}

	public VisitedLocationBean getLastVisitedLocation() {
		return visitedLocations.get(visitedLocations.size() - 1);
	}
	
	public void setTripDeals(List<Provider> tripDeals) {
		this.tripDeals = tripDeals;
	}
	
	public List<Provider> getTripDeals() {
		return tripDeals;
	}


	@Override
	public String toString() {
		return "User{" +
				"userId=" + userId +
				", userName='" + userName + '\'' +
				", phoneNumber='" + phoneNumber + '\'' +
				", emailAddress='" + emailAddress + '\'' +
				", latestLocationTimestamp=" + latestLocationTimestamp +
				", visitedLocations=" + visitedLocations +
				", userRewards=" + userRewards +
				", userPreferences=" + userPreferences +
				", tripDeals=" + tripDeals +
				'}';
	}
}
