package com.example.ecovault_pi_mobile.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecovault_pi_mobile.R;
import com.example.ecovault_pi_mobile.model.Badge;

import java.util.List;

public class BadgeAdapter extends RecyclerView.Adapter<BadgeAdapter.BadgeViewHolder> {

    private final Context context;
    private final List<Badge> badges;

    public BadgeAdapter(Context context, List<Badge> badges) {
        this.context = context;
        this.badges = badges;
    }

    @NonNull
    @Override
    public BadgeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_badge, parent, false);
        return new BadgeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BadgeViewHolder holder, int position) {
        Badge badge = badges.get(position);
        holder.txtEmoji.setText(badge.getEmoji());
        holder.txtLabel.setText(badge.getLabel());

        if (badge.isUnlocked()) {
            holder.txtEmoji.setBackgroundResource(R.drawable.badge_bg_unlocked);
            holder.txtEmoji.setAlpha(1f);
        } else {
            holder.txtEmoji.setBackgroundResource(R.drawable.badge_bg_locked);
            holder.txtEmoji.setAlpha(0.5f);
        }
    }

    @Override
    public int getItemCount() {
        return badges.size();
    }

    static class BadgeViewHolder extends RecyclerView.ViewHolder {
        TextView txtEmoji, txtLabel;

        public BadgeViewHolder(@NonNull View itemView) {
            super(itemView);
            txtEmoji = itemView.findViewById(R.id.txtBadgeEmoji);
            txtLabel = itemView.findViewById(R.id.txtBadgeLabel);
        }
    }
}
