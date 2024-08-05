package com.my.audio_video_fm.model;

import java.util.List;

public class CategoryItem {
    private int id;
    private String title;
    private String description;
    private String imageUrl;
    private List<Episode2> episodes2;// Add fields according to your JSON structure

    public CategoryItem(int id, String title, String description, String imageUrl, List<Episode2> episodes2) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.imageUrl = imageUrl;
        this.episodes2 = episodes2;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Episode2> getEpisodes2() {
        return episodes2;
    }

    public void setEpisodes2(List<Episode2> episodes2) {
        this.episodes2 = episodes2;
    }
}
