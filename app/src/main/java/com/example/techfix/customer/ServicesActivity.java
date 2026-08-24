package com.example.techfix.customer;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.techfix.R;
import com.example.techfix.adapter.ServiceAdapter;
import com.example.techfix.model.RepairService;

import java.util.ArrayList;
import java.util.List;

public class ServicesActivity extends AppCompatActivity {

    private RecyclerView recyclerServices;
    private ServiceAdapter serviceAdapter;
    private List<RepairService> serviceList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services);

        Button btnBackServices = findViewById(R.id.btnBackServices);
        recyclerServices = findViewById(R.id.recyclerServices);

        // Back button
        btnBackServices.setOnClickListener(v -> finish());

        // RecyclerView layout
        recyclerServices.setLayoutManager(
                new LinearLayoutManager(this)
        );

        // Create service list
        serviceList = new ArrayList<>();

        serviceList.add(new RepairService(
                "Screen Replacement",
                "Replace damaged or cracked mobile phone screens.",
                "Starting from Rs. 8,000",
                "Estimated time: 1 - 2 days",
                R.drawable.ic_launcher_foreground
        ));

        serviceList.add(new RepairService(
                "Battery Replacement",
                "Replace weak, damaged, or fast-draining batteries.",
                "Starting from Rs. 5,000",
                "Estimated time: 1 day",
                R.drawable.ic_launcher_foreground
        ));

        serviceList.add(new RepairService(
                "Laptop Repair",
                "Diagnosis and repair for common laptop hardware problems.",
                "Starting from Rs. 6,000",
                "Estimated time: 2 - 4 days",
                R.drawable.ic_launcher_foreground
        ));

        serviceList.add(new RepairService(
                "Keyboard Replacement",
                "Repair or replacement of damaged laptop keyboards.",
                "Starting from Rs. 4,500",
                "Estimated time: 1 - 2 days",
                R.drawable.ic_launcher_foreground
        ));

        // Create adapter
        serviceAdapter = new ServiceAdapter(
                serviceList,
                this::openServiceDetails
        );

        // Connect adapter to RecyclerView
        recyclerServices.setAdapter(serviceAdapter);
    }

    private void openServiceDetails(RepairService service) {

        Intent intent = new Intent(
                ServicesActivity.this,
                ServiceDetailsActivity.class
        );

        intent.putExtra(
                "service_name",
                service.getName()
        );

        intent.putExtra(
                "service_description",
                service.getDescription()
        );

        intent.putExtra(
                "service_price",
                service.getPrice()
        );

        intent.putExtra(
                "service_days",
                service.getEstimatedDays()
        );

        intent.putExtra(
                "service_image",
                service.getImageResId()
        );

        startActivity(intent);
    }
}