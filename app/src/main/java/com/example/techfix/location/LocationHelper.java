package com.example.techfix.location;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
public class LocationHelper {

    public static final int PERMISSION_REQUEST_CODE = 1001;

    public interface LocationResultCallback {
        void onLocationResult(Location location);
        default void onLocationUnavailable() { }
    }

    private final Activity activity;
    private final LocationManager locationManager;

    public LocationHelper(Activity activity) {
        this.activity = activity;
        this.locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
    }

    public boolean hasPermission() {
        return ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED;
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
        if (!hasPermission()) {
            callback.onLocationUnavailable();
            return;
        }

        Location best = null;
        for (String provider : new String[]{LocationManager.GPS_PROVIDER, LocationManager.NETWORK_PROVIDER}) {
            if (locationManager.isProviderEnabled(provider)) {
                Location loc = locationManager.getLastKnownLocation(provider);
                if (loc != null && (best == null || loc.getAccuracy() < best.getAccuracy())) {
                    best = loc;
                }
            }
        }

        if (best != null) {
            callback.onLocationResult(best);
            return;
        }

        // No cached fix yet - request a single fresh update.
        String provider = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                ? LocationManager.GPS_PROVIDER
                : LocationManager.NETWORK_PROVIDER;

        if (!locationManager.isProviderEnabled(provider)) {
            callback.onLocationUnavailable();
            return;
        }

        LocationListener listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                locationManager.removeUpdates(this);
                callback.onLocationResult(location);
            }

            @Override
            public void onStatusChanged(String p, int status, Bundle extras) { }

            @Override
            public void onProviderEnabled(String p) { }

            @Override
            public void onProviderDisabled(String p) {
                locationManager.removeUpdates(this);
                callback.onLocationUnavailable();
            }
        };

        locationManager.requestLocationUpdates(provider, 0, 0, listener, Looper.getMainLooper());
    }
}
