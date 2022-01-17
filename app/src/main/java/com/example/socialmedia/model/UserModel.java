package com.example.socialmedia.model;

public class UserModel {
    private String name,email,password, profession;
    private String coverPhoto;
    private String userID;
    private int follerCount;

    public int getFollerCount() {
        return follerCount;
    }

    public void setFollerCount(int follerCount) {
        this.follerCount = follerCount;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    private String profile;

    public String getCoverPhoto() {
        return coverPhoto;
    }

    public void setCoverPhoto(String coverPhoto) {
        this.coverPhoto = coverPhoto;
    }

    public UserModel(String name, String email, String password, String profession) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.profession = profession;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public UserModel() {
    }
}
