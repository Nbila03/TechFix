package com.example.techfix.utils;
import android.content.Context;
import android.content.SharedPreferences;
public class SessionManager {

    private static final String PREFS = "techfix_session";
    private static final String KEY_USER_ID = "user_id";

    public static void saveUserId(Context context, int userId) {
        prefs(context).edit().putInt(KEY_USER_ID, userId).apply();
    }
    public static int getUserId(Context context) {
        return prefs(context).getInt(KEY_USER_ID, -1);
    }
    private static SharedPreferences prefs(Context context) {
        return context.getApplicationContext()
                .getSharedPreferences(PREFS, Context.MODE_PRIVATE);
    }
}
