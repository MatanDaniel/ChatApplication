package com.example.springapi.model;

import jakarta.persistence.*;
import jakarta.persistence.Lob;



@Entity
@Table(name = "users")
public class User {

    // This stores the full image inside our DB -
    // can bloat our Database.
    @Lob
    @Column(name = "profile_image")
    private byte[] profileImage;


    private String username;
    private String password;
    private String email;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public User() {
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public Long getId() {
        return id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public byte[] getProfileImage() {
        return profileImage;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setProfileImage(byte[] profileImage) {
        this.profileImage = profileImage;
    }


}
