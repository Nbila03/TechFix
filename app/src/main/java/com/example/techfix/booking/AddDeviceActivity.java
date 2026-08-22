package com.example.techfix.booking;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.techfix.R;
import com.example.techfix.database.TechFixDBHelper;
import com.example.techfix.model.Device;

public class AddDeviceActivity extends AppCompatActivity {

    private Spinner spinnerCategory;

    private EditText editDeviceName;
    private EditText editBrand;
    private EditText editModel;
    private EditText editSerialNumber;

    private Button btnBackAddDevice;
    private Button btnSaveDevice;

    private TechFixDBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_device);

        spinnerCategory = findViewById(R.id.spinnerCategory);

        editDeviceName = findViewById(R.id.editDeviceName);
        editBrand = findViewById(R.id.editBrand);
        editModel = findViewById(R.id.editModel);
        editSerialNumber = findViewById(R.id.editSerialNumber);

        btnBackAddDevice = findViewById(R.id.btnBackAddDevice);
        btnSaveDevice = findViewById(R.id.btnSaveDevice);

        // Create local SQLite helper
        dbHelper = new TechFixDBHelper(this);

        setupCategorySpinner();

        // Back button
        btnBackAddDevice.setOnClickListener(v ->
                finish()
        );

        // Save device
        btnSaveDevice.setOnClickListener(v ->
                saveDevice()
        );
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

        /*
         Temporary user ID = 1.

         Later, when login is connected,
         replace this with the actual logged-in user ID.
        */
        int userId = 1;

        Device device =
                new Device(
                        0,
                        userId,
                        categoryId,
                        deviceName,
                        brand,
                        model,
                        serialNumber
                );

        long result =
                dbHelper.insertDevice(device);

        if (result != -1) {

            Toast.makeText(
                    this,
                    "Device saved successfully",
                    Toast.LENGTH_SHORT
            ).show();

            finish();

        } else {

            Toast.makeText(
                    this,
                    "Failed to save device",
                    Toast.LENGTH_SHORT
            ).show();
        }
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