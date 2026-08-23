package com.example.techfix.branch;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.techfix.R;
import com.example.techfix.adapter.BranchAdapter;
import com.example.techfix.database.TechFixDBHelper;
import com.example.techfix.location.BranchAssignmentHelper;
import com.example.techfix.location.LocationHelper;
import com.example.techfix.location.NetworkUtils;
import com.example.techfix.model.Branch;
import java.util.ArrayList;
import java.util.List;
public class BranchesActivity extends AppCompatActivity{
    private RecyclerView recyclerView;
    private TextView emptyView;
    private BranchAdapter adapter;
    private List<Branch> branchList = new ArrayList<>();
    private LocationHelper locationHelper;
    private TechFixDBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_branches);
        setTitle("TechFix Branches");

        recyclerView = findViewById(R.id.rvBranches);
        emptyView = findViewById(R.id.tvEmptyBranches);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        dbHelper = new TechFixDBHelper(this);
        locationHelper = new LocationHelper(this);

        adapter = new BranchAdapter(branchList, branch -> {
            Intent intent = new Intent(BranchesActivity.this, BranchDetailsActivity.class);
            intent.putExtra("branch_id", branch.getBranchId());
            intent.putExtra("branch_name", branch.getBranchName());
            intent.putExtra("branch_address", branch.getAddress());
            intent.putExtra("branch_phone", branch.getPhone());
            intent.putExtra("branch_lat", branch.getLatitude());
            intent.putExtra("branch_lng", branch.getLongitude());
            startActivity(intent);
        });
        recyclerView.setAdapter(adapter);

        loadBranches();
    }

    private void loadBranches() {
        List<Branch> branches = fetchBranches();
        branchList.clear();
        branchList.addAll(branches);

        if (!locationHelper.hasPermission()) {
            locationHelper.requestPermission();
            adapter.notifyDataSetChanged();
            toggleEmptyView();
            return;
        }

        locationHelper.getCurrentLocation(new LocationHelper.LocationResultCallback() {
            @Override
            public void onLocationResult(Location location) {
                List<Branch> sorted = BranchAssignmentHelper.sortByDistance(
                        location.getLatitude(), location.getLongitude(), branchList);
                branchList.clear();
                branchList.addAll(sorted);
                adapter.notifyDataSetChanged();
                toggleEmptyView();
            }

            @Override
            public void onLocationUnavailable() {
                Toast.makeText(BranchesActivity.this,
                        "Couldn't get your location - showing branches unsorted.", Toast.LENGTH_SHORT).show();
                adapter.notifyDataSetChanged();
                toggleEmptyView();
            }
        });
    }
    
    private List<Branch> fetchBranches() {
        if (NetworkUtils.isOnline(this)) {
            return dbHelper.getCachedBranches().isEmpty() ? demoBranches() : dbHelper.getCachedBranches();
        } else {
            Toast.makeText(this, "Offline - showing last saved branches.", Toast.LENGTH_SHORT).show();
            List<Branch> cached = dbHelper.getCachedBranches();
            return cached.isEmpty() ? demoBranches() : cached;
        }
    }

    private List<Branch> demoBranches() {
        List<Branch> demo = new ArrayList<>();
        demo.add(new Branch(1, "TechFix Colombo", "123 Galle Rd", "Colombo", 6.9271, 79.8612, "0112345678", true));
        demo.add(new Branch(2, "TechFix Kandy", "45 Peradeniya Rd", "Kandy", 7.2906, 80.6337, "0812345678", true));
        demo.add(new Branch(3, "TechFix Galle", "9 Matara Rd", "Galle", 6.0535, 80.2210, "0912345678", true));
        dbHelper.replaceCachedBranches(demo);
        return demo;
    }

    private void toggleEmptyView() {
        emptyView.setVisibility(branchList.isEmpty() ? android.view.View.VISIBLE : android.view.View.GONE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LocationHelper.PERMISSION_REQUEST_CODE) {
            loadBranches();
        }
    }

}