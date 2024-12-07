package com.example.firebasetst;

public class Favorite {
    private String userId;
    private String locationId;

    // Empty constructor for Firestore
    public Favorite() {}

    public Favorite(String userId, String locationId) {
        this.userId = userId;
        this.locationId = locationId;
    }

    public String getUserId() {
        return userId;
    }

    public String getLocationId() {
        return locationId;
    }
}
