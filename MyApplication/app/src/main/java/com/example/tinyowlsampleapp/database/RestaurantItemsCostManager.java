package com.example.tinyowlsampleapp.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by vihaan on 20/12/14.
 */

public class RestaurantItemsCostManager {
    public static final String mTableName = Tables.RestaurantItemsCost.TABLE_NAME;

    private final DatabaseHelper mDatabaseHelper;
    private final SQLiteDatabase mSqLiteDatabase;

    public RestaurantItemsCostManager() {
        mDatabaseHelper = DatabaseHelper.getInstance();
        mSqLiteDatabase = mDatabaseHelper.getSqliteDatabase();
    }

    public void saveToDb(String itemId, JSONObject jsonObject) throws JSONException {
        ContentValues values = null;
        JSONArray sizesJsonArray = jsonObject.getJSONArray(Tables.RestaurantItemsCost.SIZE);
        JSONArray costsJsonArray = jsonObject.getJSONArray(Tables.RestaurantItemsCost.COST);

        JSONObject itemJsonObject = null;

        String size, cost;
        for (int i = 0; i < sizesJsonArray.length(); i++) {

            size = sizesJsonArray.getString(i);
            cost = costsJsonArray.getString(i);

            values = new ContentValues();

            values.put(Tables.RestaurantItemsCost.ITEM_ID, itemId);
            values.put(Tables.RestaurantItemsCost.SIZE, size);
            values.put(Tables.RestaurantItemsCost.COST, cost);


            mSqLiteDatabase.insert(mTableName, null, values);
        }
    }

    public Cursor getCursor(String itemID) {
        Cursor cursor = null;
        cursor = mSqLiteDatabase.rawQuery("select  * from " + Tables.RestaurantItemsCost.TABLE_NAME + " where " + Tables.RestaurantItemsCost.ITEM_ID + " = " + itemID + ";", null);
        return cursor;

    }

    public void clearTable() {
        mSqLiteDatabase.delete(mTableName, null, null);
    }

}
