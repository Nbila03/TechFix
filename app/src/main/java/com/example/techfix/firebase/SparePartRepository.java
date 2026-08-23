package com.example.techfix.firebase;

import com.example.techfix.model.SparePart;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class SparePartRepository {

    private final FirebaseFirestore db;

    public SparePartRepository() {
        db = FirebaseFirestore.getInstance();
    }

    // ADD SPARE PART

    public void addSparePart(
            SparePart part,
            OnSuccessCallback onSuccess,
            OnErrorCallback onError) {

        String id = String.valueOf(part.getPartId());

        db.collection("spareParts")
                .document(id)
                .set(part)
                .addOnSuccessListener(unused -> {

                    onSuccess.onSuccess();

                })
                .addOnFailureListener(e -> {

                    onError.onError(e);

                });
    }

    // GET ALL SPARE PARTS=

    public void getSpareParts(
            OnSparePartsLoaded onLoaded,
            OnErrorCallback onError) {

        db.collection("spareParts")
                .get()
                .addOnSuccessListener(result -> {

                    List<SparePart> parts =
                            new ArrayList<>();

                    for (var document : result) {

                        SparePart part =
                                document.toObject(
                                        SparePart.class
                                );

                        parts.add(part);
                    }

                    onLoaded.onLoaded(parts);

                })
                .addOnFailureListener(e -> {

                    onError.onError(e);

                });
    }

    // UPDATE SPARE PART

    public void updateSparePart(
            SparePart part,
            OnSuccessCallback onSuccess,
            OnErrorCallback onError) {

        String id = String.valueOf(part.getPartId());

        db.collection("spareParts")
                .document(id)
                .set(part)
                .addOnSuccessListener(unused -> {

                    onSuccess.onSuccess();

                })
                .addOnFailureListener(e -> {

                    onError.onError(e);

                });
    }

    // DELETE SPARE PART

    public void deleteSparePart(
            int partId,
            OnSuccessCallback onSuccess,
            OnErrorCallback onError) {

        db.collection("spareParts")
                .document(String.valueOf(partId))
                .delete()
                .addOnSuccessListener(unused -> {

                    onSuccess.onSuccess();

                })
                .addOnFailureListener(e -> {

                    onError.onError(e);

                });
    }

    // SUCCESS CALLBACK

    public interface OnSuccessCallback {

        void onSuccess();
    }

    // ERROR CALLBACK

    public interface OnErrorCallback {

        void onError(Exception e);
    }

    // LOAD CALLBACK

    public interface OnSparePartsLoaded {

        void onLoaded(
                List<SparePart> parts
        );
    }
}