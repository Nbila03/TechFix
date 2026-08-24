package com.example.techfix.branch;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
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

    private List<Branch> branchList =
            new ArrayList<>();

    private LocationHelper locationHelper;
    private BranchLocalStore branchLocalStore;
    private BranchRepository branchRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_branches);

        setTitle("TechFix Branches");

        // Find the views from the layout.
        recyclerView =
                findViewById(R.id.rvBranches);

        emptyView =
                findViewById(R.id.tvEmptyBranches);

        recyclerView.setLayoutManager(
                new LinearLayoutManager(this)
        );

        // Create the helper and repository objects.
        branchLocalStore =
                new BranchLocalStore(this);

        locationHelper =
                new LocationHelper(this);

        branchRepository =
                new BranchRepository();

        // Create the branch adapter.
        adapter = new BranchAdapter(
                branchList,
                branch -> {

                    Intent intent =
                            new Intent(
                                    BranchesActivity.this,
                                    BranchDetailsActivity.class
                            );

                    intent.putExtra(
                            "branch_id",
                            branch.getBranchId()
                    );

                    intent.putExtra(
                            "branch_name",
                            branch.getBranchName()
                    );

                    intent.putExtra(
                            "branch_address",
                            branch.getAddress()
                    );

                    intent.putExtra(
                            "branch_phone",
                            branch.getPhone()
                    );

                    intent.putExtra(
                            "branch_lat",
                            branch.getLatitude()
                    );

                    intent.putExtra(
                            "branch_lng",
                            branch.getLongitude()
                    );

                    startActivity(intent);
                }
        );

        recyclerView.setAdapter(adapter);

        // Load branches from Firebase or local storage.
        loadBranches();
    }

    private void loadBranches() {

        // device has internet or not
        if (NetworkUtils.isOnline(this)) {

            // Load the latest branches from Firebase.
            branchRepository.getAllBranches(

                    branches -> {

                        // Save the latest branches locally.
                        branchLocalStore
                                .replaceCachedBranches(branches);

                        // Display the branches.
                        onBranchesReady(branches);
                    },

                    error -> {

                        Toast.makeText(
                                this,
                                "Failed to load branches: "
                                        + error.getMessage(),
                                Toast.LENGTH_SHORT
                        ).show();

                        // Ifirebase fails, use the locally cached branches.
                        List<Branch> cachedBranches =
                                branchLocalStore.getCachedBranches();

                        onBranchesReady(cachedBranches);
                    }
            );

        } else {

            // No internet connection.
            Toast.makeText(
                    this,
                    "Offline - showing last saved branches.",
                    Toast.LENGTH_SHORT
            ).show();

            List<Branch> cachedBranches =
                    branchLocalStore.getCachedBranches();

            onBranchesReady(cachedBranches);
        }
    }

    private void onBranchesReady(
            List<Branch> branches) {

        // Update the branch list.
        branchList.clear();

        branchList.addAll(branches);

        // Check whether location permission
        // has already been granted.
        if (!locationHelper.hasPermission()) {

            locationHelper.requestPermission();

            adapter.notifyDataSetChanged();

            toggleEmptyView();

            return;
        }

        // Get the customer's current location.
        locationHelper.getCurrentLocation(
                new LocationHelper.LocationResultCallback() {

                    @Override
                    public void onLocationResult(
                            Location location) {

                        // Sort branches according to
                        // distance from the customer.
                        List<Branch> sortedBranches =
                                BranchAssignmentHelper.sortByDistance(
                                        location.getLatitude(),
                                        location.getLongitude(),
                                        branchList
                                );

                        // Replace the list with
                        // the sorted branches.
                        branchList.clear();

                        branchList.addAll(
                                sortedBranches
                        );

                        adapter.notifyDataSetChanged();

                        toggleEmptyView();
                    }

                    @Override
                    public void onLocationUnavailable() {

                        Toast.makeText(
                                BranchesActivity.this,
                                "Couldn't get your location - "
                                        + "showing branches unsorted.",
                                Toast.LENGTH_SHORT
                        ).show();

                        adapter.notifyDataSetChanged();

                        toggleEmptyView();
                    }
                }
        );
    }

    private void toggleEmptyView() {

        if (branchList.isEmpty()) {

            emptyView.setVisibility(
                    View.VISIBLE
            );

        } else {

            emptyView.setVisibility(
                    View.GONE
            );
        }
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode,
            @NonNull String[] permissions,
            @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(
                requestCode,
                permissions,
                grantResults
        );

        if (requestCode ==
                LocationHelper.PERMISSION_REQUEST_CODE) {

            // Try loading the branches again after
            // the user responds to the permission request.
            loadBranches();
        }
    }
}