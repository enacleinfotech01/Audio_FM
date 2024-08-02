package com.my.audio_video_fm.api;

import com.my.audio_video_fm.model.CategoryItem;

import java.util.List;

public class ApiService {
    public static class ApiResponse {
        private List<CategoryItem> UHD;
        private String name;
        private String image;

        public List<CategoryItem> getUHD() {
            return UHD;
        }

        public void setUHD(List<CategoryItem> UHD) {
            this.UHD = UHD;
        }

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
    }
}
