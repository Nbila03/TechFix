package com.example.techfix.booking;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.techfix.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

public class RepairDetailsActivity extends AppCompatActivity {

    private TextView tvRepairSummary;
    private TextView tvSelectedDate;
    private TextView tvSelectedTime;

    private EditText editProblemDescription;

    private ImageView imgDamagePhoto;

    private Button btnBackRepairDetails;
    private Button btnTakePhoto;
    private Button btnSelectDate;
    private Button btnSelectTime;
    private Button btnContinueRepair;

    private String selectedDate = "";
    private String selectedTime = "";
    private String damageImagePath = "";

    private Bitmap damageBitmap;

    private int deviceId;
    private String deviceName;
    private String deviceBrand;
    private String deviceModel;
    private String serviceName;

    private ActivityResultLauncher<Void> cameraLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_repair_details);

        // Connect UI components
        tvRepairSummary =
                findViewById(R.id.tvRepairSummary);

        tvSelectedDate =
                findViewById(R.id.tvSelectedDate);

        tvSelectedTime =
                findViewById(R.id.tvSelectedTime);

        editProblemDescription =
                findViewById(R.id.editProblemDescription);

        imgDamagePhoto =
                findViewById(R.id.imgDamagePhoto);

        btnBackRepairDetails =
                findViewById(R.id.btnBackRepairDetails);

        btnTakePhoto =
                findViewById(R.id.btnTakePhoto);

        btnSelectDate =
                findViewById(R.id.btnSelectDate);

        btnSelectTime =
                findViewById(R.id.btnSelectTime);

        btnContinueRepair =
                findViewById(R.id.btnContinueRepair);

        // Read selected device and service
        readBookingData();

        // Setup camera
        setupCamera();

        // Back to Book Repair screen
        btnBackRepairDetails.setOnClickListener(v ->
                finish()
        );

        // Take damage photo
        btnTakePhoto.setOnClickListener(v ->
                cameraLauncher.launch(null)
        );

        // Select appointment date
        btnSelectDate.setOnClickListener(v ->
                showDatePicker()
        );

        // Select appointment time
        btnSelectTime.setOnClickListener(v ->
                showTimePicker()
        );

        // Validate and continue to confirmation
        btnContinueRepair.setOnClickListener(v ->
                validateAndContinue()
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

        tvRepairSummary.setText(
                deviceName
                        + "\n"
                        + deviceBrand
                        + " • "
                        + deviceModel
                        + "\n"
                        + serviceName
        );
    }

    private void setupCamera() {

        cameraLauncher =
                registerForActivityResult(
                        new ActivityResultContracts.TakePicturePreview(),
                        bitmap -> {

                            if (bitmap != null) {

                                damageBitmap = bitmap;

                                imgDamagePhoto.setImageBitmap(
                                        bitmap
                                );

                                damageImagePath =
                                        saveImageToInternalStorage(
                                                bitmap
                                        );

                                Toast.makeText(
                                        this,
                                        "Damage photo captured",
                                        Toast.LENGTH_SHORT
                                ).show();

                            } else {

                                Toast.makeText(
                                        this,
                                        "Photo capture cancelled",
                                        Toast.LENGTH_SHORT
                                ).show();
                            }
                        }
                );
    }

    private String saveImageToInternalStorage(
            Bitmap bitmap
    ) {

        String fileName =
                "repair_"
                        + System.currentTimeMillis()
                        + ".jpg";

        File file =
                new File(
                        getFilesDir(),
                        fileName
                );

        try {

            FileOutputStream outputStream =
                    new FileOutputStream(file);

            bitmap.compress(
                    Bitmap.CompressFormat.JPEG,
                    90,
                    outputStream
            );

            outputStream.flush();
            outputStream.close();

            return file.getAbsolutePath();

        } catch (IOException e) {

            e.printStackTrace();

            Toast.makeText(
                    this,
                    "Unable to save image",
                    Toast.LENGTH_SHORT
            ).show();

            return "";
        }
    }

    private void showDatePicker() {

        Calendar calendar =
                Calendar.getInstance();

        int year =
                calendar.get(
                        Calendar.YEAR
                );

        int month =
                calendar.get(
                        Calendar.MONTH
                );

        int day =
                calendar.get(
                        Calendar.DAY_OF_MONTH
                );

        DatePickerDialog datePickerDialog =
                new DatePickerDialog(
                        this,
                        (view,
                         selectedYear,
                         selectedMonth,
                         selectedDay) -> {

                            selectedDate =
                                    selectedYear
                                            + "-"
                                            + String.format(
                                            "%02d",
                                            selectedMonth + 1
                                    )
                                            + "-"
                                            + String.format(
                                            "%02d",
                                            selectedDay
                                    );

                            tvSelectedDate.setText(
                                    selectedDate
                            );
                        },
                        year,
                        month,
                        day
                );

        // Prevent selecting past dates
        datePickerDialog
                .getDatePicker()
                .setMinDate(
                        System.currentTimeMillis()
                );

        datePickerDialog.show();
    }

    private void showTimePicker() {

        Calendar calendar =
                Calendar.getInstance();

        int hour =
                calendar.get(
                        Calendar.HOUR_OF_DAY
                );

        int minute =
                calendar.get(
                        Calendar.MINUTE
                );

        TimePickerDialog timePickerDialog =
                new TimePickerDialog(
                        this,
                        (view,
                         selectedHour,
                         selectedMinute) -> {

                            selectedTime =
                                    String.format(
                                            "%02d:%02d",
                                            selectedHour,
                                            selectedMinute
                                    );

                            tvSelectedTime.setText(
                                    selectedTime
                            );
                        },
                        hour,
                        minute,
                        true
                );

        timePickerDialog.show();
    }

    private void validateAndContinue() {

        String problemDescription =
                editProblemDescription
                        .getText()
                        .toString()
                        .trim();

        // Validate problem description
        if (problemDescription.isEmpty()) {

            editProblemDescription.setError(
                    "Problem description is required"
            );

            editProblemDescription.requestFocus();

            return;
        }

        // Validate damage photo
        if (damageBitmap == null
                || damageImagePath.isEmpty()) {

            Toast.makeText(
                    this,
                    "Please take a damage photo",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        // Validate appointment date
        if (selectedDate.isEmpty()) {

            Toast.makeText(
                    this,
                    "Please select an appointment date",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        // Validate appointment time
        if (selectedTime.isEmpty()) {

            Toast.makeText(
                    this,
                    "Please select an appointment time",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        // Continue to Appointment Confirmation
        Intent intent = new Intent(
                RepairDetailsActivity.this,
                AppointmentConfirmationActivity.class
        );

        intent.putExtra(
                "DEVICE_ID",
                deviceId
        );

        intent.putExtra(
                "DEVICE_NAME",
                deviceName
        );

        intent.putExtra(
                "DEVICE_BRAND",
                deviceBrand
        );

        intent.putExtra(
                "DEVICE_MODEL",
                deviceModel
        );

        intent.putExtra(
                "SERVICE_NAME",
                serviceName
        );

        intent.putExtra(
                "PROBLEM_DESCRIPTION",
                problemDescription
        );

        intent.putExtra(
                "APPOINTMENT_DATE",
                selectedDate
        );

        intent.putExtra(
                "APPOINTMENT_TIME",
                selectedTime
        );

        intent.putExtra(
                "IMAGE_PATH",
                damageImagePath
        );

        startActivity(intent);
    }
}
