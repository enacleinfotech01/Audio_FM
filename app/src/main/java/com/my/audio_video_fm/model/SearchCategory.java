package com.my.audio_video_fm.model;

import java.util.List;

public class SearchCategory {
    private String name;
    private String image;
    private List<CategoryItem> categoryItemItems;

    public SearchCategory(String name, String image, List<CategoryItem> categoryItemItems) {
        this.name = name;
        this.image = image;
        this.categoryItemItems = categoryItemItems;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public List<CategoryItem> getcategoryItemItems() {
        return categoryItemItems;
    }
}
