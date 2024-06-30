package com.example.diabfitapp;

public class Tutorial {
    private String title;
    private String author;
    private String date;
    private String description;
    private String imageName;

    public Tutorial(String title, String author, String date, String description, String imageName) {
        this.title = title;
        this.author = author;
        this.date = date;
        this.description = description;
        this.imageName = imageName;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public String getImageName() {
        return imageName;
    }
}
