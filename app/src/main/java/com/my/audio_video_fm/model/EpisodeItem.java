package com.my.audio_video_fm.model;

public class EpisodeItem {
    private String imageUrl;
    private String title;
    private String time;
    private int iconResId;

    public EpisodeItem(String imageUrl, String title, String time, int iconResId) {
        this.imageUrl = imageUrl;
        this.title = title;
        this.time = time;
        this.iconResId = iconResId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getTime() {
        return time;
    }

    public int getIconResId() {
        return iconResId;
    }
}
