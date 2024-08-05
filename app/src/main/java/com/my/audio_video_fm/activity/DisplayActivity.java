package com.my.audio_video_fm.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.my.audio_video_fm.R;
import com.my.audio_video_fm.adapter.CategoryAdapter;
import com.my.audio_video_fm.model.CategoryItem;
import com.my.audio_video_fm.model.Episode2;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DisplayActivity extends AppCompatActivity {

    private static final String TAG = "DisplayActivity";
    private TextView categoryNameTextView;
    private RecyclerView recyclerView;
    private static final String API_URL = "https://api.npoint.io/3d04705d56c3d6463fc1";
    private OkHttpClient client = new OkHttpClient();
    private List<CategoryItem> categoryItems;
    private String selectedCategoryName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        categoryNameTextView = findViewById(R.id.category_name_textview);
        recyclerView = findViewById(R.id.category_list);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1)); // Adjust span count if needed

        selectedCategoryName = getIntent().getStringExtra("CATEGORY_NAME");
        categoryNameTextView.setText(selectedCategoryName);

        categoryItems = new ArrayList<>();
        fetchJsonData(API_URL);
    }

    private void fetchJsonData(String apiUrl) {
        Request request = new Request.Builder().url(apiUrl).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.e(TAG, "Failed to fetch JSON data", e);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful() && response.body() != null) {
                    String jsonData = response.body().string();
                    Log.d(TAG, "JSON data fetched successfully");
                    parseJson(jsonData);

                    runOnUiThread(() -> setupRecyclerView());
                } else {
                    Log.e(TAG, "Response unsuccessful or body is null");
                }
            }
        });
    }

    private void parseJson(String jsonData) {
        Gson gson = new Gson();
        try {
            JsonObject jsonObject = JsonParser.parseString(jsonData).getAsJsonObject();
            JsonArray categoryArray = jsonObject.getAsJsonArray("category");

            for (JsonElement categoryElement : categoryArray) {
                JsonObject categoryObject = categoryElement.getAsJsonObject();
                String name = categoryObject.get("name").getAsString();
                JsonArray categoryItemArray = categoryObject.getAsJsonArray("categoryItem");

                if (name.equals(selectedCategoryName)) {
                    for (JsonElement categoryItemElement : categoryItemArray) {
                        JsonObject categoryItemObject = categoryItemElement.getAsJsonObject();
                        int id = categoryItemObject.get("id").getAsInt();
                        String title = categoryItemObject.get("title").getAsString();
                        String imageUrl = categoryItemObject.get("imageUrl").getAsString();
                        String description = categoryItemObject.has("description") ? categoryItemObject.get("description").getAsString() : "";

                        List<Episode2> episodes = new ArrayList<>();
                        if (categoryItemObject.has("episodes")) {
                            JsonArray episodesArray = categoryItemObject.getAsJsonArray("episodes");
                            for (JsonElement episodeElement : episodesArray) {
                                JsonObject episodeObject = episodeElement.getAsJsonObject();
                                int episodeId = episodeObject.get("id").getAsInt();
                                String episodeTitle = episodeObject.get("title").getAsString();
                                String episodeTime = episodeObject.get("time").getAsString();
                                String episodeImageUrl = episodeObject.get("imageUrl").getAsString();
                                String episodeAudioUrl = episodeObject.has("audioUrl") ? episodeObject.get("audioUrl").getAsString() : null;

                                Episode2 episode = new Episode2(episodeId, episodeTitle, episodeTime, episodeImageUrl, episodeAudioUrl);
                                episodes.add(episode);
                            }
                        }

                        CategoryItem item = new CategoryItem(id, title, description, imageUrl, episodes);
                        categoryItems.add(item);
                    }
                }
            }

            Log.d(TAG, "JSON parsing completed successfully");
        } catch (Exception e) {
            Log.e(TAG, "Error parsing JSON data", e);
        }
    }

    private void setupRecyclerView() {
        CategoryAdapter adapter = new CategoryAdapter(this, categoryItems);
        recyclerView.setAdapter(adapter);
    }
}
