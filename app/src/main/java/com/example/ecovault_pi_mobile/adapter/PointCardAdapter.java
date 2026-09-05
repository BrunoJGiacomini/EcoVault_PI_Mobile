package com.example.ecovault_pi_mobile.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecovault_pi_mobile.R;
import com.example.ecovault_pi_mobile.model.AcceptedItem;
import com.example.ecovault_pi_mobile.model.CollectionPoint;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.List;

public class PointCardAdapter extends RecyclerView.Adapter<PointCardAdapter.PointViewHolder> {

    public interface OnPointClickListener {
        void onPointClick(CollectionPoint point);
    }

    private final Context context;
    private final List<CollectionPoint> points;
    private final OnPointClickListener listener;

    public PointCardAdapter(Context context, List<CollectionPoint> points, OnPointClickListener listener) {
        this.context = context;
        this.points = points;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PointViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_point_card, parent, false);
        return new PointViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PointViewHolder holder, int position) {
        CollectionPoint point = points.get(position);

        holder.txtName.setText(point.getName());
        holder.txtAddress.setText(point.getAddress());
        holder.txtDistance.setText(point.getDistance() + " de você");

        if (point.isOpen()) {
            holder.txtOpenStatus.setText(point.getOpenStatus() != null ? point.getOpenStatus() : "Aberto agora");
            holder.txtOpenStatus.setBackgroundResource(R.drawable.status_badge_open);
            holder.txtOpenStatus.setTextColor(context.getColor(R.color.status_success_text));
        } else {
            holder.txtOpenStatus.setText(point.getOpenStatus() != null ? point.getOpenStatus() : "Fechado");
            holder.txtOpenStatus.setBackgroundResource(R.drawable.status_badge_closed);
            holder.txtOpenStatus.setTextColor(context.getColor(R.color.status_danger_text));
        }

        holder.chipGroup.removeAllViews();
        for (AcceptedItem item : point.getAcceptedItems()) {
            Chip chip = new Chip(context);
            chip.setText(item.getLabel());
            chip.setTextSize(11f);
            chip.setChipBackgroundColorResource(R.color.card);
            chip.setChipStrokeColorResource(R.color.border);
            chip.setChipStrokeWidth(1.5f);
            chip.setClickable(false);
            chip.setCheckable(false);
            if (item.getIconRes() != 0) {
                chip.setChipIconResource(item.getIconRes());
                chip.setChipIconVisible(true);
            }
            holder.chipGroup.addView(chip);
        }

        holder.root.setOnClickListener(v -> listener.onPointClick(point));
    }

    @Override
    public int getItemCount() {
        return points.size();
    }

    static class PointViewHolder extends RecyclerView.ViewHolder {
        LinearLayout root;
        TextView txtName, txtAddress, txtDistance, txtOpenStatus;
        ChipGroup chipGroup;

        public PointViewHolder(@NonNull View itemView) {
            super(itemView);
            root = (LinearLayout) itemView;
            txtName = itemView.findViewById(R.id.txtPointName);
            txtAddress = itemView.findViewById(R.id.txtPointAddress);
            txtDistance = itemView.findViewById(R.id.txtDistance);
            txtOpenStatus = itemView.findViewById(R.id.txtOpenStatus);
            chipGroup = itemView.findViewById(R.id.chipGroupItems);
        }
    }
}
