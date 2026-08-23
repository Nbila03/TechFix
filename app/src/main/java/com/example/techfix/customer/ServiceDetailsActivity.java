package com.example.techfix.customer;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.techfix.R;
import com.example.techfix.booking.BookRepairActivity;

public class ServiceDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_details);

        TextView txtServiceName = findViewById(R.id.txtServiceName);
        TextView txtServiceDescription = findViewById(R.id.txtServiceDescription);
        TextView txtServicePrice = findViewById(R.id.txtServicePrice);
        TextView txtServiceDays = findViewById(R.id.txtServiceDays);
        Button btnBookRepair = findViewById(R.id.btnBookRepair);

        String serviceName = getIntent().getStringExtra("service_name");
        String serviceDescription = getIntent().getStringExtra("service_description");
        String servicePrice = getIntent().getStringExtra("service_price");
        String serviceDays = getIntent().getStringExtra("service_days");

        txtServiceName.setText(serviceName);
        txtServiceDescription.setText(serviceDescription);
        txtServicePrice.setText(servicePrice);
        txtServiceDays.setText(serviceDays);

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