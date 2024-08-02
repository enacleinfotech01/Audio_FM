package com.my.audio_video_fm.model;

import java.util.List;

public class SearchCategory {
    private String name;
    private String imageUrl;
    private List<String> UHD;

    public SearchCategory(String name, String imageUrl, List<String> UHD) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.UHD = UHD;
    }

    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public List<String> getUHD() {
        return UHD;
    }
}
