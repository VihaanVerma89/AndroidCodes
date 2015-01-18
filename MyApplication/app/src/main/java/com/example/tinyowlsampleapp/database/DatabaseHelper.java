package com.example.tinyowlsampleapp.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.tinyowlsampleapp.RestaurantsApplication;

/**
 * Created by vihaan on 20/12/14.
 */
public class DatabaseHelper  extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "restaurant.db";
    private static final int DATABASE_VERSION = 1;
    private SQLiteDatabase mSqliteDatabase;

    private static DatabaseHelper mDatabaseHelper = new DatabaseHelper(
            RestaurantsApplication.getAppContext());

    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static DatabaseHelper getInstance() {
        return mDatabaseHelper;
    }

    public SQLiteDatabase getSqliteDatabase() {
        if (mSqliteDatabase == null) {
            mSqliteDatabase = getWritableDatabase();
        }

        return mSqliteDatabase;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Tables.RestaurantItems.SCHEMA);
        db.execSQL(Tables.RestaurantItemsCost.SCHEMA);
        db.execSQL(Tables.Cart.SCHEMA);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
