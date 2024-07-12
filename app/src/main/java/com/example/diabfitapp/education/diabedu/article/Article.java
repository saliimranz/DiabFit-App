package com.example.diabfitapp.education.diabedu.article;

public class Article {
    private String title;
    private String author;
    private String date;
    private String content;
    private String imageResName;

    public Article(String title, String author, String date, String content, String imageResName) {
        this.title = title;
        this.author = author;
        this.date = date;
        this.content = content;
        this.imageResName = imageResName;
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

    public String getContent() {
        return content;
    }

    public String getImageResName() {
        return imageResName;
    }
}
