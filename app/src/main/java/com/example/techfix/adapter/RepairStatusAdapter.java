package com.example.techfix.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.techfix.R;
import com.example.techfix.model.RepairStatus;
// marks each stage as done (check), current (dot), or pending (circle)
// based on the repair's current status
public class RepairStatusAdapter extends RecyclerView.Adapter<RepairStatusAdapter.StatusViewHolder> {

    private final String currentStatus;

    public RepairStatusAdapter(String currentStatus) {
        this.currentStatus = currentStatus;
    }

    @NonNull
    @Override
    public StatusViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_status, parent, false);
        return new StatusViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StatusViewHolder holder, int position) {
        String stage = RepairStatus.STAGE_ORDER[position];
        int currentIndex = RepairStatus.indexOf(currentStatus);

        String marker;
        int color;
        if (position < currentIndex) {
            marker = "✓";
            color = ContextCompat.getColor(holder.itemView.getContext(), R.color.techfix_success);
        } else if (position == currentIndex) {
            marker = "●";
            color = ContextCompat.getColor(holder.itemView.getContext(), R.color.techfix_gold);
        } else {
            marker = "○";
            color = ContextCompat.getColor(holder.itemView.getContext(), R.color.techfix_text_secondary);
        }

        holder.marker.setText(marker);
        holder.marker.setTextColor(color);
        holder.label.setText(RepairStatus.label(stage));
        holder.label.setTextColor(color);
    }

    @Override
    public int getItemCount() {
        return RepairStatus.STAGE_ORDER.length;
    }

    static class StatusViewHolder extends RecyclerView.ViewHolder {
        TextView marker, label;

        StatusViewHolder(@NonNull View itemView) {
            super(itemView);
            marker = itemView.findViewById(R.id.tvStatusMarker);
            label = itemView.findViewById(R.id.tvStatusLabel);
        }
    }
}