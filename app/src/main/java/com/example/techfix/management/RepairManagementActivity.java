package com.example.techfix.management;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
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

    // =========================================================
    // RECYCLER VIEW
    // =========================================================

    private RecyclerView recyclerRepairs;

    // =========================================================
    // SEARCH
    // =========================================================

    private TextInputEditText etSearchRepair;

    // =========================================================
    // FILTER BUTTONS
    // =========================================================

    private MaterialButton btnFilterAll;
    private MaterialButton btnFilterPending;
    private MaterialButton btnFilterProgress;
    private MaterialButton btnFilterCompleted;

    private MaterialButton btnBackRepairManagement;

    // =========================================================
    // SUMMARY TEXT VIEWS
    // =========================================================

    private TextView tvTotalRepairs;
    private TextView tvPendingRepairs;
    private TextView tvProgressRepairs;
    private TextView tvRepairCount;

    // =========================================================
    // ADAPTER + REPOSITORY
    // =========================================================

    private RepairAdapter repairAdapter;
    private RepairRepository repairRepository;

    // =========================================================
    // REPAIR LIST
    // =========================================================

    private final List<RepairRequest> repairList =
            new ArrayList<>();


    // =========================================================
    // ON CREATE
    // =========================================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_repair_management
        );

        initializeViews();

        setupRecyclerView();

        setupFilters();

        setupSearch();

        setupBackButton();

        repairRepository =
                new RepairRepository();

        // Load repairs from Firebase
        loadRepairs();
    }


    // =========================================================
    // INITIALIZE VIEWS
    // =========================================================

    private void initializeViews() {

        // RecyclerView
        recyclerRepairs =
                findViewById(
                        R.id.recyclerRepairs
                );

        // Search
        etSearchRepair =
                findViewById(
                        R.id.etSearchRepair
                );

        // Filter buttons
        btnFilterAll =
                findViewById(
                        R.id.btnFilterAll
                );

        btnFilterPending =
                findViewById(
                        R.id.btnFilterPending
                );

        btnFilterProgress =
                findViewById(
                        R.id.btnFilterProgress
                );

        btnFilterCompleted =
                findViewById(
                        R.id.btnFilterCompleted
                );

        btnBackRepairManagement =
                findViewById(R.id.btnBackRepairManagement);

        // Summary cards
        tvTotalRepairs =
                findViewById(
                        R.id.tvTotalRepairs
                );

        tvPendingRepairs =
                findViewById(
                        R.id.tvPendingRepairs
                );

        tvProgressRepairs =
                findViewById(
                        R.id.tvProgressRepairs
                );

        tvRepairCount =
                findViewById(
                        R.id.tvRepairCount
                );
    }


    // =========================================================
    // RECYCLER VIEW
    // =========================================================

    private void setupRecyclerView() {

        recyclerRepairs.setLayoutManager(
                new LinearLayoutManager(this)
        );

        repairAdapter =
                new RepairAdapter(
                        repairList,

                        repair -> {

                            // Open Repair Details Management screen

                            Intent intent =
                                    new Intent(
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

        recyclerRepairs.setAdapter(
                repairAdapter
        );
    }


    // =========================================================
    // LOAD REPAIRS FROM FIRESTORE
    // =========================================================

    private void loadRepairs() {

        if (repairRepository == null) {
            return;
        }

        repairRepository.getAllRepairs(

                repairs -> {

                    // Clear existing list
                    repairList.clear();

                    // Add latest Firebase data
                    if (repairs != null) {

                        repairList.addAll(
                                repairs
                        );
                    }

                    // Update summary cards
                    updateSummary(
                            repairList
                    );

                    // Update RecyclerView
                    repairAdapter.updateList(
                            repairList
                    );
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


    // =========================================================
    // UPDATE SUMMARY CARDS
    // =========================================================

    private void updateSummary(
            List<RepairRequest> repairs) {

        int total = 0;
        int pending = 0;
        int progress = 0;

        // -----------------------------------------------------
        // TOTAL REPAIRS
        // -----------------------------------------------------

        if (repairs != null) {

            total = repairs.size();
        }

        // -----------------------------------------------------
        // COUNT REPAIRS BY STATUS
        // -----------------------------------------------------

        if (repairs != null) {

            for (RepairRequest repair : repairs) {

                if (repair == null) {
                    continue;
                }

                String status =
                        repair.getStatus();

                if (status == null) {
                    continue;
                }

                status =
                        status.trim()
                                .toUpperCase();

                // Pending
                if (status.equals("SUBMITTED")) {

                    pending++;
                }

                // In Progress
                else if (
                        status.equals("IN_PROGRESS")
                ) {

                    progress++;
                }
            }
        }

        // -----------------------------------------------------
        // DISPLAY TOTAL
        // -----------------------------------------------------

        tvTotalRepairs.setText(
                String.valueOf(total)
        );

        // -----------------------------------------------------
        // DISPLAY PENDING
        // -----------------------------------------------------

        tvPendingRepairs.setText(
                String.valueOf(pending)
        );

        // -----------------------------------------------------
        // DISPLAY IN PROGRESS
        // -----------------------------------------------------

        tvProgressRepairs.setText(
                String.valueOf(progress)
        );

        // -----------------------------------------------------
        // DISPLAY REPAIR COUNT
        // -----------------------------------------------------

        if (total == 1) {

            tvRepairCount.setText(
                    "1 repair"
            );

        } else {

            tvRepairCount.setText(
                    total + " repairs"
            );
        }
    }


    // =========================================================
    // REFRESH WHEN RETURNING FROM DETAILS SCREEN
    // =========================================================

    @Override
    protected void onResume() {

        super.onResume();

        /*
         * Reload Firebase data whenever
         * the admin returns to this screen.
         *
         * This keeps:
         *
         * - Repair cards
         * - Total count
         * - Pending count
         * - In Progress count
         *
         * up to date.
         */

        if (repairRepository != null) {

            recyclerRepairs.post(
                    this::loadRepairs
            );
        }
    }


    // =========================================================
    // SEARCH
    // =========================================================

    private void setupSearch() {

        etSearchRepair.setOnEditorActionListener(
                (v, actionId, event) -> {

                    String query =
                            etSearchRepair
                                    .getText()
                                    .toString()
                                    .trim();

                    repairAdapter.filter(
                            query
                    );

                    return false;
                }
        );
    }


    // =========================================================
    // FILTER BUTTONS
    // =========================================================

    private void setupFilters() {

        // -----------------------------------------------------
        // ALL
        // -----------------------------------------------------

        btnFilterAll.setOnClickListener(v -> {

            repairAdapter.filterByStatus(
                    "ALL"
            );
        });


        // -----------------------------------------------------
        // PENDING
        // -----------------------------------------------------

        btnFilterPending.setOnClickListener(v -> {

            repairAdapter.filterByStatus(
                    "SUBMITTED"
            );
        });


        // -----------------------------------------------------
        // IN PROGRESS
        // -----------------------------------------------------

        btnFilterProgress.setOnClickListener(v -> {

            repairAdapter.filterByStatus(
                    "IN_PROGRESS"
            );
        });


        // -----------------------------------------------------
        // COMPLETED
        // -----------------------------------------------------

        btnFilterCompleted.setOnClickListener(v -> {

            repairAdapter.filterByStatus(
                    "COMPLETED"
            );
        });
    }
    private void setupBackButton() {

        btnBackRepairManagement.setOnClickListener(v -> {
            finish();
        });
    }
}