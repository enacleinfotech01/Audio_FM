package com.my.audio_video_fm.fragment;

import static android.content.Context.MODE_PRIVATE;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.my.audio_video_fm.Episode;
import com.my.audio_video_fm.R;
import com.my.audio_video_fm.adapter.EpisodeAdapter;
import com.my.audio_video_fm.model.EpisodeItem;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class PlayFragment extends Fragment {

    private ImageView targetImageView;
    Button music;
    private ImageView bookmarkImageView;
    private Set<Integer> bookmarkedMusicIds = new HashSet<>();
    private ImageView playMusicImageView;
    private boolean isPlaying = false;
    private Button playPauseButton;
    private TextView textView;
    private static final String PREFS_NAME = "bookmarks_prefs";
    private static final String BOOKMARKS_KEY = "bookmarked_music_ids";
    private String imageUrl;
    private CardView cardView;
    private RecyclerView recyclerView;
    private EpisodeAdapter adapter;
    private List<EpisodeItem> episodeList;
    private TextView readMoreView;
    private boolean isExpanded = false;
    private int currentIndex = 0; // Initialize the index

    public static PlayFragment newInstance(String videoId, String imageUrl) {
        PlayFragment fragment = new PlayFragment();
        Bundle args = new Bundle();
        args.putString("video_id", videoId);
        args.putString("image_url", imageUrl);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_play, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        playMusicImageView = view.findViewById(R.id.playmusic);
        textView = view.findViewById(R.id.text);
        readMoreView = view.findViewById(R.id.read_more);
        playPauseButton = view.findViewById(R.id.music);
        cardView = view.findViewById(R.id.cardview);
        targetImageView = view.findViewById(R.id.image);
        bookmarkImageView = view.findViewById(R.id.bookmark);
        playPauseButton = view.findViewById(R.id.music);

        playPauseButton.setOnClickListener(v -> togglePlayPause());
        // Set an initial state
        textView.setMaxLines(3); // Set initial maximum lines
        textView.setEllipsize(TextUtils.TruncateAt.END); // Show ellipsis when text is too long

        readMoreView.setOnClickListener(v -> toggleText());

        if (getArguments() != null) {
            String videoId = getArguments().getString("video_id");
            imageUrl = getArguments().getString("image_url");
            populateData(videoId, imageUrl);

            if (videoId != null && imageUrl != null) {
                displayThumbnail(imageUrl);
            } else {
                Toast.makeText(getContext(), "No media item provided", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getContext(), "No arguments provided", Toast.LENGTH_SHORT).show();
        }

        recyclerView = view.findViewById(R.id.episodes_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new EpisodeAdapter(episodeList, getContext());
        recyclerView.setAdapter(adapter);

        playPauseButton.setOnClickListener(v -> togglePlayPause());

        cardView.setOnClickListener(v -> {
            if (episodeList != null && !episodeList.isEmpty()) {
                EpisodeItem selectedEpisode = episodeList.get(currentIndex);
                Intent intent = new Intent(getActivity(), Episode.class);
                intent.putExtra("image_url", selectedEpisode.getImageUrl());
                intent.putExtra("title", selectedEpisode.getTitle());
               startActivity(intent);
            } else {
                Toast.makeText(getContext(), "No episodes available", Toast.LENGTH_SHORT).show();
            }
        });


        updateBookmarkIcon();

        bookmarkImageView.setOnClickListener(v -> {
            toggleBookmark();
            updateBookmarkIcon();
        });

        loadBookmarks();

        bookmarkImageView.setImageResource(R.drawable.bookmarks_24dp_e8eaed_fill0_wght400_grad0_opsz24); // Replace with a default icon if necessary

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
            playMusicImageView.setImageResource(R.drawable.play);
            pausePlayback();
        } else {
            playMusicImageView.setImageResource(R.drawable.pause);
            startPlayback();
        }
        isPlaying = !isPlaying;
    }

    private void startPlayback() {
        // Implement the logic to start playback
    }

    private void pausePlayback() {
        // Implement the logic to pause playback
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

    private void populateData(String videoId, String imageUrl) {
        if (episodeList == null) {
            episodeList = new ArrayList<>();
        }

        episodeList.add(new EpisodeItem(imageUrl, "Episode 1", "10:00", R.drawable.download_24dp_e8eaed_fill0_wght400_grad0_opsz24));
        episodeList.add(new EpisodeItem(imageUrl, "Episode 2", "12:00", R.drawable.download_24dp_e8eaed_fill0_wght400_grad0_opsz24));
        episodeList.add(new EpisodeItem(imageUrl, "Episode 3", "14:00", R.drawable.download_24dp_e8eaed_fill0_wght400_grad0_opsz24));
        episodeList.add(new EpisodeItem(imageUrl, "Episode 4", "16:00", R.drawable.download_24dp_e8eaed_fill0_wght400_grad0_opsz24));
        episodeList.add(new EpisodeItem(imageUrl, "Episode 5", "18:00", R.drawable.download_24dp_e8eaed_fill0_wght400_grad0_opsz24));
        episodeList.add(new EpisodeItem(imageUrl, "Episode 6", "20:00", R.drawable.download_24dp_e8eaed_fill0_wght400_grad0_opsz24));

        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    private void togglePlayPause() {
        if (isPlaying) {
            // Update to play icon and text
            playPauseButton.setText("Resume Episode 1");
            playPauseButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.play_arrow_24dp_e8eaed_fill0_wght400_grad0_opsz24, 0, 0, 0);
            // Pause playback
            pausePlayback();
        } else {
            // Update to pause icon and text
            playPauseButton.setText("Pause Episode 1");
            playPauseButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.pause_24dp_e8eaed_fill0_wght400_grad0_opsz24, 0, 0, 0);
            // Start playback
            startPlayback();
        }
        isPlaying = !isPlaying; // Toggle playback state
    }

    private void toggleBookmark() {
        if (episodeList != null && !episodeList.isEmpty() && currentIndex >= 0 && currentIndex < episodeList.size()) {
            int currentTrackId = episodeList.get(currentIndex).getIconResId();

            if (bookmarkedMusicIds.contains(currentTrackId)) {
                // Remove from bookmarks
                bookmarkedMusicIds.remove(currentTrackId);
                Toast.makeText(getContext(), "Removed from bookmarks", Toast.LENGTH_SHORT).show();
            } else {
                // Add to bookmarks
                bookmarkedMusicIds.add(currentTrackId);
                Toast.makeText(getContext(), "Added to bookmarks", Toast.LENGTH_SHORT).show();
            }

            // Save the updated bookmark state
            saveBookmarks();
            // Update the bookmark icon
            updateBookmarkIcon();
        } else {
            Toast.makeText(getContext(), "No episode selected", Toast.LENGTH_SHORT).show();
        }
    }


    private void updateBookmarkIcon() {
        if (episodeList != null && !episodeList.isEmpty() && currentIndex >= 0 && currentIndex < episodeList.size()) {
            int currentTrackId = episodeList.get(currentIndex).getIconResId();

            if (bookmarkedMusicIds.contains(currentTrackId)) {
                bookmarkImageView.setImageResource(R.drawable.bookmarks_24dp_e8eaed_fill0_wght400_grad0_opsz24); // Replace with your bookmarked icon
            } else {
                bookmarkImageView.setImageResource(R.drawable.bookmark_remove_24dp_e8eaed_fill0_wght400_grad0_opsz24); // Replace with your unbookmarked icon
            }
        } else {
            // Default icon when there is no valid episode
            bookmarkImageView.setImageResource(R.drawable.bookmarks_24dp_e8eaed_fill0_wght400_grad0_opsz24); // Replace with a default icon if necessary
        }
    }


    private void saveBookmarks() {
        SharedPreferences prefs = getActivity().getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putStringSet(BOOKMARKS_KEY, bookmarkedMusicIds.stream().map(String::valueOf).collect(Collectors.toSet()));
        editor.apply();
        Toast.makeText(getContext(), "Bookmarks saved", Toast.LENGTH_SHORT).show(); // Show a toast message
    }


    private void loadBookmarks() {
        SharedPreferences prefs = getActivity().getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        Set<String> bookmarkedSet = prefs.getStringSet(BOOKMARKS_KEY, new HashSet<>());
        bookmarkedMusicIds = bookmarkedSet.stream().map(Integer::valueOf).collect(Collectors.toSet());
    }



}
