package com.my.audio_video_fm.Room;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "users")
public class User {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String imageUri;
    private String name;
    private String bio;
    private int age;
    private String gender; // Male, Female, Other
    private String occupation;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public User(int id, String imageUri, String name, String bio, int age, String gender, String occupation) {
        this.id = id;
        this.imageUri = imageUri;
        this.name = name;
        this.bio = bio;
        this.age = age;
        this.gender = gender;
        this.occupation = occupation;
    }

    // Getters and Setters
    // ...
}
