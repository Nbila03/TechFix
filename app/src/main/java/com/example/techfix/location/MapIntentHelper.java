package com.example.techfix.location;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

// opens maps app
public class MapIntentHelper {

    // drop a pin on the branch locationS
    public static void openLocation(Context context, double latitude, double longitude, String label) {
        String uriString = "geo:" + latitude + "," + longitude
                + "?q=" + latitude + "," + longitude
                + "(" + Uri.encode(label) + ")";

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uriString));
        launch(context, intent);
    }

    // ask the maps app for turn by turn directions from current location to the branch
    public static void openDirections(Context context, double destLat, double destLng) {
        String uriString = "google.navigation:q=" + destLat + "," + destLng;
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uriString));
        intent.setPackage("com.google.android.apps.maps");
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
        } else {
            // Gmaps not installed ? fall back to a generic geo intent
            openLocation(context, destLat, destLng, "TechFix Branch");
        }
    }

    private static void launch(Context context, Intent intent) {
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
        } else {
            Toast.makeText(context, "No maps application found on this device.", Toast.LENGTH_SHORT).show();
        }
    }
}