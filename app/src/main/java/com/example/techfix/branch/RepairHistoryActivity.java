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

// shows the customer's full repair history with a device id filters it
public class RepairHistoryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TextView emptyView;
    private RepairRepository repairRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repair_history);

        int filterDeviceId = getIntent().getIntExtra("device_id", -1);
        setTitle(filterDeviceId == -1 ? "Repair History" : "Device Repair History");

        recyclerView = findViewById(R.id.rvRepairHistory);
        emptyView = findViewById(R.id.tvEmptyHistory);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        repairRepository = new RepairRepository();
        loadRepairs(filterDeviceId);
    }

    private void loadRepairs(int filterDeviceId) {
        repairRepository.getAllRepairs(
                allRepairs -> {
                    List<RepairRequest> repairs;
                    if (filterDeviceId == -1) {
                        repairs = allRepairs;
                    } else {
                        repairs = new ArrayList<>();
                        for (RepairRequest r : allRepairs) {
                            if (r.getDeviceId() == filterDeviceId) repairs.add(r);
                        }
                    }
                    bindRepairs(repairs);
                },
                error -> {
                    Toast.makeText(this, "Failed to load repair history: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    bindRepairs(new ArrayList<>());
                }
        );
    }
    private void bindRepairs(List<RepairRequest> repairs) {
        RepairHistoryAdapter adapter = new RepairHistoryAdapter(repairs, repair -> {
            Intent intent = new Intent(RepairHistoryActivity.this, RepairTrackingActivity.class);
            intent.putExtra("repair_id", repair.getRepairId());
            intent.putExtra("current_status", repair.getStatus());
            startActivity(intent);
        });
        recyclerView.setAdapter(adapter);
        emptyView.setVisibility(repairs.isEmpty() ? android.view.View.VISIBLE : android.view.View.GONE);
    }
}