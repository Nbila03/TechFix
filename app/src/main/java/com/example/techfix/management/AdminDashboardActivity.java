package com.example.techfix.management;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.techfix.R;
import com.google.android.material.button.MaterialButton;

public class AdminDashboardActivity extends AppCompatActivity {

    private MaterialButton btnRepairManagement;
    private MaterialButton btnTechnicianManagement;
    private MaterialButton btnSparePartsManagement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_admin_dashboard
        );

        initializeViews();

        setupButtons();
    }


    // =========================================================
    // INITIALIZE VIEWS
    // =========================================================

    private void initializeViews() {

        btnRepairManagement =
                findViewById(
                        R.id.btnRepairManagement
                );

        btnTechnicianManagement =
                findViewById(
                        R.id.btnTechnicianManagement
                );

        btnSparePartsManagement =
                findViewById(
                        R.id.btnSparePartsManagement
                );
    }


    // =========================================================
    // BUTTONS
    // =========================================================

    private void setupButtons() {

        // -----------------------------------------------------
        // REPAIR MANAGEMENT
        // -----------------------------------------------------

        btnRepairManagement.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            AdminDashboardActivity.this,
                            RepairManagementActivity.class
                    );

            startActivity(intent);
        });


        // -----------------------------------------------------
        // TECHNICIAN MANAGEMENT
        // -----------------------------------------------------

        btnTechnicianManagement.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            AdminDashboardActivity.this,
                            TechnicianManagementActivity.class
                    );

            startActivity(intent);
        });


        // -----------------------------------------------------
        // SPARE PARTS MANAGEMENT
        // -----------------------------------------------------

        btnSparePartsManagement.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            AdminDashboardActivity.this,
                            SparePartsManagementActivity.class
                    );

            startActivity(intent);
        });
    }
}