package com.example.techfix.management;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.techfix.R;
import com.example.techfix.adapter.RepairAdapter;
import com.example.techfix.firebase.RepairRepository;
import com.example.techfix.model.RepairRequest;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

public class RepairManagementActivity extends AppCompatActivity {

    private RecyclerView recyclerRepairs;
    private TextInputEditText etSearchRepair;

    private MaterialButton btnFilterAll;
    private MaterialButton btnFilterPending;
    private MaterialButton btnFilterProgress;
    private MaterialButton btnFilterCompleted;

    private RepairAdapter repairAdapter;
    private RepairRepository repairRepository;

    private final List<RepairRequest> repairList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_repair_management);

        initializeViews();
        setupRecyclerView();
        setupFilters();
        setupSearch();

        repairRepository = new RepairRepository();

        loadRepairs();
    }

    // INITIALIZE VIEWS

    private void initializeViews() {

        recyclerRepairs = findViewById(R.id.recyclerRepairs);

        etSearchRepair = findViewById(R.id.etSearchRepair);

        btnFilterAll = findViewById(R.id.btnFilterAll);
        btnFilterPending = findViewById(R.id.btnFilterPending);
        btnFilterProgress = findViewById(R.id.btnFilterProgress);
        btnFilterCompleted = findViewById(R.id.btnFilterCompleted);
    }


    // RECYCLER VIEW

    private void setupRecyclerView() {

        recyclerRepairs.setLayoutManager(
                new LinearLayoutManager(this)
        );

        repairAdapter = new RepairAdapter(
                repairList,
                repair -> {

                    // Open Repair Details Management screen

                    Intent intent = new Intent(
                            RepairManagementActivity.this,
                            RepairDetailsManagementActivity.class
                    );

                    // Send selected repair ID
                    intent.putExtra(
                            "repairId",
                            repair.getRepairId()
                    );

                    startActivity(intent);
                }
        );

        recyclerRepairs.setAdapter(repairAdapter);
    }


    // LOAD REPAIRS FROM FIRESTORE

    private void loadRepairs() {

        repairRepository.getAllRepairs(

                repairs -> {

                    repairList.clear();
                    repairList.addAll(repairs);

                    repairAdapter.updateList(repairList);
                },

                error -> {

                    Toast.makeText(
                            RepairManagementActivity.this,
                            "Failed to load repairs: "
                                    + error.getMessage(),
                            Toast.LENGTH_LONG
                    ).show();
                }
        );
    }


    // SEARCH

    private void setupSearch() {

        etSearchRepair.setOnEditorActionListener(
                (v, actionId, event) -> {

                    String query =
                            etSearchRepair.getText()
                                    .toString()
                                    .trim();

                    repairAdapter.filter(query);

                    return false;
                }
        );
    }


    // FILTER BUTTONS

    private void setupFilters() {

        btnFilterAll.setOnClickListener(v -> {

            repairAdapter.filterByStatus("ALL");

        });

        btnFilterPending.setOnClickListener(v -> {

            repairAdapter.filterByStatus("SUBMITTED");

        });

        btnFilterProgress.setOnClickListener(v -> {

            repairAdapter.filterByStatus("IN_PROGRESS");

        });

        btnFilterCompleted.setOnClickListener(v -> {

            repairAdapter.filterByStatus("COMPLETED");

        });
    }
}