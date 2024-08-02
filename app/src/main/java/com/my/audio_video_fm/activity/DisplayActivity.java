package com.my.audio_video_fm.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.my.audio_video_fm.R;
import com.my.audio_video_fm.adapter.CategoryAdapter;
import com.my.audio_video_fm.model.CategoryItem;
import com.my.audio_video_fm.model.SearchCategory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class DisplayActivity extends AppCompatActivity {

    private static final String TAG = "DisplayActivity";
    private TextView categoryNameTextView;
    private RecyclerView recyclerView;
    private static final String API_URL = "https://api.npoint.io/3d04705d56c3d6463fc1";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        categoryNameTextView = findViewById(R.id.category_name_textview);
        recyclerView = findViewById(R.id.category_list);

        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));

        String categoryName = getIntent().getStringExtra("CATEGORY_NAME");
        if (categoryName != null) {
            categoryNameTextView.setText(categoryName);
        }

    }
}
