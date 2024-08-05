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
import com.google.gson.Gson;
import com.my.audio_video_fm.R;
import com.my.audio_video_fm.activity.EpisodeActivity;
import com.my.audio_video_fm.activity.playvideo;
import com.my.audio_video_fm.model.CategoryItem;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    private List<CategoryItem> categoryItems;
    private Context context;

    public CategoryAdapter(Context context, List<CategoryItem> categoryItems) {
        this.context = context;
        this.categoryItems = categoryItems;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CategoryItem categoryItem = categoryItems.get(position);

        holder.titleTextView.setText(categoryItem.getTitle());
        holder.description.setText(categoryItem.getDescription());

        Glide.with(context).load(categoryItem.getImageUrl()).into(holder.imageView);

        holder.itemView.setOnClickListener(v -> {
            // Create an Intent to start the PlayActivity
            Intent intent = new Intent(context, playvideo.class);
            intent.putExtra("CATEGORY_NAME", categoryItem.getTitle());
            intent.putExtra("VIDEO_ID", "your_video_id_here");
            intent.putExtra("IMAGE_URL", "your_image_url_here");
            Gson gson = new Gson();
            String jsonCategoryItems = gson.toJson(categoryItem.getEpisodes2());
            intent.putExtra("CATEGORY_ITEM", jsonCategoryItems);
            context.startActivity(intent);

        });

    }

    @Override
    public int getItemCount() {
        return categoryItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView,description;
        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.title);
            description = itemView.findViewById(R.id.description);
            imageView = itemView.findViewById(R.id.image);
        }
    }
}
