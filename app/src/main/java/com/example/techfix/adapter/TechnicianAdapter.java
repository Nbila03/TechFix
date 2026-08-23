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

    // =========================================================
    // LISTENERS
    // =========================================================

    public interface OnTechnicianClickListener {
        void onTechnicianClick(Technician technician);
    }

    public interface OnEditClickListener {
        void onEditClick(Technician technician);
    }

    public interface OnDeleteClickListener {
        void onDeleteClick(Technician technician);
    }


    // =========================================================
    // VARIABLES
    // =========================================================

    private final List<Technician> technicianList;
    private final List<Technician> allTechnicians;

    private final OnTechnicianClickListener clickListener;
    private final OnEditClickListener editListener;
    private final OnDeleteClickListener deleteListener;


    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public TechnicianAdapter(
            List<Technician> technicianList,
            OnTechnicianClickListener clickListener,
            OnEditClickListener editListener,
            OnDeleteClickListener deleteListener) {

        this.technicianList = technicianList;

        this.allTechnicians =
                new ArrayList<>(technicianList);

        this.clickListener = clickListener;
        this.editListener = editListener;
        this.deleteListener = deleteListener;
    }


    // =========================================================
    // VIEW HOLDER
    // =========================================================

    @NonNull
    @Override
    public TechnicianViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType) {

        View view =
                LayoutInflater.from(parent.getContext())
                        .inflate(
                                R.layout.item_technician,
                                parent,
                                false
                        );

        return new TechnicianViewHolder(view);
    }


    // =========================================================
    // BIND DATA
    // =========================================================

    @Override
    public void onBindViewHolder(
            @NonNull TechnicianViewHolder holder,
            int position) {

        Technician technician =
                technicianList.get(position);

        holder.bind(technician);
    }


    // =========================================================
    // ITEM COUNT
    // =========================================================

    @Override
    public int getItemCount() {

        return technicianList.size();
    }


    // =========================================================
    // UPDATE LIST
    // =========================================================

    public void updateList(
            List<Technician> technicians) {

        technicianList.clear();

        if (technicians != null) {

            technicianList.addAll(
                    technicians
            );
        }

        allTechnicians.clear();

        if (technicians != null) {

            allTechnicians.addAll(
                    technicians
            );
        }

        notifyDataSetChanged();
    }


    // =========================================================
    // SEARCH
    // =========================================================

    public void filter(String query) {

        String searchQuery =
                query == null
                        ? ""
                        : query.trim().toLowerCase();

        technicianList.clear();

        if (searchQuery.isEmpty()) {

            technicianList.addAll(
                    allTechnicians
            );

        } else {

            for (Technician technician :
                    allTechnicians) {

                String name =
                        technician.getTechnicianName() == null
                                ? ""
                                : technician.getTechnicianName()
                                .toLowerCase();

                String specialization =
                        technician.getSpecialization() == null
                                ? ""
                                : technician.getSpecialization()
                                .toLowerCase();

                String phone =
                        technician.getPhone() == null
                                ? ""
                                : technician.getPhone()
                                .toLowerCase();

                String technicianId =
                        String.valueOf(
                                technician.getTechnicianId()
                        );

                if (name.contains(searchQuery)
                        || specialization.contains(searchQuery)
                        || phone.contains(searchQuery)
                        || technicianId.contains(searchQuery)) {

                    technicianList.add(
                            technician
                    );
                }
            }
        }

        notifyDataSetChanged();
    }


    // =========================================================
    // VIEW HOLDER CLASS
    // =========================================================

    class TechnicianViewHolder
            extends RecyclerView.ViewHolder {

        private final TextView tvInitial;
        private final TextView tvTechnicianName;
        private final TextView tvSpecialization;
        private final TextView tvStatus;
        private final TextView tvPhone;
        private final TextView tvBranch;
        private final MaterialButton btnEdit;
        private final MaterialButton btnDelete;


        TechnicianViewHolder(
                @NonNull View itemView) {

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


        // =====================================================
        // BIND TECHNICIAN
        // =====================================================

        void bind(
                Technician technician) {

            // -------------------------------------------------
            // NAME
            // -------------------------------------------------

            String name =
                    technician.getTechnicianName();

            if (name == null) {
                name = "Unknown Technician";
            }

            tvTechnicianName.setText(
                    name
            );


            // -------------------------------------------------
            // INITIAL
            // -------------------------------------------------

            if (!name.isEmpty()) {

                tvInitial.setText(
                        name.substring(0, 1)
                                .toUpperCase()
                );

            } else {

                tvInitial.setText("?");

            }


            // -------------------------------------------------
            // SPECIALIZATION
            // -------------------------------------------------

            String specialization =
                    technician.getSpecialization();

            if (specialization == null
                    || specialization.trim().isEmpty()) {

                specialization =
                        "General Technician";
            }

            tvSpecialization.setText(
                    specialization
            );


            // -------------------------------------------------
            // PHONE
            // -------------------------------------------------

            String phone =
                    technician.getPhone();

            if (phone == null
                    || phone.trim().isEmpty()) {

                phone = "No phone number";
            }

            tvPhone.setText(
                    phone
            );


            // -------------------------------------------------
            // BRANCH
            // -------------------------------------------------

            tvBranch.setText(
                    "Branch ID: "
                            + technician.getBranchId()
            );


            // -------------------------------------------------
            // AVAILABILITY
            // -------------------------------------------------

            if (technician.isAvailable()) {

                tvStatus.setText(
                        "AVAILABLE"
                );

            } else {

                tvStatus.setText(
                        "BUSY"
                );
            }


            // -------------------------------------------------
            // CARD CLICK
            // -------------------------------------------------

            itemView.setOnClickListener(v -> {

                if (clickListener != null) {

                    clickListener.onTechnicianClick(
                            technician
                    );
                }
            });


            // -------------------------------------------------
            // EDIT
            // -------------------------------------------------

            btnEdit.setOnClickListener(v -> {

                if (editListener != null) {

                    editListener.onEditClick(
                            technician
                    );
                }
            });


            // -------------------------------------------------
            // DELETE
            // -------------------------------------------------

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