package com.example.techfix.firebase;

import com.example.techfix.model.Technician;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class TechRepository {

    private final FirebaseFirestore db;

    private static final String COLLECTION = "technicians";

    public TechRepository() {
        db = FirebaseFirestore.getInstance();
    }

    // =========================================================
    // ADD TECHNICIAN
    // =========================================================

    public void addTechnician(
            Technician technician,
            OnSuccessListener listener,
            OnErrorListener errorListener) {

        db.collection(COLLECTION)
                .add(technician)
                .addOnSuccessListener(documentReference -> {

                    listener.onSuccess();

                })
                .addOnFailureListener(
                        errorListener::onError
                );
    }

    // =========================================================
    // GET ALL TECHNICIANS
    // =========================================================

    public void getAllTechnicians(
            OnTechniciansLoadedListener listener,
            OnErrorListener errorListener) {

        db.collection(COLLECTION)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                    List<Technician> technicians =
                            new ArrayList<>();

                    for (QueryDocumentSnapshot document :
                            queryDocumentSnapshots) {

                        Technician technician =
                                document.toObject(
                                        Technician.class
                                );

                        technicians.add(technician);
                    }

                    listener.onLoaded(technicians);

                })
                .addOnFailureListener(
                        errorListener::onError
                );
    }

    // =========================================================
    // GET FIRESTORE DOCUMENT ID BY TECHNICIAN ID
    // =========================================================

    public void getDocumentIdByTechnicianId(
            int technicianId,
            OnDocumentIdLoadedListener listener,
            OnErrorListener errorListener) {

        db.collection(COLLECTION)
                .whereEqualTo(
                        "technicianId",
                        technicianId
                )
                .limit(1)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                    if (!queryDocumentSnapshots.isEmpty()) {

                        String documentId =
                                queryDocumentSnapshots
                                        .getDocuments()
                                        .get(0)
                                        .getId();

                        listener.onLoaded(documentId);

                    } else {

                        listener.onLoaded(null);
                    }

                })
                .addOnFailureListener(
                        errorListener::onError
                );
    }

    // =========================================================
    // GET TECHNICIAN BY FIREBASE DOCUMENT ID
    // =========================================================

    public void getTechnician(
            String documentId,
            OnTechnicianLoadedListener listener,
            OnErrorListener errorListener) {

        db.collection(COLLECTION)
                .document(documentId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {

                    if (documentSnapshot.exists()) {

                        Technician technician =
                                documentSnapshot.toObject(
                                        Technician.class
                                );

                        listener.onLoaded(technician);

                    } else {

                        listener.onLoaded(null);
                    }

                })
                .addOnFailureListener(
                        errorListener::onError
                );
    }

    // =========================================================
    // GET TECHNICIAN BY TECHNICIAN ID
    // =========================================================

    public void getTechnicianById(
            int technicianId,
            OnTechnicianLoadedListener listener,
            OnErrorListener errorListener) {

        db.collection(COLLECTION)
                .whereEqualTo(
                        "technicianId",
                        technicianId
                )
                .limit(1)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                    if (!queryDocumentSnapshots.isEmpty()) {

                        Technician technician =
                                queryDocumentSnapshots
                                        .getDocuments()
                                        .get(0)
                                        .toObject(
                                                Technician.class
                                        );

                        listener.onLoaded(technician);

                    } else {

                        listener.onLoaded(null);
                    }

                })
                .addOnFailureListener(
                        errorListener::onError
                );
    }

    // =========================================================
    // UPDATE TECHNICIAN
    // =========================================================

    public void updateTechnician(
            String documentId,
            Technician technician,
            OnSuccessListener listener,
            OnErrorListener errorListener) {

        db.collection(COLLECTION)
                .document(documentId)
                .set(technician)
                .addOnSuccessListener(unused -> {

                    listener.onSuccess();

                })
                .addOnFailureListener(
                        errorListener::onError
                );
    }

    // =========================================================
    // UPDATE AVAILABILITY
    // =========================================================

    public void updateAvailability(
            String documentId,
            boolean available,
            OnSuccessListener listener,
            OnErrorListener errorListener) {

        db.collection(COLLECTION)
                .document(documentId)
                .update(
                        "available",
                        available
                )
                .addOnSuccessListener(unused -> {

                    listener.onSuccess();

                })
                .addOnFailureListener(
                        errorListener::onError
                );
    }

    // =========================================================
    // DELETE TECHNICIAN
    // =========================================================

    public void deleteTechnician(
            String documentId,
            OnSuccessListener listener,
            OnErrorListener errorListener) {

        db.collection(COLLECTION)
                .document(documentId)
                .delete()
                .addOnSuccessListener(unused -> {

                    listener.onSuccess();

                })
                .addOnFailureListener(
                        errorListener::onError
                );
    }

    // =========================================================
    // CALLBACKS
    // =========================================================

    public interface OnSuccessListener {
        void onSuccess();
    }

    public interface OnErrorListener {
        void onError(Exception error);
    }

    public interface OnTechniciansLoadedListener {
        void onLoaded(List<Technician> technicians);
    }

    public interface OnTechnicianLoadedListener {
        void onLoaded(Technician technician);
    }

    public interface OnDocumentIdLoadedListener {
        void onLoaded(String documentId);
    }
}