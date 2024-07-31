package com.my.audio_video_fm.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.my.audio_video_fm.R;
import com.my.audio_video_fm.model.MediaItem;
import com.my.audio_video_fm.model.SharedViewModel;
import com.my.audio_video_fm.playvideo;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.MediaItemViewHolder> {

    public interface OnItemClickListener {


        void onItemClick(MediaItem item);
    }


    private List<MediaItem> mediaItems;
    private Context context;
    private OnItemClickListener listener;
    private SharedViewModel sharedViewModel;

    public ItemAdapter(List<MediaItem> mediaItems, Context context, OnItemClickListener listener) {
        this.mediaItems = mediaItems;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MediaItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.media_item, parent, false);
        return new MediaItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MediaItemViewHolder holder, int position) {
        MediaItem item = mediaItems.get(position);

        Glide.with(context)
                .load(item.getThumbnailUrl())
                .placeholder(R.drawable.ic_video)
                .error(R.drawable.ic_audiotrack)
                .into(holder.thumbnailImageView);
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, playvideo.class);
            intent.putExtra("VIDEO_ID", item.getVideoId());
            intent.putExtra("IMAGE_URL", item.getThumbnailUrl());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return mediaItems.size();
    }

    public class MediaItemViewHolder extends RecyclerView.ViewHolder {
        ImageView thumbnailImageView;

        public MediaItemViewHolder(@NonNull View itemView) {
            super(itemView);
            thumbnailImageView = itemView.findViewById(R.id.media_thumbnail);
        }
    }
}
