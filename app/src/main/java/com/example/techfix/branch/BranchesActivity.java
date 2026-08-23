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
import com.example.techfix.database.BranchLocalStore;
import com.example.techfix.firebase.BranchRepository;
import com.example.techfix.location.BranchAssignmentHelper;
import com.example.techfix.location.LocationHelper;
import com.example.techfix.location.NetworkUtils;
import com.example.techfix.model.Branch;
import java.util.ArrayList;
import java.util.List;
public class BranchesActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private TextView emptyView;
    private BranchAdapter adapter;
    private List<Branch> branchList = new ArrayList<>();
    private LocationHelper locationHelper;
    private BranchLocalStore branchLocalStore;
    private BranchRepository branchRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_branches);
        setTitle("TechFix Branches");

        recyclerView = findViewById(R.id.rvBranches);
        emptyView = findViewById(R.id.tvEmptyBranches);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        branchLocalStore = new BranchLocalStore(this);
        locationHelper = new LocationHelper(this);
        branchRepository = new BranchRepository();

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
        if (NetworkUtils.isOnline(this)) {
            branchRepository.getAllBranches(
                    branches -> {
                        branchLocalStore.replaceCachedBranches(branches);
                        onBranchesReady(branches);
                    },
                    error -> {
                        Toast.makeText(this, "Failed to load branches: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        onBranchesReady(branchLocalStore.getCachedBranches());
                    }
            );
        } else {
            Toast.makeText(this, "Offline - showing last saved branches.", Toast.LENGTH_SHORT).show();
            onBranchesReady(branchLocalStore.getCachedBranches());
        }
    }

    private void onBranchesReady(List<Branch> branches) {
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