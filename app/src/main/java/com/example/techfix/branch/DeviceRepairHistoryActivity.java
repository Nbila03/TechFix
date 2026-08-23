package com.example.techfix.branch;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.techfix.R;
import com.example.techfix.adapter.RepairHistoryAdapter;
import com.example.techfix.model.RepairRequest;

import java.util.List;

//tap a device -> this screen shows every repair for spes id
public class DeviceRepairHistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_repair_history);

        int deviceId = getIntent().getIntExtra("device_id", -1);
        String deviceName = getIntent().getStringExtra("device_name");

        setTitle(deviceName != null ? deviceName : "Device History");

        TextView tvHeader = findViewById(R.id.tvDeviceHeader);
        tvHeader.setText(deviceName != null ? deviceName : "Device #" + deviceId);

        RecyclerView recyclerView = findViewById(R.id.rvDeviceRepairs);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<RepairRequest> repairs = loadRepairsForDevice(deviceId);
        RepairHistoryAdapter adapter = new RepairHistoryAdapter(repairs, repair -> {
            Intent intent = new Intent(DeviceRepairHistoryActivity.this, RepairTrackingActivity.class);
            intent.putExtra("repair_id", repair.getRepairId());
            intent.putExtra("current_status", repair.getStatus());
            startActivity(intent);
        });
        recyclerView.setAdapter(adapter);

        TextView emptyView = findViewById(R.id.tvEmptyDeviceRepairs);
        emptyView.setVisibility(repairs.isEmpty() ? android.view.View.VISIBLE : android.view.View.GONE);
    }

    // swap DemoRepairData for repairRepository.getRepairsForDevice(deviceId) once Firestore is ready
    private List<RepairRequest> loadRepairsForDevice(int deviceId) {
        return DemoRepairData.getRepairsForDevice(deviceId);
    }
}