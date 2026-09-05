package com.example.ecovault_pi_mobile.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecovault_pi_mobile.R;
import com.example.ecovault_pi_mobile.model.CategoryItem;

import java.util.List;

public class CategoryPillsAdapter extends RecyclerView.Adapter<CategoryPillsAdapter.PillViewHolder> {

    public interface OnCategoryClickListener {
        void onCategoryClick(CategoryItem item);
    }

    private final Context context;
    private final List<CategoryItem> categories;
    private final OnCategoryClickListener listener;
    private int activePosition = 0;

    public CategoryPillsAdapter(Context context, List<CategoryItem> categories, OnCategoryClickListener listener) {
        this.context = context;
        this.categories = categories;
        this.listener = listener;

        for (int i = 0; i < categories.size(); i++) {
            if (categories.get(i).isActive()) {
                activePosition = i;
                break;
            }
        }
    }

    @NonNull
    @Override
    public PillViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_category_pill, parent, false);
        return new PillViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PillViewHolder holder, int position) {
        CategoryItem item = categories.get(position);

        holder.txtLabel.setText(item.getLabel());

        if (item.getPoints() != null && !item.getPoints().isEmpty()) {
            holder.txtPoints.setVisibility(View.VISIBLE);
            holder.txtPoints.setText(item.getPoints());
        } else {
            holder.txtPoints.setVisibility(View.GONE);
        }

        if (item.getIconRes() != 0) {
            holder.imgIcon.setVisibility(View.VISIBLE);
            holder.imgIcon.setImageResource(item.getIconRes());
        } else {
            holder.imgIcon.setVisibility(View.GONE);
        }

        boolean isActive = position == activePosition;

        holder.root.setBackgroundResource(isActive ? R.drawable.pill_bg_active : R.drawable.pill_bg_inactive);

        int textColor = context.getResources().getColor(
                isActive ? R.color.primary : R.color.muted_foreground
        );
        holder.txtLabel.setTextColor(textColor);
        holder.txtPoints.setTextColor(textColor);

        holder.root.setOnClickListener(v -> {
            int oldPosition = activePosition;
            activePosition = holder.getAdapterPosition();
            notifyItemChanged(oldPosition);
            notifyItemChanged(activePosition);
            listener.onCategoryClick(item);
        });
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    static class PillViewHolder extends RecyclerView.ViewHolder {
        LinearLayout root;
        ImageView imgIcon;
        TextView txtLabel;
        TextView txtPoints;

        public PillViewHolder(@NonNull View itemView) {
            super(itemView);
            root = (LinearLayout) itemView;
            imgIcon = itemView.findViewById(R.id.imgPillIcon);
            txtLabel = itemView.findViewById(R.id.txtPillLabel);
            txtPoints = itemView.findViewById(R.id.txtPillPoints);
        }
    }
}
