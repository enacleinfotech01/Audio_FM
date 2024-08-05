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
import com.my.audio_video_fm.activity.DisplayActivity;
import com.my.audio_video_fm.R;
import com.my.audio_video_fm.model.SearchCategory;

import java.util.List;

public class SearchCategoryAdapter extends RecyclerView.Adapter<SearchCategoryAdapter.ViewHolder> {

    private Context context;
    private List<SearchCategory> searchCategoryList;

    public SearchCategoryAdapter(Context context, List<SearchCategory> searchCategoryList) {
        this.context = context;
        this.searchCategoryList = searchCategoryList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_searchlist, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SearchCategory searchCategory = searchCategoryList.get(position);
        holder.categoryNameTextView.setText(searchCategory.getName());

        // Check if image URL is not null and valid
        String imageUrl = searchCategory.getImage();
        if (imageUrl != null && !imageUrl.isEmpty()) {
            Glide.with(context)
                    .load(imageUrl)
                    .placeholder(R.drawable.ic_audiotrack)
                    .into(holder.imageView);
        } else {
            holder.imageView.setImageResource(R.drawable.ic_audiotrack); // Set placeholder image
        }

        holder.imageView.setOnClickListener(v -> {
            // Create intent and pass the image URLs
            Intent intent = new Intent(context, DisplayActivity.class);
            intent.putExtra("CATEGORY_NAME", searchCategory.getName());
            intent.putExtra("CATEGORY_IMAGE_URL", imageUrl); // Pass the image URL

            context.startActivity(intent);

            // Pass CategoryItems as a JSON string
            Gson gson = new Gson();
            String jsonCategoryItems = gson.toJson(searchCategory.getcategoryItemItems());
            intent.putExtra("CATEGORY_ITEMS", jsonCategoryItems);
        });
    }

    @Override
    public int getItemCount() {
        return searchCategoryList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView categoryNameTextView;
        ImageView imageView;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryNameTextView = itemView.findViewById(R.id.category_name_textview);
            imageView = itemView.findViewById(R.id.image);
        }
    }
}
