package com.example.techfix.customer;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.techfix.R;
import com.example.techfix.booking.BookRepairActivity;

public class ServiceDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_details);

        // Connect views
        TextView txtServiceName = findViewById(R.id.txtServiceName);
        TextView txtServiceDescription = findViewById(R.id.txtServiceDescription);
        TextView txtServicePrice = findViewById(R.id.txtServicePrice);
        TextView txtServiceDays = findViewById(R.id.txtServiceDays);

        ImageView imgServiceSample = findViewById(R.id.imgServiceSample);

        Button btnBookRepair = findViewById(R.id.btnBookRepair);
        Button btnBack = findViewById(R.id.btnBack);

        // Get service details from previous activity
        String serviceName = getIntent().getStringExtra("service_name");
        String serviceDescription = getIntent().getStringExtra("service_description");
        String servicePrice = getIntent().getStringExtra("service_price");
        String serviceDays = getIntent().getStringExtra("service_days");

        int serviceImage = getIntent().getIntExtra(
                "service_image",
                R.drawable.ic_launcher_foreground
        );

        // Display service details
        if (serviceName != null) {
            txtServiceName.setText(serviceName);
        }

        if (serviceDescription != null) {
            txtServiceDescription.setText(serviceDescription);
        }

        if (servicePrice != null) {
            txtServicePrice.setText(servicePrice);
        }

        if (serviceDays != null) {
            txtServiceDays.setText(serviceDays);
        }

        // Display sample image
        imgServiceSample.setImageResource(serviceImage);

        // Back button
        btnBack.setOnClickListener(v -> finish());

        // Book Repair button
        btnBookRepair.setOnClickListener(v -> {

            Intent intent = new Intent(
                    ServiceDetailsActivity.this,
                    BookRepairActivity.class
            );

            intent.putExtra("service_name", serviceName);

            startActivity(intent);
        });
    }
}