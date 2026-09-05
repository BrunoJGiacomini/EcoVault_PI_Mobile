package com.example.ecovault_pi_mobile.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecovault_pi_mobile.R;
import com.example.ecovault_pi_mobile.model.GuideCategoryItem;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.List;

public class GuideCategoryAdapter extends RecyclerView.Adapter<GuideCategoryAdapter.CategoryViewHolder> {

    private final Context context;
    private final List<GuideCategoryItem> categories;

    public GuideCategoryAdapter(Context context, List<GuideCategoryItem> categories) {
        this.context = context;
        this.categories = categories;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_guide_category, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        GuideCategoryItem item = categories.get(position);

        holder.frameIconBg.setBackgroundResource(item.getBgColorRes());
        holder.imgIcon.setImageResource(item.getIconRes());
        holder.imgIcon.setColorFilter(context.getColor(item.getIconTintColorRes()));
        holder.txtTitle.setText(item.getTitle());
        holder.txtTitle.setTextColor(context.getColor(item.getIconTintColorRes()));
        holder.txtDesc.setText(item.getDescription());

        holder.chipGroup.removeAllViews();
        for (String tag : item.getTags()) {
            Chip chip = new Chip(context);
            chip.setText(tag);
            chip.setTextSize(11.5f);
            chip.setChipBackgroundColorResource(R.color.chip_bg);
            chip.setChipStrokeWidth(0f);
            chip.setClickable(false);
            chip.setCheckable(false);
            holder.chipGroup.addView(chip);
        }
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    static class CategoryViewHolder extends RecyclerView.ViewHolder {
        FrameLayout frameIconBg;
        ImageView imgIcon;
        TextView txtTitle;
        TextView txtDesc;
        ChipGroup chipGroup;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            frameIconBg = itemView.findViewById(R.id.frameCatIconBg);
            imgIcon = itemView.findViewById(R.id.imgCatIcon);
            txtTitle = itemView.findViewById(R.id.txtCatTitle);
            txtDesc = itemView.findViewById(R.id.txtCatDesc);
            chipGroup = itemView.findViewById(R.id.chipGroupTags);
        }
    }
}
