package com.example.firebasetst;

public class Location {

    private String id;  // New field for unique location ID
    private String title;
    private String location;
    private String rating;
    private int imageResId;
    private String overview;

    public Location(String id, String title, String location, String rating, int imageResId, String overview) {
        this.id = id;
        this.title = title;
        this.location = location;
        this.rating = rating;
        this.imageResId = imageResId;
        this.overview = overview;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public int getImageResId() {
        return imageResId;
    }

    public void setImageResId(int imageResId) {
        this.imageResId = imageResId;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }
}
