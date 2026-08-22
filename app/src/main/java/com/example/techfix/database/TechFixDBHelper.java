package com.example.techfix.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.techfix.model.Device;

import java.util.ArrayList;
import java.util.List;

public class TechFixDBHelper extends SQLiteOpenHelper {

    // =====================================================
    // DATABASE INFORMATION
    // =====================================================

    private static final String DATABASE_NAME = "techfix_local.db";

    // Increased from 1 to 2 because we are adding a new table
    private static final int DATABASE_VERSION = 2;


    // =====================================================
    // DEVICE TABLE
    // =====================================================

    private static final String TABLE_DEVICES = "devices";

    private static final String COLUMN_DEVICE_ID = "device_id";
    private static final String COLUMN_USER_ID = "user_id";
    private static final String COLUMN_CATEGORY_ID = "category_id";
    private static final String COLUMN_DEVICE_NAME = "device_name";
    private static final String COLUMN_BRAND = "brand";
    private static final String COLUMN_MODEL = "model";
    private static final String COLUMN_SERIAL_NUMBER = "serial_number";


    // =====================================================
    // REPAIR REQUEST TABLE
    // =====================================================

    private static final String TABLE_REPAIR_REQUESTS =
            "repair_requests";

    private static final String COLUMN_REPAIR_ID =
            "repair_id";

    private static final String COLUMN_REPAIR_DEVICE_ID =
            "device_id";

    private static final String COLUMN_REPAIR_SERVICE =
            "service_name";

    private static final String COLUMN_PROBLEM_DESCRIPTION =
            "problem_description";

    private static final String COLUMN_APPOINTMENT_DATE =
            "appointment_date";

    private static final String COLUMN_APPOINTMENT_TIME =
            "appointment_time";

    private static final String COLUMN_IMAGE_PATH =
            "image_path";

    private static final String COLUMN_STATUS =
            "status";


    // =====================================================
    // CONSTRUCTOR
    // =====================================================

    public TechFixDBHelper(Context context) {

        super(
                context,
                DATABASE_NAME,
                null,
                DATABASE_VERSION
        );
    }


    // =====================================================
    // CREATE DATABASE TABLES
    // =====================================================

    @Override
    public void onCreate(SQLiteDatabase db) {

        // -----------------------------
        // Create Devices table
        // -----------------------------

        String createDeviceTable =
                "CREATE TABLE " + TABLE_DEVICES + " (" +
                        COLUMN_DEVICE_ID +
                        " INTEGER PRIMARY KEY AUTOINCREMENT, " +

                        COLUMN_USER_ID +
                        " INTEGER NOT NULL, " +

                        COLUMN_CATEGORY_ID +
                        " INTEGER NOT NULL, " +

                        COLUMN_DEVICE_NAME +
                        " TEXT NOT NULL, " +

                        COLUMN_BRAND +
                        " TEXT, " +

                        COLUMN_MODEL +
                        " TEXT, " +

                        COLUMN_SERIAL_NUMBER +
                        " TEXT" +

                        ")";

        db.execSQL(createDeviceTable);


        // -----------------------------
        // Create Repair Requests table
        // -----------------------------

        String createRepairRequestTable =
                "CREATE TABLE "
                        + TABLE_REPAIR_REQUESTS
                        + " (" +

                        COLUMN_REPAIR_ID +
                        " INTEGER PRIMARY KEY AUTOINCREMENT, " +

                        COLUMN_REPAIR_DEVICE_ID +
                        " INTEGER NOT NULL, " +

                        COLUMN_REPAIR_SERVICE +
                        " TEXT NOT NULL, " +

                        COLUMN_PROBLEM_DESCRIPTION +
                        " TEXT NOT NULL, " +

                        COLUMN_APPOINTMENT_DATE +
                        " TEXT NOT NULL, " +

                        COLUMN_APPOINTMENT_TIME +
                        " TEXT NOT NULL, " +

                        COLUMN_IMAGE_PATH +
                        " TEXT, " +

                        COLUMN_STATUS +
                        " TEXT NOT NULL DEFAULT 'SUBMITTED'" +

                        ")";

        db.execSQL(createRepairRequestTable);
    }


    // =====================================================
    // UPGRADE DATABASE
    // =====================================================

    @Override
    public void onUpgrade(
            SQLiteDatabase db,
            int oldVersion,
            int newVersion
    ) {

        /*
         For this coursework version, we recreate local tables
         when the schema changes.

         This is okay for development/testing.
        */

        db.execSQL(
                "DROP TABLE IF EXISTS "
                        + TABLE_REPAIR_REQUESTS
        );

        db.execSQL(
                "DROP TABLE IF EXISTS "
                        + TABLE_DEVICES
        );

        onCreate(db);
    }


    // =====================================================
    // INSERT DEVICE
    // =====================================================

    public long insertDevice(Device device) {

        SQLiteDatabase db =
                this.getWritableDatabase();

        ContentValues values =
                new ContentValues();

        values.put(
                COLUMN_USER_ID,
                device.getUserId()
        );

        values.put(
                COLUMN_CATEGORY_ID,
                device.getCategoryId()
        );

        values.put(
                COLUMN_DEVICE_NAME,
                device.getDeviceName()
        );

        values.put(
                COLUMN_BRAND,
                device.getBrand()
        );

        values.put(
                COLUMN_MODEL,
                device.getModel()
        );

        values.put(
                COLUMN_SERIAL_NUMBER,
                device.getSerialNumber()
        );

        long result =
                db.insert(
                        TABLE_DEVICES,
                        null,
                        values
                );

        db.close();

        return result;
    }


    // =====================================================
    // GET ALL DEVICES
    // =====================================================

    public List<Device> getAllDevices() {

        List<Device> deviceList =
                new ArrayList<>();

        SQLiteDatabase db =
                this.getReadableDatabase();

        Cursor cursor =
                db.query(
                        TABLE_DEVICES,
                        null,
                        null,
                        null,
                        null,
                        null,
                        COLUMN_DEVICE_ID + " DESC"
                );

        if (cursor.moveToFirst()) {

            do {

                int deviceId =
                        cursor.getInt(
                                cursor.getColumnIndexOrThrow(
                                        COLUMN_DEVICE_ID
                                )
                        );

                int userId =
                        cursor.getInt(
                                cursor.getColumnIndexOrThrow(
                                        COLUMN_USER_ID
                                )
                        );

                int categoryId =
                        cursor.getInt(
                                cursor.getColumnIndexOrThrow(
                                        COLUMN_CATEGORY_ID
                                )
                        );

                String deviceName =
                        cursor.getString(
                                cursor.getColumnIndexOrThrow(
                                        COLUMN_DEVICE_NAME
                                )
                        );

                String brand =
                        cursor.getString(
                                cursor.getColumnIndexOrThrow(
                                        COLUMN_BRAND
                                )
                        );

                String model =
                        cursor.getString(
                                cursor.getColumnIndexOrThrow(
                                        COLUMN_MODEL
                                )
                        );

                String serialNumber =
                        cursor.getString(
                                cursor.getColumnIndexOrThrow(
                                        COLUMN_SERIAL_NUMBER
                                )
                        );

                Device device =
                        new Device(
                                deviceId,
                                userId,
                                categoryId,
                                deviceName,
                                brand,
                                model,
                                serialNumber
                        );

                deviceList.add(device);

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return deviceList;
    }


    // =====================================================
    // DELETE DEVICE
    // =====================================================

    public int deleteDevice(int deviceId) {

        SQLiteDatabase db =
                this.getWritableDatabase();

        int result =
                db.delete(
                        TABLE_DEVICES,
                        COLUMN_DEVICE_ID + " = ?",
                        new String[]{
                                String.valueOf(deviceId)
                        }
                );

        db.close();

        return result;
    }


    // =====================================================
    // INSERT REPAIR REQUEST
    // =====================================================

    public long insertRepairRequest(
            int deviceId,
            String serviceName,
            String problemDescription,
            String appointmentDate,
            String appointmentTime,
            String imagePath
    ) {

        SQLiteDatabase db =
                this.getWritableDatabase();

        ContentValues values =
                new ContentValues();

        values.put(
                COLUMN_REPAIR_DEVICE_ID,
                deviceId
        );

        values.put(
                COLUMN_REPAIR_SERVICE,
                serviceName
        );

        values.put(
                COLUMN_PROBLEM_DESCRIPTION,
                problemDescription
        );

        values.put(
                COLUMN_APPOINTMENT_DATE,
                appointmentDate
        );

        values.put(
                COLUMN_APPOINTMENT_TIME,
                appointmentTime
        );

        values.put(
                COLUMN_IMAGE_PATH,
                imagePath
        );

        values.put(
                COLUMN_STATUS,
                "SUBMITTED"
        );

        long result =
                db.insert(
                        TABLE_REPAIR_REQUESTS,
                        null,
                        values
                );

        db.close();

        return result;
    }
}