package com.my.audio_video_fm.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.my.audio_video_fm.R;
import com.my.audio_video_fm.adapter.ViewPagerAdapterStore;

public class StoreCoins extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_coins);

        TabLayout tabLayout = findViewById(R.id.tab);
        ViewPager2 viewPager = findViewById(R.id.view);

        ViewPagerAdapterStore adapter = new ViewPagerAdapterStore(this);
        viewPager.setAdapter(adapter);

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText("Buy Coins");
                    break;
                case 1:
                    tab.setText("Earn Coins");
                    break;
            }
        }).attach();
    }
}