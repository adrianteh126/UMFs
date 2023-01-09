package com.example.umfs;

public class NotificationModel {
    private String userID;
    private String postID;
    private String text;
    private boolean isPost;

    public NotificationModel() {
    }

    public NotificationModel(String userID, String postID, String text, boolean isPost) {
        this.userID = userID;
        this.postID = postID;
        this.text = text;
        this.isPost = isPost;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getPostID() {
        return postID;
    }

    public void setPostID(String postID) {
        this.postID = postID;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isPost() {
        return isPost;
    }

    public void setPost(boolean post) {
        isPost = post;
    }
}
