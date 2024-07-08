// VideoItem.java
package com.example.diabfitapp;

public class VideoItem {
    private String title;
    private String description;
    private int imageResId;
    private String videoUrl;

    public VideoItem(String title, String description, int imageResId, String videoUrl) {
        this.title = title;
        this.description = description;
        this.imageResId = imageResId;
        this.videoUrl = videoUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getImageResId() {
        return imageResId;
    }

    public String getVideoUrl() {
        return videoUrl;
    }
}
