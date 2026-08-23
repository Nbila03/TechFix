package com.example.techfix.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.techfix.R;
import com.example.techfix.model.RepairService;

import java.util.List;

public class ServiceAdapter
        extends RecyclerView.Adapter<ServiceAdapter.ServiceViewHolder> {

    private final List<RepairService> serviceList;
    private final OnServiceClickListener listener;

    public interface OnServiceClickListener {
        void onServiceClick(RepairService service);
    }

    public ServiceAdapter(
            List<RepairService> serviceList,
            OnServiceClickListener listener
    ) {
        this.serviceList = serviceList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ServiceViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType
    ) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_service, parent, false);

        return new ServiceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull ServiceViewHolder holder,
            int position
    ) {
        RepairService service = serviceList.get(position);

        holder.txtName.setText(service.getName());
        holder.txtDescription.setText(service.getDescription());
        holder.txtPrice.setText(service.getPrice());
        holder.imgService.setImageResource(service.getImageResId());

        holder.itemView.setOnClickListener(v ->
                listener.onServiceClick(service)
        );
    }

    @Override
    public int getItemCount() {
        return serviceList.size();
    }

    static class ServiceViewHolder extends RecyclerView.ViewHolder {

        ImageView imgService;
        TextView txtName;
        TextView txtDescription;
        TextView txtPrice;

        public ServiceViewHolder(@NonNull View itemView) {
            super(itemView);

            imgService = itemView.findViewById(R.id.imgService);
            txtName = itemView.findViewById(R.id.txtServiceItemName);
            txtDescription = itemView.findViewById(R.id.txtServiceItemDescription);
            txtPrice = itemView.findViewById(R.id.txtServiceItemPrice);
        }
    }
}