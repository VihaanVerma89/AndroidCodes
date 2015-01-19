package com.example.justunfollowsampleapp;

import android.net.Uri;

import java.util.ArrayList;

/**
 * Created by vihaan on 19/1/15.
 */
public class Favorites {
    private Uri mUpperImageFavUri;
    private Uri mLowerImageFavUri;

    public Favorites(Uri upperUri, Uri lowerUri)
    {
        mUpperImageFavUri = upperUri;
        mLowerImageFavUri = lowerUri;
    }

    public Uri getUpperImageUri()
    {
        return mUpperImageFavUri;
    }

    public Uri getLowerImageUri()
    {
        return mLowerImageFavUri;
    }
}
