package com.example.tinyowlsampleapp.database;

import android.database.Cursor;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by vihaan on 20/12/14.
 */
public abstract  class Manager {

    public abstract void saveToDb(JSONObject jsonObject) throws JSONException;

    public abstract Cursor getCursor();
}
