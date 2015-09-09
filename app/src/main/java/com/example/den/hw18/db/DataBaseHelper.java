package com.example.den.hw18.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.provider.BaseColumns;
import android.util.Log;

import com.example.den.hw18.models.MarkersModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Den on 08.09.15.
 */
public class DataBaseHelper extends SQLiteOpenHelper implements BaseColumns {
    private static final String DATABASE_NAME = "database.db";
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_TABLE = "markers";

    public static final String COLUMN_ID = BaseColumns._ID;
    public static final String COLUMN_LATITUDE = "latitude";
    public static final String COLUMN_LONGITUDE = "longitude";
    public static final String COLUMN_TEXT = "text";
    public static final String COLUMN_URI = "uri";



    private static final String DATABASE_CREATE = "create table "
            + DATABASE_TABLE + " (" + BaseColumns._ID
            + " integer primary key autoincrement, " + COLUMN_LATITUDE
            + " text not null, " + COLUMN_LONGITUDE + " text not null, " + COLUMN_TEXT
            + " text not null, " + COLUMN_URI + "  uri);";

    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public DataBaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public DataBaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w("SQLite", "Обновляемся с версии " + oldVersion + " на версию " + newVersion);

        db.execSQL("DROP TABLE IF IT EXISTS " + DATABASE_TABLE);
        onCreate(db);
    }

    public void addNewMarker(String latitude, String longitude, String text, Uri uri) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_LATITUDE, latitude);
        values.put(COLUMN_LONGITUDE, longitude);
        values.put(COLUMN_TEXT, text);
        values.put(COLUMN_URI, String.valueOf(uri));

        db.insert(DATABASE_TABLE, null, values);
        db.close();
    }

    public List<MarkersModel> getAllMarkers() {
        List<MarkersModel> markerList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + DATABASE_TABLE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                MarkersModel markersModel = new MarkersModel();

                markersModel.setId(cursor.getString(0));
                markersModel.setLatitude(cursor.getString(1));
                markersModel.setLongitude(cursor.getString(2));
                markersModel.setText(cursor.getString(3));
                markersModel.setUri(Uri.parse(cursor.getString(4)));

                markerList.add(markersModel);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return markerList;
    }
}
