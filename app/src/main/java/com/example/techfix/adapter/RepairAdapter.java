package com.example.techfix.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.techfix.R;
import com.example.techfix.model.RepairRequest;

import java.util.ArrayList;
import java.util.List;

public class RepairAdapter extends RecyclerView.Adapter<RepairAdapter.RepairViewHolder> {

    private List<RepairRequest> repairList;
    private final List<RepairRequest> originalList;

    private OnRepairClickListener listener;

    public interface OnRepairClickListener {
        void onRepairClick(RepairRequest repair);
    }

    public RepairAdapter(
            List<RepairRequest> repairList,
            OnRepairClickListener listener) {

        this.repairList = repairList;
        this.originalList = new ArrayList<>(repairList);
        this.listener = listener;
    }

    @NonNull
    @Override
    public RepairViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_repair, parent, false);

        return new RepairViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull RepairViewHolder holder,
            int position) {

        RepairRequest repair = repairList.get(position);

        // Repair ID
        holder.tvRepairId.setText(
                "Repair #" + repair.getRepairId()
        );

        // Device
        String deviceName = repair.getDeviceName();

        if (deviceName == null || deviceName.trim().isEmpty()) {
            deviceName = "Device #" + repair.getDeviceId();
        }

        holder.tvDeviceName.setText(deviceName);

        // Status
        String status = repair.getStatus();

        if (status == null || status.trim().isEmpty()) {
            status = "UNKNOWN";
        }

        holder.tvRepairStatus.setText(
                status.replace("_", " ")
        );

        // Service
        String serviceName = repair.getServiceName();

        if (serviceName == null || serviceName.trim().isEmpty()) {
            serviceName = "Service #" + repair.getServiceId();
        }

        holder.tvServiceName.setText(serviceName);

        // Technician
        if (repair.getTechnicianId() != null) {

            holder.tvTechnician.setText(
                    "Technician ID: " + repair.getTechnicianId()
            );

        } else {

            holder.tvTechnician.setText(
                    "Technician: Not Assigned"
            );
        }

        // Cost
        double cost = repair.getFinalCost();

        if (cost <= 0) {
            cost = repair.getEstimatedCost();
        }

        holder.tvRepairCost.setText(
                String.format("LKR %.2f", cost)
        );

        // Appointment
        String date = repair.getAppointmentDate();
        String time = repair.getAppointmentTime();

        if (date != null && !date.trim().isEmpty()
                && time != null && !time.trim().isEmpty()) {

            holder.tvAppointment.setText(
                    "Appointment: " + date + " • " + time
            );

        } else if (date != null && !date.trim().isEmpty()) {

            holder.tvAppointment.setText(
                    "Appointment: " + date
            );

        } else {

            holder.tvAppointment.setText(
                    "Appointment: Not scheduled"
            );
        }

        // Card click
        holder.itemView.setOnClickListener(v -> {

            if (listener != null) {
                listener.onRepairClick(repair);
            }

        });
    }

    @Override
    public int getItemCount() {
        return repairList.size();
    }

    // UPDATE DATA

    public void updateList(List<RepairRequest> newList) {

        repairList = new ArrayList<>(newList);

        originalList.clear();
        originalList.addAll(newList);

        notifyDataSetChanged();
    }

    // SEARCH

    public void filter(String query) {

        String searchText = query.toLowerCase().trim();

        List<RepairRequest> filteredList = new ArrayList<>();

        if (searchText.isEmpty()) {

            filteredList.addAll(originalList);

        } else {

            for (RepairRequest repair : originalList) {

                String repairId =
                        String.valueOf(repair.getRepairId());

                String deviceName =
                        repair.getDeviceName() == null
                                ? ""
                                : repair.getDeviceName().toLowerCase();

                String serviceName =
                        repair.getServiceName() == null
                                ? ""
                                : repair.getServiceName().toLowerCase();

                if (repairId.contains(searchText)
                        || deviceName.contains(searchText)
                        || serviceName.contains(searchText)) {

                    filteredList.add(repair);
                }
            }
        }

        repairList = filteredList;

        notifyDataSetChanged();
    }

    // FILTER BY STATUS

    public void filterByStatus(String status) {

        if (status.equalsIgnoreCase("ALL")) {

            repairList = new ArrayList<>(originalList);

        } else {

            List<RepairRequest> filteredList = new ArrayList<>();

            for (RepairRequest repair : originalList) {

                if (repair.getStatus() != null
                        && repair.getStatus().equalsIgnoreCase(status)) {

                    filteredList.add(repair);
                }
            }

            repairList = filteredList;
        }

        notifyDataSetChanged();
    }

    // VIEW HOLDER

    static class RepairViewHolder
            extends RecyclerView.ViewHolder {

        TextView tvRepairId;
        TextView tvDeviceName;
        TextView tvRepairStatus;
        TextView tvServiceName;
        TextView tvTechnician;
        TextView tvRepairCost;
        TextView tvAppointment;

        public RepairViewHolder(@NonNull View itemView) {

            super(itemView);

            tvRepairId =
                    itemView.findViewById(R.id.tvRepairId);

            tvDeviceName =
                    itemView.findViewById(R.id.tvDeviceName);

            tvRepairStatus =
                    itemView.findViewById(R.id.tvRepairStatus);

            tvServiceName =
                    itemView.findViewById(R.id.tvServiceName);

            tvTechnician =
                    itemView.findViewById(R.id.tvTechnician);

            tvRepairCost =
                    itemView.findViewById(R.id.tvRepairCost);

            tvAppointment =
                    itemView.findViewById(R.id.tvAppointment);
        }
    }
}