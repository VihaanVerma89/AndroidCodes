package com.example.tinyowlsampleapp;

import android.app.Application;
import android.content.Context;

/**
 * Created by vihaan on 20/12/14.
 */
public class RestaurantsApplication extends Application {
    private static Context context;

    public void onCreate() {
        super.onCreate();
        RestaurantsApplication.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return RestaurantsApplication.context;
    }
}
