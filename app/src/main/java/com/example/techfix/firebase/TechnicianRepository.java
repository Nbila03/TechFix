package com.example.techfix.firebase;

import com.google.firebase.firestore.FirebaseFirestore;

public class TechnicianRepository {
    private final FirebaseFirestore db;
    public TechnicianRepository() {
        db = FirebaseFirestore.getInstance();
    }
}