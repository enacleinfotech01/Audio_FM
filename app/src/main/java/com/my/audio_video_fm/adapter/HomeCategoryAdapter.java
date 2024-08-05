package com.my.audio_video_fm.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.my.audio_video_fm.R;
import com.my.audio_video_fm.model.HomeCategory;

import java.util.List;

public class HomeCategoryAdapter extends RecyclerView.Adapter<HomeCategoryAdapter.CategoryViewHolder> {
    private List<HomeCategory> categories;
    private Context context;
    private ItemAdapter.OnItemClickListener itemClickListener;

    public
    HomeCategoryAdapter(List<HomeCategory> categories, Context context, ItemAdapter.OnItemClickListener itemClickListener) {
        this.categories = categories;
        this.context = context;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.category_item, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        HomeCategory category = categories.get(position);
        holder.categoryName.setText(category.getName());

        ItemAdapter itemAdapter = new ItemAdapter(category.getItems(), context, itemClickListener);
        holder.itemsRecyclerView.setAdapter(itemAdapter);
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public void updateCategories(List<HomeCategory> newCategories) {
        this.categories = newCategories;
        notifyDataSetChanged();
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder {
        TextView categoryName;
        RecyclerView itemsRecyclerView;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryName = itemView.findViewById(R.id.category_name);
            itemsRecyclerView = itemView.findViewById(R.id.items_recycler_view);
            itemsRecyclerView.setLayoutManager(new GridLayoutManager(context, 1, GridLayoutManager.HORIZONTAL, false));
        }
    }
}
