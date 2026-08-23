package com.example.techfix.firebase;

import com.example.techfix.model.Technician;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class TechnicianRepository {

    private final FirebaseFirestore db;

    public TechnicianRepository() {
        db = FirebaseFirestore.getInstance();
    }

    public interface OnTechniciansLoaded {
        void onLoaded(List<Technician> technicians);
    }

    public interface OnTechnicianLoaded {
        void onLoaded(Technician technician);
    }

    public interface OnSuccessCallback {
        void onSuccess();
    }

    public interface OnErrorCallback {
        void onError(Exception e);
    }

    // Get all technicians
    public void getAllTechnicians(
            OnTechniciansLoaded onLoaded,
            OnErrorCallback onError) {

        db.collection("technicians")
                .get()
                .addOnSuccessListener(result -> {

                    List<Technician> technicians = new ArrayList<>();

                    for (var document : result) {
                        Technician technician =
                                document.toObject(Technician.class);

                        technicians.add(technician);
                    }

                    onLoaded.onLoaded(technicians);
                })
                .addOnFailureListener(onError::onError);
    }

    // Get technicians belonging to a specific branch
    public void getTechniciansByBranch(
            String branchId,
            OnTechniciansLoaded onLoaded,
            OnErrorCallback onError) {

        db.collection("technicians")
                .whereEqualTo("branchId", branchId)
                .get()
                .addOnSuccessListener(result -> {

                    List<Technician> technicians = new ArrayList<>();

                    for (var document : result) {
                        Technician technician =
                                document.toObject(Technician.class);

                        technicians.add(technician);
                    }

                    onLoaded.onLoaded(technicians);
                })
                .addOnFailureListener(onError::onError);
    }

    // Get one technician
    public void getTechnicianById(
            String technicianId,
            OnTechnicianLoaded onLoaded,
            OnErrorCallback onError) {

        db.collection("technicians")
                .document(technicianId)
                .get()
                .addOnSuccessListener(document -> {

                    if (document.exists()) {
                        Technician technician =
                                document.toObject(Technician.class);

                        onLoaded.onLoaded(technician);
                    } else {
                        onError.onError(
                                new Exception("Technician not found"));
                    }
                })
                .addOnFailureListener(onError::onError);
    }

    // Add a technician
    public void addTechnician(
            Technician technician,
            OnSuccessCallback onSuccess,
            OnErrorCallback onError) {

        db.collection("technicians")
                .add(technician)
                .addOnSuccessListener(documentReference -> {
                    onSuccess.onSuccess();
                })
                .addOnFailureListener(onError::onError);
    }

    // Update technician availability
    public void updateAvailability(
            String technicianId,
            boolean available,
            OnSuccessCallback onSuccess,
            OnErrorCallback onError) {

        db.collection("technicians")
                .document(technicianId)
                .update("available", available)
                .addOnSuccessListener(unused -> {
                    onSuccess.onSuccess();
                })
                .addOnFailureListener(onError::onError);
    }

    // Delete a technician
    public void deleteTechnician(
            String technicianId,
            OnSuccessCallback onSuccess,
            OnErrorCallback onError) {

        db.collection("technicians")
                .document(technicianId)
                .delete()
                .addOnSuccessListener(unused -> {
                    onSuccess.onSuccess();
                })
                .addOnFailureListener(onError::onError);
    }
}