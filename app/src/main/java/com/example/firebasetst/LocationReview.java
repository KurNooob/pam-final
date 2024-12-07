package com.example.firebasetst;

public class LocationReview {
    private String userName;
    private String description;
    private float rating;

    // Empty constructor needed for Firebase
    public LocationReview() {}

    public LocationReview(String userName, String description, float rating) {
        this.userName = userName;
        this.description = description;
        this.rating = rating;
    }

    // Getters
    public String getUserName() { return userName; }
    public String getDescription() { return description; }
    public float getRating() { return rating; }
}