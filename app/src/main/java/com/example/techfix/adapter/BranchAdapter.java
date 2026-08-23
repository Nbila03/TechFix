package com.example.techfix.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.techfix.R;
import com.example.techfix.model.Branch;

import java.util.List;
import java.util.Locale;

public class BranchAdapter extends RecyclerView.Adapter<BranchAdapter.BranchViewHolder> {

    public interface OnBranchClickListener {
        void onBranchClick(Branch branch);
    }

    private final List<Branch> branches;
    private final OnBranchClickListener listener;

    public BranchAdapter(List<Branch> branches, OnBranchClickListener listener) {
        this.branches = branches;
        this.listener = listener;
    }

    @NonNull
    @Override
    public BranchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_branch, parent, false);
        return new BranchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BranchViewHolder holder, int position) {
        Branch branch = branches.get(position);
        holder.name.setText(branch.getBranchName());
        holder.address.setText(String.format("%s, %s", branch.getAddress(), branch.getCity()));

        if (branch.getDistanceKm() >= 0) {
            holder.distance.setVisibility(View.VISIBLE);
            holder.distance.setText(String.format(Locale.getDefault(), "%.1f km away", branch.getDistanceKm()));
        } else {
            holder.distance.setVisibility(View.GONE);
        }

        // open/closed pill, colored using the app palette instead of hardcoded hex
        holder.status.setText(branch.isActive() ? "OPEN" : "CLOSED");
        holder.status.setTextColor(ContextCompat.getColor(holder.itemView.getContext(),
                branch.isActive() ? R.color.techfix_success : R.color.techfix_error));

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onBranchClick(branch);
        });
    }

    @Override
    public int getItemCount() {
        return branches.size();
    }

    static class BranchViewHolder extends RecyclerView.ViewHolder {
        TextView name, address, distance, status;

        BranchViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tvBranchName);
            address = itemView.findViewById(R.id.tvBranchAddress);
            distance = itemView.findViewById(R.id.tvBranchDistance);
            status = itemView.findViewById(R.id.tvBranchStatus);
        }
    }
}