package com.example.tinyowlsampleapp.async;

import android.database.Cursor;
import android.os.AsyncTask;

import com.example.tinyowlsampleapp.database.RestaurantItemsManager;
import com.example.tinyowlsampleapp.interfaces.CartDataLoader;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by vihaan on 20/12/14.
 */
public class BootStrapAsync extends AsyncTask<String, Void, Cursor> {

    private boolean mSuccess = true;
    private CartDataLoader mDataLoader;

    private RestaurantItemsManager mManager;

    public BootStrapAsync(CartDataLoader dataLoader)
    {
        mManager = new RestaurantItemsManager();
        mDataLoader = dataLoader;
    }

    @Override
    protected Cursor doInBackground(String... params) {

        Cursor cursor = null;
        String items = params[0];

        try
        {
            mManager.clearTable();
            mManager.saveToDb(new JSONObject(items));
            cursor = mManager.getCursor();
        } catch (JSONException e) {
            mSuccess = false;
            e.printStackTrace();
        }


        return cursor;

    }

    @Override
    protected void onPostExecute(Cursor cursor) {
        super.onPostExecute(cursor);
        if(mSuccess)
        {
            mDataLoader.onSuccess(cursor);
        }
        else
        {
            mDataLoader.onFailure();
        }
    }
}
