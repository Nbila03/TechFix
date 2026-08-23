package com.example.techfix.branch;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.techfix.R;
import com.example.techfix.location.MapIntentHelper;

public class BranchDetailsActivity extends AppCompatActivity{
    private double latitude, longitude;
    private String branchName, phone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_branch_details);

        branchName = getIntent().getStringExtra("branch_name");
        String address = getIntent().getStringExtra("branch_address");
        phone = getIntent().getStringExtra("branch_phone");
        latitude = getIntent().getDoubleExtra("branch_lat", 0);
        longitude = getIntent().getDoubleExtra("branch_lng", 0);

        setTitle(branchName);

        TextView tvName = findViewById(R.id.tvDetailBranchName);
        TextView tvAddress = findViewById(R.id.tvDetailBranchAddress);
        TextView tvPhone = findViewById(R.id.tvDetailBranchPhone);
        Button btnOpenMaps = findViewById(R.id.btnOpenMaps);
        Button btnDirections = findViewById(R.id.btnDirections);
        Button btnCall = findViewById(R.id.btnCallBranch);

        tvName.setText(branchName);
        tvAddress.setText(address);
        tvPhone.setText(phone);

        btnOpenMaps.setOnClickListener(v ->
                MapIntentHelper.openLocation(this, latitude, longitude, branchName));

        btnDirections.setOnClickListener(v ->
                MapIntentHelper.openDirections(this, latitude, longitude));

        btnCall.setOnClickListener(v -> {
            Intent dialIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
            startActivity(dialIntent);
        });
    }
}