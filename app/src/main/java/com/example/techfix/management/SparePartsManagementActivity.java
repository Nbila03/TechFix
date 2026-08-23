
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

public class SparePartsManagementActivity extends AppCompatActivity {

    private SparePartRepository repository;

    private Button btnAddPart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Connect to our Spare Parts UI
        setContentView(R.layout.activity_spare_parts_management);

        // Firebase repository
        repository = new SparePartRepository();

        // Add button
        btnAddPart = findViewById(R.id.btnAddPart);

        btnAddPart.setOnClickListener(v -> {

            Intent intent = new Intent(
                    SparePartsManagementActivity.this,
                    SparePartFormActivity.class
            );

            startActivity(intent);
        });

        // Load existing parts
        loadSpareParts();
    }

    // LOAD SPARE PARTS

    private void loadSpareParts() {

        repository.getSpareParts(
                parts -> {

                    Toast.makeText(
                            this,
                            "Loaded " + parts.size() + " spare parts",
                            Toast.LENGTH_SHORT
                    ).show();

                    // For now, print the Firebase data.
                    // Later we will display it in RecyclerView.
                    for (SparePart part : parts) {

                        System.out.println(
                                part.getPartName()
                                        + " | "
                                        + part.getQuantity()
                                        + " units | LKR "
                                        + part.getUnitPrice()
                        );
                    }
                }
        );
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Reload Firebase data when we return
        // from the Add/Edit form.
        loadSpareParts();
    }
}

