package com.my.audio_video_fm.model;

import java.io.Serializable;
import java.util.List;

public class MediaItem implements Serializable {
    private int id;
    private String type;
    private String category;
    private String title;
    private String thumbnailUrl;
    private String videoId;
    private List<Episode2> episodes2;// Add fields according to your JSON structure

    public List<Episode2> getEpisodes2() {
        return episodes2;
    }

    public void setEpisodes2(List<Episode2> episodes2) {
        this.episodes2 = episodes2;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }
}