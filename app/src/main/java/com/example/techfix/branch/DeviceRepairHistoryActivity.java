package com.example.techfix.branch;

import android.content.Intent;
import android.os.Bundle;
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
// recorded against that specific id
public class DeviceRepairHistoryActivity extends AppCompatActivity {

    private RepairRepository repairRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_repair_history);

        int deviceId = getIntent().getIntExtra("device_id", -1);
        String deviceName = getIntent().getStringExtra("device_name");

        // Set activity title
        if (deviceName != null) {
            setTitle(deviceName);
        } else {
            setTitle("Device History");
        }

        TextView tvHeader = findViewById(R.id.tvDeviceHeader);

        // Set device header
        if (deviceName != null) {
            tvHeader.setText(deviceName);
        } else {
            tvHeader.setText("Device #" + deviceId);
        }

        RecyclerView recyclerView = findViewById(R.id.rvDeviceRepairs);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        TextView emptyView = findViewById(R.id.tvEmptyDeviceRepairs);

        repairRepository = new RepairRepository();

        repairRepository.getAllRepairs(
                allRepairs -> {

                    List<RepairRequest> filtered = new ArrayList<>();

                    for (RepairRequest r : allRepairs) {
                        if (r.getDeviceId() == deviceId) {
                            filtered.add(r);
                        }
                    }

                    RepairHistoryAdapter adapter = new RepairHistoryAdapter(
                            filtered,
                            repair -> {

                                Intent intent = new Intent(
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

                    // Show empty message if there are no repairs
                    if (filtered.isEmpty()) {
                        emptyView.setVisibility(android.view.View.VISIBLE);
                    } else {
                        emptyView.setVisibility(android.view.View.GONE);
                    }
                },

                error -> Toast.makeText(
                        this,
                        "Failed to load device history: " + error.getMessage(),
                        Toast.LENGTH_SHORT
                ).show()
        );
    }
}