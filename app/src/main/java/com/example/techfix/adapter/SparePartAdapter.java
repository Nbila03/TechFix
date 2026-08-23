package com.example.techfix.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.techfix.R;
import com.example.techfix.model.SparePart;
import com.example.techfix.management.SparePartFormActivity;
import java.util.List;

public class SparePartAdapter
        extends RecyclerView.Adapter<SparePartAdapter.SparePartViewHolder> {

    private Context context;
    private List<SparePart> spareParts;

    public SparePartAdapter(
            Context context,
            List<SparePart> spareParts) {

        this.context = context;
        this.spareParts = spareParts;
    }

    @NonNull
    @Override
    public SparePartViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType) {

        View view = LayoutInflater.from(context)
                .inflate(
                        R.layout.item_spare_part,
                        parent,
                        false
                );

        return new SparePartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull SparePartViewHolder holder,
            int position) {

        SparePart part = spareParts.get(position);

        // Part name
        holder.tvPartName.setText(
                part.getPartName()
        );

        // Compatible device
        holder.tvCompatibleDevice.setText(
                part.getCompatibleDevice()
        );

        // Part ID
        holder.tvPartId.setText(
                "Part ID: " + part.getPartId()
        );

        // Quantity
        holder.tvQuantity.setText(
                part.getQuantity() + " units"
        );

        // Unit price
        holder.tvUnitPrice.setText(
                "LKR " + part.getUnitPrice()
        );

        // Availability
        if (part.isAvailable()) {

            holder.tvAvailability.setText(
                    "Available"
            );

            holder.tvAvailability.setTextColor(
                    context.getColor(
                            R.color.techfix_success
                    )
            );

        } else {

            holder.tvAvailability.setText(
                    "Unavailable"
            );

            holder.tvAvailability.setTextColor(
                    context.getColor(
                            R.color.techfix_error
                    )
            );
        }

        // Low stock
        if (part.getQuantity() <= 5) {

            holder.tvQuantity.setTextColor(
                    context.getColor(
                            R.color.techfix_error
                    )
            );

        } else {

            holder.tvQuantity.setTextColor(
                    context.getColor(
                            R.color.techfix_success
                    )
            );
        }

        // CARD CLICK
        // Open the form in EDIT mode

        holder.itemView.setOnClickListener(v -> {

            Intent intent = new Intent(
                    context,
                    SparePartFormActivity.class
            );

            intent.putExtra(
                    "partId",
                    part.getPartId()
            );

            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {

        return spareParts.size();
    }

    // VIEW HOLDER

    public static class SparePartViewHolder
            extends RecyclerView.ViewHolder {

        TextView tvPartName;
        TextView tvCompatibleDevice;
        TextView tvPartId;
        TextView tvQuantity;
        TextView tvAvailability;
        TextView tvUnitPrice;

        public SparePartViewHolder(
                @NonNull View itemView) {

            super(itemView);

            tvPartName =
                    itemView.findViewById(
                            R.id.tvPartName
                    );

            tvCompatibleDevice =
                    itemView.findViewById(
                            R.id.tvCompatibleDevice
                    );

            tvPartId =
                    itemView.findViewById(
                            R.id.tvPartId
                    );

            tvQuantity =
                    itemView.findViewById(
                            R.id.tvQuantity
                    );

            tvAvailability =
                    itemView.findViewById(
                            R.id.tvAvailability
                    );

            tvUnitPrice =
                    itemView.findViewById(
                            R.id.tvUnitPrice
                    );
        }
    }
}

