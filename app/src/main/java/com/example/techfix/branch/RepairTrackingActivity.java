package com.example.techfix.branch;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.techfix.R;
import com.example.techfix.adapter.RepairStatusAdapter;
import com.example.techfix.model.RepairStatus;

public class RepairTrackingActivity extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repair_tracking);

        int repairId = getIntent().getIntExtra("repair_id", -1);
        String currentStatus = getIntent().getStringExtra("current_status");
        if (currentStatus == null) currentStatus = RepairStatus.SUBMITTED;

        setTitle("Repair #TF" + repairId);

        TextView tvRepairId = findViewById(R.id.tvTrackingRepairId);
        tvRepairId.setText("Repair #TF" + repairId);

        RecyclerView recyclerView = findViewById(R.id.rvStatusTimeline);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new RepairStatusAdapter(currentStatus));
    }
}
