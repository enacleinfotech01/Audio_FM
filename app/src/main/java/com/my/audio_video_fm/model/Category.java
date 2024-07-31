package com.my.audio_video_fm.model;

import java.util.List;

public class Category {
    private String name;
    private List<MediaItem> items;

    public Category(String name, List<MediaItem> items) {
        this.name = name;
        this.items = items;
    }

    // Getters and setters

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<MediaItem> getItems() {
        return items;
    }

    public void setItems(List<MediaItem> items) {
        this.items = items;
    }
}
