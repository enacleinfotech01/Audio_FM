package com.my.audio_video_fm.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.my.audio_video_fm.R;
import com.my.audio_video_fm.activity.EpisodeActivity;
import com.my.audio_video_fm.activity.Fragment_container;
import com.my.audio_video_fm.model.Episode2;

import java.util.List;

public class EpisodeAdapter2 extends RecyclerView.Adapter<EpisodeAdapter2.ViewHolder> {
    private final List<Episode2> episodes;
    private final Context context;
    private int completedPosition = -1; // Keeps track of which item has completed playback

    public EpisodeAdapter2(Context context, List<Episode2> episodes) {
        this.context = context;
        this.episodes = episodes;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_episode, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Episode2 episode = episodes.get(position);

        // Load image using Glide
        Glide.with(context)
                .load(episode.getImageUrl2())
                .placeholder(R.drawable.arrow_back_24dp_ff000000_fill0_wght400_grad0_opsz24)
                .into(holder.imageView);

        // Set title and time
        holder.titleTextView.setText(episode.getTitle2());
        holder.timeTextView.setText(episode.getTime2());

        // Determine if the episode is completed
        boolean isCompleted = completedPosition == position;
        boolean isPremium = episode.getId2() == 1;

        // Update view visibility and text based on the status
        holder.lockIcon.setVisibility(isPremium ? View.GONE : View.VISIBLE);
        holder.diamondIcon.setVisibility(isPremium ? View.GONE : View.VISIBLE);
        holder.permalinkText.setVisibility(isPremium ? View.GONE : View.VISIBLE);

        holder.permalinkText.setText(isPremium ? "" : (isCompleted ? "Completed" : "Try Premium for free"));

        // Set item click listener
        holder.itemView.setOnClickListener(v -> {
            if (position == 0) {
                Intent intent = new Intent(context, EpisodeActivity.class);
                intent.putExtra("IMAGE_URL", episode.getImageUrl2());
                intent.putExtra("title", episode.getTitle2());
                intent.putExtra("AUDIO_URL", episode.getAudioUrl());
                context.startActivity(intent);
            } else {
                Intent intent = new Intent(context, Fragment_container.class);
                intent.putExtra("ID", episode.getId2()); // Include ID in intent extras
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return episodes.size();
    }

    // Call this method when playback is completed
    public void setPlaybackComplete(int position) {
        completedPosition = position;
        notifyDataSetChanged(); // Refresh the RecyclerView to reflect changes
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView titleTextView;
        TextView timeTextView;
        ImageView itemIcon;
        ImageView lockIcon;
        ImageView diamondIcon;
        TextView permalinkText;

        ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.item_image);
            titleTextView = itemView.findViewById(R.id.item_title);
            timeTextView = itemView.findViewById(R.id.item_time);
            itemIcon = itemView.findViewById(R.id.item_icon);
            lockIcon = itemView.findViewById(R.id.lock);
            diamondIcon = itemView.findViewById(R.id.dimond);
            permalinkText = itemView.findViewById(R.id.permalink_text);
        }
    }
}
