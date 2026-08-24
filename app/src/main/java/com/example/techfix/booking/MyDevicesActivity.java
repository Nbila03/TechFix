package com.example.techfix.booking;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.techfix.R;
import com.example.techfix.adapter.DeviceAdapter;
import com.example.techfix.database.TechFixDBHelper;
import com.example.techfix.model.Device;

import java.util.ArrayList;
import java.util.List;

public class MyDevicesActivity extends AppCompatActivity {

    private RecyclerView recyclerDevices;
    private Button btnAddDevice;

    private List<Device> deviceList;
    private DeviceAdapter deviceAdapter;

    private TechFixDBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_my_devices);

        recyclerDevices = findViewById(R.id.recyclerDevices);
        btnAddDevice = findViewById(R.id.btnAddDevice);

        // Initialize local SQLite helper
        dbHelper = new TechFixDBHelper(this);

        deviceList = new ArrayList<>();

        deviceAdapter = new DeviceAdapter(
                deviceList,
                device -> {

                    Intent intent = new Intent(
                            MyDevicesActivity.this,
                            BookRepairActivity.class
                    );

                    intent.putExtra(
                            "DEVICE_ID",
                            device.getDeviceId()
                    );

                    intent.putExtra(
                            "DEVICE_NAME",
                            device.getDeviceName()
                    );

                    intent.putExtra(
                            "DEVICE_BRAND",
                            device.getBrand()
                    );

                    intent.putExtra(
                            "DEVICE_MODEL",
                            device.getModel()
                    );

                    startActivity(intent);
                }
        );

        recyclerDevices.setLayoutManager(
                new LinearLayoutManager(this)
        );

        recyclerDevices.setAdapter(deviceAdapter);

        btnAddDevice.setOnClickListener(v -> {

            Intent intent = new Intent(
                    MyDevicesActivity.this,
                    AddDeviceActivity.class
            );

            startActivity(intent);
        });

        loadDevices();
    }

    @Override
    protected void onResume() {
        super.onResume();

       
        loadDevices();
    }

    private void loadDevices() {

        List<Device> savedDevices =
                dbHelper.getAllDevices();

        deviceList.clear();
        deviceList.addAll(savedDevices);

        deviceAdapter.notifyDataSetChanged();
    }
}
