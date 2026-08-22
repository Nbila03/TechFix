package com.example.techfix.booking;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.techfix.R;
import com.example.techfix.adapter.DeviceAdapter;
import com.example.techfix.model.Device;

import java.util.ArrayList;
import java.util.List;

public class MyDevicesActivity extends AppCompatActivity {

    private RecyclerView recyclerDevices;
    private Button btnAddDevice;

    private List<Device> deviceList;
    private DeviceAdapter deviceAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_my_devices);

        recyclerDevices = findViewById(R.id.recyclerDevices);
        btnAddDevice = findViewById(R.id.btnAddDevice);

        deviceList = new ArrayList<>();

        // Temporary test data
        deviceList.add(
                new Device(
                        1,
                        1,
                        1,
                        "My Phone",
                        "Apple",
                        "iPhone 13",
                        "SN001"
                )
        );

        deviceList.add(
                new Device(
                        2,
                        1,
                        2,
                        "My Laptop",
                        "Dell",
                        "Inspiron 15",
                        "SN002"
                )
        );

        deviceAdapter = new DeviceAdapter(
                deviceList,
                device -> {
                    // We will connect Book Repair later
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
    }
}