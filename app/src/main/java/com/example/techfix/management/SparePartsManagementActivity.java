package com.example.techfix.management;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.techfix.R;
import com.example.techfix.adapter.SparePartAdapter;
import com.example.techfix.firebase.SparePartRepository;
import com.example.techfix.model.SparePart;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

public class SparePartsManagementActivity extends AppCompatActivity {

    private SparePartRepository repository;

    private Button btnAddPart;

    private Button btnBackSpareParts;
    private RecyclerView recyclerSpareParts;

    private TextView tvTotalParts;
    private TextView tvLowStock;

    private TextInputEditText etSearchParts;

    private SparePartAdapter adapter;

    private List<SparePart> sparePartsList = new ArrayList<>();

    // Keep the complete Firebase list
    private List<SparePart> allSpareParts = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_spare_parts_management);

        repository = new SparePartRepository();

        // Find views
        btnAddPart = findViewById(R.id.btnAddPart);

        recyclerSpareParts =
                findViewById(R.id.recyclerSpareParts);

        tvTotalParts =
                findViewById(R.id.tvTotalParts);

        tvLowStock =
                findViewById(R.id.tvLowStock);

        etSearchParts =
                findViewById(R.id.etSearchParts);

        btnBackSpareParts =
                findViewById(
                        R.id.btnBackSpareParts
                );

        // RecyclerView
        recyclerSpareParts.setLayoutManager(
                new LinearLayoutManager(this)
        );

        adapter = new SparePartAdapter(
                this,
                sparePartsList
        );

        recyclerSpareParts.setAdapter(adapter);

        btnBackSpareParts.setOnClickListener(
                v -> finish()
        );


        // Add Spare Part
        btnAddPart.setOnClickListener(v -> {

            Intent intent = new Intent(
                    SparePartsManagementActivity.this,
                    SparePartFormActivity.class
            );

            startActivity(intent);
        });

        // SEARCH
        etSearchParts.addTextChangedListener(
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

                        filterSpareParts(
                                s.toString()
                        );
                    }

                    @Override
                    public void afterTextChanged(
                            Editable s) {
                    }
                }
        );

        loadSpareParts();
    }

    // LOAD SPARE PARTS FROM FIREBASE

    private void loadSpareParts() {

        repository.getSpareParts(

                parts -> {

                    // Save complete Firebase list
                    allSpareParts.clear();
                    allSpareParts.addAll(parts);

                    // Initially show everything
                    sparePartsList.clear();
                    sparePartsList.addAll(parts);

                    adapter.notifyDataSetChanged();

                    // Summary
                    tvTotalParts.setText(
                            String.valueOf(parts.size())
                    );

                    int lowStockCount = 0;

                    for (SparePart part : parts) {

                        if (part.getQuantity() <= 5) {
                            lowStockCount++;
                        }
                    }

                    tvLowStock.setText(
                            String.valueOf(lowStockCount)
                    );

                    Toast.makeText(
                            this,
                            "Loaded " + parts.size()
                                    + " spare parts",
                            Toast.LENGTH_SHORT
                    ).show();
                },

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

    // SEARCH FILTER

    private void filterSpareParts(String searchText) {

        String query =
                searchText.trim().toLowerCase();

        sparePartsList.clear();

        // Empty search = show everything
        if (query.isEmpty()) {

            sparePartsList.addAll(
                    allSpareParts
            );

        } else {

            for (SparePart part : allSpareParts) {

                String partName =
                        part.getPartName() == null
                                ? ""
                                : part.getPartName().toLowerCase();

                String compatibleDevice =
                        part.getCompatibleDevice() == null
                                ? ""
                                : part.getCompatibleDevice().toLowerCase();

                String partId =
                        String.valueOf(
                                part.getPartId()
                        ).toLowerCase();

                // Search by:
                // Part Name
                // Compatible Device
                // Part ID

                if (partName.contains(query)
                        || compatibleDevice.contains(query)
                        || partId.contains(query)) {

                    sparePartsList.add(part);
                }
            }
        }

        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {

        super.onResume();

        loadSpareParts();
    }
}