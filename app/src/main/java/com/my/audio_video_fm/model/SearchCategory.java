package com.my.audio_video_fm.model;

import java.util.List;

public class SearchCategory {
    private String name;
    private String image;
    private List<CategoryItem> uhdItems;

    public SearchCategory(String name, String image, List<CategoryItem> uhdItems) {
        this.name = name;
        this.image = image;
        this.uhdItems = uhdItems;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public List<CategoryItem> getUhdItems() {
        return uhdItems;
    }
}
