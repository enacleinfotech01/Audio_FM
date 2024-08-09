package com.my.audio_video_fm.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import com.my.audio_video_fm.R;
import com.my.audio_video_fm.Video;
import com.my.audio_video_fm.adapter.VideoAdapter;

import java.util.ArrayList;
import java.util.List;

public class ActivityReels extends AppCompatActivity {
    private ViewPager2 viewPager2;
    private VideoAdapter videoPagerAdapter;
    private List<Video> videoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reels);

        viewPager2 = findViewById(R.id.viewpager2); // Ensure you have a ViewPager2 with this ID in your layout

        videoList = new ArrayList<>();
        // Add Video objects to the list with direct video URLs
        videoList.add(new Video("https://www.youtube.com/shorts/EGWNXCRFiwg?feature=share", "Video Title 1"));
        videoList.add(new Video("https://www.youtube.com/shorts/EGWNXCRFiwg?feature=share", "Video Title 2"));
        videoList.add(new Video("https://www.youtube.com/shorts/EGWNXCRFiwg?feature=share", "Video Title 3"));

        videoPagerAdapter = new VideoAdapter(videoList);
        viewPager2.setAdapter(videoPagerAdapter);
    }
}
