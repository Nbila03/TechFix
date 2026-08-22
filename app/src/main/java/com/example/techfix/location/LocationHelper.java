package com.example.techfix.location;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;

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
}