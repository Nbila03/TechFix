package com.example.techfix.firebase;

import androidx.annotation.NonNull;

import com.example.techfix.model.RepairRequest;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class RepairRepository {

    private final FirebaseFirestore db;
    private final CollectionReference repairsRef;

    public RepairRepository() {
        db = FirebaseFirestore.getInstance();
        repairsRef = db.collection("repairs");
    }

    // CREATE REPAIR

    public void addRepair(
            RepairRequest repair,
            OnSuccessListener<Void> onSuccess,
            OnFailureListener onFailure) {

        String documentId = String.valueOf(repair.getRepairId());

        repairsRef.document(documentId)
                .set(repair)
                .addOnSuccessListener(onSuccess)
                .addOnFailureListener(onFailure);
    }

    // GET ALL REPAIRS

    public void getAllRepairs(
            OnSuccessListener<List<RepairRequest>> onSuccess,
            OnFailureListener onFailure) {

        repairsRef.get()
                .addOnSuccessListener(querySnapshot -> {

                    List<RepairRequest> repairs = new ArrayList<>();

                    for (DocumentSnapshot document : querySnapshot.getDocuments()) {

                        RepairRequest repair =
                                document.toObject(RepairRequest.class);

                        if (repair != null) {
                            repairs.add(repair);
                        }
                    }

                    onSuccess.onSuccess(repairs);
                })
                .addOnFailureListener(onFailure);
    }


    // GET ONE REPAIR

    public void getRepairById(
            int repairId,
            OnSuccessListener<RepairRequest> onSuccess,
            OnFailureListener onFailure) {

        repairsRef.document(String.valueOf(repairId))
                .get()
                .addOnSuccessListener(documentSnapshot -> {

                    if (documentSnapshot.exists()) {

                        RepairRequest repair =
                                documentSnapshot.toObject(RepairRequest.class);

                        onSuccess.onSuccess(repair);

                    } else {

                        onSuccess.onSuccess(null);
                    }
                })
                .addOnFailureListener(onFailure);
    }


    // UPDATE REPAIR

    public void updateRepair(
            RepairRequest repair,
            OnSuccessListener<Void> onSuccess,
            OnFailureListener onFailure) {

        String documentId = String.valueOf(repair.getRepairId());

        repairsRef.document(documentId)
                .set(repair)
                .addOnSuccessListener(onSuccess)
                .addOnFailureListener(onFailure);
    }

    // UPDATE STATUS

    public void updateRepairStatus(
            int repairId,
            String newStatus,
            OnSuccessListener<Void> onSuccess,
            OnFailureListener onFailure) {

        repairsRef.document(String.valueOf(repairId))
                .update("status", newStatus)
                .addOnSuccessListener(onSuccess)
                .addOnFailureListener(onFailure);
    }

    // ASSIGN TECHNICIAN

    public void assignTechnician(
            int repairId,
            int technicianId,
            OnSuccessListener<Void> onSuccess,
            OnFailureListener onFailure) {

        repairsRef.document(String.valueOf(repairId))
                .update("technicianId", technicianId)
                .addOnSuccessListener(onSuccess)
                .addOnFailureListener(onFailure);
    }

    // DELETE REPAIR

    public void deleteRepair(
            int repairId,
            OnSuccessListener<Void> onSuccess,
            OnFailureListener onFailure) {

        repairsRef.document(String.valueOf(repairId))
                .delete()
                .addOnSuccessListener(onSuccess)
                .addOnFailureListener(onFailure);
    }
}