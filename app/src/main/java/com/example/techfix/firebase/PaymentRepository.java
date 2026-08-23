package com.example.techfix.firebase;

import com.example.techfix.model.Payment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class PaymentRepository {

    private final FirebaseFirestore db;
    private final CollectionReference paymentsRef;

    public PaymentRepository() {
        db = FirebaseFirestore.getInstance();
        paymentsRef = db.collection("payments");
    }

    // CREATE PAYMENT

    public void addPayment(
            Payment payment,
            OnSuccessListener<Void> onSuccess,
            OnFailureListener onFailure) {

        String documentId =
                String.valueOf(payment.getPaymentId());

        paymentsRef.document(documentId)
                .set(payment)
                .addOnSuccessListener(onSuccess)
                .addOnFailureListener(onFailure);
    }

    // GET ALL PAYMENTS

    public void getAllPayments(
            OnSuccessListener<List<Payment>> onSuccess,
            OnFailureListener onFailure) {

        paymentsRef.get()
                .addOnSuccessListener(querySnapshot -> {

                    List<Payment> payments =
                            new ArrayList<>();

                    for (DocumentSnapshot document :
                            querySnapshot.getDocuments()) {

                        Payment payment =
                                document.toObject(
                                        Payment.class
                                );

                        if (payment != null) {
                            payments.add(payment);
                        }
                    }

                    onSuccess.onSuccess(payments);
                })
                .addOnFailureListener(onFailure);
    }

    // GET PAYMENT BY ID

    public void getPaymentById(
            int paymentId,
            OnSuccessListener<Payment> onSuccess,
            OnFailureListener onFailure) {

        paymentsRef
                .document(String.valueOf(paymentId))
                .get()
                .addOnSuccessListener(documentSnapshot -> {

                    if (documentSnapshot.exists()) {

                        Payment payment =
                                documentSnapshot.toObject(
                                        Payment.class
                                );

                        onSuccess.onSuccess(payment);

                    } else {

                        onSuccess.onSuccess(null);
                    }
                })
                .addOnFailureListener(onFailure);
    }

    // GET PAYMENTS BY REPAIR ID

    public void getPaymentsByRepairId(
            int repairId,
            OnSuccessListener<List<Payment>> onSuccess,
            OnFailureListener onFailure) {

        paymentsRef
                .whereEqualTo("repairId", repairId)
                .get()
                .addOnSuccessListener(querySnapshot -> {

                    List<Payment> payments =
                            new ArrayList<>();

                    for (DocumentSnapshot document :
                            querySnapshot.getDocuments()) {

                        Payment payment =
                                document.toObject(
                                        Payment.class
                                );

                        if (payment != null) {
                            payments.add(payment);
                        }
                    }

                    onSuccess.onSuccess(payments);
                })
                .addOnFailureListener(onFailure);
    }

    // UPDATE PAYMENT STATUS

    public void updatePaymentStatus(
            int paymentId,
            String newStatus,
            OnSuccessListener<Void> onSuccess,
            OnFailureListener onFailure) {

        paymentsRef
                .document(String.valueOf(paymentId))
                .update("status", newStatus)
                .addOnSuccessListener(onSuccess)
                .addOnFailureListener(onFailure);
    }

    // DELETE PAYMENT

    public void deletePayment(
            int paymentId,
            OnSuccessListener<Void> onSuccess,
            OnFailureListener onFailure) {

        paymentsRef
                .document(String.valueOf(paymentId))
                .delete()
                .addOnSuccessListener(onSuccess)
                .addOnFailureListener(onFailure);
    }
}