package com.example.tinyowlsampleapp.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by vihaan on 21/12/14.
 */
public class CartManager {

    public static final String mTableName = Tables.Cart.TABLE_NAME;

    private final DatabaseHelper mDatabaseHelper;
    private final SQLiteDatabase mSqLiteDatabase;

    public CartManager() {
        mDatabaseHelper = DatabaseHelper.getInstance();
        mSqLiteDatabase = mDatabaseHelper.getSqliteDatabase();
    }

    public void addToCart(String itemId, String name, String type, String size, String cost, String quantity) {

        if(presentInCart(itemId, cost))
        {
            increaseQuantity(itemId, cost);
        }
        else
        {
            insertNewItem(itemId, name, type, size, cost, quantity);
        }
    }

    public boolean presentInCart(String itemId, String cost)
    {
        boolean present = false;
        Cursor cursor = null;

        cursor = getUniqueItem(itemId, cost);

        if(cursor.getCount() > 0)
        {
            present = true;
        }

        return present;
    }

    public void insertNewItem(String itemId, String name , String type , String size, String cost, String quantity) {
        ContentValues values = null;
        values = new ContentValues();

        values.put(Tables.Cart.ITEM_ID, itemId);
        values.put(Tables.Cart.NAME, name);
        values.put(Tables.Cart.TYPE, type);
        values.put(Tables.Cart.SIZE, size);
        values.put(Tables.Cart.COST, cost);
        values.put(Tables.Cart.QUANTITY, quantity);

        mSqLiteDatabase.insert(mTableName, null, values);
    }


    public Cursor getUniqueItem(String itemId, String cost)
    {
        Cursor cursor = null;
        cursor = mSqLiteDatabase.rawQuery("select * from " + Tables.Cart.TABLE_NAME
                + " where " + Tables.Cart.ITEM_ID + " = " + itemId
                + " and " + Tables.Cart.COST + " = " + cost
                , null);
        return cursor;
    }


    public void increaseQuantity(String itemId, String cost)
    {

        ContentValues values = new ContentValues();
        String quantity = getQuantity(itemId, cost);
        int temp = Integer.parseInt(quantity);
        values.put(Tables.Cart.QUANTITY, ++temp);

        mSqLiteDatabase.update(Tables.Cart.TABLE_NAME, values, Tables.Cart.ITEM_ID +"="+ itemId + " and " + Tables.Cart.COST + " = " + cost, null);
    }

    public void decreseQuantity(String itemId, String cost)
    {
        ContentValues values = new ContentValues();
        String quantity = getQuantity(itemId, cost);
        int temp = Integer.parseInt(quantity);
        if(temp > 0)
        {
            values.put(Tables.Cart.QUANTITY, --temp);
            mSqLiteDatabase.update(Tables.Cart.TABLE_NAME, values, Tables.Cart.ITEM_ID +"="+ itemId, null);
        }
    }

    public String getQuantity(String itemId, String cost)
    {
        Cursor cursor = getUniqueItem(itemId, cost);
        cursor.moveToFirst();
        String quantity = cursor.getString(cursor.getColumnIndex(Tables.Cart.QUANTITY));
        cursor.close();
        return quantity;
    }

    public void deleteItem(String itemId)
    {
        mSqLiteDatabase.execSQL("delete from " + Tables.Cart.TABLE_NAME + " where " + Tables.Cart.ITEM_ID + " = " + itemId +" ;" , null);
    }

    public String getCartCount()
    {
        Cursor cursor = mSqLiteDatabase.rawQuery("select sum("+ Tables.Cart.QUANTITY + ") from " + Tables.Cart.TABLE_NAME, null);
        cursor.moveToFirst();
        String cartCount = cursor.getString(0);
        cursor.close();
        return cartCount;
    }

    public String getCartTotal()
    {
        Cursor cursor = mSqLiteDatabase.rawQuery("select sum("+ Tables.Cart.COST + " * " + Tables.Cart.QUANTITY + " ) from "+ Tables.Cart.TABLE_NAME , null);
        cursor.moveToFirst();
        String cartTotal = cursor.getString(0);
        cursor.close();
        return cartTotal;
    }

    public void clearTable()
    {
        mSqLiteDatabase.delete(mTableName, null, null);
    }


    public Cursor getCursor() {
        Cursor cursor = null;
        cursor = mSqLiteDatabase.rawQuery("select rowid _id, * from " + Tables.Cart.TABLE_NAME + ";", null);
        return cursor;
    }

    public void removeZeroQuantityItem()
    {
        mSqLiteDatabase.delete( mTableName, Tables.Cart.QUANTITY + "= 0 " , null);
    }


}
