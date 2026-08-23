package com.example.techfix.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import com.example.techfix.model.Branch;
import com.example.techfix.provider.TechFixContentProvider;
import java.util.ArrayList;
import java.util.List;

public class ContentProviderHelper {

    // reads cached branches through the ContentProvider (instead of TechFixDBHelper directly)
    public static List<Branch> getCachedBranches(Context context) {
        List<Branch> branches = new ArrayList<>();
        try (Cursor c = context.getContentResolver().query(
                TechFixContentProvider.BRANCHES_URI, null, null, null, "branch_name ASC")) {
            if (c != null) {
                while (c.moveToNext()) {
                    Branch b = new Branch();
                    b.setBranchId(c.getInt(c.getColumnIndexOrThrow("branch_id")));
                    b.setBranchName(c.getString(c.getColumnIndexOrThrow("branch_name")));
                    b.setAddress(c.getString(c.getColumnIndexOrThrow("address")));
                    b.setCity(c.getString(c.getColumnIndexOrThrow("city")));
                    b.setLatitude(c.getDouble(c.getColumnIndexOrThrow("latitude")));
                    b.setLongitude(c.getDouble(c.getColumnIndexOrThrow("longitude")));
                    b.setPhone(c.getString(c.getColumnIndexOrThrow("phone")));
                    b.setActive(c.getInt(c.getColumnIndexOrThrow("is_active")) == 1);
                    branches.add(b);
                }
            }
        }
        return branches;
    }

    // queues an offline repair through the ContentProvider
    public static void queuePendingRepair(Context context, String deviceName, int serviceId,
                                          String problem, String date, String time, double lat, double lng) {
        ContentValues cv = new ContentValues();
        cv.put("device_name", deviceName);
        cv.put("service_id", serviceId);
        cv.put("problem_description", problem);
        cv.put("appointment_date", date);
        cv.put("appointment_time", time);
        cv.put("customer_lat", lat);
        cv.put("customer_lng", lng);
        cv.put("synced", 0);
        context.getContentResolver().insert(TechFixContentProvider.PENDING_REPAIRS_URI, cv);
    }
}