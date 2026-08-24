package com.example.techfix.management;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.techfix.R;
import com.example.techfix.firebase.TechRepository;
import com.example.techfix.model.Technician;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class TechnicianFormActivity extends AppCompatActivity {

    private TextInputEditText etTechnicianName;
    private TextInputEditText etSpecialization;
    private TextInputEditText etPhone;

    private MaterialButton btnSaveTechnician;
    private MaterialButton btnBackTechnician;

    private TechRepository repository;

    // Edit information
    private String mode;
    private int technicianId;
    private int branchId;
    private boolean available;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_technician_form
        );

        repository = new TechRepository();

        initializeViews();

        getIntentData();

        setupButtons();

        loadEditData();
    }

    // =========================================================
    // INITIALIZE
    // =========================================================

    private void initializeViews() {

        etTechnicianName =
                findViewById(R.id.etTechnicianName);

        etSpecialization =
                findViewById(R.id.etSpecialization);

        etPhone =
                findViewById(R.id.etTechnicianPhone);

        btnSaveTechnician =
                findViewById(R.id.btnSaveTechnician);

        btnBackTechnician =
                findViewById(R.id.btnBackTechnician);
    }

    // =========================================================
    // GET INTENT DATA
    // =========================================================

    private void getIntentData() {

        mode = getIntent().getStringExtra("mode");

        if (mode == null) {
            mode = "add";
        }

        technicianId =
                getIntent().getIntExtra(
                        "technicianId",
                        0
                );

        branchId =
                getIntent().getIntExtra(
                        "branchId",
                        1
                );

        available =
                getIntent().getBooleanExtra(
                        "available",
                        true
                );
    }

    // =========================================================
    // LOAD EDIT DATA
    // =========================================================

    private void loadEditData() {

        if (mode.equals("edit")) {

            String name =
                    getIntent().getStringExtra(
                            "technicianName"
                    );

            String specialization =
                    getIntent().getStringExtra(
                            "specialization"
                    );

            String phone =
                    getIntent().getStringExtra(
                            "phone"
                    );

            if (name != null) {
                etTechnicianName.setText(name);
            }

            if (specialization != null) {
                etSpecialization.setText(
                        specialization
                );
            }

            if (phone != null) {
                etPhone.setText(phone);
            }

            btnSaveTechnician.setText(
                    "Update Technician"
            );
        }
    }

    // =========================================================
    // BUTTONS
    // =========================================================

    private void setupButtons() {

        btnBackTechnician.setOnClickListener(v -> {
            finish();
        });

        btnSaveTechnician.setOnClickListener(v -> {
            saveTechnician();
        });
    }

    // =========================================================
    // SAVE / UPDATE
    // =========================================================

    private void saveTechnician() {

        String name =
                etTechnicianName
                        .getText()
                        .toString()
                        .trim();

        String specialization =
                etSpecialization
                        .getText()
                        .toString()
                        .trim();

        String phone =
                etPhone
                        .getText()
                        .toString()
                        .trim();

        // =====================================================
        // VALIDATION
        // =====================================================

        if (name.isEmpty()) {

            etTechnicianName.setError(
                    "Enter technician name"
            );

            etTechnicianName.requestFocus();

            return;
        }

        if (specialization.isEmpty()) {

            etSpecialization.setError(
                    "Enter specialization"
            );

            etSpecialization.requestFocus();

            return;
        }

        if (phone.isEmpty()) {

            etPhone.setError(
                    "Enter phone number"
            );

            etPhone.requestFocus();

            return;
        }

        // =====================================================
        // CREATE OBJECT
        // =====================================================

        Technician technician =
                new Technician(
                        technicianId,
                        branchId,
                        name,
                        specialization,
                        phone,
                        available
                );

        btnSaveTechnician.setEnabled(false);

        // =====================================================
        // ADD
        // =====================================================

        if (mode.equals("add")) {

            repository.addTechnician(

                    technician,

                    () -> {

                        Toast.makeText(
                                this,
                                "Technician added successfully",
                                Toast.LENGTH_SHORT
                        ).show();

                        finish();
                    },

                    error -> {

                        btnSaveTechnician.setEnabled(true);

                        Toast.makeText(
                                this,
                                "Failed to add technician: "
                                        + error.getMessage(),
                                Toast.LENGTH_LONG
                        ).show();
                    }
            );

            return;
        }

        // =====================================================
        // EDIT
        // =====================================================

        repository.getDocumentIdByTechnicianId(

                technicianId,

                documentId -> {

                    if (documentId == null) {

                        btnSaveTechnician.setEnabled(true);

                        Toast.makeText(
                                this,
                                "Technician not found",
                                Toast.LENGTH_SHORT
                        ).show();

                        return;
                    }

                    repository.updateTechnician(

                            documentId,
                            technician,

                            () -> {

                                Toast.makeText(
                                        this,
                                        "Technician updated successfully",
                                        Toast.LENGTH_SHORT
                                ).show();

                                finish();
                            },

                            error -> {

                                btnSaveTechnician.setEnabled(true);

                                Toast.makeText(
                                        this,
                                        "Failed to update technician: "
                                                + error.getMessage(),
                                        Toast.LENGTH_LONG
                                ).show();
                            }
                    );
                },

                error -> {

                    btnSaveTechnician.setEnabled(true);

                    Toast.makeText(
                            this,
                            "Failed to find technician: "
                                    + error.getMessage(),
                            Toast.LENGTH_LONG
                    ).show();
                }
        );
    }
}