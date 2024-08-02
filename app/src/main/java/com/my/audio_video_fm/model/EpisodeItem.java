package com.my.audio_video_fm.model;

public class EpisodeItem {

    private int id;
    private String imageUrl;
    private String title;
    private String time;
    private int iconResId;
    private int lockIconResId; // New icon field
    private int diamondIconResId; // New diamond icon field
    private String permalinkText; // New permalink text field

    // Constructor with ID field and new icon fields
    public EpisodeItem(int id, String imageUrl, String title, String time, int iconResId, int lockIconResId, int diamondIconResId, String permalinkText) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.title = title;
        this.time = time;
        this.iconResId = iconResId;
        this.lockIconResId = lockIconResId;
        this.diamondIconResId = diamondIconResId;
        this.permalinkText = permalinkText;
    }

    // Getter for ID field
    public int getId() {
        return id;
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

    public int getLockIconResId() {
        return lockIconResId;
    }

    public int getDiamondIconResId() {
        return diamondIconResId;
    }

    public String getPermalinkText() {
        return permalinkText;
    }
}
