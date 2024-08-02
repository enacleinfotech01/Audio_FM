// DisplayActivity.java
package com.my.audio_video_fm;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.my.audio_video_fm.adapter.CategoryAdapter;

import java.util.ArrayList;

public class DisplayActivity extends AppCompatActivity {

    private TextView categoryNameTextView;
    private RecyclerView recyclerView;
    private ArrayList<String> imageUrls;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        categoryNameTextView = findViewById(R.id.category_name_textview);
        recyclerView = findViewById(R.id.category_list);

        // Set up RecyclerView with GridLayoutManager
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));

        Intent intent = getIntent();
        if (intent != null) {
            String categoryName = intent.getStringExtra("CATEGORY_NAME");
            imageUrls = intent.getStringArrayListExtra("imageUrls");

            if (categoryName != null) {
                categoryNameTextView.setText(categoryName);
            }

            if (imageUrls != null) {
                // Create and set adapter for RecyclerView
                CategoryAdapter adapter = new CategoryAdapter(this, imageUrls);
                recyclerView.setAdapter(adapter);
            }
        }
    }
}
