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

    private static final String DATABASE_NAME = "techfix_local.db";
    private static final int DATABASE_VERSION = 1;

    // devices table
    private static final String TABLE_DEVICES = "devices";
    private static final String COLUMN_DEVICE_ID = "device_id";
    private static final String COLUMN_USER_ID = "user_id";
    private static final String COLUMN_CATEGORY_ID = "category_id";
    private static final String COLUMN_DEVICE_NAME = "device_name";
    private static final String COLUMN_BRAND = "brand";
    private static final String COLUMN_MODEL = "model";
    private static final String COLUMN_SERIAL_NUMBER = "serial_number";

    // offline cache tables
    public static final String TABLE_CACHED_BRANCHES = "cached_branches";
    public static final String TABLE_CACHED_SERVICES = "cached_services";
    public static final String TABLE_LOCAL_DEVICES = "local_devices";
    public static final String TABLE_PENDING_REPAIRS = "pending_repairs";

    public TechFixDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // devices table
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

        // offline cache tables
        db.execSQL("CREATE TABLE " + TABLE_CACHED_BRANCHES + " (" +
                "branch_id INTEGER PRIMARY KEY, " +
                "branch_name TEXT, " +
                "address TEXT, " +
                "city TEXT, " +
                "latitude REAL, " +
                "longitude REAL, " +
                "phone TEXT, " +
                "is_active INTEGER DEFAULT 1)");

        db.execSQL("CREATE TABLE " + TABLE_CACHED_SERVICES + " (" +
                "service_id INTEGER PRIMARY KEY, " +
                "service_name TEXT, " +
                "base_price REAL, " +
                "estimated_days INTEGER)");

        db.execSQL("CREATE TABLE " + TABLE_LOCAL_DEVICES + " (" +
                "local_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "device_name TEXT, " +
                "brand TEXT, " +
                "model TEXT, " +
                "synced INTEGER DEFAULT 0)");

        db.execSQL("CREATE TABLE " + TABLE_PENDING_REPAIRS + " (" +
                "pending_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "device_name TEXT, " +
                "service_id INTEGER, " +
                "problem_description TEXT, " +
                "appointment_date TEXT, " +
                "appointment_time TEXT, " +
                "customer_lat REAL, " +
                "customer_lng REAL, " +
                "created_at TEXT DEFAULT CURRENT_TIMESTAMP, " +
                "synced INTEGER DEFAULT 0)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DEVICES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CACHED_BRANCHES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CACHED_SERVICES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOCAL_DEVICES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PENDING_REPAIRS);
        onCreate(db);
    }

    public long insertDevice(Device device) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_ID, device.getUserId());
        values.put(COLUMN_CATEGORY_ID, device.getCategoryId());
        values.put(COLUMN_DEVICE_NAME, device.getDeviceName());
        values.put(COLUMN_BRAND, device.getBrand());
        values.put(COLUMN_MODEL, device.getModel());
        values.put(COLUMN_SERIAL_NUMBER, device.getSerialNumber());
        long result = db.insert(TABLE_DEVICES, null, values);
        return result;
    }

    public List<Device> getAllDevices() {
        List<Device> deviceList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_DEVICES, null, null, null, null, null, COLUMN_DEVICE_ID + " DESC");

        if (cursor.moveToFirst()) {
            do {
                int deviceId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_DEVICE_ID));
                int userId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_USER_ID));
                int categoryId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CATEGORY_ID));
                String deviceName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DEVICE_NAME));
                String brand = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BRAND));
                String model = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MODEL));
                String serialNumber = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SERIAL_NUMBER));

                Device device = new Device(deviceId, userId, categoryId, deviceName, brand, model, serialNumber);
                deviceList.add(device);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return deviceList;
    }

    public int deleteDevice(int deviceId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_DEVICES, COLUMN_DEVICE_ID + " = ?", new String[]{String.valueOf(deviceId)});
        return result;
    }

    public void replaceCachedBranches(List<Branch> branches) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            db.delete(TABLE_CACHED_BRANCHES, null, null);
            for (Branch b : branches) {
                ContentValues cv = new ContentValues();
                cv.put("branch_id", b.getBranchId());
                cv.put("branch_name", b.getBranchName());
                cv.put("address", b.getAddress());
                cv.put("city", b.getCity());
                cv.put("latitude", b.getLatitude());
                cv.put("longitude", b.getLongitude());
                cv.put("phone", b.getPhone());
                cv.put("is_active", b.isActive() ? 1 : 0);
                db.insertWithOnConflict(TABLE_CACHED_BRANCHES, null, cv, SQLiteDatabase.CONFLICT_REPLACE);
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    public List<Branch> getCachedBranches() {
        List<Branch> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(TABLE_CACHED_BRANCHES, null, null, null, null, null, "branch_name ASC");
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
            list.add(b);
        }
        c.close();
        return list;
    }

    public long queuePendingRepair(String deviceName, int serviceId, String problem,
                                   String date, String time, double lat, double lng) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("device_name", deviceName);
        cv.put("service_id", serviceId);
        cv.put("problem_description", problem);
        cv.put("appointment_date", date);
        cv.put("appointment_time", time);
        cv.put("customer_lat", lat);
        cv.put("customer_lng", lng);
        cv.put("synced", 0);
        return db.insert(TABLE_PENDING_REPAIRS, null, cv);
    }

    public Cursor getUnsyncedPendingRepairs() {
        SQLiteDatabase db = getReadableDatabase();
        return db.query(TABLE_PENDING_REPAIRS, null, "synced = 0", null, null, null, "created_at ASC");
    }

    public void markPendingRepairSynced(long pendingId) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("synced", 1);
        db.update(TABLE_PENDING_REPAIRS, cv, "pending_id = ?", new String[]{String.valueOf(pendingId)});
    }
}