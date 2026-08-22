package com.example.techfix.booking;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.techfix.R;
import com.example.techfix.database.TechFixDBHelper;

public class AppointmentConfirmationActivity extends AppCompatActivity {

    private TextView tvConfirmDevice;
    private TextView tvConfirmService;
    private TextView tvConfirmProblem;
    private TextView tvConfirmAppointment;

    private ImageView imgConfirmPhoto;

    private Button btnBackConfirmation;
    private Button btnConfirmBooking;

    private TechFixDBHelper dbHelper;

    private int deviceId;

    private String deviceName;
    private String deviceBrand;
    private String deviceModel;

    private String serviceName;
    private String problemDescription;

    private String appointmentDate;
    private String appointmentTime;

    private String imagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_appointment_confirmation);

        tvConfirmDevice =
                findViewById(R.id.tvConfirmDevice);

        tvConfirmService =
                findViewById(R.id.tvConfirmService);

        tvConfirmProblem =
                findViewById(R.id.tvConfirmProblem);

        tvConfirmAppointment =
                findViewById(R.id.tvConfirmAppointment);

        imgConfirmPhoto =
                findViewById(R.id.imgConfirmPhoto);

        btnBackConfirmation =
                findViewById(R.id.btnBackConfirmation);

        btnConfirmBooking =
                findViewById(R.id.btnConfirmBooking);

        // Local SQLite helper
        dbHelper =
                new TechFixDBHelper(this);

        // Read all data received from RepairDetailsActivity
        readBookingData();

        // Display booking information
        displayBookingData();

        // Return to Repair Details
        btnBackConfirmation.setOnClickListener(v ->
                finish()
        );

        // Save booking into SQLite
        btnConfirmBooking.setOnClickListener(v ->
                saveRepairRequest()
        );
    }

    private void readBookingData() {

        deviceId =
                getIntent().getIntExtra(
                        "DEVICE_ID",
                        -1
                );

        deviceName =
                getIntent().getStringExtra(
                        "DEVICE_NAME"
                );

        deviceBrand =
                getIntent().getStringExtra(
                        "DEVICE_BRAND"
                );

        deviceModel =
                getIntent().getStringExtra(
                        "DEVICE_MODEL"
                );

        serviceName =
                getIntent().getStringExtra(
                        "SERVICE_NAME"
                );

        problemDescription =
                getIntent().getStringExtra(
                        "PROBLEM_DESCRIPTION"
                );

        appointmentDate =
                getIntent().getStringExtra(
                        "APPOINTMENT_DATE"
                );

        appointmentTime =
                getIntent().getStringExtra(
                        "APPOINTMENT_TIME"
                );

        imagePath =
                getIntent().getStringExtra(
                        "IMAGE_PATH"
                );
    }

    private void displayBookingData() {

        tvConfirmDevice.setText(
                deviceName
                        + "\n"
                        + deviceBrand
                        + " • "
                        + deviceModel
        );

        tvConfirmService.setText(
                serviceName
        );

        tvConfirmProblem.setText(
                problemDescription
        );

        tvConfirmAppointment.setText(
                appointmentDate
                        + " • "
                        + appointmentTime
        );

        if (imagePath != null
                && !imagePath.isEmpty()) {

            imgConfirmPhoto.setImageBitmap(
                    BitmapFactory.decodeFile(
                            imagePath
                    )
            );
        }
    }

    private void saveRepairRequest() {

        if (deviceId == -1) {

            Toast.makeText(
                    this,
                    "Invalid device selected",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        long result =
                dbHelper.insertRepairRequest(
                        deviceId,
                        serviceName,
                        problemDescription,
                        appointmentDate,
                        appointmentTime,
                        imagePath
                );

        if (result != -1) {

            Toast.makeText(
                    this,
                    "Repair booking confirmed",
                    Toast.LENGTH_SHORT
            ).show();

            // Prevent repeated accidental inserts
            btnConfirmBooking.setEnabled(false);

        } else {

            Toast.makeText(
                    this,
                    "Failed to save repair booking",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }
}