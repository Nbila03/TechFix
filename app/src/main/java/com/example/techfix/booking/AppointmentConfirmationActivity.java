package com.example.techfix.booking;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.techfix.R;
import com.example.techfix.database.ContentProviderHelper;
import com.example.techfix.firebase.BranchRepository;
import com.example.techfix.firebase.RepairRepository;
import com.example.techfix.location.BranchAssignmentHelper;
import com.example.techfix.location.LocationHelper;
import com.example.techfix.location.NetworkUtils;
import com.example.techfix.model.Branch;
import com.example.techfix.model.RepairRequest;
import com.example.techfix.model.RepairStatus;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.List;

public class AppointmentConfirmationActivity extends AppCompatActivity {

    // ---- screen widgets ----
    private TextView tvConfirmDevice;
    private TextView tvConfirmService;
    private TextView tvConfirmProblem;
    private TextView tvConfirmAppointment;
    private ImageView imgConfirmPhoto;
    private Button btnBackConfirmation;
    private Button btnConfirmBooking;

    // booking data passed in from the previous screen
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

        // connect widgets
        tvConfirmDevice = findViewById(R.id.tvConfirmDevice);
        tvConfirmService = findViewById(R.id.tvConfirmService);
        tvConfirmProblem = findViewById(R.id.tvConfirmProblem);
        tvConfirmAppointment = findViewById(R.id.tvConfirmAppointment);
        imgConfirmPhoto = findViewById(R.id.imgConfirmPhoto);
        btnBackConfirmation = findViewById(R.id.btnBackConfirmation);
        btnConfirmBooking = findViewById(R.id.btnConfirmBooking);

        // read everything that was passed from RepairDetailsActivity
        deviceId = getIntent().getIntExtra("DEVICE_ID", -1);
        deviceName = getIntent().getStringExtra("DEVICE_NAME");
        deviceBrand = getIntent().getStringExtra("DEVICE_BRAND");
        deviceModel = getIntent().getStringExtra("DEVICE_MODEL");
        serviceName = getIntent().getStringExtra("SERVICE_NAME");
        problemDescription = getIntent().getStringExtra("PROBLEM_DESCRIPTION");
        appointmentDate = getIntent().getStringExtra("APPOINTMENT_DATE");
        appointmentTime = getIntent().getStringExtra("APPOINTMENT_TIME");
        imagePath = getIntent().getStringExtra("IMAGE_PATH");

        // show it on screen
        tvConfirmDevice.setText(deviceName + "\n" + deviceBrand + " - " + deviceModel);
        tvConfirmService.setText(serviceName);
        tvConfirmProblem.setText(problemDescription);
        tvConfirmAppointment.setText(appointmentDate + " - " + appointmentTime);

        if (imagePath != null && imagePath.length() > 0) {
            imgConfirmPhoto.setImageBitmap(BitmapFactory.decodeFile(imagePath));
        }

        // back close this screen
        btnBackConfirmation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //
        btnConfirmBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onConfirmBookingClicked();
            }
        });
    }

    // STEP 1: check/ask for location permission, then get GPS location
    private void onConfirmBookingClicked() {

        LocationHelper locationHelper = new LocationHelper(this);

        if (locationHelper.hasPermission() == false) {
            locationHelper.requestPermission();
            Toast.makeText(this, "Allow location access, then tap Confirm again", Toast.LENGTH_LONG).show();
            return;
        }

        btnConfirmBooking.setEnabled(false);

        locationHelper.getCurrentLocation(new LocationHelper.LocationResultCallback() {
            @Override
            public void onLocationResult(Location location) {
                double lat = location.getLatitude();
                double lng = location.getLongitude();
                findBranchAndSaveRepair(lat, lng);
            }

            @Override
            public void onLocationUnavailable() {
                Toast.makeText(AppointmentConfirmationActivity.this,
                        "Could not get your location. Turn on GPS and try again.", Toast.LENGTH_LONG).show();
                btnConfirmBooking.setEnabled(true);
            }
        });
    }

    // STEP 2: if offline, save locally and stop here. if online, load branches.
    private void findBranchAndSaveRepair(final double lat, final double lng) {

        boolean online = NetworkUtils.isOnline(this);

        if (online == false) {
            ContentProviderHelper.queuePendingRepair(this, deviceName, 0, problemDescription,
                    appointmentDate, appointmentTime, lat, lng);
            Toast.makeText(this, "You're offline - booking saved and will sync later", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        BranchRepository branchRepository = new BranchRepository();

        branchRepository.getAllBranches(
                new BranchRepository.OnBranchesLoaded() {
                    @Override
                    public void onLoaded(List<Branch> branches) {
                        onBranchesLoaded(branches, lat, lng);
                    }
                },
                new BranchRepository.OnErrorCallback() {
                    @Override
                    public void onError(Exception e) {
                        Toast.makeText(AppointmentConfirmationActivity.this,
                                "Could not reach branch list: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        btnConfirmBooking.setEnabled(true);
                    }
                }
        );
    }

    // STEP 3: out of all branches, keep only the active ones, then pick the nearest
    private void onBranchesLoaded(List<Branch> allBranches, double lat, double lng) {

        List<Branch> activeBranches = new ArrayList<Branch>();

        for (int i = 0; i < allBranches.size(); i++) {
            Branch b = allBranches.get(i);
            if (b.isActive()) {
                activeBranches.add(b);
            }
        }

        if (activeBranches.size() == 0) {
            Toast.makeText(this, "No active branches available right now", Toast.LENGTH_LONG).show();
            btnConfirmBooking.setEnabled(true);
            return;
        }

        List<Branch> sortedByDistance = BranchAssignmentHelper.sortByDistance(lat, lng, activeBranches);
        Branch nearestBranch = sortedByDistance.get(0);
        saveRepairRequest(nearestBranch);
    }

    // STEP 4: build the RepairRequest object and save it to Firestore
    private void saveRepairRequest(final Branch branch) {

        RepairRequest repair = new RepairRequest();

        long timeNow = System.currentTimeMillis();
        repair.setRepairId((int) timeNow);
        repair.setDeviceId(deviceId);
        repair.setDeviceName(deviceName);
        repair.setServiceName(serviceName);
        repair.setBranchId(branch.getBranchId());
        repair.setBranchName(branch.getBranchName());
        repair.setProblemDescription(problemDescription);
        repair.setAppointmentDate(appointmentDate);
        repair.setAppointmentTime(appointmentTime);
        repair.setStatus(RepairStatus.BRANCH_ASSIGNED);

        final RepairRequest repairToSave = repair;
        RepairRepository repairRepository = new RepairRepository();

        repairRepository.addRepair(
                repair,
                new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        onRepairSaved(repairToSave, branch);
                    }
                },
                new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        Toast.makeText(AppointmentConfirmationActivity.this,
                                "Save failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        btnConfirmBooking.setEnabled(true);
                    }
                }
        );
    }

    // STEP 5: booking is saved
    private void onRepairSaved(RepairRequest repair, Branch branch) {

        Toast.makeText(this, "Repair booked at " + branch.getBranchName(), Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this, com.example.techfix.branch.RepairTrackingActivity.class);
        intent.putExtra("repair_id", repair.getRepairId());
        intent.putExtra("current_status", repair.getStatus());
        startActivity(intent);
        finish();
    }

    // called automatically by Android after the user answers the location permission popup
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LocationHelper.PERMISSION_REQUEST_CODE) {
            onConfirmBookingClicked();
        }
    }
}