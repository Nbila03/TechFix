package com.example.techfix.management;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.techfix.R;
import com.google.android.material.button.MaterialButton;

import android.widget.TextView;

// PayHere SDK
import lk.payhere.androidsdk.PHConfigs;
import lk.payhere.androidsdk.PHConstants;
import lk.payhere.androidsdk.PHMainActivity;
import lk.payhere.androidsdk.model.Customer;
import lk.payhere.androidsdk.model.InitRequest;

public class PaymentActivity extends AppCompatActivity {

    // PayHere result request code
    private static final int PAYHERE_REQUEST = 11001;

    // Payment details
    String repairId = "1024";
    String serviceName = "Full Vehicle Service";
    double amount = 25000.00;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Connect Java to payment XML
        setContentView(R.layout.activity_payment);

        // Find views
        TextView repairIdText = findViewById(R.id.tvPaymentRepairId);
        TextView serviceText = findViewById(R.id.tvPaymentService);
        TextView totalText = findViewById(R.id.tvPaymentTotal);

        MaterialButton payButton = findViewById(R.id.btnPayNow);

        // Display repair information
        repairIdText.setText("Repair #" + repairId);
        serviceText.setText(serviceName);

        // Display amount
        totalText.setText(
                "LKR " + String.format("%.2f", amount)
        );

        // Display amount on button
        payButton.setText(
                "PAY LKR " + String.format("%.2f", amount)
        );

        // Pay Now button
        payButton.setOnClickListener(view -> startPayHerePayment());
    }

    private void startPayHerePayment() {

        // Create PayHere payment request
        InitRequest request = new InitRequest();

        // Use Sandbox
        request.setSandBox(true);

        // Your PayHere Sandbox Merchant ID
        request.setMerchantId("1237662");

        // Payment information
        request.setOrderId("REPAIR-" + repairId);
        request.setItemsDescription(serviceName);
        request.setCurrency("LKR");
        request.setAmount(amount);

        // Customer information
        Customer customer = new Customer();

        customer.setFirstName("TechFix");
        customer.setLastName("Customer");
        customer.setEmail("customer@example.com");
        customer.setPhone("0771234567");

        request.setCustomer(customer);

        // Tell PayHere to use Sandbox
        PHConfigs.setBaseUrl(PHConfigs.SANDBOX_URL);

        // Open PayHere
        Intent intent = new Intent(
                PaymentActivity.this,
                PHMainActivity.class
        );

        // Send payment request to PayHere
        intent.putExtra(
                PHConstants.INTENT_EXTRA_DATA,
                request
        );

        // Start PayHere
        startActivityForResult(
                intent,
                PAYHERE_REQUEST
        );
    }

    @Override
    protected void onActivityResult(
            int requestCode,
            int resultCode,
            Intent data
    ) {
        super.onActivityResult(
                requestCode,
                resultCode,
                data
        );

        // Payment result will be handled
        // in the next step.
    }
}