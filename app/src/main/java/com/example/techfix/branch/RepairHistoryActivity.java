package com.example.techfix.branch;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.techfix.R;
import com.example.techfix.adapter.RepairHistoryAdapter;
import com.example.techfix.firebase.RepairRepository;
import com.example.techfix.model.RepairRequest;

import java.util.ArrayList;
import java.util.List;

// Shows the customer's full repair history.
// A device ID can be used to filter the history.
public class RepairHistoryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TextView emptyView;
    private RepairRepository repairRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_repair_history
        );

        // Get the device ID if this screen was opened
        // for a specific device.
        int filterDeviceId =
                getIntent().getIntExtra(
                        "device_id",
                        -1
                );

        // Set the activity title.
        if (filterDeviceId == -1) {

            setTitle("Repair History");

        } else {

            setTitle("Device Repair History");
        }

        // Find the RecyclerView and empty message.
        recyclerView =
                findViewById(R.id.rvRepairHistory);

        emptyView =
                findViewById(R.id.tvEmptyHistory);

        recyclerView.setLayoutManager(
                new LinearLayoutManager(this)
        );

        repairRepository =
                new RepairRepository();

        loadRepairs(filterDeviceId);
    }

    // Loads all repairs and filters them when
    // a specific device was selected.
    private void loadRepairs(int filterDeviceId) {

        repairRepository.getAllRepairs(

                allRepairs -> {

                    List<RepairRequest> repairs;

                    // No device filter was provided.
                    if (filterDeviceId == -1) {

                        repairs = allRepairs;

                    } else {

                        // Create a list for the selected device.
                        repairs = new ArrayList<>();

                        for (RepairRequest repair : allRepairs) {

                            if (repair.getDeviceId()
                                    == filterDeviceId) {

                                repairs.add(repair);
                            }
                        }
                    }

                    bindRepairs(repairs);
                },

                error -> {

                    Toast.makeText(
                            this,
                            "Failed to load repair history: "
                                    + error.getMessage(),
                            Toast.LENGTH_SHORT
                    ).show();

                    // Show an empty list if loading fails.
                    bindRepairs(
                            new ArrayList<>()
                    );
                }
        );
    }

    // Displays the repair records using the adapter.
    private void bindRepairs(
            List<RepairRequest> repairs) {

        RepairHistoryAdapter adapter =
                new RepairHistoryAdapter(
                        repairs,
                        repair -> {

                            Intent intent =
                                    new Intent(
                                            RepairHistoryActivity.this,
                                            RepairTrackingActivity.class
                                    );

                            intent.putExtra(
                                    "repair_id",
                                    repair.getRepairId()
                            );

                            intent.putExtra(
                                    "current_status",
                                    repair.getStatus()
                            );

                            startActivity(intent);
                        }
                );

        recyclerView.setAdapter(adapter);

        // Show the empty message when there are no repairs.
        if (repairs.isEmpty()) {

            emptyView.setVisibility(
                    View.VISIBLE
            );

        } else {

            emptyView.setVisibility(
                    View.GONE
            );
        }
    }
}