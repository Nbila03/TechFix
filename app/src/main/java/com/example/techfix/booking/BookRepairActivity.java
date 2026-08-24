package com.example.techfix.booking;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.techfix.R;

public class BookRepairActivity extends AppCompatActivity {

    private TextView tvSelectedDevice;
    private TextView tvServiceInfo;

    private Spinner spinnerService;

    private Button btnBackBookRepair;
    private Button btnContinue;

    private int deviceId;
    private String deviceName;
    private String deviceBrand;
    private String deviceModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_book_repair);

        // Connect UI components
        tvSelectedDevice =
                findViewById(R.id.tvSelectedDevice);

        tvServiceInfo =
                findViewById(R.id.tvServiceInfo);

        spinnerService =
                findViewById(R.id.spinnerService);

        btnBackBookRepair =
                findViewById(R.id.btnBackBookRepair);

        btnContinue =
                findViewById(R.id.btnContinue);

        // Read selected device information
        deviceId =
                getIntent().getIntExtra(
                        "DEVICE_ID",
                        -1
                );

        deviceName =
                getIntent().getStringExtra(
                        "DEVICE_NAME"
                );

        deviceBrand =
                getIntent().getStringExtra(
                        "DEVICE_BRAND"
                );

        deviceModel =
                getIntent().getStringExtra(
                        "DEVICE_MODEL"
                );

        // Display selected device
        tvSelectedDevice.setText(
                deviceName
                        + "\n"
                        + deviceBrand
                        + " • "
                        + deviceModel
        );

        // Available repair services
        String[] services = {
                "Screen Replacement",
                "Battery Replacement",
                "Charging Port Repair",
                "Keyboard Repair"
        };

        ArrayAdapter<String> serviceAdapter =
                new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_spinner_item,
                        services
                );

        serviceAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        spinnerService.setAdapter(serviceAdapter);

        // Back to previous screen
        btnBackBookRepair.setOnClickListener(v ->
                finish()
        );

        // Continue to repair details
        btnContinue.setOnClickListener(v -> {

            String selectedService =
                    spinnerService
                            .getSelectedItem()
                            .toString();

            Intent intent = new Intent(
                    BookRepairActivity.this,
                    RepairDetailsActivity.class
            );

            intent.putExtra(
                    "DEVICE_ID",
                    deviceId
            );

            intent.putExtra(
                    "DEVICE_NAME",
                    deviceName
            );

            intent.putExtra(
                    "DEVICE_BRAND",
                    deviceBrand
            );

            intent.putExtra(
                    "DEVICE_MODEL",
                    deviceModel
            );

            intent.putExtra(
                    "SERVICE_NAME",
                    selectedService
            );

            startActivity(intent);
        });
    }
}
