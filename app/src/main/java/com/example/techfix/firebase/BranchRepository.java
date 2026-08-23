package com.example.techfix.firebase;

import com.example.techfix.model.Branch;
import com.google.firebase.firestore.DocumentSnapshot;
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

    // Gets every branch, active or not.
    // The caller decides what to do with the active status.
    public void getAllBranches(
            OnBranchesLoaded onLoaded,
            OnErrorCallback onError) {

        db.collection("branches")
                .get()
                .addOnSuccessListener(result -> {

                    List<Branch> branches = new ArrayList<>();

                    for (DocumentSnapshot document : result) {

                        Branch branch = document.toObject(Branch.class);

                        branches.add(branch);
                    }

                    onLoaded.onLoaded(branches);

                })
                .addOnFailureListener(error -> {

                    onError.onError(error);

                });
    }
}