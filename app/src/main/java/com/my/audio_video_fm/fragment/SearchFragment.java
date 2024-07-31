package com.my.audio_video_fm.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.my.audio_video_fm.R;
import com.my.audio_video_fm.adapter.CategoryAdapter;
import com.my.audio_video_fm.model.Category;
import com.my.audio_video_fm.model.MediaItem;
import com.my.audio_video_fm.playvideo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SearchFragment extends Fragment {
    private static final int REQUEST_CODE_SPEECH_INPUT = 1000;
    private static final int REQUEST_PERMISSION_CODE = 2000;

    private RecyclerView categoriesRecyclerView;
    private CategoryAdapter categoryAdapter;
    private List<Category> categories;
    private List<Category> filteredCategories = new ArrayList<>();
    private OkHttpClient client = new OkHttpClient();
    private EditText searchEditText;
    private ImageView speakImageView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        categoriesRecyclerView = view.findViewById(R.id.categories_recycler_view);
        searchEditText = view.findViewById(R.id.editext);
        speakImageView = view.findViewById(R.id.speakuser);

        categoriesRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        fetchJsonData("https://api.npoint.io/dbb28723c94bbbc7c5e5");

        searchEditText.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                filterCategories(charSequence.toString());
            }

            @Override
            public void afterTextChanged(android.text.Editable editable) {
            }
        });

        speakImageView.setOnClickListener(v -> {
            Log.d("SearchFragment", "Speak button clicked");
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.RECORD_AUDIO)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(requireActivity(),
                        new String[]{Manifest.permission.RECORD_AUDIO}, REQUEST_PERMISSION_CODE);
            } else {
                promptSpeechInput();
            }
        });

        return view;
    }

    private void fetchJsonData(String url) {
        Request request = new Request.Builder().url(url).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
                Log.e("SearchFragment", "Failed to fetch data", e);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful() && response.body() != null) {
                    String jsonData = response.body().string();
                    categories = parseJson(jsonData);

                    requireActivity().runOnUiThread(() -> {
                        filteredCategories.clear();
                        filteredCategories.addAll(categories);
                        categoryAdapter = new CategoryAdapter(filteredCategories, requireContext(), item -> {
                            Log.d("ItemType", "Item Type: " + item.getType());

                            if ("video".equals(item.getType())) {
                                String videoId = item.getVideoId();
                                String image = item.getThumbnailUrl();
                                Log.d("VideoID", "Video ID: " + videoId);
                                openPlayFragment(videoId, image);
                            } else {
                                Toast.makeText(requireContext(), "Not a video item: " + item.getType(), Toast.LENGTH_SHORT).show();
                            }
                        });

                        categoriesRecyclerView.setAdapter(categoryAdapter);
                    });
                } else {
                    Log.e("SearchFragment", "Response not successful");
                }
            }
        });
    }

    private List<Category> parseJson(String jsonData) {
        Gson gson = new Gson();
        List<Category> categories = new ArrayList<>();

        try {
            JsonObject jsonObject = JsonParser.parseString(jsonData).getAsJsonObject();
            JsonArray mediaItemsArray = jsonObject.getAsJsonArray("media_items");

            Map<String, List<MediaItem>> audioCategoryMap = new HashMap<>();
            Map<String, List<MediaItem>> videoCategoryMap = new HashMap<>();

            for (JsonElement element : mediaItemsArray) {
                MediaItem item = gson.fromJson(element, MediaItem.class);
                if ("audio".equals(item.getType())) {
                    audioCategoryMap.computeIfAbsent(item.getCategory(), k -> new ArrayList<>()).add(item);
                } else if ("video".equals(item.getType())) {
                    videoCategoryMap.computeIfAbsent(item.getCategory(), k -> new ArrayList<>()).add(item);
                }
            }

            for (Map.Entry<String, List<MediaItem>> entry : audioCategoryMap.entrySet()) {
                categories.add(new Category(entry.getKey(), entry.getValue()));
            }

            for (Map.Entry<String, List<MediaItem>> entry : videoCategoryMap.entrySet()) {
                categories.add(new Category(entry.getKey(), entry.getValue()));
            }

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("SearchFragment", "Failed to parse JSON", e);
        }

        return categories;
    }

    private void filterCategories(String query) {
        if (categories != null && !categories.isEmpty()) {
            List<Category> filteredList = new ArrayList<>();
            for (Category category : categories) {
                if (category.getName().toLowerCase().contains(query.toLowerCase())) {
                    filteredList.add(category);
                }
            }

            filteredCategories.clear();
            filteredCategories.addAll(filteredList);
            categoryAdapter.notifyDataSetChanged();

            if (filteredList.isEmpty()) {
                Toast.makeText(requireContext(), "No results found", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(requireContext(), "No categories available to filter", Toast.LENGTH_SHORT).show();
        }
    }

    private void openPlayFragment(String videoId, String imageUrl) {
        Intent intent = new Intent(requireActivity(), playvideo.class);
        intent.putExtra("VIDEO_ID", videoId);
        intent.putExtra("IMAGE_URL", imageUrl);
        startActivity(intent);
    }

    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak now");
        if (intent.resolveActivity(requireContext().getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT);
        } else {
            Toast.makeText(requireContext(), "Speech recognition not available", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_SPEECH_INPUT && resultCode == getActivity().RESULT_OK && data != null) {
            List<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (result != null && !result.isEmpty()) {
                String spokenText = result.get(0);
                searchEditText.setText(spokenText);
                filterCategories(spokenText);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                promptSpeechInput();
            } else {
                Toast.makeText(requireContext(), "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
