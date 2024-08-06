package com.my.audio_video_fm.model;

import com.my.audio_video_fm.model.CategoryItem;

import java.util.List;

public class HomeCategory {
    private String name;
    private List<CategoryItem> categoryItem;

    public HomeCategory(String name, List<CategoryItem> categoryItem) {
        this.name = name;
        this.categoryItem = categoryItem;
    }

    // Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<CategoryItem> getCategoryItem() {
        return categoryItem;
    }

    public void setCategoryItem(List<CategoryItem> categoryItem) {
        this.categoryItem = categoryItem;
    }
}
