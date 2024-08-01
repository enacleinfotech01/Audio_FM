package com.my.audio_video_fm.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.my.audio_video_fm.Profile;
import com.my.audio_video_fm.R;
import com.my.audio_video_fm.adapter.CategoryAdapter;
import com.my.audio_video_fm.adapter.ViewPagerAdapter;
import com.my.audio_video_fm.model.Category;
import com.my.audio_video_fm.model.MediaItem;
import com.my.audio_video_fm.playvideo;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;

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

public class HomeFragment extends Fragment {
    private static final String TAG = "HomeFragment";
    private ViewPager2 viewPager;
    ImageView profile;
    private DotsIndicator dotsIndicator;
    private Handler handler;
    private Runnable runnable;

    private RecyclerView categoriesRecyclerView;
    private CategoryAdapter categoryAdapter;
    private List<Category> categories;
    private OkHttpClient client = new OkHttpClient();
    private static final String JSON_URL = "https://api.npoint.io/dbb28723c94bbbc7c5e5";
    private List<Integer> imageList;
    TextView search;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        search = view.findViewById(R.id.search);
        profile = view.findViewById(R.id.profile);
        // Initialize views
        initializeViews(view);

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireActivity(), Profile.class);
                startActivity(intent);
            }
        });

        // Setup ViewPager for image slider
        setupViewPager();

        // Fetch JSON data from the URL
        fetchJsonData(JSON_URL);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = NavHostFragment.findNavController(HomeFragment.this);
                navController.navigate(R.id.searchFragment);

            }
        });
        return view;
    }

    private void initializeViews(View view) {
        viewPager = view.findViewById(R.id.viewpager);
        dotsIndicator = view.findViewById(R.id.dots_indicator);
        categoriesRecyclerView = view.findViewById(R.id.categories_recycler_view);
        categoriesRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
    }

    private void setupViewPager() {
        imageList = new ArrayList<>();
        imageList.add(R.drawable.image);
        imageList.add(R.drawable.image2);
        imageList.add(R.drawable.image3);

        ViewPagerAdapter adapter = new ViewPagerAdapter(imageList);
        viewPager.setAdapter(adapter);
        dotsIndicator.setViewPager2(viewPager);

        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                int currentPage = viewPager.getCurrentItem();
                int nextPage = (currentPage + 1) % imageList.size();
                viewPager.setCurrentItem(nextPage, true);
                handler.postDelayed(this, 3000); // Auto-scroll every 3 seconds
            }
        };
        handler.postDelayed(runnable, 3000);
    }

    private void fetchJsonData(String url) {
        // Create a request to fetch data from the provided URL
        Request request = new Request.Builder().url(url).build();
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
                    categories = parseJson(jsonData);

                    // Update UI on the main thread
                    requireActivity().runOnUiThread(() -> setupRecyclerView());
                } else {
                    Log.e(TAG, "Response unsuccessful or body is null");
                }
            }
        });
    }

    private void setupRecyclerView() {
        categoryAdapter = new CategoryAdapter(categories, requireContext(), item -> {
            Log.d(TAG, "Item clicked: " + item.getType());
            if ("video".equals(item.getType())) {
                String videoId = item.getVideoId();
                String image = item.getThumbnailUrl();
                Log.d(TAG, "Video ID: " + videoId);
                openPlayFragment(videoId, image);
            } else {
                Toast.makeText(requireContext(), "Not a video item: " + item.getType(), Toast.LENGTH_SHORT).show();
            }
        });
        categoriesRecyclerView.setAdapter(categoryAdapter);
        Log.d(TAG, "RecyclerView set up with category adapter");
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

            // Add audio categories to the list
            for (Map.Entry<String, List<MediaItem>> entry : audioCategoryMap.entrySet()) {
                categories.add(new Category(entry.getKey(), entry.getValue()));
            }

            // Add video categories to the list
            for (Map.Entry<String, List<MediaItem>> entry : videoCategoryMap.entrySet()) {
                categories.add(new Category(entry.getKey(), entry.getValue()));
            }

            Log.d(TAG, "JSON parsing completed successfully");
        } catch (Exception e) {
            Log.e(TAG, "Error parsing JSON data", e);
        }
        return categories;
    }

    private void openPlayFragment(String videoId, String imageUrl) {
        Intent intent = new Intent(requireActivity(), playvideo.class);
        intent.putExtra("VIDEO_ID", videoId);
        intent.putExtra("IMAGE_URL", imageUrl);
        startActivity(intent);
        Log.d(TAG, "Opening play fragment for video ID: " + videoId);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        handler.removeCallbacks(runnable);
        Log.d(TAG, "Handler callbacks removed");
    }
}
