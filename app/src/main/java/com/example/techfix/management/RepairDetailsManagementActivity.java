package com.example.techfix.management;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.InputType;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.techfix.R;
import com.example.techfix.firebase.RepairRepository;
import com.example.techfix.model.RepairRequest;

public class RepairDetailsManagementActivity extends AppCompatActivity {

    // =========================================================
    // TEXT VIEWS
    // =========================================================

    private TextView tvAdminRepairId;
    private TextView tvAdminDevice;
    private TextView tvAdminService;
    private TextView tvAdminProblem;
    private TextView tvAdminBranch;
    private TextView tvAdminTechnician;
    private TextView tvAdminAppointment;

    // =========================================================
    // INPUTS
    // =========================================================

    private Spinner spinnerRepairStatus;
    private EditText etEstimatedCost;
    private EditText etFinalCost;

    // =========================================================
    // BUTTONS
    // =========================================================

    private Button btnBackAdminRepair;
    private Button btnAssignTechnician;
    private Button btnUpdateStatus;
    private Button btnSaveRepair;
    private Button btnDeleteRepair;

    // =========================================================
    // REPOSITORY
    // =========================================================

    private RepairRepository repairRepository;

    private RepairRequest repair;

    private int repairId;

    // =========================================================
    // STATUS OPTIONS
    // =========================================================

    private final String[] statuses = {
            "SUBMITTED",
            "BRANCH_ASSIGNED",
            "TECHNICIAN_ASSIGNED",
            "IN_PROGRESS",
            "READY_FOR_COLLECTION",
            "COMPLETED",
            "CANCELLED"
    };

    // =========================================================
    // ON CREATE
    // =========================================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_repair_admin_details);

        initializeViews();

        repairRepository = new RepairRepository();

        // Get Repair ID from RepairManagementActivity
        repairId = getIntent().getIntExtra("repairId", -1);

        if (repairId == -1) {

            Toast.makeText(
                    this,
                    "Invalid repair ID",
                    Toast.LENGTH_LONG
            ).show();

            finish();
            return;
        }

        setupStatusSpinner();
        setupButtons();

        loadRepair();
    }

    // =========================================================
    // INITIALIZE VIEWS
    // =========================================================

    private void initializeViews() {

        tvAdminRepairId =
                findViewById(R.id.tvAdminRepairId);

        tvAdminDevice =
                findViewById(R.id.tvAdminDevice);

        tvAdminService =
                findViewById(R.id.tvAdminService);

        tvAdminProblem =
                findViewById(R.id.tvAdminProblem);

        tvAdminBranch =
                findViewById(R.id.tvAdminBranch);

        tvAdminTechnician =
                findViewById(R.id.tvAdminTechnician);

        tvAdminAppointment =
                findViewById(R.id.tvAdminAppointment);

        spinnerRepairStatus =
                findViewById(R.id.spinnerRepairStatus);

        etEstimatedCost =
                findViewById(R.id.etEstimatedCost);

        etFinalCost =
                findViewById(R.id.etFinalCost);

        btnBackAdminRepair =
                findViewById(R.id.btnBackAdminRepair);

        btnAssignTechnician =
                findViewById(R.id.btnAssignTechnician);

        btnUpdateStatus =
                findViewById(R.id.btnUpdateStatus);

        btnSaveRepair =
                findViewById(R.id.btnSaveRepair);

        btnDeleteRepair =
                findViewById(R.id.btnDeleteRepair);
    }

    // =========================================================
    // STATUS SPINNER
    // =========================================================

    private void setupStatusSpinner() {

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_spinner_item,
                        statuses
                );

        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        spinnerRepairStatus.setAdapter(adapter);
    }

    // =========================================================
    // LOAD REPAIR
    // =========================================================

    private void loadRepair() {

        repairRepository.getRepairById(
                repairId,

                loadedRepair -> {

                    if (loadedRepair == null) {

                        Toast.makeText(
                                this,
                                "Repair not found",
                                Toast.LENGTH_LONG
                        ).show();

                        finish();
                        return;
                    }

                    repair = loadedRepair;

                    displayRepair();
                },

                error -> {

                    Toast.makeText(
                            this,
                            "Failed to load repair: "
                                    + error.getMessage(),
                            Toast.LENGTH_LONG
                    ).show();
                }
        );
    }

    // =========================================================
    // DISPLAY REPAIR
    // =========================================================

    private void displayRepair() {

        if (repair == null) {
            return;
        }

        // -----------------------------------------------------
        // REPAIR ID
        // -----------------------------------------------------

        tvAdminRepairId.setText(
                "Repair #" + repair.getRepairId()
        );

        // -----------------------------------------------------
        // DEVICE
        // -----------------------------------------------------

        String deviceName =
                repair.getDeviceName();

        if (deviceName == null ||
                deviceName.trim().isEmpty()) {

            deviceName =
                    "Device #" + repair.getDeviceId();
        }

        tvAdminDevice.setText(deviceName);

        // -----------------------------------------------------
        // SERVICE
        // -----------------------------------------------------

        String serviceName =
                repair.getServiceName();

        if (serviceName == null ||
                serviceName.trim().isEmpty()) {

            serviceName =
                    "Service #" + repair.getServiceId();
        }

        tvAdminService.setText(serviceName);

        // -----------------------------------------------------
        // PROBLEM
        // -----------------------------------------------------

        String problem =
                repair.getProblemDescription();

        if (problem == null ||
                problem.trim().isEmpty()) {

            problem =
                    "No problem description provided.";
        }

        tvAdminProblem.setText(problem);

        // -----------------------------------------------------
        // BRANCH
        // -----------------------------------------------------

        if (repair.getBranchId() != null) {

            tvAdminBranch.setText(
                    "Branch ID: "
                            + repair.getBranchId()
            );

        } else {

            tvAdminBranch.setText(
                    "Not Assigned"
            );
        }

        // -----------------------------------------------------
        // TECHNICIAN
        // -----------------------------------------------------

        if (repair.getTechnicianId() != null) {

            tvAdminTechnician.setText(
                    "Technician ID: "
                            + repair.getTechnicianId()
            );

        } else {

            tvAdminTechnician.setText(
                    "Not Assigned"
            );
        }

        // -----------------------------------------------------
        // APPOINTMENT
        // -----------------------------------------------------

        String date =
                repair.getAppointmentDate();

        String time =
                repair.getAppointmentTime();

        if (date != null &&
                !date.trim().isEmpty() &&
                time != null &&
                !time.trim().isEmpty()) {

            tvAdminAppointment.setText(
                    date + " • " + time
            );

        } else if (date != null &&
                !date.trim().isEmpty()) {

            tvAdminAppointment.setText(date);

        } else if (time != null &&
                !time.trim().isEmpty()) {

            tvAdminAppointment.setText(time);

        } else {

            tvAdminAppointment.setText(
                    "No appointment"
            );
        }

        // -----------------------------------------------------
        // ESTIMATED COST
        // -----------------------------------------------------

        etEstimatedCost.setText(
                String.valueOf(
                        repair.getEstimatedCost()
                )
        );

        // -----------------------------------------------------
        // FINAL COST
        // -----------------------------------------------------

        double finalCost =
                repair.getFinalCost();

        if (finalCost > 0) {

            etFinalCost.setText(
                    String.valueOf(finalCost)
            );

        } else {

            etFinalCost.setText("");
        }

        // -----------------------------------------------------
        // STATUS
        // -----------------------------------------------------

        setSpinnerStatus(
                repair.getStatus()
        );
    }

    // =========================================================
    // SET SPINNER STATUS
    // =========================================================

    private void setSpinnerStatus(String status) {

        if (status == null ||
                status.trim().isEmpty()) {

            return;
        }

        for (int i = 0; i < statuses.length; i++) {

            if (statuses[i]
                    .equalsIgnoreCase(status)) {

                spinnerRepairStatus
                        .setSelection(i);

                break;
            }
        }
    }

    // =========================================================
    // BUTTONS
    // =========================================================

    private void setupButtons() {

        // BACK
        btnBackAdminRepair.setOnClickListener(
                v -> finish()
        );

        // ASSIGN TECHNICIAN
        btnAssignTechnician.setOnClickListener(
                v -> showAssignTechnicianDialog()
        );

        // UPDATE STATUS
        btnUpdateStatus.setOnClickListener(
                v -> updateStatus()
        );

        // SAVE REPAIR
        btnSaveRepair.setOnClickListener(
                v -> saveRepair()
        );

        // DELETE REPAIR
        btnDeleteRepair.setOnClickListener(
                v -> confirmDelete()
        );
    }

    // =========================================================
    // UPDATE STATUS
    // =========================================================

    private void updateStatus() {

        if (repair == null) {
            return;
        }

        String newStatus =
                spinnerRepairStatus
                        .getSelectedItem()
                        .toString();

        repairRepository.updateRepairStatus(
                repairId,
                newStatus,

                unused -> {

                    repair.setStatus(newStatus);

                    Toast.makeText(
                            this,
                            "Repair status updated",
                            Toast.LENGTH_SHORT
                    ).show();

                },

                error -> {

                    Toast.makeText(
                            this,
                            "Failed to update status: "
                                    + error.getMessage(),
                            Toast.LENGTH_LONG
                    ).show();
                }
        );
    }

    // =========================================================
    // SAVE REPAIR
    // =========================================================

    private void saveRepair() {

        if (repair == null) {
            return;
        }

        String estimatedText =
                etEstimatedCost
                        .getText()
                        .toString()
                        .trim();

        String finalText =
                etFinalCost
                        .getText()
                        .toString()
                        .trim();

        // -----------------------------------------------------
        // VALIDATE ESTIMATED COST
        // -----------------------------------------------------

        if (estimatedText.isEmpty()) {

            etEstimatedCost.setError(
                    "Enter estimated cost"
            );

            return;
        }

        try {

            double estimatedCost =
                    Double.parseDouble(
                            estimatedText
                    );

            double finalCost = 0;

            if (!finalText.isEmpty()) {

                finalCost =
                        Double.parseDouble(
                                finalText
                        );
            }

            // Update local object
            repair.setEstimatedCost(
                    estimatedCost
            );

            repair.setFinalCost(
                    finalCost
            );

            // Save to Firestore
            repairRepository.updateRepair(
                    repair,

                    unused -> {

                        Toast.makeText(
                                this,
                                "Repair saved successfully",
                                Toast.LENGTH_SHORT
                        ).show();

                    },

                    error -> {

                        Toast.makeText(
                                this,
                                "Failed to save repair: "
                                        + error.getMessage(),
                                Toast.LENGTH_LONG
                        ).show();
                    }
            );

        } catch (NumberFormatException e) {

            Toast.makeText(
                    this,
                    "Enter valid cost values",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }

    // =========================================================
    // ASSIGN TECHNICIAN DIALOG
    // =========================================================

    private void showAssignTechnicianDialog() {

        final EditText input =
                new EditText(this);

        input.setHint(
                "Enter Technician ID"
        );

        input.setInputType(
                InputType.TYPE_CLASS_NUMBER
        );

        AlertDialog dialog =
                new AlertDialog.Builder(this)
                        .setTitle(
                                "Assign Technician"
                        )
                        .setMessage(
                                "Enter the technician ID to assign this repair."
                        )
                        .setView(input)
                        .setNegativeButton(
                                "CANCEL",
                                null
                        )
                        .setPositiveButton(
                                "ASSIGN",
                                null
                        )
                        .create();

        dialog.setOnShowListener(
                dialogInterface -> {

                    Button assignButton =
                            dialog.getButton(
                                    AlertDialog.BUTTON_POSITIVE
                            );

                    assignButton.setOnClickListener(
                            v -> {

                                String value =
                                        input.getText()
                                                .toString()
                                                .trim();

                                if (value.isEmpty()) {

                                    input.setError(
                                            "Enter technician ID"
                                    );

                                    return;
                                }

                                try {

                                    int technicianId =
                                            Integer.parseInt(
                                                    value
                                            );

                                    assignTechnician(
                                            technicianId
                                    );

                                    dialog.dismiss();

                                } catch (
                                        NumberFormatException e) {

                                    input.setError(
                                            "Invalid technician ID"
                                    );
                                }
                            }
                    );
                }
        );

        dialog.show();
    }

    // =========================================================
    // ASSIGN TECHNICIAN
    // =========================================================

    private void assignTechnician(
            int technicianId) {

        repairRepository.assignTechnician(
                repairId,
                technicianId,

                unused -> {

                    repair.setTechnicianId(
                            technicianId
                    );

                    tvAdminTechnician.setText(
                            "Technician ID: "
                                    + technicianId
                    );

                    Toast.makeText(
                            this,
                            "Technician assigned",
                            Toast.LENGTH_SHORT
                    ).show();

                },

                error -> {

                    Toast.makeText(
                            this,
                            "Failed to assign technician: "
                                    + error.getMessage(),
                            Toast.LENGTH_LONG
                    ).show();
                }
        );
    }

    // =========================================================
    // DELETE CONFIRMATION
    // =========================================================

    private void confirmDelete() {

        new AlertDialog.Builder(this)
                .setTitle(
                        "Delete Repair"
                )
                .setMessage(
                        "Are you sure you want to delete Repair #"
                                + repairId
                                + "?"
                )
                .setNegativeButton(
                        "CANCEL",
                        null
                )
                .setPositiveButton(
                        "DELETE",
                        (dialog, which) ->
                                deleteRepair()
                )
                .show();
    }

    // =========================================================
    // DELETE REPAIR
    // =========================================================

    private void deleteRepair() {

        repairRepository.deleteRepair(
                repairId,

                unused -> {

                    Toast.makeText(
                            this,
                            "Repair deleted",
                            Toast.LENGTH_SHORT
                    ).show();

                    finish();
                },

                error -> {

                    Toast.makeText(
                            this,
                            "Failed to delete repair: "
                                    + error.getMessage(),
                            Toast.LENGTH_LONG
                    ).show();
                }
        );
    }
}