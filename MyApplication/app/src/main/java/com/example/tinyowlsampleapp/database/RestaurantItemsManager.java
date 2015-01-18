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
public class RestaurantItemsManager extends Manager {

    public static final String mTableName = Tables.RestaurantItems.TABLE_NAME;

    private final DatabaseHelper mDatabaseHelper;
    private final SQLiteDatabase mSqLiteDatabase;
    private RestaurantItemsCostManager mRestaurantItemsCostManager;


    public RestaurantItemsManager() {
        mDatabaseHelper = DatabaseHelper.getInstance();
        mSqLiteDatabase = mDatabaseHelper.getSqliteDatabase();
        mRestaurantItemsCostManager = new RestaurantItemsCostManager();
    }

    @Override
    public void saveToDb(JSONObject jsonObject) throws JSONException {
        ContentValues values = null;
        JSONArray itemsJsonArray = jsonObject.getJSONArray(Tables.RestaurantItems.ITEMS);

        JSONObject itemJsonObject = null;

        for (int i = 0; i < itemsJsonArray.length(); i++) {
            itemJsonObject = (JSONObject) itemsJsonArray.get(i);

            values = new ContentValues();

            values.put(Tables.RestaurantItems.NAME, itemJsonObject.getString(Tables.RestaurantItems.NAME));
            values.put(Tables.RestaurantItems.TYPE, itemJsonObject.getString(Tables.RestaurantItems.TYPE));

            long itemId = mSqLiteDatabase.insert(mTableName, null, values);
            mRestaurantItemsCostManager.saveToDb(itemId + "" , itemJsonObject);
        }
    }


    @Override
    public Cursor getCursor() {
        Cursor cursor = null;
//        cursor = mSqLiteDatabase.rawQuery("select r.rowid as _id ,* from " + Tables.RestaurantItems.TABLE_NAME
//                +" r INNER join  "
//                + Tables.RestaurantItemsCost.TABLE_NAME
//                + " c on r.rowid=c." + Tables.RestaurantItemsCost.ITEM_ID+ " ;", null);
        cursor = mSqLiteDatabase.rawQuery("select rowid _id, * from " + Tables.RestaurantItems.TABLE_NAME + ";", null);
        return cursor;
    }

    public void clearTable()
    {
        mSqLiteDatabase.delete(mTableName, null, null);
        mRestaurantItemsCostManager.clearTable();
    }


}
