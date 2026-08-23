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
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class BranchAdapter extends RecyclerView.Adapter<BranchAdapter.BranchViewHolder> {

    public interface OnBranchClickListener {
        void onBranchClick(Branch branch);
    }

    private final List<Branch> branches;
    private final OnBranchClickListener listener;

    public BranchAdapter(List<Branch> branches, OnBranchClickListener listener) {
        this.branches = branches != null
                ? new ArrayList<>(branches)
                : new ArrayList<>();

        this.listener = listener;
    }

    @NonNull
    @Override
    public BranchViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_branch, parent, false);

        return new BranchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull BranchViewHolder holder,
            int position) {

        Branch branch = branches.get(position);

        // Display basic branch information
        holder.name.setText(branch.getBranchName());
        holder.address.setText(
                String.format(
                        Locale.getDefault(),
                        "%s, %s",
                        branch.getAddress(),
                        branch.getCity()
                )
        );

        // Display branch distance when available
        displayDistance(holder, branch);

        // Display whether the branch is currently active
        displayBranchStatus(holder, branch);

        // Handle branch selection
        holder.itemView.setOnClickListener(v -> {

            int adapterPosition = holder.getBindingAdapterPosition();

            if (adapterPosition != RecyclerView.NO_POSITION
                    && adapterPosition < branches.size()
                    && listener != null) {

                listener.onBranchClick(branches.get(adapterPosition));
            }
        });
    }

    /**
     * Displays the distance from the customer to the branch.
     */
    private void displayDistance(
            BranchViewHolder holder,
            Branch branch) {

        if (branch.getDistanceKm() >= 0) {

            holder.distance.setVisibility(View.VISIBLE);

            holder.distance.setText(
                    String.format(
                            Locale.getDefault(),
                            "%.1f km away",
                            branch.getDistanceKm()
                    )
            );

        } else {

            holder.distance.setVisibility(View.GONE);
        }
    }

    /**
     * Updates the branch status text and colour.
     */
    private void displayBranchStatus(
            BranchViewHolder holder,
            Branch branch) {

        if (branch.isActive()) {

            holder.status.setText("OPEN");

            holder.status.setTextColor(
                    ContextCompat.getColor(
                            holder.itemView.getContext(),
                            R.color.techfix_success
                    )
            );

        } else {

            holder.status.setText("CLOSED");

            holder.status.setTextColor(
                    ContextCompat.getColor(
                            holder.itemView.getContext(),
                            R.color.techfix_error
                    )
            );
        }
    }

    /**
     * Replaces the current branch list.
     */
    public void setBranches(List<Branch> newBranches) {

        branches.clear();

        if (newBranches != null) {
            branches.addAll(newBranches);
        }

        notifyDataSetChanged();
    }

    /**
     * Adds a new branch to the adapter.
     */
    public void addBranch(Branch branch) {

        if (branch == null) {
            return;
        }

        branches.add(branch);
        notifyItemInserted(branches.size() - 1);
    }

    /**
     * Removes a branch from the adapter.
     */
    public void removeBranch(int position) {

        if (position >= 0 && position < branches.size()) {

            branches.remove(position);
            notifyItemRemoved(position);
        }
    }

    /**
     * Returns a branch at the requested position.
     */
    public Branch getBranchAt(int position) {

        if (position >= 0 && position < branches.size()) {
            return branches.get(position);
        }

        return null;
    }

    /**
     * Checks whether the adapter currently contains branches.
     */
    public boolean isEmpty() {
        return branches.isEmpty();
    }

    @Override
    public int getItemCount() {
        return branches.size();
    }

    static class BranchViewHolder extends RecyclerView.ViewHolder {

        private final TextView name;
        private final TextView address;
        private final TextView distance;
        private final TextView status;

        BranchViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.tvBranchName);
            address = itemView.findViewById(R.id.tvBranchAddress);
            distance = itemView.findViewById(R.id.tvBranchDistance);
            status = itemView.findViewById(R.id.tvBranchStatus);
        }
    }
}