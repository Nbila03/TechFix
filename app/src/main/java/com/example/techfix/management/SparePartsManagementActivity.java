package com.example.techfix.management;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.techfix.R;
import com.example.techfix.firebase.SparePartRepository;
import com.example.techfix.model.SparePart;

import java.util.List;

public class SparePartsManagementActivity
        extends AppCompatActivity {

    private SparePartRepository repository;

    private Button btnAddPart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Connect Activity to Spare Parts UI
        setContentView(
                R.layout.activity_spare_parts_management
        );

        // Create Firebase repository
        repository = new SparePartRepository();

        // Find Add button
        btnAddPart =
                findViewById(R.id.btnAddPart);

        // ADD SPARE PART

        btnAddPart.setOnClickListener(v -> {

            Intent intent = new Intent(
                    SparePartsManagementActivity.this,
                    SparePartFormActivity.class
            );

            startActivity(intent);
        });

        // LOAD DATA

        loadSpareParts();
    }

    // LOAD SPARE PARTS

    private void loadSpareParts() {

        repository.getSpareParts(

                // SUCCESS

                parts -> {

                    Toast.makeText(
                            this,
                            "Loaded "
                                    + parts.size()
                                    + " spare parts",
                            Toast.LENGTH_SHORT
                    ).show();

                    // Temporary testing
                    // Print Firebase data

                    for (SparePart part : parts) {

                        System.out.println(
                                "Part ID: "
                                        + part.getPartId()
                                        + " | Name: "
                                        + part.getPartName()
                                        + " | Device: "
                                        + part.getCompatibleDevice()
                                        + " | Quantity: "
                                        + part.getQuantity()
                                        + " | Price: LKR "
                                        + part.getUnitPrice()
                                        + " | Available: "
                                        + part.isAvailable()
                        );
                    }
                },


                // ERROR

                error -> {

                    Toast.makeText(
                            this,
                            "Failed to load spare parts: "
                                    + error.getMessage(),
                            Toast.LENGTH_LONG
                    ).show();
                }
        );
    }

    // RELOAD DATA


    @Override
    protected void onResume() {

        super.onResume();

        // When we return from
        // SparePartFormActivity,
        // load Firebase data again.

        loadSpareParts();
    }
}