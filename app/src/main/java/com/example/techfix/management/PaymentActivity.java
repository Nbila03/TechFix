package com.example.techfix.management;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.techfix.R;
import com.example.techfix.firebase.PaymentRepository;
import com.example.techfix.model.Payment;
import com.google.android.material.button.MaterialButton;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import lk.payhere.androidsdk.PHConfigs;
import lk.payhere.androidsdk.PHConstants;
import lk.payhere.androidsdk.PHMainActivity;
import lk.payhere.androidsdk.PHResponse;
import lk.payhere.androidsdk.model.Customer;
import lk.payhere.androidsdk.model.InitRequest;
import lk.payhere.androidsdk.model.StatusResponse;

public class PaymentActivity extends AppCompatActivity {

    private static final String TAG = "PAYHERE_DEBUG";
    private static final int PAYHERE_REQUEST = 11001;

    private String repairId = "1024";
    private String serviceName = "Full Vehicle Service";
    private double amount = 25000.00;

    private PaymentRepository paymentRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_payment);

        // Initialize Payment Repository
        paymentRepository = new PaymentRepository();

        /*
         * If another activity sends actual repair information,
         * use it instead of the demo values above.
         */
        Intent receivedIntent = getIntent();

        if (receivedIntent != null) {

            String receivedRepairId =
                    receivedIntent.getStringExtra("REPAIR_ID");

            String receivedService =
                    receivedIntent.getStringExtra("SERVICE_NAME");

            double receivedAmount =
                    receivedIntent.getDoubleExtra(
                            "PAYMENT_AMOUNT",
                            -1
                    );

            if (receivedRepairId != null &&
                    !receivedRepairId.trim().isEmpty()) {

                repairId = receivedRepairId;
            }

            if (receivedService != null &&
                    !receivedService.trim().isEmpty()) {

                serviceName = receivedService;
            }

            if (receivedAmount > 0) {
                amount = receivedAmount;
            }
        }

        TextView repairIdText =
                findViewById(R.id.tvPaymentRepairId);

        TextView serviceText =
                findViewById(R.id.tvPaymentService);

        TextView totalText =
                findViewById(R.id.tvPaymentTotal);

        MaterialButton payButton =
                findViewById(R.id.btnPayNow);

        // Display repair ID

        repairIdText.setText(
                "Repair #" + repairId
        );

        // Display service

        serviceText.setText(
                serviceName
        );

        // Display amount

        totalText.setText(
                String.format(
                        Locale.US,
                        "LKR %,.2f",
                        amount
                )
        );

        // Display amount on Pay button

        payButton.setText(
                String.format(
                        Locale.US,
                        "PAY LKR %,.2f",
                        amount
                )
        );

        // Start PayHere when button is clicked

        payButton.setOnClickListener(
                view -> startPayHerePayment()
        );
    }


    /**
     * Starts PayHere Sandbox payment.
     */
    private void startPayHerePayment() {

        Log.d(TAG, "====================================");
        Log.d(TAG, "Starting PayHere payment");
        Log.d(TAG, "====================================");

        try {

            /*
             * Get PayHere Sandbox credentials
             * from strings.xml.
             */
            String merchantId =
                    getString(
                            R.string.payhere_merchant_id
                    ).trim();

            String merchantSecret =
                    getString(
                            R.string.payhere_merchant_secret
                    ).trim();

            if (merchantId.isEmpty() ||
                    merchantSecret.isEmpty()) {

                Toast.makeText(
                        this,
                        "PayHere merchant credentials are missing",
                        Toast.LENGTH_LONG
                ).show();

                Log.e(
                        TAG,
                        "Merchant ID or Merchant Secret is empty"
                );

                return;
            }

            /*
             * Generate a unique PayHere order ID.
             */
            String orderId =
                    "REPAIR-" +
                            repairId +
                            "-" +
                            System.currentTimeMillis();

            InitRequest request =
                    new InitRequest();

            /*
             * TRUE = Sandbox
             * FALSE = Live
             */
            request.setSandBox(true);

            request.setMerchantId(
                    merchantId
            );

            request.setMerchantSecret(
                    merchantSecret
            );

            /*
             * Payment information
             */
            request.setOrderId(
                    orderId
            );

            request.setItemsDescription(
                    serviceName
            );

            request.setCurrency(
                    "LKR"
            );

            request.setAmount(
                    amount
            );

            /*
             * Customer information
             */
            Customer customer =
                    new Customer();

            customer.setFirstName(
                    "TechFix"
            );

            customer.setLastName(
                    "Customer"
            );

            customer.setEmail(
                    "customer@example.com"
            );

            customer.setPhone(
                    "+94771234567"
            );

            /*
             * Customer address
             */
            customer.getAddress().setAddress(
                    "No. 1, Galle Road"
            );

            customer.getAddress().setCity(
                    "Colombo"
            );

            customer.getAddress().setCountry(
                    "Sri Lanka"
            );

            request.setCustomer(
                    customer
            );

            /*
             * Force PayHere Sandbox URL.
             */
            PHConfigs.setBaseUrl(
                    PHConfigs.SANDBOX_URL
            );

            Log.d(
                    TAG,
                    "PayHere mode: SANDBOX"
            );

            Log.d(
                    TAG,
                    "Base URL: " +
                            PHConfigs.getBaseUrl()
            );

            Log.d(
                    TAG,
                    "Merchant ID: " +
                            merchantId
            );

            /*
             * Never print Merchant Secret.
             */
            Log.d(
                    TAG,
                    "Merchant Secret: [HIDDEN]"
            );

            Log.d(
                    TAG,
                    "Order ID: " +
                            orderId
            );

            Log.d(
                    TAG,
                    "Repair ID: " +
                            repairId
            );

            Log.d(
                    TAG,
                    "Service: " +
                            serviceName
            );

            Log.d(
                    TAG,
                    "Amount: " +
                            amount
            );

            /*
             * Start PayHere Activity.
             */
            Intent payHereIntent =
                    new Intent(
                            PaymentActivity.this,
                            PHMainActivity.class
                    );

            payHereIntent.putExtra(
                    PHConstants.INTENT_EXTRA_DATA,
                    request
            );

            Log.d(
                    TAG,
                    "Launching PayHere SDK..."
            );

            startActivityForResult(
                    payHereIntent,
                    PAYHERE_REQUEST
            );

        } catch (Exception e) {

            Log.e(
                    TAG,
                    "Error while starting PayHere",
                    e
            );

            Toast.makeText(
                    this,
                    "Unable to start PayHere: "
                            + e.getMessage(),
                    Toast.LENGTH_LONG
            ).show();
        }
    }


    /**
     * Receives PayHere SDK result.
     */
    @Override
    @SuppressWarnings("unchecked")
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

        if (requestCode != PAYHERE_REQUEST) {
            return;
        }

        Log.d(TAG, "====================================");
        Log.d(TAG, "PayHere returned result");
        Log.d(TAG, "Result code: " + resultCode);
        Log.d(TAG, "====================================");

        /*
         * Check whether PayHere returned an Intent.
         */
        if (data == null) {

            Log.e(
                    TAG,
                    "PayHere returned NULL Intent"
            );

            Toast.makeText(
                    this,
                    "No response received from PayHere",
                    Toast.LENGTH_LONG
            ).show();

            return;
        }

        /*
         * Check for PayHere result.
         */
        if (!data.hasExtra(
                PHConstants.INTENT_EXTRA_RESULT
        )) {

            Log.e(
                    TAG,
                    "INTENT_EXTRA_RESULT is missing"
            );

            /*
             * Print available extras for debugging.
             */
            if (data.getExtras() != null) {

                for (String key :
                        data.getExtras().keySet()) {

                    Object value =
                            data.getExtras().get(key);

                    Log.d(
                            TAG,
                            "Extra -> " +
                                    key +
                                    " = " +
                                    value
                    );
                }
            }

            Toast.makeText(
                    this,
                    "Invalid PayHere response",
                    Toast.LENGTH_LONG
            ).show();

            return;
        }

        PHResponse<StatusResponse> response;

        try {

            /*
             * Read PayHere response.
             */
            if (Build.VERSION.SDK_INT >=
                    Build.VERSION_CODES.TIRAMISU) {

                response =
                        (PHResponse<StatusResponse>)
                                data.getSerializableExtra(
                                        PHConstants
                                                .INTENT_EXTRA_RESULT,
                                        PHResponse.class
                                );

            } else {

                response =
                        (PHResponse<StatusResponse>)
                                data.getSerializableExtra(
                                        PHConstants
                                                .INTENT_EXTRA_RESULT
                                );
            }

        } catch (Exception e) {

            Log.e(
                    TAG,
                    "Could not read PayHere response",
                    e
            );

            Toast.makeText(
                    this,
                    "Unable to read PayHere response",
                    Toast.LENGTH_LONG
            ).show();

            return;
        }

        if (response == null) {

            Log.e(
                    TAG,
                    "PayHere response is NULL"
            );

            Toast.makeText(
                    this,
                    "Empty PayHere response",
                    Toast.LENGTH_LONG
            ).show();

            return;
        }

        /*
         * Debug information.
         */
        Log.d(
                TAG,
                "PHResponse status: " +
                        response.getStatus()
        );

        Log.d(
                TAG,
                "PHResponse success: " +
                        response.isSuccess()
        );

        Log.d(
                TAG,
                "PHResponse: " +
                        response
        );


        // ==========================================
        // PAYMENT SUCCESS
        // ==========================================

        if (resultCode == Activity.RESULT_OK) {

            if (response.isSuccess()) {

                StatusResponse paymentData =
                        response.getData();

                if (paymentData == null) {

                    Log.e(
                            TAG,
                            "Successful response but StatusResponse is NULL"
                    );

                    Toast.makeText(
                            this,
                            "Payment response data is missing",
                            Toast.LENGTH_LONG
                    ).show();

                    return;
                }

                /*
                 * Get PayHere payment information.
                 */
                long paymentNumber =
                        paymentData.getPaymentNo();

                int paymentStatus =
                        paymentData.getStatus();

                String paymentMessage =
                        paymentData.getMessage();

                Log.d(
                        TAG,
                        "========== PAYMENT SUCCESS =========="
                );

                Log.d(
                        TAG,
                        "Payment Number: " +
                                paymentNumber
                );

                Log.d(
                        TAG,
                        "Payment Status: " +
                                paymentStatus
                );

                Log.d(
                        TAG,
                        "Payment Message: " +
                                paymentMessage
                );

                Log.d(
                        TAG,
                        "Status State: " +
                                paymentData.getStatusState()
                );

                Log.d(
                        TAG,
                        "Currency: " +
                                paymentData.getCurrency()
                );

                Log.d(
                        TAG,
                        "====================================="
                );


                // ==========================================
                // CREATE PAYMENT RECORD
                // ==========================================

                /*
                 * Generate TechFix's own payment ID.
                 *
                 * PayHere payment number is stored separately
                 * as the transaction ID.
                 */
                int paymentId =
                        (int) (
                                System.currentTimeMillis()
                                        / 1000
                        );

                /*
                 * PayHere transaction/payment reference.
                 */
                String transactionId =
                        String.valueOf(
                                paymentNumber
                        );

                /*
                 * Current payment date/time.
                 */
                String paymentDate =
                        new SimpleDateFormat(
                                "dd MMMM yyyy HH:mm:ss",
                                Locale.getDefault()
                        ).format(
                                new Date()
                        );

                /*
                 * Convert repair ID to integer because
                 * Payment.repairId is an int.
                 */
                int repairIdNumber;

                try {

                    repairIdNumber =
                            Integer.parseInt(
                                    repairId
                            );

                } catch (NumberFormatException e) {

                    Log.e(
                            TAG,
                            "Invalid repair ID: " +
                                    repairId,
                            e
                    );

                    Toast.makeText(
                            this,
                            "Invalid repair ID",
                            Toast.LENGTH_LONG
                    ).show();

                    return;
                }


                /*
                 * Create Payment object.
                 */
                Payment payment =
                        new Payment(
                                paymentId,
                                repairIdNumber,
                                amount,
                                "PayHere",
                                "PAID",
                                transactionId,
                                paymentDate
                        );


                // ==========================================
                // SAVE PAYMENT TO FIRESTORE
                // ==========================================

                Log.d(
                        TAG,
                        "Saving payment to Firestore..."
                );

                paymentRepository.addPayment(

                        payment,

                        // SUCCESS
                        unused -> {

                            Log.d(
                                    TAG,
                                    "Payment saved to Firestore successfully"
                            );

                            Toast.makeText(
                                    PaymentActivity.this,
                                    "Payment Successful",
                                    Toast.LENGTH_SHORT
                            ).show();


                            // ==================================
                            // OPEN SUCCESS SCREEN
                            // ==================================

                            Intent successIntent =
                                    new Intent(
                                            PaymentActivity.this,
                                            PaymentSuccessActivity.class
                                    );

                            /*
                             * IMPORTANT:
                             *
                             * These names match the keys
                             * expected by PaymentSuccessActivity.
                             */

                            successIntent.putExtra(
                                    "repairId",
                                    repairId
                            );

                            successIntent.putExtra(
                                    "serviceName",
                                    serviceName
                            );

                            successIntent.putExtra(
                                    "amount",
                                    amount
                            );

                            successIntent.putExtra(
                                    "transactionId",
                                    transactionId
                            );

                            successIntent.putExtra(
                                    "paymentDate",
                                    paymentDate
                            );

                            startActivity(
                                    successIntent
                            );

                            finish();
                        },

                        // FAILURE
                        error -> {

                            Log.e(
                                    TAG,
                                    "Failed to save payment",
                                    error
                            );

                            /*
                             * PayHere succeeded but Firebase
                             * saving failed.
                             */
                            Toast.makeText(
                                    PaymentActivity.this,
                                    "Payment successful, but failed to save payment record: "
                                            + error.getMessage(),
                                    Toast.LENGTH_LONG
                            ).show();
                        }
                );

            } else {

                // ==========================================
                // PAYMENT FAILED
                // ==========================================

                String errorMessage =
                        response.toString();

                Log.e(
                        TAG,
                        "PAYMENT FAILED"
                );

                Log.e(
                        TAG,
                        "PayHere error: " +
                                errorMessage
                );

                Toast.makeText(
                        this,
                        "Payment failed: "
                                + errorMessage,
                        Toast.LENGTH_LONG
                ).show();
            }


            // ==========================================
            // PAYMENT CANCELLED
            // ==========================================

        } else if (
                resultCode == Activity.RESULT_CANCELED
        ) {

            String cancelMessage =
                    response.toString();

            Log.e(
                    TAG,
                    "PAYMENT CANCELLED"
            );

            Log.e(
                    TAG,
                    "PayHere response: " +
                            cancelMessage
            );

            Toast.makeText(
                    this,
                    "Payment cancelled: "
                            + cancelMessage,
                    Toast.LENGTH_LONG
            ).show();


            // ==========================================
            // UNKNOWN RESULT
            // ==========================================

        } else {

            Log.e(
                    TAG,
                    "Unknown PayHere result code: "
                            + resultCode
            );

            Toast.makeText(
                    this,
                    "Unknown payment response",
                    Toast.LENGTH_LONG
            ).show();
        }
    }
}