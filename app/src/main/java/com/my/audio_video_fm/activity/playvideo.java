package com.my.audio_video_fm.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.my.audio_video_fm.R;
import com.my.audio_video_fm.fragment.PlayFragment;
import com.my.audio_video_fm.model.Episode2;

import java.lang.reflect.Type;
import java.util.List;

public class playvideo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playvideo);

        if (savedInstanceState == null) {
            Intent intent = getIntent();
            String categoryName = intent.getStringExtra("CATEGORY_NAME");
            String categoryImageUrl = intent.getStringExtra("CATEGORY_IMAGE_URL");
            String jsonCategoryItems = intent.getStringExtra("CATEGORY_ITEMS");

            PlayFragment playFragment = PlayFragment.newInstance(categoryName, categoryImageUrl, jsonCategoryItems);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, playFragment)
                    .commit();
        }
    }
}
