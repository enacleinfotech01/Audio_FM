package com.my.audio_video_fm.fragment;

import android.content.Intent;
import android.media.MediaPlayer;
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
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.my.audio_video_fm.R;
import com.my.audio_video_fm.activity.Episode;
import com.my.audio_video_fm.adapter.EpisodeAdapter;
import com.my.audio_video_fm.model.EpisodeItem;

import java.util.ArrayList;
import java.util.List;

public class PlayFragment extends Fragment {

    private ImageView targetImageView;
    private Button musicButton;
    private ImageView playMusicImageView;
    private TextView textView;
    private TextView readMoreView;
    private RecyclerView recyclerView;
    private EpisodeAdapter adapter;
    private ImageView bookmarkImageView;
    private List<EpisodeItem> episodeList;
    private MediaPlayer mediaPlayer;
    private boolean isPlaying = false;
    private boolean isExpanded = false;
    private String imageUrl;
    private ImageView image;
    private int currentIndex = 0;

    public static PlayFragment newInstance(String videoId, String imageUrl) {
        PlayFragment fragment = new PlayFragment();
        Bundle args = new Bundle();
        args.putString("video_id", videoId);
        args.putString("image_url", imageUrl);
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

        playMusicImageView = view.findViewById(R.id.playmusic);
        musicButton = view.findViewById(R.id.music);
        textView = view.findViewById(R.id.text);
        readMoreView = view.findViewById(R.id.read_more);
        image = view.findViewById(R.id.image);
        targetImageView = view.findViewById(R.id.image);
        bookmarkImageView = view.findViewById(R.id.bookmark);
        recyclerView = view.findViewById(R.id.episodes_recycler_view);

        textView.setMaxLines(3);
        textView.setEllipsize(TextUtils.TruncateAt.END);

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

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new EpisodeAdapter(episodeList, requireActivity());
        recyclerView.setAdapter(adapter);

        playMusicImageView.setOnClickListener(v -> togglePlayback());
        musicButton.setOnClickListener(v -> togglePlayback());

        image.setOnClickListener(v -> {
            if (episodeList != null && !episodeList.isEmpty()) {
                if (!isPlaying) {  // Check if the audio is paused
                    EpisodeItem selectedEpisode = episodeList.get(currentIndex);
                    Intent intent = new Intent(getActivity(), Episode.class);
                    intent.putExtra("image_url", selectedEpisode.getImageUrl());
                    intent.putExtra("title", selectedEpisode.getTitle());
                    startActivity(intent);
                } else {
                    Toast.makeText(getContext(), "Pause the audio first", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getContext(), "No episodes available", Toast.LENGTH_SHORT).show();
            }
        });

        bookmarkImageView.setImageResource(R.drawable.bookmarks_24dp_e8eaed_fill0_wght400_grad0_opsz24);
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
            updateUI(false);

            // Check if there are episodes available before starting the activity
            if (episodeList != null && !episodeList.isEmpty()) {
                EpisodeItem selectedEpisode = episodeList.get(currentIndex);
                Intent intent = new Intent(getActivity(), Episode.class);
                intent.putExtra("image_url", selectedEpisode.getImageUrl());
                intent.putExtra("title", selectedEpisode.getTitle());
                startActivity(intent);
            } else {
                Toast.makeText(getContext(), "No episodes available", Toast.LENGTH_SHORT).show();
            }
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

    private void populateData(String videoId, String imageUrl) {
        if (episodeList == null) {
            episodeList = new ArrayList<>();
        }

        episodeList.add(new EpisodeItem(1, imageUrl, "Episode 1", "10:00", R.drawable.download_24dp_e8eaed_fill0_wght400_grad0_opsz24, R.drawable.lock_24dp_e8eaed_fill0_wght400_grad0_opsz24, R.drawable.daimond, "Try Premium for free"));
        episodeList.add(new EpisodeItem(2, imageUrl, "Episode 2", "12:00", R.drawable.download_24dp_e8eaed_fill0_wght400_grad0_opsz24, R.drawable.lock_24dp_e8eaed_fill0_wght400_grad0_opsz24, R.drawable.daimond, "Try Premium for free"));
        episodeList.add(new EpisodeItem(3, imageUrl, "Episode 3", "14:00", R.drawable.download_24dp_e8eaed_fill0_wght400_grad0_opsz24, R.drawable.lock_24dp_e8eaed_fill0_wght400_grad0_opsz24, R.drawable.daimond, "Try Premium for free"));
        episodeList.add(new EpisodeItem(4, imageUrl, "Episode 4", "16:00", R.drawable.download_24dp_e8eaed_fill0_wght400_grad0_opsz24, R.drawable.lock_24dp_e8eaed_fill0_wght400_grad0_opsz24, R.drawable.daimond, "Try Premium for free"));
        episodeList.add(new EpisodeItem(5, imageUrl, "Episode 5", "18:00", R.drawable.download_24dp_e8eaed_fill0_wght400_grad0_opsz24, R.drawable.lock_24dp_e8eaed_fill0_wght400_grad0_opsz24, R.drawable.daimond, "Try Premium for free"));
        episodeList.add(new EpisodeItem(6, imageUrl, "Episode 6", "20:00", R.drawable.download_24dp_e8eaed_fill0_wght400_grad0_opsz24, R.drawable.lock_24dp_e8eaed_fill0_wght400_grad0_opsz24, R.drawable.daimond, "Try Premium for free"));

        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }
}
