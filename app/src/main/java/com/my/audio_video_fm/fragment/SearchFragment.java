package com.my.audio_video_fm.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextWatcher;
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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.my.audio_video_fm.R;
import com.my.audio_video_fm.adapter.SearchCategoryAdapter;
import com.my.audio_video_fm.model.CategoryItem;
import com.my.audio_video_fm.model.SearchCategory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SearchFragment extends Fragment {
    private static final int REQUEST_CODE_SPEECH_INPUT = 1000;
    private static final int REQUEST_PERMISSION_CODE = 2000;

    private RecyclerView categoriesRecyclerView;
    private SearchCategoryAdapter searchCategoryAdapter;
    private List<SearchCategory> searchCategories;
    private List<SearchCategory> filteredCategories = new ArrayList<>();
    private OkHttpClient client = new OkHttpClient();
    private EditText searchEditText;
    private ImageView speakImageView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        categoriesRecyclerView = view.findViewById(R.id.categories_recycler_view);
        searchEditText = view.findViewById(R.id.editext);
        speakImageView = view.findViewById(R.id.speakuser);

        // Initialize adapter here
        searchCategoryAdapter = new SearchCategoryAdapter(requireContext(), filteredCategories);
        categoriesRecyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        categoriesRecyclerView.setAdapter(searchCategoryAdapter);

        fetchJsonData("https://api.npoint.io/3d04705d56c3d6463fc1");

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                filterCategories(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
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
                Log.e("SearchFragment", "Failed to fetch data: " + e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful() && response.body() != null) {
                    String jsonData = response.body().string();
                    Log.d("SearchFragment", "JSON data received: " + jsonData);

                    try {
                        searchCategories = parseJson(jsonData);

                        requireActivity().runOnUiThread(() -> {
                            filteredCategories.clear();
                            filteredCategories.addAll(searchCategories);
                            searchCategoryAdapter.notifyDataSetChanged();
                        });
                    } catch (Exception e) {
                        Log.e("SearchFragment", "Error parsing JSON: " + e.getMessage());
                    }
                } else {
                    Log.e("SearchFragment", "Response not successful: " + response.message());
                }
            }
        });
    }

    private List<SearchCategory> parseJson(String jsonData) {
        List<SearchCategory> categories = new ArrayList<>();
        Gson gson = new Gson();
        try {
            JsonObject jsonObject = JsonParser.parseString(jsonData).getAsJsonObject();
            JsonArray categoryArray = jsonObject.getAsJsonArray("category");

            for (JsonElement element : categoryArray) {
                JsonObject categoryObject = element.getAsJsonObject();
                String name = categoryObject.get("name").getAsString();
                String image = categoryObject.get("image").getAsString();

                JsonArray categoryItemArray = categoryObject.getAsJsonArray("categoryItem");
                List<CategoryItem> categoryItemItems = new ArrayList<>();
                for (JsonElement categoryItemElement : categoryItemArray) {
                    CategoryItem categoryItemItem = gson.fromJson(categoryItemElement, CategoryItem.class);
                    categoryItemItems.add(categoryItemItem);
                }

                categories.add(new SearchCategory(name, image, categoryItemItems));
            }
        } catch (JsonSyntaxException e) {
            Log.e("SearchFragment", "JSON syntax error: " + e.getMessage());
        } catch (Exception e) {
            Log.e("SearchFragment", "Error parsing JSON: " + e.getMessage());
        }
        return categories;
    }

    private void filterCategories(String query) {
        if (searchCategories != null && !searchCategories.isEmpty()) {
            List<SearchCategory> filteredList = new ArrayList<>();
            for (SearchCategory category : searchCategories) {
                if (category.getName().toLowerCase().contains(query.toLowerCase())) {
                    filteredList.add(category);
                }
            }

            filteredCategories.clear();
            filteredCategories.addAll(filteredList);
            searchCategoryAdapter.notifyDataSetChanged();

            if (filteredList.isEmpty()) {
                Toast.makeText(requireContext(), "No results found", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(requireContext(), "No categories available to filter", Toast.LENGTH_SHORT).show();
        }
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
