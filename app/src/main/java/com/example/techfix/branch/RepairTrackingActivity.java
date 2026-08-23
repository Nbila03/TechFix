package com.example.techfix.branch;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.techfix.R;
import com.example.techfix.adapter.RepairStatusAdapter;
import com.example.techfix.model.RepairStatus;

public class RepairTrackingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_repair_tracking
        );

        // Get the repair information from the previous screen.
        int repairId =
                getIntent().getIntExtra(
                        "repair_id",
                        -1
                );

        String currentStatus =
                getIntent().getStringExtra(
                        "current_status"
                );

        // Use SUBMITTED as the default status
        // if no status was provided.
        if (currentStatus == null) {

            currentStatus =
                    RepairStatus.SUBMITTED;
        }

        // Set the activity title.
        setTitle(
                "Repair #TF" + repairId
        );

        // Display the repair ID.
        TextView tvRepairId =
                findViewById(
                        R.id.tvTrackingRepairId
                );

        tvRepairId.setText(
                "Repair #TF" + repairId
        );

        // Set up the repair status timeline.
        RecyclerView recyclerView =
                findViewById(
                        R.id.rvStatusTimeline
                );

        recyclerView.setLayoutManager(
                new LinearLayoutManager(this)
        );

        // Display each stage of the repair.
        RepairStatusAdapter statusAdapter =
                new RepairStatusAdapter(
                        currentStatus
                );

        recyclerView.setAdapter(
                statusAdapter
        );
    }
}