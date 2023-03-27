package com.mtaner.android_ticmeet;

import android.content.Intent;

import java.util.Date;
import java.util.List;

public class User {
    private String userName;
    private String userEmail;
    private String userPassword;
    private String userImage;
    private String userTopImage;
    private String userAge;
    private String userGender;
    private String userBio;
    private String userLocation;
    private List<String> userFollowers;
    private List<String> userFollowing;
    private Date userRegisterDate;
    private List<String> userEventsID;

    public User(String userName, String userEmail, String userPassword, String userImage, String userTopImage, String userAge, String userGender, String userBio, String userLocation, List<String> userFollowers, List<String> userFollowing, Date userRegisterDate, List<String> userEventsID) {
        this.userName = userName;
        this.userEmail = userEmail;
        this.userPassword = userPassword;
        this.userImage = userImage;
        this.userTopImage = userTopImage;
        this.userAge = userAge;
        this.userGender = userGender;
        this.userBio = userBio;
        this.userLocation = userLocation;
        this.userFollowers = userFollowers;
        this.userFollowing = userFollowing;
        this.userRegisterDate = userRegisterDate;
        this.userEventsID = userEventsID;
    }

    public User() {
    }

    public User(String userName, String userBio, String userLocation, String userAge, String userGender) {
        this.userName = userName;
        this.userBio = userBio;
        this.userLocation = userLocation;
        this.userAge = userAge;
        this.userGender = userGender;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getUserTopImage() {
        return userTopImage;
    }

    public void setUserTopImage(String userTopImage) {
        this.userTopImage = userTopImage;
    }

    public String getUserAge() {
        return userAge;
    }

    public void setUserAge(String userAge) {
        this.userAge = userAge;
    }

    public String getUserGender() {
        return userGender;
    }

    public void setUserGender(String userGender) {
        this.userGender = userGender;
    }

    public String getUserBio() {
        return userBio;
    }

    public void setUserBio(String userBio) {
        this.userBio = userBio;
    }

    public String getUserLocation() {
        return userLocation;
    }

    public void setUserLocation(String userLocation) {
        this.userLocation = userLocation;
    }

    public List<String> getUserFollowers() {
        return userFollowers;
    }

    public void setUserFollowers(List<String> userFollowers) {
        this.userFollowers = userFollowers;
    }

    public List<String> getUserFollowing() {
        return userFollowing;
    }

    public void setUserFollowing(List<String> userFollowing) {
        this.userFollowing = userFollowing;
    }

    public Date getUserRegisterDate() {
        return userRegisterDate;
    }

    public void setUserRegisterDate(Date userRegisterDate) {
        this.userRegisterDate = userRegisterDate;
    }

    public List<String> getUserEventsID() {
        return userEventsID;
    }

    public void setUserEventsID(List<String> userEventsID) {
        this.userEventsID =userEventsID;
    }
}
