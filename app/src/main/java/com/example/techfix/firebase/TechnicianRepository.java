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

    public interface OnErrorCallback {
        void onError(Exception e);
    }

    public void getAllTechnicians(OnTechniciansLoaded onLoaded, OnErrorCallback onError) {
        db.collection("technicians")
                .get()
                .addOnSuccessListener(result -> {
                    List<Technician> technicians = new ArrayList<>();
                    for (var document : result) {
                        Technician t = document.toObject(Technician.class);
                        technicians.add(t);
                    }
                    onLoaded.onLoaded(technicians);
                })
                .addOnFailureListener(onError::onError);
    }
}