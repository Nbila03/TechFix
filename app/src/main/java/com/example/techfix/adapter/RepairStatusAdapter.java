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

// Marks each repair stage using the repair's current status.
public class RepairStatusAdapter
        extends RecyclerView.Adapter<RepairStatusAdapter.StatusViewHolder> {

    private final String currentStatus;

    public RepairStatusAdapter(String currentStatus) {
        this.currentStatus = currentStatus;
    }

    @NonNull
    @Override
    public StatusViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(
                        R.layout.item_status,
                        parent,
                        false
                );

        return new StatusViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull StatusViewHolder holder,
            int position) {

        // Get the status stage for this position.
        String stage = RepairStatus.STAGE_ORDER[position];

        // Find the position of the current repair status.
        int currentIndex =
                RepairStatus.indexOf(currentStatus);

        String marker;
        int color;

        // Stages before the current status are completed.
        if (position < currentIndex) {

            marker = "✓";

            color = ContextCompat.getColor(
                    holder.itemView.getContext(),
                    R.color.techfix_success
            );

            // The current stage is highlighted.
        } else if (position == currentIndex) {

            marker = "●";

            color = ContextCompat.getColor(
                    holder.itemView.getContext(),
                    R.color.techfix_gold
            );

            // Stages after the current status are still pending.
        } else {

            marker = "○";

            color = ContextCompat.getColor(
                    holder.itemView.getContext(),
                    R.color.techfix_text_secondary
            );
        }

        // Set the marker and its colour.
        holder.marker.setText(marker);
        holder.marker.setTextColor(color);

        // Set the readable status name.
        String statusLabel =
                RepairStatus.label(stage);

        holder.label.setText(statusLabel);
        holder.label.setTextColor(color);
    }

    @Override
    public int getItemCount() {
        return RepairStatus.STAGE_ORDER.length;
    }

    static class StatusViewHolder
            extends RecyclerView.ViewHolder {

        TextView marker;
        TextView label;

        StatusViewHolder(@NonNull View itemView) {

            super(itemView);

            marker = itemView.findViewById(
                    R.id.tvStatusMarker
            );

            label = itemView.findViewById(
                    R.id.tvStatusLabel
            );
        }
    }
}