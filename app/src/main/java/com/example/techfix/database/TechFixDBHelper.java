package com.example.techfix.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.techfix.model.Branch;
import com.example.techfix.model.Device;

import java.util.ArrayList;
import java.util.List;

public class TechFixDBHelper extends SQLiteOpenHelper {

    // =====================================================
    // DATABASE INFORMATION
    // =====================================================

    private static final String DATABASE_NAME = "techfix_local.db";

    /*
     * Version 2 because repair_requests and the offline/cache
     * tables are part of the merged database structure.
     */
    private static final int DATABASE_VERSION = 2;


    // =====================================================
    // DEVICES TABLE
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
    // LOCAL REPAIR REQUEST TABLE
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
    // OFFLINE / CACHE TABLES
    // =====================================================

    public static final String TABLE_CACHED_BRANCHES =
            "cached_branches";

    public static final String TABLE_CACHED_SERVICES =
            "cached_services";

    public static final String TABLE_LOCAL_DEVICES =
            "local_devices";

    public static final String TABLE_PENDING_REPAIRS =
            "pending_repairs";


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
    // CREATE TABLES
    // =====================================================

    @Override
    public void onCreate(SQLiteDatabase db) {

        // ---------------------------------------------
        // Devices table
        // ---------------------------------------------

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


        // ---------------------------------------------
        // Repair Requests table
        // ---------------------------------------------

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


        // ---------------------------------------------
        // Cached branches
        // ---------------------------------------------

        db.execSQL(
                "CREATE TABLE " + TABLE_CACHED_BRANCHES + " (" +
                        "branch_id INTEGER PRIMARY KEY, " +
                        "branch_name TEXT, " +
                        "address TEXT, " +
                        "city TEXT, " +
                        "latitude REAL, " +
                        "longitude REAL, " +
                        "phone TEXT, " +
                        "is_active INTEGER DEFAULT 1" +
                        ")"
        );


        // ---------------------------------------------
        // Cached services
        // ---------------------------------------------

        db.execSQL(
                "CREATE TABLE " + TABLE_CACHED_SERVICES + " (" +
                        "service_id INTEGER PRIMARY KEY, " +
                        "service_name TEXT, " +
                        "base_price REAL, " +
                        "estimated_days INTEGER" +
                        ")"
        );


        // ---------------------------------------------
        // Local devices for offline sync
        // ---------------------------------------------

        db.execSQL(
                "CREATE TABLE " + TABLE_LOCAL_DEVICES + " (" +
                        "local_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "device_name TEXT, " +
                        "brand TEXT, " +
                        "model TEXT, " +
                        "synced INTEGER DEFAULT 0" +
                        ")"
        );


        // ---------------------------------------------
        // Pending repair requests for offline sync
        // ---------------------------------------------

        db.execSQL(
                "CREATE TABLE " + TABLE_PENDING_REPAIRS + " (" +
                        "pending_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "device_name TEXT, " +
                        "service_id INTEGER, " +
                        "problem_description TEXT, " +
                        "appointment_date TEXT, " +
                        "appointment_time TEXT, " +
                        "customer_lat REAL, " +
                        "customer_lng REAL, " +
                        "created_at TEXT DEFAULT CURRENT_TIMESTAMP, " +
                        "synced INTEGER DEFAULT 0" +
                        ")"
        );
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

        db.execSQL(
                "DROP TABLE IF EXISTS "
                        + TABLE_REPAIR_REQUESTS
        );

        db.execSQL(
                "DROP TABLE IF EXISTS "
                        + TABLE_DEVICES
        );

        db.execSQL(
                "DROP TABLE IF EXISTS "
                        + TABLE_CACHED_BRANCHES
        );

        db.execSQL(
                "DROP TABLE IF EXISTS "
                        + TABLE_CACHED_SERVICES
        );

        db.execSQL(
                "DROP TABLE IF EXISTS "
                        + TABLE_LOCAL_DEVICES
        );

        db.execSQL(
                "DROP TABLE IF EXISTS "
                        + TABLE_PENDING_REPAIRS
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

        return db.insert(
                TABLE_DEVICES,
                null,
                values
        );
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

        return deviceList;
    }


    // =====================================================
    // DELETE DEVICE
    // =====================================================

    public int deleteDevice(int deviceId) {

        SQLiteDatabase db =
                this.getWritableDatabase();

        return db.delete(
                TABLE_DEVICES,
                COLUMN_DEVICE_ID + " = ?",
                new String[]{
                        String.valueOf(deviceId)
                }
        );
    }


    // =====================================================
    // INSERT LOCAL REPAIR REQUEST
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

        return db.insert(
                TABLE_REPAIR_REQUESTS,
                null,
                values
        );
    }


    // =====================================================
    // REPLACE CACHED BRANCHES
    // =====================================================

    public void replaceCachedBranches(
            List<Branch> branches
    ) {

        SQLiteDatabase db =
                getWritableDatabase();

        db.beginTransaction();

        try {

            db.delete(
                    TABLE_CACHED_BRANCHES,
                    null,
                    null
            );

            for (Branch branch : branches) {

                ContentValues values =
                        new ContentValues();

                values.put(
                        "branch_id",
                        branch.getBranchId()
                );

                values.put(
                        "branch_name",
                        branch.getBranchName()
                );

                values.put(
                        "address",
                        branch.getAddress()
                );

                values.put(
                        "city",
                        branch.getCity()
                );

                values.put(
                        "latitude",
                        branch.getLatitude()
                );

                values.put(
                        "longitude",
                        branch.getLongitude()
                );

                values.put(
                        "phone",
                        branch.getPhone()
                );

                values.put(
                        "is_active",
                        branch.isActive() ? 1 : 0
                );

                db.insertWithOnConflict(
                        TABLE_CACHED_BRANCHES,
                        null,
                        values,
                        SQLiteDatabase.CONFLICT_REPLACE
                );
            }

            db.setTransactionSuccessful();

        } finally {

            db.endTransaction();
        }
    }


    // =====================================================
    // GET CACHED BRANCHES
    // =====================================================

    public List<Branch> getCachedBranches() {

        List<Branch> branchList =
                new ArrayList<>();

        SQLiteDatabase db =
                getReadableDatabase();

        Cursor cursor =
                db.query(
                        TABLE_CACHED_BRANCHES,
                        null,
                        null,
                        null,
                        null,
                        null,
                        "branch_name ASC"
                );

        while (cursor.moveToNext()) {

            Branch branch =
                    new Branch();

            branch.setBranchId(
                    cursor.getInt(
                            cursor.getColumnIndexOrThrow(
                                    "branch_id"
                            )
                    )
            );

            branch.setBranchName(
                    cursor.getString(
                            cursor.getColumnIndexOrThrow(
                                    "branch_name"
                            )
                    )
            );

            branch.setAddress(
                    cursor.getString(
                            cursor.getColumnIndexOrThrow(
                                    "address"
                            )
                    )
            );

            branch.setCity(
                    cursor.getString(
                            cursor.getColumnIndexOrThrow(
                                    "city"
                            )
                    )
            );

            branch.setLatitude(
                    cursor.getDouble(
                            cursor.getColumnIndexOrThrow(
                                    "latitude"
                            )
                    )
            );

            branch.setLongitude(
                    cursor.getDouble(
                            cursor.getColumnIndexOrThrow(
                                    "longitude"
                            )
                    )
            );

            branch.setPhone(
                    cursor.getString(
                            cursor.getColumnIndexOrThrow(
                                    "phone"
                            )
                    )
            );

            branch.setActive(
                    cursor.getInt(
                            cursor.getColumnIndexOrThrow(
                                    "is_active"
                            )
                    ) == 1
            );

            branchList.add(branch);
        }

        cursor.close();

        return branchList;
    }


    // =====================================================
    // QUEUE PENDING REPAIR
    // =====================================================

    public long queuePendingRepair(
            String deviceName,
            int serviceId,
            String problem,
            String date,
            String time,
            double lat,
            double lng
    ) {

        SQLiteDatabase db =
                getWritableDatabase();

        ContentValues values =
                new ContentValues();

        values.put(
                "device_name",
                deviceName
        );

        values.put(
                "service_id",
                serviceId
        );

        values.put(
                "problem_description",
                problem
        );

        values.put(
                "appointment_date",
                date
        );

        values.put(
                "appointment_time",
                time
        );

        values.put(
                "customer_lat",
                lat
        );

        values.put(
                "customer_lng",
                lng
        );

        values.put(
                "synced",
                0
        );

        return db.insert(
                TABLE_PENDING_REPAIRS,
                null,
                values
        );
    }


    // =====================================================
    // GET UNSYNCED REPAIRS
    // =====================================================

    public Cursor getUnsyncedPendingRepairs() {

        SQLiteDatabase db =
                getReadableDatabase();

        return db.query(
                TABLE_PENDING_REPAIRS,
                null,
                "synced = 0",
                null,
                null,
                null,
                "created_at ASC"
        );
    }


    // =====================================================
    // MARK REPAIR AS SYNCED
    // =====================================================

    public void markPendingRepairSynced(
            long pendingId
    ) {

        SQLiteDatabase db =
                getWritableDatabase();

        ContentValues values =
                new ContentValues();

        values.put(
                "synced",
                1
        );

        db.update(
                TABLE_PENDING_REPAIRS,
                values,
                "pending_id = ?",
                new String[]{
                        String.valueOf(pendingId)
                }
        );
    }
}