package com.my.audio_video_fm.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.my.audio_video_fm.R;
import com.my.audio_video_fm.adapter.ViewPagerAdapter;
import com.my.audio_video_fm.adapter.ViewPagerAdapterStudio;

import java.util.List;

public class Studio extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_studio);

        TabLayout tabLayout = findViewById(R.id.tab_layout);
        ViewPager2 viewPager = findViewById(R.id.view_pager);

        ViewPagerAdapterStudio adapter = new ViewPagerAdapterStudio(this);
        viewPager.setAdapter(adapter);

        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> {
                    switch (position) {
                        case 0:
                            tab.setText("Record");
                            break;
                        case 1:
                            tab.setText("My Audios");
                            break;
                        case 2:
                            tab.setText("Analytics");
                            break;
                    }
                }).attach();
    }

}