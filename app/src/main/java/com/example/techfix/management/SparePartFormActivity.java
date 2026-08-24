package com.example.techfix.management;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.techfix.R;
import com.example.techfix.firebase.SparePartRepository;
import com.example.techfix.model.SparePart;

public class SparePartFormActivity extends AppCompatActivity {

    private EditText etPartId;
    private EditText etBranchId;
    private EditText etPartName;
    private EditText etCompatibleDevice;
    private EditText etQuantity;
    private EditText etUnitPrice;

    private CheckBox checkAvailable;

    private Button btnBack;
    private Button btnSave;
    private Button btnDelete;

    private TextView tvTitle;

    private SparePartRepository repository;

    private boolean editMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_spare_part_form
        );

        initializeViews();

        repository = new SparePartRepository();

        int partId = getIntent().getIntExtra(
                "partId",
                -1
        );

        if (partId != -1) {

            editMode = true;

            loadPart(partId);

        } else {

            editMode = false;

            tvTitle.setText("ADD SPARE PART");

            btnSave.setText("SAVE SPARE PART");

            btnDelete.setVisibility(View.GONE);
        }

        setupButtons();
    }

    // INITIALIZE VIEWS

    private void initializeViews() {

        tvTitle = findViewById(
                R.id.tvSparePartFormTitle
        );

        etPartId = findViewById(
                R.id.etPartId
        );

        etBranchId = findViewById(
                R.id.etBranchId
        );

        etPartName = findViewById(
                R.id.etPartName
        );

        etCompatibleDevice = findViewById(
                R.id.etCompatibleDevice
        );

        etQuantity = findViewById(
                R.id.etQuantity
        );

        etUnitPrice = findViewById(
                R.id.etUnitPrice
        );

        checkAvailable = findViewById(
                R.id.checkAvailable
        );

        btnBack = findViewById(
                R.id.btnBackSparePart
        );

        btnSave = findViewById(
                R.id.btnSaveSparePart
        );

        btnDelete = findViewById(
                R.id.btnDeleteSparePart
        );
    }

    // BUTTONS

    private void setupButtons() {

        btnBack.setOnClickListener(
                v -> finish()
        );

        btnSave.setOnClickListener(
                v -> saveSparePart()
        );

        btnDelete.setOnClickListener(
                v -> confirmDelete()
        );
    }

    // LOAD PART

    private void loadPart(int partId) {

        repository.getSpareParts(

                // SUCCESS
                parts -> {

                    SparePart selectedPart = null;

                    for (SparePart part : parts) {

                        if (part.getPartId() == partId) {

                            selectedPart = part;
                            break;
                        }
                    }

                    if (selectedPart == null) {

                        Toast.makeText(
                                this,
                                "Spare part not found",
                                Toast.LENGTH_LONG
                        ).show();

                        finish();
                        return;
                    }

                    displayPart(selectedPart);
                },

                // ERROR
                error -> {

                    Toast.makeText(
                            this,
                            "Failed to load part: "
                                    + error.getMessage(),
                            Toast.LENGTH_LONG
                    ).show();
                }
        );
    }

    // DISPLAY PART

    private void displayPart(SparePart part) {

        tvTitle.setText(
                "EDIT SPARE PART"
        );

        btnSave.setText(
                "SAVE CHANGES"
        );

        btnDelete.setVisibility(
                View.VISIBLE
        );

        etPartId.setText(
                String.valueOf(
                        part.getPartId()
                )
        );

        etBranchId.setText(
                String.valueOf(
                        part.getBranchId()
                )
        );

        etPartName.setText(
                part.getPartName()
        );

        etCompatibleDevice.setText(
                part.getCompatibleDevice()
        );

        etQuantity.setText(
                String.valueOf(
                        part.getQuantity()
                )
        );

        etUnitPrice.setText(
                String.valueOf(
                        part.getUnitPrice()
                )
        );

        checkAvailable.setChecked(
                part.isAvailable()
        );
    }

    // SAVE
    // CREATE / UPDATE

    private void saveSparePart() {

        String partIdText =
                etPartId.getText()
                        .toString()
                        .trim();

        String branchIdText =
                etBranchId.getText()
                        .toString()
                        .trim();

        String partName =
                etPartName.getText()
                        .toString()
                        .trim();

        String compatibleDevice =
                etCompatibleDevice.getText()
                        .toString()
                        .trim();

        String quantityText =
                etQuantity.getText()
                        .toString()
                        .trim();

        String priceText =
                etUnitPrice.getText()
                        .toString()
                        .trim();

        // VALIDATION

        if (partIdText.isEmpty()) {
            etPartId.setError("Enter part ID");
            return;
        }

        if (branchIdText.isEmpty()) {
            etBranchId.setError("Enter branch ID");
            return;
        }

        if (partName.isEmpty()) {
            etPartName.setError("Enter part name");
            return;
        }

        if (compatibleDevice.isEmpty()) {
            etCompatibleDevice.setError(
                    "Enter compatible device"
            );
            return;
        }

        if (quantityText.isEmpty()) {
            etQuantity.setError("Enter quantity");
            return;
        }

        if (priceText.isEmpty()) {
            etUnitPrice.setError(
                    "Enter unit price"
            );
            return;
        }

        try {

            int partId =
                    Integer.parseInt(partIdText);

            int branchId =
                    Integer.parseInt(branchIdText);

            int quantity =
                    Integer.parseInt(quantityText);

            double unitPrice =
                    Double.parseDouble(priceText);

            boolean available =
                    checkAvailable.isChecked();

            // CREATE OBJECT

            SparePart part =
                    new SparePart(
                            partId,
                            branchId,
                            partName,
                            compatibleDevice,
                            quantity,
                            unitPrice,
                            available
                    );

            // ADD

            if (!editMode) {

                repository.addSparePart(
                        part,

                        // SUCCESS
                        () -> {

                            Toast.makeText(
                                    this,
                                    "Spare part added",
                                    Toast.LENGTH_SHORT
                            ).show();

                            finish();
                        },

                        // ERROR
                        error -> {

                            Toast.makeText(
                                    this,
                                    "Failed to add spare part: "
                                            + error.getMessage(),
                                    Toast.LENGTH_LONG
                            ).show();
                        }
                );
            }
            // UPDATE

            else {

                repository.updateSparePart(
                        part,

                        // SUCCESS
                        () -> {

                            Toast.makeText(
                                    this,
                                    "Spare part updated",
                                    Toast.LENGTH_SHORT
                            ).show();

                            finish();
                        },

                        // ERROR
                        error -> {

                            Toast.makeText(
                                    this,
                                    "Failed to update spare part: "
                                            + error.getMessage(),
                                    Toast.LENGTH_LONG
                            ).show();
                        }
                );
            }

        } catch (NumberFormatException e) {

            Toast.makeText(
                    this,
                    "Enter valid numbers",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }

    // DELETE CONFIRMATION

    private void confirmDelete() {

        String partIdText =
                etPartId.getText()
                        .toString()
                        .trim();

        if (partIdText.isEmpty()) {

            Toast.makeText(
                    this,
                    "Invalid part ID",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        new AlertDialog.Builder(this)

                .setTitle(
                        "Delete Spare Part"
                )

                .setMessage(
                        "Are you sure you want to delete Part #"
                                + partIdText
                                + "?"
                )

                .setNegativeButton(
                        "CANCEL",
                        null
                )

                .setPositiveButton(
                        "DELETE",
                        (dialog, which) -> {

                            try {

                                int partId =
                                        Integer.parseInt(
                                                partIdText
                                        );

                                deleteSparePart(partId);

                            } catch (
                                    NumberFormatException e) {

                                Toast.makeText(
                                        this,
                                        "Invalid part ID",
                                        Toast.LENGTH_SHORT
                                ).show();
                            }
                        }
                )

                .show();
    }

    // DELETE

    private void deleteSparePart(
            int partId) {

        repository.deleteSparePart(
                partId,

                // SUCCESS
                () -> {

                    Toast.makeText(
                            this,
                            "Spare part deleted",
                            Toast.LENGTH_SHORT
                    ).show();

                    finish();
                },

                // ERROR
                error -> {

                    Toast.makeText(
                            this,
                            "Failed to delete spare part: "
                                    + error.getMessage(),
                            Toast.LENGTH_LONG
                    ).show();
                }
        );
    }
}