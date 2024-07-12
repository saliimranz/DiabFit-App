package com.example.diabfitapp.nutrition.meal;

public class MealItem {
    private String title;
    private String details;
    private int imageResId;

    public MealItem(String title, String details, int imageResId) {
        this.title = title;
        this.details = details;
        this.imageResId = imageResId;
    }

    public String getTitle() {
        return title;
    }

    public String getDetails() {
        return details;
    }

    public int getImageResId() {
        return imageResId;
    }
}
