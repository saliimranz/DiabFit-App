package com.example.diabfitapp.education.communityforum;

public class Post {
    private int profileImage;
    private String title;
    private String username;
    private String time;
    private String content;
    private String category;
    private int views;
    private int replies;

    public Post(int profileImage, String title, String username,String time, String content, String category, int views, int replies) {
        this.profileImage = profileImage;
        this.title = title;
        this.username = username;
        this.time = time;
        this.content = content;
        this.category = category;
        this.views = views;
        this.replies = replies;
    }

    public int getProfileImage() {
        return profileImage;
    }
    public String getUsername() {
        return username;
    }

    public String getTitle() {
        return title;
    }

    public String getTime() {
        return time;
    }

    public String getContent() {
        return content;
    }

    public String getCategory() {
        return category;
    }

    public int getViews() {
        return views;
    }

    public int getReplies() {
        return replies;
    }
}
