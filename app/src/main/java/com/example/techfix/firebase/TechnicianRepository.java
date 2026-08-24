package com.example.techfix.firebase;

import com.example.techfix.model.Technician;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class TechnicianRepository {

    private final FirebaseFirestore db;

    public TechnicianRepository() {
        db = FirebaseFirestore.getInstance();
    }

    // ADD TECHNICIAN

    public void addTechnician(
            final Technician technician,
            final OnSuccessCallback onSuccess,
            final OnErrorCallback onError) {

        String id = String.valueOf(technician.getTechnicianId());

        db.collection("technicians")
                .document(id)
                .set(technician)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        onSuccess.onSuccess();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        onError.onError(e);
                    }
                });
    }

    // GET ALL TECHNICIANS

    public void getAllTechnicians(
            final OnTechniciansLoaded onLoaded,
            final OnErrorCallback onError) {

        db.collection("technicians")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot result) {

                        List<Technician> technicians = new ArrayList<Technician>();
                        List<DocumentSnapshot> documents = result.getDocuments();

                        for (int i = 0; i < documents.size(); i++) {
                            DocumentSnapshot document = documents.get(i);
                            Technician technician = document.toObject(Technician.class);
                            technicians.add(technician);
                        }

                        onLoaded.onLoaded(technicians);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        onError.onError(e);
                    }
                });
    }

    // UPDATE TECHNICIAN

    public void updateTechnician(
            final Technician technician,
            final OnSuccessCallback onSuccess,
            final OnErrorCallback onError) {

        String id = String.valueOf(technician.getTechnicianId());

        db.collection("technicians")
                .document(id)
                .set(technician)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        onSuccess.onSuccess();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        onError.onError(e);
                    }
                });
    }

    // DELETE TECHNICIAN

    public void deleteTechnician(
            int technicianId,
            final OnSuccessCallback onSuccess,
            final OnErrorCallback onError) {

        db.collection("technicians")
                .document(String.valueOf(technicianId))
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        onSuccess.onSuccess();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        onError.onError(e);
                    }
                });
    }

    public interface OnSuccessCallback {
        void onSuccess();
    }

    public interface OnErrorCallback {
        void onError(Exception e);
    }

    public interface OnTechniciansLoaded {
        void onLoaded(List<Technician> technicians);
    }
}
