package com.example.umfs;

public class Post {

    private String postUrl; // generate by database reference
    private String postImage; //store uri.toString(), use this string to retrieve the img, via glide | ref:https://bumptech.github.io/glide/
    private String postCategory;
    private String postBy; //store userId
    private String postTitle; // store post title
    private String postDescription; //store description
    private long postedAt; //store post time : System.currentTimeMillis()

    public Post(String postUrl, String postImage, String postCategory, String postBy, String postTitle, String postDescription, long postedAt) {
        this.postUrl = postUrl;
        this.postImage = postImage;
        this.postCategory = postCategory;
        this.postBy = postBy;
        this.postTitle = postTitle;
        this.postDescription = postDescription;
        this.postedAt = postedAt;
    }

    public Post() {
    }

    public String getPostUrl() {
        return postUrl;
    }

    public void setPostUrl(String postUrl) {
        this.postUrl = postUrl;
    }

    public String getPostImage() {
        return postImage;
    }

    public void setPostImage(String postImage) {
        this.postImage = postImage;
    }

    public String getPostCategory() {
        return postCategory;
    }

    public void setPostCategory(String postCategory) {
        this.postCategory = postCategory;
    }

    public String getPostBy() {
        return postBy;
    }

    public void setPostBy(String postBy) {
        this.postBy = postBy;
    }

    public String getPostTitle() {
        return postTitle;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public String getPostDescription() {
        return postDescription;
    }

    public void setPostDescription(String postDescription) {
        this.postDescription = postDescription;
    }

    public long getPostedAt() {
        return postedAt;
    }

    public void setPostedAt(long postedAt) {
        this.postedAt = postedAt;
    }
}
