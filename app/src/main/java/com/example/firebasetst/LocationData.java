package com.example.firebasetst;

public class LocationData {
    private String title;
    private String location;
    private String description;
    private String userEmail;  // Field for the user's email

    // Constructor
    public LocationData(String title, String location, String description, String userEmail) {
        this.title = title;
        this.location = location;
        this.description = description;
        this.userEmail = userEmail;
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

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
}