package com.example.firebasetst;

public class Review {
    private String title;
    private String location;
    private String description;
    private String userName;
    private String userId;
    private String locationId;
    private float rating;

    // No-argument constructor (required for Firestore)
    public Review() {
    }

    // Full constructor
    public Review(String title, String location, String description, String userName, String userId, String locationId, float rating) {
        this.title = title;
        this.location = location;
        this.description = description;
        this.userName = userName;
        this.userId = userId;
        this.locationId = locationId;
        this.rating = rating;
    }

    // Getters and setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }
}
