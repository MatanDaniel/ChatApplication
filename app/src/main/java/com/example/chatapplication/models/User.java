package com.example.chatapplication.models;

public class User {
    private Long id;
    private String username;
    private String email;
    private String profilePicturePath;

    public Long getId() {
        return id;
    }
    public String getUsername() {
        return username;
    }
    public String getEmail() {
        return email;
    }
    public String getProfilePicturePath() {
        return profilePicturePath;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setProfilePicturePath(String profilePicturePath) {
        this.profilePicturePath = profilePicturePath;
    }

    public String getImageUrl() {

        return profilePicturePath;
    }
}
