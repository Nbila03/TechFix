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

        setContentView(R.layout.activity_admin_dashboard);

        // Find buttons
        btnRepairManagement = findViewById(R.id.btnRepairManagement);
        btnTechnicianManagement = findViewById(R.id.btnTechnicianManagement);
        btnSparePartsManagement = findViewById(R.id.btnSparePartsManagement);

        // Repair Management
        btnRepairManagement.setOnClickListener(v -> {

            Intent intent = new Intent(
                    AdminDashboardActivity.this,
                    RepairManagementActivity.class
            );

            startActivity(intent);
        });

        // Technician Management
        btnTechnicianManagement.setOnClickListener(v -> {

            Intent intent = new Intent(
                    AdminDashboardActivity.this,
                    TechnicianManagementActivity.class
            );

            startActivity(intent);
        });

        // Spare Parts Management
        btnSparePartsManagement.setOnClickListener(v -> {

            Intent intent = new Intent(
                    AdminDashboardActivity.this,
                    SparePartsManagementActivity.class
            );

            startActivity(intent);
        });
    }
}