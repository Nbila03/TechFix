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

public class RepairHistoryActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private TextView emptyView;
    private List<RepairRequest> repairs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repair_history);

        int filterDeviceId = getIntent().getIntExtra("device_id", -1);
        setTitle(filterDeviceId == -1 ? "Repair History" : "Device Repair History");

        recyclerView = findViewById(R.id.rvRepairHistory);
        emptyView = findViewById(R.id.tvEmptyHistory);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        repairs = loadRepairs(filterDeviceId);

        RepairHistoryAdapter adapter = new RepairHistoryAdapter(repairs, repair -> {
            Intent intent = new Intent(RepairHistoryActivity.this, RepairTrackingActivity.class);
            intent.putExtra("repair_id", repair.getRepairId());
            intent.putExtra("current_status", repair.getStatus());
            startActivity(intent);
        });
        recyclerView.setAdapter(adapter);

        emptyView.setVisibility(repairs.isEmpty() ? android.view.View.VISIBLE : android.view.View.GONE);
    }

   //c1
    private List<RepairRequest> loadRepairs(int filterDeviceId) {
        return null;
    }
}