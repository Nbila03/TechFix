package com.example.techfix.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.techfix.R;
import com.example.techfix.model.RepairRequest;
import com.example.techfix.model.RepairStatus;

import java.util.List;
import java.util.Locale;

public class RepairHistoryAdapter
        extends RecyclerView.Adapter<RepairHistoryAdapter.RepairViewHolder> {

    public interface OnRepairClickListener {
        void onRepairClick(RepairRequest repair);
    }

    private final List<RepairRequest> repairs;
    private final OnRepairClickListener listener;

    public RepairHistoryAdapter(
            List<RepairRequest> repairs,
            OnRepairClickListener listener) {

        this.repairs = repairs;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RepairViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(
                        R.layout.item_repair_history,
                        parent,
                        false
                );

        return new RepairViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull RepairViewHolder holder,
            int position) {

        RepairRequest repair = repairs.get(position);

        // Display device and service
        String title = String.format(
                Locale.getDefault(),
                "%s - %s",
                repair.getDeviceName(),
                repair.getServiceName()
        );

        holder.title.setText(title);

        // Display branch
        if (repair.getBranchName() != null) {

            holder.branch.setText(
                    repair.getBranchName()
            );

        } else {

            holder.branch.setText(
                    "Not yet assigned"
            );
        }

        // Display repair status
        String status = RepairStatus.label(
                repair.getStatus()
        );

        holder.status.setText(status);

        // Display repair cost
        if (repair.getFinalCost() > 0) {

            String finalCost = String.format(
                    Locale.getDefault(),
                    "Rs. %.2f",
                    repair.getFinalCost()
            );

            holder.cost.setText(finalCost);

        } else {

            String estimatedCost = String.format(
                    Locale.getDefault(),
                    "Est. Rs. %.2f",
                    repair.getEstimatedCost()
            );

            holder.cost.setText(estimatedCost);
        }

        // Handle repair selection
        holder.itemView.setOnClickListener(v -> {

            if (listener != null) {
                listener.onRepairClick(repair);
            }
        });
    }

    @Override
    public int getItemCount() {
        return repairs.size();
    }

    static class RepairViewHolder
            extends RecyclerView.ViewHolder {

        TextView title;
        TextView branch;
        TextView status;
        TextView cost;

        RepairViewHolder(@NonNull View itemView) {

            super(itemView);

            title = itemView.findViewById(
                    R.id.tvRepairTitle
            );

            branch = itemView.findViewById(
                    R.id.tvRepairBranch
            );

            status = itemView.findViewById(
                    R.id.tvRepairStatus
            );

            cost = itemView.findViewById(
                    R.id.tvRepairCost
            );
        }
    }
}