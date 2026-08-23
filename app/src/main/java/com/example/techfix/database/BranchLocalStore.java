package com.example.techfix.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import com.example.techfix.model.Branch;
import com.example.techfix.provider.TechFixContentProvider;
import java.util.ArrayList;
import java.util.List;

public class BranchLocalStore {

    private final Context context;

    public BranchLocalStore(Context context) {
        this.context = context.getApplicationContext();
    }

    //
    public void replaceCachedBranches(List<Branch> branches) {

        // Remove the old cached branch records.
        context.getContentResolver().delete(
                TechFixContentProvider.BRANCHES_URI,
                null,
                null
        );

        // Add the new branch records to the local database.
        for (Branch branch : branches) {

            ContentValues values = new ContentValues();

            values.put("branch_id", branch.getBranchId());
            values.put("branch_name", branch.getBranchName());
            values.put("address", branch.getAddress());
            values.put("city", branch.getCity());
            values.put("latitude", branch.getLatitude());
            values.put("longitude", branch.getLongitude());
            values.put("phone", branch.getPhone());

            // Store the boolean active value as 1 or 0.
            if (branch.isActive()) {
                values.put("is_active", 1);
            } else {
                values.put("is_active", 0);
            }

            context.getContentResolver().insert(
                    TechFixContentProvider.BRANCHES_URI,
                    values
            );
        }
    }

    // Gets all branches currently stored in the local cache.
    public List<Branch> getCachedBranches() {

        List<Branch> branches = new ArrayList<>();

        Cursor cursor = context.getContentResolver().query(
                TechFixContentProvider.BRANCHES_URI,
                null,
                null,
                null,
                "branch_name ASC"
        );

        if (cursor != null) {

            while (cursor.moveToNext()) {

                Branch branch = new Branch();

                int branchIdIndex =
                        cursor.getColumnIndexOrThrow("branch_id");

                int branchNameIndex =
                        cursor.getColumnIndexOrThrow("branch_name");

                int addressIndex =
                        cursor.getColumnIndexOrThrow("address");

                int cityIndex =
                        cursor.getColumnIndexOrThrow("city");

                int latitudeIndex =
                        cursor.getColumnIndexOrThrow("latitude");

                int longitudeIndex =
                        cursor.getColumnIndexOrThrow("longitude");

                int phoneIndex =
                        cursor.getColumnIndexOrThrow("phone");

                int activeIndex =
                        cursor.getColumnIndexOrThrow("is_active");

                branch.setBranchId(
                        cursor.getInt(branchIdIndex)
                );

                branch.setBranchName(
                        cursor.getString(branchNameIndex)
                );

                branch.setAddress(
                        cursor.getString(addressIndex)
                );

                branch.setCity(
                        cursor.getString(cityIndex)
                );

                branch.setLatitude(
                        cursor.getDouble(latitudeIndex)
                );

                branch.setLongitude(
                        cursor.getDouble(longitudeIndex)
                );

                branch.setPhone(
                        cursor.getString(phoneIndex)
                );

                // Convert the stored 1/0 value back into true/false.
                if (cursor.getInt(activeIndex) == 1) {
                    branch.setActive(true);
                } else {
                    branch.setActive(false);
                }

                branches.add(branch);
            }

            cursor.close();
        }

        return branches;
    }
}