package com.example.techfix.booking;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.techfix.R;

public class AppointmentConfirmationActivity extends AppCompatActivity {

    private TextView tvConfirmDevice;
    private TextView tvConfirmService;
    private TextView tvConfirmProblem;
    private TextView tvConfirmAppointment;

    private ImageView imgConfirmPhoto;

    private Button btnBackConfirmation;
    private Button btnConfirmBooking;

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

        String deviceName =
                getIntent().getStringExtra(
                        "DEVICE_NAME"
                );

        String deviceBrand =
                getIntent().getStringExtra(
                        "DEVICE_BRAND"
                );

        String deviceModel =
                getIntent().getStringExtra(
                        "DEVICE_MODEL"
                );

        String serviceName =
                getIntent().getStringExtra(
                        "SERVICE_NAME"
                );

        String problemDescription =
                getIntent().getStringExtra(
                        "PROBLEM_DESCRIPTION"
                );

        String appointmentDate =
                getIntent().getStringExtra(
                        "APPOINTMENT_DATE"
                );

        String appointmentTime =
                getIntent().getStringExtra(
                        "APPOINTMENT_TIME"
                );

        String imagePath =
                getIntent().getStringExtra(
                        "IMAGE_PATH"
                );

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

        // Return to Repair Details
        btnBackConfirmation.setOnClickListener(v ->
                finish()
        );

        // Confirm booking
        btnConfirmBooking.setOnClickListener(v -> {

            Toast.makeText(
                    this,
                    "Repair booking confirmed",
                    Toast.LENGTH_SHORT
            ).show();

        });
    }
}