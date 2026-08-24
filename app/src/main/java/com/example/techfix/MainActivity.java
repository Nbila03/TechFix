package com.example.techfix;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.techfix.management.PaymentActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Open Payment screen directly for testing
        Intent intent = new Intent(
                MainActivity.this,
                PaymentActivity.class
        );

        startActivity(intent);

        // Close MainActivity so Back does not return here
        finish();
    }
}