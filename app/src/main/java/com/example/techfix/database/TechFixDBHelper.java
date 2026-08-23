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

    // Database information
    private static final String DATABASE_NAME = "techfix_local.db";
    private static final int DATABASE_VERSION = 1;

    // Device table
    private static final String TABLE_DEVICES = "devices";

    private static final String COLUMN_DEVICE_ID = "device_id";
    private static final String COLUMN_USER_ID = "user_id";
    private static final String COLUMN_CATEGORY_ID = "category_id";
    private static final String COLUMN_DEVICE_NAME = "device_name";
    private static final String COLUMN_BRAND = "brand";
    private static final String COLUMN_MODEL = "model";
    private static final String COLUMN_SERIAL_NUMBER = "serial_number";

    public TechFixDBHelper(Context context) {
        super(
                context,
                DATABASE_NAME,
                null,
                DATABASE_VERSION
        );
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String createDeviceTable =
                "CREATE TABLE " + TABLE_DEVICES + " (" +
                        COLUMN_DEVICE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COLUMN_USER_ID + " INTEGER NOT NULL, " +
                        COLUMN_CATEGORY_ID + " INTEGER NOT NULL, " +
                        COLUMN_DEVICE_NAME + " TEXT NOT NULL, " +
                        COLUMN_BRAND + " TEXT, " +
                        COLUMN_MODEL + " TEXT, " +
                        COLUMN_SERIAL_NUMBER + " TEXT" +
                        ")";

        db.execSQL(createDeviceTable);
    }

    @Override
    public void onUpgrade(
            SQLiteDatabase db,
            int oldVersion,
            int newVersion
    ) {

        db.execSQL(
                "DROP TABLE IF EXISTS " + TABLE_DEVICES
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
}