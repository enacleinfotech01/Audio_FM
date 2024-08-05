package com.my.audio_video_fm.model;

public  class Episode2 {
    private int id2;
    private String title2;
    private String time2;
    private String imageUrl2;
    private String audioUrl; // Add this field

    // Getters and Setters


    public Episode2(int id2, String title2, String time2, String imageUrl2, String audioUrl) {
        this.id2 = id2;
        this.title2 = title2;
        this.time2 = time2;
        this.imageUrl2 = imageUrl2;
        this.audioUrl = audioUrl;
    }

    public String getAudioUrl() {
        return audioUrl;
    }

    public void setAudioUrl(String audioUrl) {
        this.audioUrl = audioUrl;
    }

    public int getId2() {
        return id2;
    }

    public void setId2(int id2) {
        this.id2 = id2;
    }

    public String getTitle2() {
        return title2;
    }

    public void setTitle2(String title2) {
        this.title2 = title2;
    }

    public String getTime2() {
        return time2;
    }

    public void setTime2(String time2) {
        this.time2 = time2;
    }

    public String getImageUrl2() {
        return imageUrl2;
    }

    public void setImageUrl2(String imageUrl2) {
        this.imageUrl2 = imageUrl2;
    }

}
