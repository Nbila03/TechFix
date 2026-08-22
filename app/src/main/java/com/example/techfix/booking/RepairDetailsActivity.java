package com.example.techfix.booking;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.techfix.R;

import java.util.Calendar;

public class RepairDetailsActivity extends AppCompatActivity {

    private TextView tvRepairSummary;
    private TextView tvSelectedDate;
    private TextView tvSelectedTime;

    private EditText editProblemDescription;

    private Button btnSelectDate;
    private Button btnSelectTime;
    private Button btnContinueRepair;

    private String selectedDate = "";
    private String selectedTime = "";

    private int deviceId;
    private String deviceName;
    private String deviceBrand;
    private String deviceModel;
    private String serviceName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_repair_details);

        tvRepairSummary = findViewById(R.id.tvRepairSummary);
        tvSelectedDate = findViewById(R.id.tvSelectedDate);
        tvSelectedTime = findViewById(R.id.tvSelectedTime);

        editProblemDescription =
                findViewById(R.id.editProblemDescription);

        btnSelectDate = findViewById(R.id.btnSelectDate);
        btnSelectTime = findViewById(R.id.btnSelectTime);
        btnContinueRepair = findViewById(R.id.btnContinueRepair);

        readBookingData();

        btnSelectDate.setOnClickListener(v ->
                showDatePicker()
        );

        btnSelectTime.setOnClickListener(v ->
                showTimePicker()
        );

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

    private void showDatePicker() {

        Calendar calendar =
                Calendar.getInstance();

        int year =
                calendar.get(Calendar.YEAR);

        int month =
                calendar.get(Calendar.MONTH);

        int day =
                calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog =
                new DatePickerDialog(
                        this,
                        (view, selectedYear,
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

        if (problemDescription.isEmpty()) {

            editProblemDescription.setError(
                    "Problem description is required"
            );

            editProblemDescription.requestFocus();
            return;
        }

        if (selectedDate.isEmpty()) {

            Toast.makeText(
                    this,
                    "Please select an appointment date",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        if (selectedTime.isEmpty()) {

            Toast.makeText(
                    this,
                    "Please select an appointment time",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        Toast.makeText(
                this,
                "Repair details are valid",
                Toast.LENGTH_SHORT
        ).show();

        // Confirmation navigation will be added next.
    }
}