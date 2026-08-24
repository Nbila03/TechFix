package com.example.techfix.branch;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.techfix.R;
import com.example.techfix.location.MapIntentHelper;

public class BranchDetailsActivity extends AppCompatActivity {

    private double latitude;
    private double longitude;

    private String branchName;
    private String phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_branch_details);

        // Get branch information from the previous screen.
        branchName = getIntent().getStringExtra("branch_name");

        String address =
                getIntent().getStringExtra("branch_address");

        phone =
                getIntent().getStringExtra("branch_phone");

        latitude =
                getIntent().getDoubleExtra("branch_lat", 0);

        longitude =
                getIntent().getDoubleExtra("branch_lng", 0);

        // Set the screen title.
        setTitle(branchName);

        // Find the views from the layout.
        TextView tvName =
                findViewById(R.id.tvDetailBranchName);

        TextView tvAddress =
                findViewById(R.id.tvDetailBranchAddress);

        TextView tvPhone =
                findViewById(R.id.tvDetailBranchPhone);

        Button btnOpenMaps =
                findViewById(R.id.btnOpenMaps);

        Button btnDirections =
                findViewById(R.id.btnDirections);

        Button btnCall =
                findViewById(R.id.btnCallBranch);

        // Display the branch information.
        tvName.setText(branchName);
        tvAddress.setText(address);
        tvPhone.setText(phone);

        // Open the branch location in Maps.
        btnOpenMaps.setOnClickListener(v -> {

            MapIntentHelper.openLocation(
                    this,
                    latitude,
                    longitude,
                    branchName
            );
        });

        // Open directions to the branch.
        btnDirections.setOnClickListener(v -> {

            MapIntentHelper.openDirections(
                    this,
                    latitude,
                    longitude
            );
        });

        // Open the phone dialler with the branch number.
        btnCall.setOnClickListener(v -> {

            Uri phoneUri =
                    Uri.parse("tel:" + phone);

            Intent dialIntent =
                    new Intent(
                            Intent.ACTION_DIAL,
                            phoneUri
                    );

            startActivity(dialIntent);
        });
    }
}