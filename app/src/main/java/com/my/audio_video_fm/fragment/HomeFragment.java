package com.my.audio_video_fm.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.my.audio_video_fm.R;
import com.my.audio_video_fm.activity.Profile;
import com.my.audio_video_fm.activity.StoreCoins;
import com.my.audio_video_fm.adapter.HomeCategoryAdapter;
import com.my.audio_video_fm.adapter.ViewPagerAdapter;
import com.my.audio_video_fm.bottomsheet.LanguageBottomSheetDialog;
import com.my.audio_video_fm.model.CategoryItem;
import com.my.audio_video_fm.model.Episode2;
import com.my.audio_video_fm.model.HomeCategory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HomeFragment extends Fragment {
    private static final String TAG = "HomeFragment";
    private ViewPager2 viewPager;
    private ImageView profile;
    private ImageView langues;
    private ImageView store;
    private Handler handler;
    private Runnable runnable;

    private RecyclerView categoriesRecyclerView;
    private HomeCategoryAdapter categoryAdapter;
    private List<HomeCategory> categories;
    private OkHttpClient client = new OkHttpClient();
    private static final String JSON_URL = "https://api.npoint.io/b51b1365f802d503b7fd";
    private List<Integer> imageList;
    private TextView search;
    private BottomNavigationView bottomNavigationView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        search = view.findViewById(R.id.search);
        profile = view.findViewById(R.id.profile);
        langues = view.findViewById(R.id.langues);
        store = view.findViewById(R.id.store);
        bottomNavigationView = requireActivity().findViewById(R.id.bottomnev);

        store.setOnClickListener(v -> {
            Intent intent = new Intent(requireActivity(), StoreCoins.class);
            startActivity(intent);
        });

        langues.setOnClickListener(v -> {
            LanguageBottomSheetDialog bottomSheetDialog = new LanguageBottomSheetDialog();
            bottomSheetDialog.setOnLanguageSelectedListener(language -> {
                Toast.makeText(requireContext(), "Selected: " + language, Toast.LENGTH_SHORT).show();
                // Handle language selection
            });
            bottomSheetDialog.show(getChildFragmentManager(), "LanguageBottomSheet");
        });

        // Initialize views
        initializeViews(view);

        profile.setOnClickListener(v -> {
            Intent intent = new Intent(requireActivity(), Profile.class);
            startActivity(intent);
        });

        // Setup ViewPager for image slider
        setupViewPager();

        // Fetch JSON data from the URL
        fetchJsonData(JSON_URL);

        search.setOnClickListener(v -> {
            // Navigate to SearchFragment using FragmentTransaction
            Fragment searchFragment = new SearchFragment();
            FragmentManager fragmentManager = getParentFragmentManager(); // Use getParentFragmentManager() if within a nested fragment
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragmentContainerView, searchFragment); // Replace with your container ID
            fragmentTransaction.addToBackStack(null); // Optional: Add to back stack to allow navigation back
            fragmentTransaction.commit();
        });

        return view;
    }

    private void initializeViews(View view) {
        viewPager = view.findViewById(R.id.viewpager);
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
        handler = new Handler(Looper.getMainLooper());
        runnable = new Runnable() {
            @Override
            public void run() {
                int currentPage = viewPager.getCurrentItem();
                int nextPage = (currentPage + 1) % imageList.size();
                viewPager.setCurrentItem(nextPage, true);
                handler.postDelayed(this, 3000); // Auto-scroll every 3 seconds
            }
        };

        // Register the page change callback to reset the auto-scroll timer on user scroll
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                handler.removeCallbacks(runnable);
                handler.postDelayed(runnable, 3000);
            }
        });

        // Start auto-scrolling
        handler.postDelayed(runnable, 3000);
    }

    private void fetchJsonData(String url) {
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

                    if (isAdded()) {
                        requireActivity().runOnUiThread(() -> setupRecyclerView());
                    } else {
                        Log.e(TAG, "Fragment not attached to activity");
                    }
                } else {
                    Log.e(TAG, "Response unsuccessful or body is null");
                }
            }
        });
    }

    private void setupRecyclerView() {
        categoryAdapter = new HomeCategoryAdapter(categories, requireContext(), item -> {
            Log.d(TAG, "Item clicked: " + item.getTitle());
            if ("video".equals(item.getTitle())) {
                String videoId = item.getDescription();
                String image = item.getImageUrl();
                Log.d(TAG, "Video ID: " + videoId);
            } else {
                Toast.makeText(requireContext(), "Not a video item: " + item.getTitle(), Toast.LENGTH_SHORT).show();
            }
        });
        categoriesRecyclerView.setAdapter(categoryAdapter);
        Log.d(TAG, "RecyclerView set up with category adapter");
    }

    private List<HomeCategory> parseJson(String jsonData) {
        Gson gson = new Gson();
        List<HomeCategory> categories = new ArrayList<>();
        try {
            JsonObject jsonObject = JsonParser.parseString(jsonData).getAsJsonObject();
            JsonArray categoryArray = jsonObject.getAsJsonArray("category");

            for (JsonElement categoryElement : categoryArray) {
                JsonObject categoryObject = categoryElement.getAsJsonObject();
                String name = categoryObject.get("name").getAsString();
                JsonArray categoryItemArray = categoryObject.getAsJsonArray("categoryItem");

                List<CategoryItem> categoryItems = new ArrayList<>();
                for (JsonElement itemElement : categoryItemArray) {
                    JsonObject itemObject = itemElement.getAsJsonObject();
                    int id = itemObject.get("id").getAsInt();
                    String title = itemObject.get("title").getAsString();
                    String description = itemObject.get("description").getAsString();
                    String imageUrl = itemObject.get("imageUrl").getAsString();

                    JsonArray episodesArray = itemObject.has("episodes") ? itemObject.getAsJsonArray("episodes") : new JsonArray();
                    List<Episode2> episodes = new ArrayList<>();
                    for (JsonElement episodeElement : episodesArray) {
                        JsonObject episodeObject = episodeElement.getAsJsonObject();
                        int episodeId = episodeObject.get("id").getAsInt();
                        String episodeTitle = episodeObject.get("title").getAsString();
                        String time = episodeObject.get("time").getAsString();
                        String episodeImageUrl = episodeObject.get("imageUrl").getAsString();
                        String audioUrl = episodeObject.has("audioUrl") ? episodeObject.get("audioUrl").getAsString() : null;

                        episodes.add(new Episode2(episodeId, episodeTitle, time, episodeImageUrl, audioUrl));
                    }

                    categoryItems.add(new CategoryItem(id, title, description, imageUrl, episodes));
                }

                categories.add(new HomeCategory(name, categoryItems));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return categories;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        handler.removeCallbacks(runnable);
    }
}
