package com.my.audio_video_fm;

public class Video {
    public String getVideourl() {
        return videourl;
    }

    public String getTitle() {
        return title;
    }

    public Video(String videourl, String title) {
        this.videourl = videourl;
        this.title = title;
    }

    private String videourl,title;
}
