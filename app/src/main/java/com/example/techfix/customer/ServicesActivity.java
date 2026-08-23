package com.example.techfix.customer;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.techfix.R;

public class ServicesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services);

        // Back button
        Button btnBackServices = findViewById(R.id.btnBackServices);

        // Service cards
        LinearLayout serviceScreen = findViewById(R.id.serviceScreen);
        LinearLayout serviceBattery = findViewById(R.id.serviceBattery);
        LinearLayout serviceLaptop = findViewById(R.id.serviceLaptop);
        LinearLayout serviceKeyboard = findViewById(R.id.serviceKeyboard);

        // Back to previous page
        btnBackServices.setOnClickListener(v -> finish());

        // Screen Replacement
        serviceScreen.setOnClickListener(v ->
                openServiceDetails(
                        "Screen Replacement",
                        "Replace damaged or cracked mobile phone screens.",
                        "Starting from Rs. 8,000",
                        "Estimated time: 1 - 2 days"
                )
        );

        // Battery Replacement
        serviceBattery.setOnClickListener(v ->
                openServiceDetails(
                        "Battery Replacement",
                        "Replace weak, damaged, or fast-draining batteries.",
                        "Starting from Rs. 5,000",
                        "Estimated time: 1 day"
                )
        );

        // Laptop Repair
        serviceLaptop.setOnClickListener(v ->
                openServiceDetails(
                        "Laptop Repair",
                        "Diagnosis and repair for common laptop hardware problems.",
                        "Starting from Rs. 6,000",
                        "Estimated time: 2 - 4 days"
                )
        );

        // Keyboard Replacement
        serviceKeyboard.setOnClickListener(v ->
                openServiceDetails(
                        "Keyboard Replacement",
                        "Repair or replacement of damaged laptop keyboards.",
                        "Starting from Rs. 4,500",
                        "Estimated time: 1 - 2 days"
                )
        );
    }

    private void openServiceDetails(
            String name,
            String description,
            String price,
            String days
    ) {

        Intent intent = new Intent(
                ServicesActivity.this,
                ServiceDetailsActivity.class
        );

        intent.putExtra("service_name", name);
        intent.putExtra("service_description", description);
        intent.putExtra("service_price", price);
        intent.putExtra("service_days", days);

        startActivity(intent);
    }
}