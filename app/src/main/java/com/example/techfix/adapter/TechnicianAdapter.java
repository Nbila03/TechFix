package com.example.techfix.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.techfix.R;
import com.example.techfix.model.Technician;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

public class TechnicianAdapter
        extends RecyclerView.Adapter<TechnicianAdapter.TechnicianViewHolder> {

    public interface OnTechnicianClickListener {
        void onTechnicianClick(Technician technician);
    }

    public interface OnEditClickListener {
        void onEditClick(Technician technician);
    }

    public interface OnDeleteClickListener {
        void onDeleteClick(Technician technician);
    }

    private final List<Technician> technicianList = new ArrayList<>();
    private final List<Technician> allTechnicians = new ArrayList<>();

    private final OnTechnicianClickListener clickListener;
    private final OnEditClickListener editListener;
    private final OnDeleteClickListener deleteListener;

    public TechnicianAdapter(
            List<Technician> technicianList,
            OnTechnicianClickListener clickListener,
            OnEditClickListener editListener,
            OnDeleteClickListener deleteListener) {

        if (technicianList != null) {
            this.technicianList.addAll(technicianList);
            this.allTechnicians.addAll(technicianList);
        }

        this.clickListener = clickListener;
        this.editListener = editListener;
        this.deleteListener = deleteListener;
    }

    @NonNull
    @Override
    public TechnicianViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(
                        R.layout.item_technician,
                        parent,
                        false
                );

        return new TechnicianViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull TechnicianViewHolder holder,
            int position) {

        holder.bind(
                technicianList.get(position)
        );
    }

    @Override
    public int getItemCount() {
        return technicianList.size();
    }

    public void updateList(List<Technician> technicians) {

        technicianList.clear();
        allTechnicians.clear();

        if (technicians != null) {
            technicianList.addAll(technicians);
            allTechnicians.addAll(technicians);
        }

        notifyDataSetChanged();
    }

    public void filter(String query) {

        String searchQuery = query == null
                ? ""
                : query.trim().toLowerCase();

        technicianList.clear();

        if (searchQuery.isEmpty()) {

            technicianList.addAll(allTechnicians);

        } else {

            for (Technician technician : allTechnicians) {

                String name =
                        technician.getTechnicianName() == null
                                ? ""
                                : technician.getTechnicianName().toLowerCase();

                String specialization =
                        technician.getSpecialization() == null
                                ? ""
                                : technician.getSpecialization().toLowerCase();

                String phone =
                        technician.getPhone() == null
                                ? ""
                                : technician.getPhone().toLowerCase();

                String id =
                        String.valueOf(
                                technician.getTechnicianId()
                        );

                if (name.contains(searchQuery)
                        || specialization.contains(searchQuery)
                        || phone.contains(searchQuery)
                        || id.contains(searchQuery)) {

                    technicianList.add(technician);
                }
            }
        }

        notifyDataSetChanged();
    }

    class TechnicianViewHolder
            extends RecyclerView.ViewHolder {

        TextView tvInitial;
        TextView tvTechnicianName;
        TextView tvSpecialization;
        TextView tvStatus;
        TextView tvPhone;
        TextView tvBranch;

        MaterialButton btnEdit;
        MaterialButton btnDelete;

        TechnicianViewHolder(@NonNull View itemView) {
            super(itemView);

            tvInitial =
                    itemView.findViewById(
                            R.id.tvTechnicianInitial
                    );

            tvTechnicianName =
                    itemView.findViewById(
                            R.id.tvTechnicianName
                    );

            tvSpecialization =
                    itemView.findViewById(
                            R.id.tvTechnicianSpecialization
                    );

            tvStatus =
                    itemView.findViewById(
                            R.id.tvTechnicianStatus
                    );

            tvPhone =
                    itemView.findViewById(
                            R.id.tvTechnicianPhone
                    );

            tvBranch =
                    itemView.findViewById(
                            R.id.tvTechnicianBranch
                    );

            btnEdit =
                    itemView.findViewById(
                            R.id.btnEditTechnician
                    );

            btnDelete =
                    itemView.findViewById(
                            R.id.btnDeleteTechnician
                    );
        }

        void bind(Technician technician) {

            String name =
                    technician.getTechnicianName();

            if (name == null || name.trim().isEmpty()) {
                name = "Unknown Technician";
            }

            tvTechnicianName.setText(name);

            tvInitial.setText(
                    name.substring(0, 1).toUpperCase()
            );

            String specialization =
                    technician.getSpecialization();

            if (specialization == null
                    || specialization.trim().isEmpty()) {

                specialization = "General Technician";
            }

            tvSpecialization.setText(
                    specialization
            );

            String phone =
                    technician.getPhone();

            if (phone == null
                    || phone.trim().isEmpty()) {

                phone = "No phone number";
            }

            tvPhone.setText(phone);

            tvBranch.setText(
                    "Branch ID: "
                            + technician.getBranchId()
            );

            if (technician.isAvailable()) {

                tvStatus.setText("AVAILABLE");

            } else {

                tvStatus.setText("BUSY");
            }

            itemView.setOnClickListener(v -> {

                if (clickListener != null) {
                    clickListener.onTechnicianClick(
                            technician
                    );
                }
            });

            btnEdit.setOnClickListener(v -> {

                if (editListener != null) {
                    editListener.onEditClick(
                            technician
                    );
                }
            });

            btnDelete.setOnClickListener(v -> {

                if (deleteListener != null) {
                    deleteListener.onDeleteClick(
                            technician
                    );
                }
            });
        }
    }
}