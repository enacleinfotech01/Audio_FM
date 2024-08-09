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
import com.google.gson.Gson;
import com.my.audio_video_fm.R;
import com.my.audio_video_fm.model.CategoryItem;
import com.my.audio_video_fm.activity.playvideo;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.MediaItemViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(CategoryItem item);
    }
    private List<CategoryItem> mediaItems;
    private Context context;
    private OnItemClickListener listener;


    public ItemAdapter(List<CategoryItem> mediaItems, Context context, OnItemClickListener listener) {
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
        CategoryItem item = mediaItems.get(position);

        Glide.with(context)
                .load(item.getImageUrl())
                .placeholder(R.drawable.ic_video)
                .error(R.drawable.ic_audiotrack)
                .into(holder.thumbnailImageView);
        holder.itemView.setOnClickListener(v -> {
            // Create an Intent to start the PlayActivity
            Intent intent = new Intent(context, playvideo.class);
            intent.putExtra("CATEGORY_IMAGE_URL", item.getImageUrl()); // Pass the image URL
            intent.putExtra("CATEGORY_NAME", item.getTitle());
            intent.putExtra("VIDEO_ID", "your_video_id_here");
            intent.putExtra("IMAGE_URL", "your_image_url_here");
            Gson gson = new Gson();
            String jsonCategoryItems = gson.toJson(item.getEpisodes2());
            intent.putExtra("CATEGORY_ITEM", jsonCategoryItems);
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
