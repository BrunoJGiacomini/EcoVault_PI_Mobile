package com.example.ecovault_pi_mobile.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecovault_pi_mobile.R;
import com.example.ecovault_pi_mobile.model.AcceptedItem;

import java.util.List;

public class AcceptedItemAdapter extends RecyclerView.Adapter<AcceptedItemAdapter.ItemViewHolder> {

    private final Context context;
    private final List<AcceptedItem> items;

    public AcceptedItemAdapter(Context context, List<AcceptedItem> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_accepted_item, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        AcceptedItem item = items.get(position);
        holder.imgIcon.setImageResource(item.getIconRes());
        holder.txtLabel.setText(item.getLabel());
        holder.txtPoints.setText("+" + item.getPointsPerDisposal() + " pts / descarte");
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {
        ImageView imgIcon;
        TextView txtLabel, txtPoints;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            imgIcon = itemView.findViewById(R.id.imgAcceptedIcon);
            txtLabel = itemView.findViewById(R.id.txtAcceptedLabel);
            txtPoints = itemView.findViewById(R.id.txtAcceptedPoints);
        }
    }
}
