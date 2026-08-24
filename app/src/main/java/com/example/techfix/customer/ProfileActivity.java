package com.example.techfix.customer;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.techfix.R;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Connect buttons
        Button btnLogout = findViewById(R.id.btnLogout);
        Button btnBackProfile = findViewById(R.id.btnBackProfile);

        // Back button - return to previous page
        btnBackProfile.setOnClickListener(v -> finish());

        // Logout button
        btnLogout.setOnClickListener(v -> {

            Intent intent = new Intent(
                    ProfileActivity.this,
                    LoginActivity.class
            );

            intent.setFlags(
                    Intent.FLAG_ACTIVITY_NEW_TASK |
                            Intent.FLAG_ACTIVITY_CLEAR_TASK
            );

            startActivity(intent);
            finish();
        });
    }
}