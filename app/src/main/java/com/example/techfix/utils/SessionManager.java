package com.example.techfix.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {

    private static final String PREFS = "techfix_session";
    private static final String KEY_USER_UID = "user_uid";
    private static final String KEY_USER_NAME = "user_name";
    private static final String KEY_USER_EMAIL = "user_email";

    public static void saveUserSession(Context context, String uid, String fullName, String email)
    {
        SharedPreferences.Editor editor = prefs(context).edit();
        editor.putString(KEY_USER_UID, uid);
        editor.putString(KEY_USER_NAME, fullName);
        editor.putString(KEY_USER_EMAIL, email);
        editor.apply();
    }

    public static String getUserUid(Context context) {
        return prefs(context).getString(KEY_USER_UID, null);
    }

    public static String getUserName(Context context) {
        return prefs(context).getString(KEY_USER_NAME, "");
    }

    public static String getUserEmail(Context context) {
        return prefs(context).getString(KEY_USER_EMAIL, "");
    }

    public static boolean isLoggedIn(Context context) {
        String uid = getUserUid(context);
        if (uid == null) {
            return false;
        }
        return true;
    }

    public static void logout(Context context) {
        prefs(context).edit().clear().apply();
    }

    private static SharedPreferences prefs(Context context) {
        return context.getApplicationContext()
                .getSharedPreferences(PREFS, Context.MODE_PRIVATE);
    }
}