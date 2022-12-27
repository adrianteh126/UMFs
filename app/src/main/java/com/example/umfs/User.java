package com.example.umfs;

public class User {

        public String siswamail,username,password, bio;
        public String[] following;

        public User() {
        }

        public User(String siswamail, String username, String password) {
            this.siswamail = siswamail;
            this.username = username;
            this.password = password;
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
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String[] getFollowing() {
        return following;
    }

    public void setFollowing(String[] following) {
        this.following = following;
    }
}

