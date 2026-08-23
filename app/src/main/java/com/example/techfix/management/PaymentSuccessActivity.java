package com.example.techfix.management;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.techfix.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PaymentSuccessActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Connect Java code to the success XML
        setContentView(R.layout.activity_payment_result);

        // Get information sent from PaymentActivity
        String repairId = getIntent().getStringExtra("repairId");
        String serviceName = getIntent().getStringExtra("serviceName");

        double amount = getIntent().getDoubleExtra(
                "amount",
                0.00
        );

        // Find TextViews
        TextView tvRepairId = findViewById(R.id.tvReceiptRepairId);
        TextView tvService = findViewById(R.id.tvReceiptService);
        TextView tvDate = findViewById(R.id.tvReceiptDate);
        TextView tvTransactionId = findViewById(R.id.tvTransactionId);
        TextView tvAmount = findViewById(R.id.tvReceiptAmount);

        // Display repair information
        tvRepairId.setText("REPAIR #" + repairId);
        tvService.setText(serviceName);

        // Display current date
        String currentDate = new SimpleDateFormat(
                "dd MMMM yyyy",
                Locale.getDefault()
        ).format(new Date());

        tvDate.setText(currentDate);

        // Create a simple transaction ID for now
        String transactionId = "TXN-" + System.currentTimeMillis();

        tvTransactionId.setText(transactionId);

        // Display amount
        tvAmount.setText(
                "LKR " + String.format("%.2f", amount)
        );
    }
}
