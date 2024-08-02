package com.my.audio_video_fm.model;

import java.util.List;

public class SearchCategory {
    private String name;
    private String image;
    private List<CategoryItem> UHD;

    public SearchCategory(String name, String image, List<CategoryItem> UHD) {
        this.name = name;
        this.image = image;
        this.UHD = UHD;
    }

    // Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<CategoryItem> getUHD() {
        return UHD;
    }

    public void setUHD(List<CategoryItem> UHD) {
        this.UHD = UHD;
    }
}
