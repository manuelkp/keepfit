package com.example.keepfit;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import java.util.HashMap;


public class ContentProviderDB extends ContentProvider {
    static final String PROVIDER_NAME = "com.example.keepfit.ContentProvider";
    static final String URL_G = "content://" + PROVIDER_NAME + "/goals";
    static final Uri CONTENT_URI_G = Uri.parse(URL_G);
    static final String URL_R = "content://" + PROVIDER_NAME + "/records";
    static final Uri CONTENT_URI_R = Uri.parse(URL_R);
    static final String URL_S = "content://" + PROVIDER_NAME + "/settings";
    static final Uri CONTENT_URI_S = Uri.parse(URL_S);


    /** COLUMNS_R */
    static final String R_ID = "_id";
    static final String R_STEPS = "steps";
    static final String DATE = "date";
    static final String DATENUM = "datenum";
    static final String SUCCESSFUL = "successful";
    static final String R_GOAL_OTD = "goalotd";
    static final String R_REC_UNIT = "recunit";


    /** COLUMNS_G */
    static final String G_ID = "_id";
    static final String G_NAME = "name";
    static final String G_STEPS = "steps";
    static final String G_SELECTED = "selected";
    static final String G_UNITS = "units";


    /** COLUMNS_S */
    static final String S_ID = "_id";
    static final String S_UNITS = "units";
    static final String S_NOTIFICATIONS = "notifications";
    static final String S_MODE = "mode";
    static final String S_EDITGOAL = "editgoal";


    private static HashMap<String, String> RECORD_PROJECTION_MAP;
    private static HashMap<String, String> GOAL_PROJECTION_MAP;
    private static HashMap<String, String> SETTINGS_PROJECTION_MAP;

    static final int GOALS = 1;
    static final int GOAL_ID = 2;
    static final int RECORDS = 3;
    static final int RECORD_ID = 4;
    static final int SETTINGS = 5;
    static final int SETTINGS_ID = 6;

    static final UriMatcher uriMatcher;
    static{
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, "goals", GOALS);
        uriMatcher.addURI(PROVIDER_NAME, "goals/#", GOAL_ID);
        uriMatcher.addURI(PROVIDER_NAME, "records", RECORDS);
        uriMatcher.addURI(PROVIDER_NAME, "records/#", RECORD_ID);
        uriMatcher.addURI(PROVIDER_NAME, "settings", SETTINGS);
        uriMatcher.addURI(PROVIDER_NAME, "settings/#", SETTINGS_ID);
    }


    /** Database specific constant declarations */
    private SQLiteDatabase db;
    static final String DATABASE_NAME = "KeepFit";
    static final String GOALS_TABLE_NAME = "Goals";
    static final String RECORDS_TABLE_NAME = "Records";
    static final String SETTINGS_TABLE_NAME = "Settings";
    static final int DATABASE_VERSION = 2;

    static final String CREATE_DB_R_TABLE = " CREATE TABLE " + RECORDS_TABLE_NAME +
                    " (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    " steps TEXT NOT NULL, " +
                    " date TEXT NOT NULL, " +
                    " datenum INTEGER NOT NULL, " +
                    " successful TEXT NOT NULL, " +
                    " goalotd TEXT NOT NULL, " +
                    " recunit TEXT NOT NULL);";

    static final String CREATE_DB_G_TABLE = " CREATE TABLE " + GOALS_TABLE_NAME +
                    " (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    " name TEXT NOT NULL, " +
                    " steps TEXT NOT NULL, " +
                    " selected TEXT NOT NULL, " +
                    " units TEXT NOT NULL);";

    static final String CREATE_DB_S_TABLE = " CREATE TABLE " + SETTINGS_TABLE_NAME +
            " (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            " units TEXT NOT NULL, " +
            " notifications TEXT NOT NULL, " +
            " mode TEXT NOT NULL, " +
            " editgoal TEXT NOT NULL);";

    /** Helper class that actually creates and manages the provider's underlying data repository. */

    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context){
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_DB_R_TABLE);
            db.execSQL(CREATE_DB_G_TABLE);
            db.execSQL(CREATE_DB_S_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " +  RECORDS_TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " +  GOALS_TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " +  SETTINGS_TABLE_NAME);
            onCreate(db);
        }
    }

    @Override
    public boolean onCreate() {
        Context context = getContext();
        DatabaseHelper dbHelper = new DatabaseHelper(context);

        /**
         * Create a write able database which will trigger its
         * creation if it doesn't already exist.
         */

        db = dbHelper.getWritableDatabase();
        return (db == null)? false:true;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Uri _uri = null;
        long rowID;
        switch (uriMatcher.match(uri)) {
            case RECORDS:
                rowID = db.insert(RECORDS_TABLE_NAME, "", values);
                /** If record is added successfully */
                if (rowID > 0) {
                    _uri = ContentUris.withAppendedId(CONTENT_URI_R, rowID);
                    getContext().getContentResolver().notifyChange(_uri, null);
                }
                break;
            case GOALS:
                rowID = db.insert(GOALS_TABLE_NAME, "", values);
                /** If record is added successfully */
                if (rowID > 0) {
                    _uri = ContentUris.withAppendedId(CONTENT_URI_G, rowID);
                    getContext().getContentResolver().notifyChange(_uri, null);
                }
                break;
            case SETTINGS:
                rowID = db.insert(SETTINGS_TABLE_NAME, "", values);
                /** If record is added successfully */
                if (rowID > 0) {
                    _uri = ContentUris.withAppendedId(CONTENT_URI_S, rowID);
                    getContext().getContentResolver().notifyChange(_uri, null);
                }
                break;
            default: throw new SQLException("Failed to insert row into " + uri);
        }
        return _uri;
    }


    /** Projection:     The list of columns to put into the cursor. // Wh
     * Selection:       A selection criteria to apply when filtering rows.
     * SelectionArgs:   You may include ?s in selection, which will be replaced by the values from
     *                  selectionArgs, in order that they appear in the selection.
     *                  The values will be bound as Strings.
     * */


    @Override
    public Cursor query(Uri uri, String[] projection,
                        String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();


        switch (uriMatcher.match(uri)) {
            case RECORDS:
                qb.setTables(RECORDS_TABLE_NAME);
                qb.setProjectionMap(RECORD_PROJECTION_MAP);
                break;

            case RECORD_ID:
                qb.setTables(RECORDS_TABLE_NAME);
                qb.appendWhere( R_ID + "=" + uri.getPathSegments().get(1));
                break;
            case GOALS:
                qb.setTables(GOALS_TABLE_NAME);
                qb.setProjectionMap(GOAL_PROJECTION_MAP);
                break;

            case GOAL_ID:
                qb.setTables(GOALS_TABLE_NAME);
                qb.appendWhere( G_ID + "=" + uri.getPathSegments().get(1));
                break;
            case SETTINGS:
                qb.setTables(SETTINGS_TABLE_NAME);
                qb.setProjectionMap(SETTINGS_PROJECTION_MAP);
                break;

            case SETTINGS_ID:
                qb.setTables(SETTINGS_TABLE_NAME);
                qb.appendWhere( S_ID + "=" + uri.getPathSegments().get(1));
                break;

            default:
        }

        if (sortOrder == null || sortOrder == ""){

            switch (uriMatcher.match(uri)) {
                case RECORDS:
                    sortOrder = DATE;
                    break;
                case GOALS:
                    sortOrder = G_NAME;
                    break;
                case SETTINGS:
                    sortOrder = S_ID;
                    break;
            }
        }

        Cursor c = qb.query(db,	projection,	selection,
                selectionArgs,null, null, sortOrder);


        /** register to watch a content URI for changes */
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }




    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int count = 0;
        switch (uriMatcher.match(uri)){
            case RECORDS:
                count = db.delete(RECORDS_TABLE_NAME, selection, selectionArgs);
                break;

            case RECORD_ID:
                String r_id = uri.getPathSegments().get(1);
                count = db.delete( RECORDS_TABLE_NAME, R_ID +  " = " + r_id + (!TextUtils.isEmpty(selection) ?  " AND (" + selection + ')' : ""), selectionArgs);
                break;


            case GOALS:
                count = db.delete(GOALS_TABLE_NAME, selection, selectionArgs);
                break;

            case GOAL_ID:
                String g_id = uri.getPathSegments().get(1);
                count = db.delete( GOALS_TABLE_NAME, G_ID +  " = " + g_id + (!TextUtils.isEmpty(selection) ?  " AND (" + selection + ')' : ""), selectionArgs);
                break;

            case SETTINGS:
                count = db.delete(SETTINGS_TABLE_NAME, selection, selectionArgs);
                break;

            case SETTINGS_ID:
                String s_id = uri.getPathSegments().get(1);
                count = db.delete( SETTINGS_TABLE_NAME, G_ID +  " = " + s_id + (!TextUtils.isEmpty(selection) ?  " AND (" + selection + ')' : ""), selectionArgs);
                break;

            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }


    @Override
    public int update(Uri uri, ContentValues values,
                      String selection, String[] selectionArgs) {
        int count = 0;
        switch (uriMatcher.match(uri)) {
            case RECORDS:
                count = db.update(RECORDS_TABLE_NAME, values, selection, selectionArgs);
                break;

            case RECORD_ID:
                count = db.update(RECORDS_TABLE_NAME, values, R_ID + " = " + uri.getPathSegments().get(1) + (!TextUtils.isEmpty(selection) ? "AND (" +selection + ')' : ""), selectionArgs);
                break;

            case GOALS:
                count = db.update(GOALS_TABLE_NAME, values, selection, selectionArgs);
                break;

            case GOAL_ID:
                count = db.update(GOALS_TABLE_NAME, values, G_ID + " = " + uri.getPathSegments().get(1) + (!TextUtils.isEmpty(selection) ? "AND (" +selection + ')' : ""), selectionArgs);
                break;

            case SETTINGS:
                count = db.update(SETTINGS_TABLE_NAME, values, selection, selectionArgs);
                break;

            case SETTINGS_ID:
                count = db.update(SETTINGS_TABLE_NAME, values, S_ID + " = " + uri.getPathSegments().get(1) + (!TextUtils.isEmpty(selection) ? "AND (" +selection + ')' : ""), selectionArgs);
                break;


            default:
                throw new IllegalArgumentException("Unknown URI " + uri );
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)){

            case RECORDS:
                return "vnd.android.cursor.dir/vnd.example.records";

            case RECORD_ID:
                return "vnd.android.cursor.item/vnd.example.records";

            case GOALS:
                return "vnd.android.cursor.dir/vnd.example.goals";

            case GOAL_ID:
                return "vnd.android.cursor.item/vnd.example.goals";

            case SETTINGS:
                return "vnd.android.cursor.dir/vnd.example.settings";

            case SETTINGS_ID:
                return "vnd.android.cursor.item/vnd.example.settings";


            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

}