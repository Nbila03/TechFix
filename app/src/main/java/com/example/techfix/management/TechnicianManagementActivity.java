package com.example.techfix.management;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.techfix.R;
import com.example.techfix.adapter.TechnicianAdapter;
import com.example.techfix.firebase.TechRepository;
import com.example.techfix.model.Technician;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

public class TechnicianManagementActivity extends AppCompatActivity {

    private RecyclerView recyclerTechnicians;
    private TextInputEditText etSearchTechnician;
    private MaterialButton btnAddTechnician;

    private TextView tvTotalTechnicians;
    private TextView tvAvailableTechnicians;

    private TechRepository repository;
    private TechnicianAdapter adapter;

    private final List<Technician> technicianList =
            new ArrayList<>();


    // =========================================================
    // ON CREATE
    // =========================================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_technician_management
        );

        repository = new TechRepository();

        initializeViews();
        setupRecyclerView();
        setupButtons();
        setupSearch();
        loadTechnicians();
    }


    // =========================================================
    // INITIALIZE VIEWS
    // =========================================================

    private void initializeViews() {

        recyclerTechnicians =
                findViewById(R.id.recyclerTechnicians);

        etSearchTechnician =
                findViewById(R.id.etSearchTechnician);

        btnAddTechnician =
                findViewById(R.id.btnAddTechnician);

        tvTotalTechnicians =
                findViewById(R.id.tvTotalTechnicians);

        tvAvailableTechnicians =
                findViewById(R.id.tvAvailableTechnicians);
    }


    // =========================================================
    // RECYCLER VIEW
    // =========================================================

    private void setupRecyclerView() {

        recyclerTechnicians.setLayoutManager(
                new LinearLayoutManager(this)
        );

        adapter = new TechnicianAdapter(

                technicianList,

                // -------------------------------------------------
                // CARD CLICK
                // -------------------------------------------------

                technician -> {
                    // Nothing for now
                },


                // -------------------------------------------------
                // EDIT
                // -------------------------------------------------

                technician -> {

                    Intent intent =
                            new Intent(
                                    TechnicianManagementActivity.this,
                                    TechnicianFormActivity.class
                            );

                    intent.putExtra(
                            "mode",
                            "edit"
                    );

                    intent.putExtra(
                            "technicianId",
                            technician.getTechnicianId()
                    );

                    intent.putExtra(
                            "branchId",
                            technician.getBranchId()
                    );

                    intent.putExtra(
                            "technicianName",
                            technician.getTechnicianName()
                    );

                    intent.putExtra(
                            "specialization",
                            technician.getSpecialization()
                    );

                    intent.putExtra(
                            "phone",
                            technician.getPhone()
                    );

                    intent.putExtra(
                            "available",
                            technician.isAvailable()
                    );

                    startActivity(intent);
                },


                // -------------------------------------------------
                // DELETE
                // -------------------------------------------------

                technician -> {

                    deleteTechnician(
                            technician
                    );
                }
        );

        recyclerTechnicians.setAdapter(
                adapter
        );
    }


    // =========================================================
    // BUTTONS
    // =========================================================

    private void setupButtons() {

        btnAddTechnician.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            TechnicianManagementActivity.this,
                            TechnicianFormActivity.class
                    );

            intent.putExtra(
                    "mode",
                    "add"
            );

            startActivity(intent);
        });
    }


    // =========================================================
    // LOAD TECHNICIANS
    // =========================================================

    private void loadTechnicians() {

        repository.getAllTechnicians(

                technicians -> {

                    technicianList.clear();

                    if (technicians != null) {

                        technicianList.addAll(
                                technicians
                        );
                    }

                    adapter.updateList(
                            technicianList
                    );

                    updateSummary(
                            technicianList
                    );
                },

                error -> {

                    Toast.makeText(
                            TechnicianManagementActivity.this,
                            "Failed to load technicians: "
                                    + error.getMessage(),
                            Toast.LENGTH_LONG
                    ).show();
                }
        );
    }


    // =========================================================
    // UPDATE SUMMARY
    // =========================================================

    private void updateSummary(
            List<Technician> technicians) {

        int total = 0;
        int available = 0;

        if (technicians != null) {

            total = technicians.size();

            for (Technician technician :
                    technicians) {

                if (technician != null
                        && technician.isAvailable()) {

                    available++;
                }
            }
        }

        tvTotalTechnicians.setText(
                String.valueOf(total)
        );

        tvAvailableTechnicians.setText(
                String.valueOf(available)
        );
    }


    // =========================================================
    // SEARCH
    // =========================================================

    private void setupSearch() {

        etSearchTechnician.addTextChangedListener(
                new TextWatcher() {

                    @Override
                    public void beforeTextChanged(
                            CharSequence s,
                            int start,
                            int count,
                            int after) {
                    }

                    @Override
                    public void onTextChanged(
                            CharSequence s,
                            int start,
                            int before,
                            int count) {

                        adapter.filter(
                                s.toString()
                        );
                    }

                    @Override
                    public void afterTextChanged(
                            Editable s) {
                    }
                }
        );
    }


    // =========================================================
    // DELETE TECHNICIAN
    // =========================================================

    private void deleteTechnician(
            Technician technician) {

        repository.getDocumentIdByTechnicianId(

                technician.getTechnicianId(),

                documentId -> {

                    if (documentId == null) {

                        Toast.makeText(
                                TechnicianManagementActivity.this,
                                "Technician not found",
                                Toast.LENGTH_SHORT
                        ).show();

                        return;
                    }

                    repository.deleteTechnician(

                            documentId,

                            // SUCCESS
                            () -> {

                                Toast.makeText(
                                        TechnicianManagementActivity.this,
                                        "Technician deleted successfully",
                                        Toast.LENGTH_SHORT
                                ).show();

                                loadTechnicians();
                            },

                            // ERROR
                            error -> {

                                Toast.makeText(
                                        TechnicianManagementActivity.this,
                                        "Failed to delete technician: "
                                                + error.getMessage(),
                                        Toast.LENGTH_LONG
                                ).show();
                            }
                    );
                },

                // FIND ERROR
                error -> {

                    Toast.makeText(
                            TechnicianManagementActivity.this,
                            "Failed to find technician: "
                                    + error.getMessage(),
                            Toast.LENGTH_LONG
                    ).show();
                }
        );
    }


    // =========================================================
    // REFRESH
    // =========================================================

    @Override
    protected void onResume() {

        super.onResume();

        if (repository != null) {

            loadTechnicians();
        }
    }
}