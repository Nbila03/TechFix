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

// "My Devices" -> tap a device -> this screen shows every repair
// recorded against that specific device.
public class DeviceRepairHistoryActivity extends AppCompatActivity {

    private RepairRepository repairRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_device_repair_history
        );

        // Get the selected device information.
        int deviceId =
                getIntent().getIntExtra(
                        "device_id",
                        -1
                );

        String deviceName =
                getIntent().getStringExtra(
                        "device_name"
                );

        // Set the activity title.
        if (deviceName != null) {

            setTitle(deviceName);

        } else {

            setTitle("Device History");
        }

        // Find the header view.
        TextView tvHeader =
                findViewById(R.id.tvDeviceHeader);

        // Display the device name.
        if (deviceName != null) {

            tvHeader.setText(deviceName);

        } else {

            tvHeader.setText(
                    "Device #" + deviceId
            );
        }

        // Set up the repair list.
        RecyclerView recyclerView =
                findViewById(R.id.rvDeviceRepairs);

        recyclerView.setLayoutManager(
                new LinearLayoutManager(this)
        );

        // Message shown when no repairs are found.
        TextView emptyView =
                findViewById(R.id.tvEmptyDeviceRepairs);

        repairRepository =
                new RepairRepository();

        // Get all repairs from the repository.
        repairRepository.getAllRepairs(

                allRepairs -> {

                    List<RepairRequest> filteredRepairs =
                            new ArrayList<>();

                    // Keep only repairs belonging
                    // to the selected device.
                    for (RepairRequest repair : allRepairs) {

                        if (repair.getDeviceId() == deviceId) {

                            filteredRepairs.add(repair);
                        }
                    }

                    // Create the repair history adapter.
                    RepairHistoryAdapter adapter =
                            new RepairHistoryAdapter(
                                    filteredRepairs,
                                    repair -> {

                                        Intent intent =
                                                new Intent(
                                                        DeviceRepairHistoryActivity.this,
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

                    // Show a message if there are no repairs.
                    if (filteredRepairs.isEmpty()) {

                        emptyView.setVisibility(
                                View.VISIBLE
                        );

                    } else {

                        emptyView.setVisibility(
                                View.GONE
                        );
                    }
                },

                error -> {

                    Toast.makeText(
                            this,
                            "Failed to load device history: "
                                    + error.getMessage(),
                            Toast.LENGTH_SHORT
                    ).show();
                }
        );
    }
}