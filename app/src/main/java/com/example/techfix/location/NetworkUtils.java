package com.example.techfix.location;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;

public class NetworkUtils {

    public static boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) return false;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            android.net.Network network = cm.getActiveNetwork();
            if (network == null) return false;
            NetworkCapabilities caps = cm.getNetworkCapabilities(network);
            return caps != null &&
                    (caps.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                            || caps.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                            || caps.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET));
        } else {
            // older devices - use the deprecated API since there's no alternative
            @SuppressWarnings("deprecation")
            NetworkInfo info = cm.getActiveNetworkInfo();
            return info != null && info.isConnected();
        }
    }
}