
package com.example.techfix.firebase;

import com.example.techfix.model.SparePart;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class SparePartRepository {

    // Firebase database
    private FirebaseFirestore db;

    public SparePartRepository() {
        db = FirebaseFirestore.getInstance();
    }

    // ADD SPARE PART

    public void addSparePart(SparePart part) {

        String id = String.valueOf(part.getPartId());

        db.collection("spareParts")
                .document(id)
                .set(part);
    }

    // GET ALL SPARE PARTS

    public void getSpareParts(
            OnSparePartsLoaded listener) {

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

                    listener.onLoaded(parts);
                });
    }

    // UPDATE STOCK

    public void updateSparePart(SparePart part) {

        String id = String.valueOf(part.getPartId());

        db.collection("spareParts")
                .document(id)
                .set(part);
    }


// DELETE SPARE PART

    public void deleteSparePart(int partId) {

        db.collection("spareParts")
                .document(String.valueOf(partId))
                .delete();
    }



    // LISTENER

    public interface OnSparePartsLoaded {

        void onLoaded(
                List<SparePart> parts
        );
    }
}

