package com.example.songsbrowser;

import java.util.Formatter;
import java.util.Locale;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

public class MusicUtils 
{

    public static Cursor query(Context context, Uri uri, String[] projection, String selection,
    		String[] selectionArgs, String sortOrder) 
    {
        return query(context, uri, projection, selection, selectionArgs, sortOrder, 0);
    }
    
    public static Cursor query(Context context, Uri uri, String[] projection,
            String selection, String[] selectionArgs, String sortOrder, int limit) 
    {
        try 
        {
            ContentResolver resolver = context.getContentResolver();
            if (resolver == null) 
            {
                return null;
            }
            if (limit > 0) 
            {
                uri = uri.buildUpon().appendQueryParameter("limit", "" + limit).build();
            }
            return resolver.query(uri, projection, selection, selectionArgs, sortOrder);
        } 
        catch (UnsupportedOperationException ex) 
        {
            return null;
        }
    }
    
    private static StringBuilder sFormatBuilder = new StringBuilder();
    private static Formatter sFormatter = new Formatter(sFormatBuilder, Locale.getDefault());
    private static final Object[] sTimeArgs = new Object[5];

    public static String makeTimeString(Context context, long secs) 
    {
        String durationformat = context.getString(
                secs < 3600 ? R.string.durationformatshort : R.string.durationformatlong);
        
        /* Provide multiple arguments so the format can be changed easily
         * by modifying the xml.
         */
        sFormatBuilder.setLength(0);

        final Object[] timeArgs = sTimeArgs;
        timeArgs[0] = secs / 3600;
        timeArgs[1] = secs / 60;
        timeArgs[2] = (secs / 60) % 60;
        timeArgs[3] = secs;
        timeArgs[4] = secs % 60;

        return sFormatter.format(durationformat, timeArgs).toString();
    }
}
