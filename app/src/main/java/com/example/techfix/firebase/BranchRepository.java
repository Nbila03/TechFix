package com.example.techfix.firebase;

import com.example.techfix.model.Branch;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class BranchRepository {

    private final FirebaseFirestore db;

    public BranchRepository() {
        db = FirebaseFirestore.getInstance();
    }

    public interface OnBranchesLoaded {
        void onLoaded(List<Branch> branches);
    }

    public interface OnErrorCallback {
        void onError(Exception e);
    }

    // get every branch, active or not - caller decides what to do with is_active
    public void getAllBranches(OnBranchesLoaded onLoaded, OnErrorCallback onError) {
        db.collection("branches")
                .get()
                .addOnSuccessListener(result -> {
                    List<Branch> branches = new ArrayList<>();
                    for (var document : result) {
                        Branch b = document.toObject(Branch.class);
                        branches.add(b);
                    }
                    onLoaded.onLoaded(branches);
                })
                .addOnFailureListener(onError::onError);
    }
}