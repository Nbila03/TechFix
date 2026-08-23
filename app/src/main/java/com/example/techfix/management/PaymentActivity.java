package com.example.techfix.management;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.techfix.R;
import com.google.android.material.button.MaterialButton;

import android.widget.TextView;

public class PaymentActivity extends AppCompatActivity {

    // Payment details
    String repairId = "1024";
    String serviceName = "Full Vehicle Service";
    double amount = 25000.00;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Connect this Java file to activity_payment.xml
        setContentView(R.layout.activity_payment);

        // Find the TextViews from XML
        TextView repairIdText = findViewById(R.id.tvPaymentRepairId);
        TextView serviceText = findViewById(R.id.tvPaymentService);
        TextView totalText = findViewById(R.id.tvPaymentTotal);

        // Find the Pay Now button
        MaterialButton payButton = findViewById(R.id.btnPayNow);

        // Display repair information
        repairIdText.setText("Repair #" + repairId);
        serviceText.setText(serviceName);

        // Display amount
        totalText.setText("LKR " + String.format("%.2f", amount));

        // Display amount on button
        payButton.setText(
                "PAY LKR " + String.format("%.2f", amount)
        );

        // When user clicks Pay Now
        payButton.setOnClickListener(view -> {

            // Open Payment Success screen
            Intent intent = new Intent(
                    PaymentActivity.this,
                    PaymentSuccessActivity.class
            );

            // Send payment information
            intent.putExtra("repairId", repairId);
            intent.putExtra("serviceName", serviceName);
            intent.putExtra("amount", amount);

            startActivity(intent);
        });
    }
}

