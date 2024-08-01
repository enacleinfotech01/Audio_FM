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
import com.my.audio_video_fm.Episode;
import com.my.audio_video_fm.R;
import com.my.audio_video_fm.model.EpisodeItem;


import java.util.List;

public class EpisodeAdapter extends RecyclerView.Adapter<EpisodeAdapter.EpisodeViewHolder> {

    public EpisodeAdapter(List<EpisodeItem> episodeList, Context context) {
        this.episodeList = episodeList;
        this.context = context;

    }

    private List<EpisodeItem> episodeList;
    private Context context;






    @NonNull
    @Override
    public EpisodeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_episode, parent, false);
        return new EpisodeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EpisodeViewHolder holder, int position) {
        EpisodeItem item = episodeList.get(position);

        // Load image using Glide
        Glide.with(context)
                .load(item.getImageUrl())
                .placeholder(R.drawable.arrow_back_24dp_ff000000_fill0_wght400_grad0_opsz24)
                .into(holder.itemImage);

        // Set title and time
        holder.itemTitle.setText(item.getTitle());
        holder.itemTime.setText(item.getTime());

        // Set icon
        holder.itemIcon.setImageResource(item.getIconResId());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Episode.class);

                intent.putExtra("IMAGE_URL", item.getImageUrl());
                intent.putExtra("title",item.getTitle());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return episodeList.size();
    }

    public static class EpisodeViewHolder extends RecyclerView.ViewHolder {
        ImageView itemImage;
        TextView itemTitle;
        TextView itemTime;
        ImageView itemIcon;

        public EpisodeViewHolder(@NonNull View itemView) {
            super(itemView);
            itemImage = itemView.findViewById(R.id.item_image);
            itemTitle = itemView.findViewById(R.id.item_title);
            itemTime = itemView.findViewById(R.id.item_time);
            itemIcon = itemView.findViewById(R.id.item_icon);
        }
    }
}
