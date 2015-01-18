package com.example.tinyowlsampleapp.interfaces;

import android.database.Cursor;

/**
 * Created by vihaan on 20/12/14.
 */
public interface CartDataLoader {

    public void onSuccess(Cursor cursor);

    public void onFailure();

    public void updateCart();

}
