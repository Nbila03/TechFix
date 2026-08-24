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

        setContentView(R.layout.activity_payment_result);

        // Get payment information from PaymentActivity

        String repairId =
                getIntent().getStringExtra("repairId");

        String serviceName =
                getIntent().getStringExtra("serviceName");

        double amount =
                getIntent().getDoubleExtra(
                        "amount",
                        0.00
                );

        String transactionId =
                getIntent().getStringExtra(
                        "transactionId"
                );

        String paymentDate =
                getIntent().getStringExtra(
                        "paymentDate"
                );

        // Find TextViews

        TextView tvRepairId =
                findViewById(
                        R.id.tvReceiptRepairId
                );

        TextView tvService =
                findViewById(
                        R.id.tvReceiptService
                );

        TextView tvDate =
                findViewById(
                        R.id.tvReceiptDate
                );

        TextView tvTransactionId =
                findViewById(
                        R.id.tvTransactionId
                );

        TextView tvAmount =
                findViewById(
                        R.id.tvReceiptAmount
                );

        // Display repair information

        tvRepairId.setText(
                "REPAIR #" + repairId
        );

        tvService.setText(
                serviceName
        );

        // Display payment date

        if (paymentDate != null &&
                !paymentDate.isEmpty()) {

            tvDate.setText(paymentDate);

        } else {

            String currentDate =
                    new SimpleDateFormat(
                            "dd MMMM yyyy",
                            Locale.getDefault()
                    ).format(new Date());

            tvDate.setText(currentDate);
        }

        // Display REAL PayHere transaction ID

        if (transactionId != null &&
                !transactionId.isEmpty()) {

            tvTransactionId.setText(
                    transactionId
            );

        } else {

            tvTransactionId.setText(
                    "N/A"
            );
        }

        // Display amount

        tvAmount.setText(
                "LKR " +
                        String.format(
                                Locale.US,
                                "%,.2f",
                                amount
                        )
        );
    }
}