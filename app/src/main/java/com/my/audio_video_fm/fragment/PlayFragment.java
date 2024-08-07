package com.my.audio_video_fm.fragment;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.my.audio_video_fm.R;
import com.my.audio_video_fm.activity.EpisodeActivity;
import com.my.audio_video_fm.adapter.EpisodeAdapter2;
import com.my.audio_video_fm.model.Episode;
import com.my.audio_video_fm.model.Episode2;
import com.my.audio_video_fm.model.EpisodeItem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class PlayFragment extends Fragment {

    private ImageView targetImageView, playMusicImageView, imageView;
    private CircleImageView cirleimage;
    private Button musicButton;
    private TextView textView, readMoreView;
    private CardView cardView;
    private RecyclerView recyclerView;
    private EpisodeAdapter2 adapter2;
    private List<Episode2> episodes;
    private static final String API_URL = "https://api.npoint.io/3d04705d56c3d6463fc1";
    private static final String API_URL2 = "https://api.npoint.io/b51b1365f802d503b7fd";
    private OkHttpClient client = new OkHttpClient();
    private String selectedCategoryName;
    private boolean isExpanded = false;
    private boolean isPlaying = false;
    private MediaPlayer mediaPlayer;
    private int currentIndex = 0;


    public static PlayFragment newInstance(String categoryName, String categoryImageUrl, String jsonCategoryItems) {
        PlayFragment fragment = new PlayFragment();
        Bundle args = new Bundle();
        args.putString("CATEGORY_NAME", categoryName);
        args.putString("CATEGORY_IMAGE_URL", categoryImageUrl);
        args.putString("CATEGORY_ITEMS", jsonCategoryItems);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_play, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize views
        playMusicImageView = view.findViewById(R.id.playmusic);
        musicButton = view.findViewById(R.id.music);
        textView = view.findViewById(R.id.text);
        imageView = view.findViewById(R.id.image);
        readMoreView = view.findViewById(R.id.read_more);
        cardView = view.findViewById(R.id.cardview);
        cirleimage = view.findViewById(R.id.cirleimage);

        // Check arguments and set data
        if (getArguments() != null) {
            String categoryImageUrl = getArguments().getString("CATEGORY_IMAGE_URL");
            selectedCategoryName = getArguments().getString("CATEGORY_NAME");

            TextView categoryNameTextView = view.findViewById(R.id.category_name_textview);
            if (categoryNameTextView != null) {
                categoryNameTextView.setText(selectedCategoryName);
            } else {
                Log.e(TAG, "TextView with ID 'category_name_textview' is null");
            }
            //set cirleimage
            if (cirleimage != null) {
                if (categoryImageUrl != null && !categoryImageUrl.isEmpty()) {
                    Glide.with(this)
                            .load(categoryImageUrl)
                            .placeholder(R.drawable.audio) // Replace with your placeholder image
                            .error(R.drawable.ic_audiotrack) // Replace with your error image
                            .into(cirleimage);
                } else {
                    Log.e(TAG, "Category image URL is null or empty");
                }
            } else {
                Log.e(TAG, "ImageView with ID 'target_image_view' is null");
            }
            // Set category image
            if (imageView != null) {
                if (categoryImageUrl != null && !categoryImageUrl.isEmpty()) {
                    Glide.with(this)
                            .load(categoryImageUrl)
                            .placeholder(R.drawable.audio) // Replace with your placeholder image
                            .error(R.drawable.ic_audiotrack) // Replace with your error image
                            .into(imageView);
                } else {
                    Log.e(TAG, "Category image URL is null or empty");

                }
            } else {
                Log.e(TAG, "ImageView with ID 'target_image_view' is null");
            }
            episodes = new ArrayList<>();
            fetchJsonData(API_URL);
            fetchJsonData(API_URL2);

        } else {
            Toast.makeText(getContext(), "No arguments provided", Toast.LENGTH_SHORT).show();
        }

        // Setup RecyclerView
        recyclerView = view.findViewById(R.id.episodes_recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(requireActivity(), 1));
        adapter2 = new EpisodeAdapter2(requireActivity(), episodes);
        recyclerView.setAdapter(adapter2);
        simulatePlaybackCompletion(adapter2);

        // Setup click listeners
        playMusicImageView.setOnClickListener(v -> togglePlayback());
        musicButton.setOnClickListener(v -> togglePlayback());

        imageView.setOnClickListener(v -> {
            if (episodes != null && !episodes.isEmpty()) {
                if (!isPlaying) {
                    // Example to use
                    Episode2 selectedEpisode = episodes.get(0);

                    Intent intent = new Intent(getActivity(), EpisodeActivity.class);
                    intent.putExtra("IMAGE_URL", selectedEpisode.getImageUrl2()); // Ensure these keys match your data
                    intent.putExtra("TITLE", selectedEpisode.getTitle2());
                    intent.putExtra("AUDIO_URL", selectedEpisode.getAudioUrl()); // Pass the audio URL
                    startActivity(intent);
                } else {
                    Toast.makeText(getContext(), "Pause the playback first", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getContext(), "No episodes available", Toast.LENGTH_SHORT).show();
            }
        });

        //    playPauseButton.setOnClickListener(v -> togglePlayPause());
        textView.setMaxLines(3);
        textView.setEllipsize(TextUtils.TruncateAt.END);
        readMoreView.setOnClickListener(v -> toggleText());
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

                    requireActivity().runOnUiThread(() -> adapter2.notifyDataSetChanged());
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
                JsonArray categoryItemArray = categoryObject.getAsJsonArray("categoryItem");

                for (JsonElement categoryItemElement : categoryItemArray) {
                    JsonObject categoryItemObject = categoryItemElement.getAsJsonObject();

                    // Check if the categoryItem name matches the provided categoryItemName
                    String currentcategoryItemName = categoryItemObject.get("title").getAsString();
                    if (currentcategoryItemName.equals(selectedCategoryName)) {
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
                    }
                }
            }

        } catch (Exception e) {
            Log.e(TAG, "Error parsing JSON data", e);
        }
    }



    private void displayThumbnail(String thumbnailUrl) {
        Log.d("PlayFragment", "Image URL: " + thumbnailUrl);
        Glide.with(this)
                .load(thumbnailUrl)
                .placeholder(R.drawable.ic_video)
                .error(R.drawable.ic_audiotrack)
                .into(targetImageView);
    }

    private void togglePlayback() {
        if (isPlaying) {
            pausePlayback();
        } else {
            startPlayback();
        }
        isPlaying = !isPlaying;
    }

    private void initializeMediaPlayer() {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(getActivity(), R.raw.music1); // Replace with your actual audio file
            mediaPlayer.setOnCompletionListener(mp -> {
                isPlaying = false;
                updateUI(false);
            });
        }
    }

    private void startPlayback() {
        initializeMediaPlayer();
        if (!mediaPlayer.isPlaying()) {
            mediaPlayer.start();
            updateUI(true);
        }
    }

    private void pausePlayback() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            updateUI(false); // Update UI to reflect playback state

            // Check if there are episodes available before starting the activity
            if (episodes != null && !episodes.isEmpty() && currentIndex >= 0 && currentIndex < episodes.size()) {
                Episode2 selectedEpisode = episodes.get(currentIndex);
                Intent intent = new Intent(getActivity(), EpisodeActivity.class); // Ensure this matches your activity name
                intent.putExtra("IMAGE_URL", selectedEpisode.getImageUrl2()); // Ensure these keys match your data
                intent.putExtra("TITLE", selectedEpisode.getTitle2());
                startActivity(intent);
            } else {
                Toast.makeText(getContext(), "No episodes available or invalid index", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getContext(), "MediaPlayer is not playing", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateUI(boolean isPlaying) {
        int playPauseIcon = isPlaying ? R.drawable.pause_24dp_e8eaed_fill0_wght400_grad0_opsz24 : R.drawable.play_arrow_24dp_e8eaed_fill0_wght400_grad0_opsz24;
        playMusicImageView.setImageResource(playPauseIcon);
        musicButton.setText(isPlaying ? "Pause Episode 1" : "Resume Episode 1");
        musicButton.setCompoundDrawablesWithIntrinsicBounds(playPauseIcon, 0, 0, 0);
    }

    private void toggleText() {
        if (isExpanded) {
            textView.setMaxLines(3);
            textView.setEllipsize(TextUtils.TruncateAt.END);
            readMoreView.setText("Read More");
        } else {
            textView.setMaxLines(Integer.MAX_VALUE);
            textView.setEllipsize(null);
            readMoreView.setText("Read Less");
        }
        isExpanded = !isExpanded;
    }
    private void simulatePlaybackCompletion(EpisodeAdapter2 adapter2) {
        // Simulate playback completion after 5 seconds
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            // Assume playback is complete for the first item (position 0)
            adapter2.setPlaybackComplete(0);
        }, 5000); // 5 seconds delay for simulation
    }

}
