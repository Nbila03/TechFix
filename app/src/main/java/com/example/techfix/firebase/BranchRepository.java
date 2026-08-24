package com.example.techfix.firebase;

import com.example.techfix.model.Branch;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class BranchRepository {

    private final FirebaseFirestore db;

    public BranchRepository() {
        db = FirebaseFirestore.getInstance();
    }

    // ADD BRANCH

    public void addBranch(
            final Branch branch,
            final OnSuccessCallback onSuccess,
            final OnErrorCallback onError) {

        String id = String.valueOf(branch.getBranchId());

        db.collection("branches")
                .document(id)
                .set(branch)
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

    // GET EVERY BRANCH - active or not. The caller decides what to do with the active status.

    public void getAllBranches(
            final OnBranchesLoaded onLoaded,
            final OnErrorCallback onError) {

        db.collection("branches")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot result) {

                        List<Branch> branches = new ArrayList<Branch>();
                        List<DocumentSnapshot> documents = result.getDocuments();

                        for (int i = 0; i < documents.size(); i++) {
                            DocumentSnapshot document = documents.get(i);
                            Branch branch = document.toObject(Branch.class);
                            branches.add(branch);
                        }

                        onLoaded.onLoaded(branches);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        onError.onError(e);
                    }
                });
    }

    // UPDATE BRANCH

    public void updateBranch(
            final Branch branch,
            final OnSuccessCallback onSuccess,
            final OnErrorCallback onError) {

        String id = String.valueOf(branch.getBranchId());

        db.collection("branches")
                .document(id)
                .set(branch)
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

    // DELETE BRANCH

    public void deleteBranch(
            int branchId,
            final OnSuccessCallback onSuccess,
            final OnErrorCallback onError) {

        db.collection("branches")
                .document(String.valueOf(branchId))
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

    public interface OnBranchesLoaded {
        void onLoaded(List<Branch> branches);
    }

    public interface OnErrorCallback {
        void onError(Exception e);
    }
}
