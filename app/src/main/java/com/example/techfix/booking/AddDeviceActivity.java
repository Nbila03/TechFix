package com.example.techfix.booking;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.techfix.R;

public class AddDeviceActivity extends AppCompatActivity {

    private Spinner spinnerCategory;

    private EditText editDeviceName;
    private EditText editBrand;
    private EditText editModel;
    private EditText editSerialNumber;

    private Button btnBackAddDevice;
    private Button btnSaveDevice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_device);

        // Connect UI components
        spinnerCategory = findViewById(R.id.spinnerCategory);

        editDeviceName = findViewById(R.id.editDeviceName);
        editBrand = findViewById(R.id.editBrand);
        editModel = findViewById(R.id.editModel);
        editSerialNumber = findViewById(R.id.editSerialNumber);

        btnBackAddDevice = findViewById(R.id.btnBackAddDevice);
        btnSaveDevice = findViewById(R.id.btnSaveDevice);

        // Setup device category spinner
        setupCategorySpinner();

        // Back button
        btnBackAddDevice.setOnClickListener(v -> finish());

        // Save device button
        btnSaveDevice.setOnClickListener(v -> saveDevice());
    }

    private void setupCategorySpinner() {

        String[] categories = {
                "Mobile Phone",
                "Laptop",
                "Desktop",
                "Tablet"
        };

        ArrayAdapter<String> categoryAdapter =
                new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_spinner_item,
                        categories
                );

        categoryAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        spinnerCategory.setAdapter(categoryAdapter);
    }

    private void saveDevice() {

        String deviceName =
                editDeviceName
                        .getText()
                        .toString()
                        .trim();

        String brand =
                editBrand
                        .getText()
                        .toString()
                        .trim();

        String model =
                editModel
                        .getText()
                        .toString()
                        .trim();

        String serialNumber =
                editSerialNumber
                        .getText()
                        .toString()
                        .trim();

        String category =
                spinnerCategory
                        .getSelectedItem()
                        .toString();

        // Validate device name
        if (deviceName.isEmpty()) {

            editDeviceName.setError(
                    "Device name is required"
            );

            editDeviceName.requestFocus();
            return;
        }

        // Validate brand
        if (brand.isEmpty()) {

            editBrand.setError(
                    "Brand is required"
            );

            editBrand.requestFocus();
            return;
        }

        // Validate model
        if (model.isEmpty()) {

            editModel.setError(
                    "Model is required"
            );

            editModel.requestFocus();
            return;
        }

        int categoryId =
                getCategoryId(category);

        // Send new device information
        // back to MyDevicesActivity
        Intent resultIntent =
                new Intent();

        resultIntent.putExtra(
                "CATEGORY_ID",
                categoryId
        );

        resultIntent.putExtra(
                "DEVICE_NAME",
                deviceName
        );

        resultIntent.putExtra(
                "BRAND",
                brand
        );

        resultIntent.putExtra(
                "MODEL",
                model
        );

        resultIntent.putExtra(
                "SERIAL_NUMBER",
                serialNumber
        );

        setResult(
                RESULT_OK,
                resultIntent
        );

        Toast.makeText(
                this,
                "Device added successfully",
                Toast.LENGTH_SHORT
        ).show();

        finish();
    }

    private int getCategoryId(String category) {

        switch (category) {

            case "Mobile Phone":
                return 1;

            case "Laptop":
                return 2;

            case "Desktop":
                return 3;

            case "Tablet":
                return 4;

            default:
                return 1;
        }
    }
}