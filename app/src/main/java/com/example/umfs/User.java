package com.example.umfs;

public class User {

    private String userUID;
    private String siswamail;
    private String username;
    private String password;
    private String Bio;
    private String Faculty;
    private String ProfilePicture;

    public User() {
    }


    public User(String uid, String siswamail, String username, String password) {
        this.userUID = uid;
        this.siswamail = siswamail;
        this.username = username;
        this.password = password;
    }

    public User(String siswamail, String username, String password, String bio, String faculty, String profilePicture) {
        this.siswamail = siswamail;
        this.username = username;
        this.password = password;
        Bio = bio;
        Faculty = faculty;
        ProfilePicture = profilePicture;
    }

    public String getSiswamail() {
        return siswamail;
    }

    public void setSiswamail(String siswamail) {
        this.siswamail = siswamail;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getBio() {
        return Bio;
    }

    public void setBio(String bio) {
        Bio = bio;
    }

    public String getFaculty() {
        return Faculty;
    }

    public void setFaculty(String faculty) {
        Faculty = faculty;
    }

    public String getProfilePicture() {
        return ProfilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        ProfilePicture = profilePicture;
    }

    public String getUserUID() {
        return userUID;
    }

    public void setUserUID(String userUID) {
        this.userUID = userUID;
    }


}