package com.example.techfix.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.techfix.database.TechFixDBHelper;

// exposes the local SQLite cache s
public class TechFixContentProvider extends ContentProvider {

    public static final String AUTHORITY = "com.example.techfix.provider";
    public static final Uri BRANCHES_URI =
            Uri.parse("content://" + AUTHORITY + "/branches");
    public static final Uri PENDING_REPAIRS_URI =
            Uri.parse("content://" + AUTHORITY + "/pending_repairs");

    private static final int BRANCHES = 1;
    private static final int BRANCH_ID = 2;
    private static final int PENDING_REPAIRS = 3;
    private static final int PENDING_REPAIR_ID = 4;

    private static final UriMatcher MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        MATCHER.addURI(AUTHORITY, "branches", BRANCHES);
        MATCHER.addURI(AUTHORITY, "branches/#", BRANCH_ID);
        MATCHER.addURI(AUTHORITY, "pending_repairs", PENDING_REPAIRS);
        MATCHER.addURI(AUTHORITY, "pending_repairs/#", PENDING_REPAIR_ID);
    }

    private TechFixDBHelper dbHelper;

    @Override
    public boolean onCreate() {
        dbHelper = new TechFixDBHelper(getContext());
        return true;
    }

    // works out which table + which selection string to use based on the incoming uri
    private String tableAndSelection(Uri uri, String selection, StringBuilder outSelection) {
        int match = MATCHER.match(uri);
        String table;
        switch (match) {
            case BRANCHES:
                table = TechFixDBHelper.TABLE_CACHED_BRANCHES;
                outSelection.append(selection == null ? "" : selection);
                break;
            case BRANCH_ID:
                table = TechFixDBHelper.TABLE_CACHED_BRANCHES;
                outSelection.append("branch_id = ").append(ContentUris.parseId(uri));
                break;
            case PENDING_REPAIRS:
                table = TechFixDBHelper.TABLE_PENDING_REPAIRS;
                outSelection.append(selection == null ? "" : selection);
                break;
            case PENDING_REPAIR_ID:
                table = TechFixDBHelper.TABLE_PENDING_REPAIRS;
                outSelection.append("pending_id = ").append(ContentUris.parseId(uri));
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        return table;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        StringBuilder finalSelection = new StringBuilder();
        String table = tableAndSelection(uri, selection, finalSelection);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(table, projection, finalSelection.toString(), selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        int match = MATCHER.match(uri);
        String table = (match == PENDING_REPAIRS) ? TechFixDBHelper.TABLE_PENDING_REPAIRS
                : TechFixDBHelper.TABLE_CACHED_BRANCHES;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long id = db.insertWithOnConflict(table, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        Uri resultUri = ContentUris.withAppendedId(uri, id);
        getContext().getContentResolver().notifyChange(resultUri, null);
        return resultUri;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection,
                      @Nullable String[] selectionArgs) {
        StringBuilder finalSelection = new StringBuilder();
        String table = tableAndSelection(uri, selection, finalSelection);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int count = db.update(table, values, finalSelection.toString(), selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        StringBuilder finalSelection = new StringBuilder();
        String table = tableAndSelection(uri, selection, finalSelection);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int count = db.delete(table, finalSelection.toString(), selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        int match = MATCHER.match(uri);
        switch (match) {
            case BRANCHES:
            case PENDING_REPAIRS:
                return "vnd.android.cursor.dir/vnd." + AUTHORITY + ".item";
            case BRANCH_ID:
            case PENDING_REPAIR_ID:
                return "vnd.android.cursor.item/vnd." + AUTHORITY + ".item";
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }
}