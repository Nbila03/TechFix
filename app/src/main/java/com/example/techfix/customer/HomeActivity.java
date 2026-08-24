package com.example.techfix.customer;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.techfix.R;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Button btnServices = findViewById(R.id.btnServices);
        Button btnProfile = findViewById(R.id.btnProfile);

        btnServices.setOnClickListener(v -> {
            Intent intent = new Intent(
                    HomeActivity.this,
                    ServicesActivity.class
            );
            startActivity(intent);
        });

        btnProfile.setOnClickListener(v -> {
            Intent intent = new Intent(
                    HomeActivity.this,
                    ProfileActivity.class
            );
            startActivity(intent);
        });
    }
}
