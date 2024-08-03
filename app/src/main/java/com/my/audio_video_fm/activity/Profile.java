package com.my.audio_video_fm.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.my.audio_video_fm.R;
import com.my.audio_video_fm.adapter.ViewPagerAdapterProfile;

public class Profile extends AppCompatActivity {
    private TabLayout tabLayout;

    ExtendedFloatingActionButton studio;
    private ViewPager2 viewPager;

    ImageView setting;
    private ViewPagerAdapterProfile viewPagerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
studio=findViewById(R.id.studio);
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
        setting=findViewById(R.id.setting);
        viewPagerAdapter = new ViewPagerAdapterProfile(this);
        viewPager.setAdapter(viewPagerAdapter);

        studio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profile.this,Studio.class);
                startActivity(intent);
            }
        });
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        new TabLayoutMediator(tabLayout, viewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (position) {
                    case 0:
                        tab.setText("My List");
                        break;
                    case 1:
                        tab.setText("Activities");
                        break;
                }
            }
        }).attach();
    }
}