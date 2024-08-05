package com.my.audio_video_fm.adapter;

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
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Episode2 episode = episodes.get(position);

        // Load image using Glide
        Glide.with(context)
                .load(episode.getImageUrl2())
                .placeholder(R.drawable.arrow_back_24dp_ff000000_fill0_wght400_grad0_opsz24)
                .into(holder.imageView);

        // Set title and time
        holder.titleTextView.setText(episode.getTitle2());
        holder.timeTextView.setText(episode.getTime2());

        // Set item click listener
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position == 0) {
                    Intent intent = new Intent(context, EpisodeActivity.class);
                    intent.putExtra("IMAGE_URL", episode.getImageUrl2());
                    intent.putExtra("title", episode.getTitle2());
                    intent.putExtra("AUDIO_URL",episode.getAudioUrl());
                    context.startActivity(intent);
                }else {
                    Intent intent = new Intent(context, Fragment_container.class);
                    intent.putExtra("ID", episode.getId2()); // Include ID in intent extras
                    context.startActivity(intent);
                }
            }
        });
        // Set the visibility and content of the lock icon, diamond icon, and permalink text
        // Here I'm assuming you have some condition to show/hide these views
        if (episode.getId2() == 1) {
            boolean showLockIcon = false; // Replace with your actual condition
            boolean showDiamondIcon = false; // Replace with your actual condition
            boolean showPermalinkText = false; // Replace with your actual condition

            holder.lockIcon.setVisibility(showLockIcon ? View.VISIBLE : View.GONE);
            holder.diamondIcon.setVisibility(showDiamondIcon ? View.VISIBLE : View.GONE);
            holder.permalinkText.setVisibility(showPermalinkText ? View.VISIBLE : View.GONE);
            holder.permalinkText.setText("Try Premium for free"); // Replace with your actual text
        } else {
            boolean showLockIcon = true; // Replace with your actual condition
            boolean showDiamondIcon = true; // Replace with your actual condition
            boolean showPermalinkText = true; // Replace with your actual condition

            holder.lockIcon.setVisibility(showLockIcon ? View.VISIBLE : View.GONE);
            holder.diamondIcon.setVisibility(showDiamondIcon ? View.VISIBLE : View.GONE);
            holder.permalinkText.setVisibility(showPermalinkText ? View.VISIBLE : View.GONE);
            holder.permalinkText.setText("Try Premium for free"); // Replace with your actual text
        }
    }

    @Override
    public int getItemCount() {
        return episodes.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView titleTextView;
        TextView timeTextView;
        ImageView itemIcon;
        ImageView lockIcon; // New ImageView for lock icon
        ImageView diamondIcon; // New ImageView for diamond icon
        TextView permalinkText;

        ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.item_image);
            titleTextView = itemView.findViewById(R.id.item_title);
            timeTextView = itemView.findViewById(R.id.item_time);
            itemIcon = itemView.findViewById(R.id.item_icon);
            lockIcon = itemView.findViewById(R.id.lock); // Find the new ImageView
            diamondIcon = itemView.findViewById(R.id.dimond); // Find the new ImageView
            permalinkText = itemView.findViewById(R.id.permalink_text); // Find the new TextView
        }
    }
}
