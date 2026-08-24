package com.example.techfix.firebase;

import com.example.techfix.model.Device;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class DeviceRepository {

    private final FirebaseFirestore db;

    public DeviceRepository() {
        db = FirebaseFirestore.getInstance();
    }

    public interface OnSuccessCallback {
        void onSuccess();
    }

    public interface OnErrorCallback {
        void onError(Exception e);
    }

    public interface OnDevicesLoaded {
        void onLoaded(List<Device> devices);
    }

    // Adds a new device to Firebase
    public void addDevice(
            Device device,
            OnSuccessCallback onSuccess,
            OnErrorCallback onError) {

        String id = String.valueOf(device.getDeviceId());

        db.collection("devices")
                .document(id)
                .set(device)
                .addOnSuccessListener(unused -> {

                    onSuccess.onSuccess();

                })
                .addOnFailureListener(error -> {

                    onError.onError(error);

                });
    }

    // Gets every device belonging to one user
    public void getDevicesForUser(
            int userId,
            OnDevicesLoaded onLoaded,
            OnErrorCallback onError) {

        db.collection("devices")
                .whereEqualTo("userId", userId)
                .get()
                .addOnSuccessListener(result -> {

                    List<Device> devices = new ArrayList<>();

                    for (com.google.firebase.firestore.DocumentSnapshot document : result) {

                        Device device =
                                document.toObject(Device.class);

                        devices.add(device);
                    }

                    onLoaded.onLoaded(devices);

                })
                .addOnFailureListener(error -> {

                    onError.onError(error);

                });
    }

    // Deletes a device from Firebase
    public void deleteDevice(
            int deviceId,
            OnSuccessCallback onSuccess,
            OnErrorCallback onError) {

        String id = String.valueOf(deviceId);

        db.collection("devices")
                .document(id)
                .delete()
                .addOnSuccessListener(unused -> {

                    onSuccess.onSuccess();

                })
                .addOnFailureListener(error -> {

                    onError.onError(error);

                });
    }
}