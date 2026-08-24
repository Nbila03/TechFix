package com.example.techfix.firebase;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class UserRepository {

    public interface OnRegisterReady {
        void onReady(FirebaseUser firebaseUser);
    }

    public interface OnLoginReady {
        void onReady(FirebaseUser firebaseUser, String fullName);
    }

    public interface OnErrorCallback {
        void onError(Exception e);
    }

    private FirebaseAuth auth;
    private FirebaseFirestore db;

    public UserRepository() {
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }

    public FirebaseUser getCurrentUser() {
        return auth.getCurrentUser();
    }

    // creates the login account, then saves the name/email/phone as a Firestore profile
    public void register(final String fullName, final String email, final String phone,
                         String password, final OnRegisterReady onReady, final OnErrorCallback onError) {

        auth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        FirebaseUser firebaseUser = authResult.getUser();
                        saveProfile(firebaseUser, fullName, email, phone, onReady, onError);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        onError.onError(e);
                    }
                });
    }

    private void saveProfile(final FirebaseUser firebaseUser, String fullName, String email,
                             String phone, final OnRegisterReady onReady, final OnErrorCallback onError) {

        Map<String, Object> profile = new HashMap<String, Object>();
        profile.put("fullName", fullName);
        profile.put("email", email);
        profile.put("phone", phone);
        profile.put("role", "customer");
        profile.put("createdAt", String.valueOf(System.currentTimeMillis()));

        db.collection("users").document(firebaseUser.getUid())
                .set(profile)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        onReady.onReady(firebaseUser);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        onError.onError(e);
                    }
                });
    }

    // signs in, then loads the saved name from Firestore so the app knows who's logged in
    public void login(String email, String password, final OnLoginReady onReady,
                      final OnErrorCallback onError) {

        auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        FirebaseUser firebaseUser = authResult.getUser();
                        loadName(firebaseUser, onReady, onError);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        onError.onError(e);
                    }
                });
    }

    private void loadName(final FirebaseUser firebaseUser, final OnLoginReady onReady,
                          final OnErrorCallback onError) {

        db.collection("users").document(firebaseUser.getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot document) {
                        String fullName = "";
                        if (document.exists()) {
                            String storedName = document.getString("fullName");
                            if (storedName != null) {
                                fullName = storedName;
                            }
                        }
                        onReady.onReady(firebaseUser, fullName);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        onError.onError(e);
                    }
                });
    }

    public void logout() {
        auth.signOut();
    }
}