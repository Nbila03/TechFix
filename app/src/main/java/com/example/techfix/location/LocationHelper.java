package com.example.techfix.location;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class LocationHelper {

    public static final int PERMISSION_REQUEST_CODE = 1001;

    // if no GPS fix arrives within this time, give up and tell the caller
    private static final long TIMEOUT_MILLIS = 15000;

    public interface LocationResultCallback {
        void onLocationResult(Location location);
        default void onLocationUnavailable() { }
    }

    private final Activity activity;
    private final LocationManager locationManager;
    private final Handler timeoutHandler = new Handler(Looper.getMainLooper());

    private LocationListener activeListener;
    private Runnable timeoutRunnable;
    private boolean callbackAlreadyFired;

    public LocationHelper(Activity activity) {
        this.activity = activity;
        this.locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
    }

    public boolean hasPermission() {
        boolean fineGranted = ContextCompat.checkSelfPermission(
                activity, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED;

        boolean coarseGranted = ContextCompat.checkSelfPermission(
                activity, Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED;

        if (fineGranted || coarseGranted) {
            return true;
        }
        return false;
    }

    public void requestPermission() {
        ActivityCompat.requestPermissions(activity,
                new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                },
                PERMISSION_REQUEST_CODE);
    }

    @SuppressWarnings("MissingPermission")
    public void getCurrentLocation(final LocationResultCallback callback) {

        if (hasPermission() == false) {
            callback.onLocationUnavailable();
            return;
        }

        callbackAlreadyFired = false;

        // first, try any cached fix - fastest path, works even with a weak signal
        Location best = null;

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Location gpsLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (gpsLocation != null) {
                best = gpsLocation;
            }
        }

        if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            Location networkLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if (networkLocation != null) {
                if (best == null || networkLocation.getAccuracy() < best.getAccuracy()) {
                    best = networkLocation;
                }
            }
        }

        if (best != null) {
            callback.onLocationResult(best);
            return;
        }

        // no cached fix - ask for a fresh one, but don't wait forever for it
        String provider;
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            provider = LocationManager.GPS_PROVIDER;
        } else {
            provider = LocationManager.NETWORK_PROVIDER;
        }

        if (locationManager.isProviderEnabled(provider) == false) {
            callback.onLocationUnavailable();
            return;
        }

        activeListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                finishWithResult(location, callback);
            }

            @Override
            public void onStatusChanged(String p, int status, Bundle extras) { }

            @Override
            public void onProviderEnabled(String p) { }

            @Override
            public void onProviderDisabled(String p) {
                finishWithTimeout(callback);
            }
        };

        locationManager.requestLocationUpdates(provider, 0, 0, activeListener, Looper.getMainLooper());

        timeoutRunnable = new Runnable() {
            @Override
            public void run() {
                finishWithTimeout(callback);
            }
        };

        timeoutHandler.postDelayed(timeoutRunnable, TIMEOUT_MILLIS);
    }

    private void finishWithResult(Location location, LocationResultCallback callback) {
        if (callbackAlreadyFired) {
            return;
        }
        callbackAlreadyFired = true;
        cleanUp();
        callback.onLocationResult(location);
    }

    private void finishWithTimeout(LocationResultCallback callback) {
        if (callbackAlreadyFired) {
            return;
        }
        callbackAlreadyFired = true;
        cleanUp();
        callback.onLocationUnavailable();
    }

    private void cleanUp() {
        if (activeListener != null) {
            locationManager.removeUpdates(activeListener);
            activeListener = null;
        }
        if (timeoutRunnable != null) {
            timeoutHandler.removeCallbacks(timeoutRunnable);
            timeoutRunnable = null;
        }
    }
}