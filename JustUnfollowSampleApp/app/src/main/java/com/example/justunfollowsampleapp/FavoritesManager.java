package com.example.justunfollowsampleapp;

import java.util.ArrayList;

/**
 * Created by vihaan on 19/1/15.
 */
public class FavoritesManager {
    private ArrayList<Favorites> mFavorites = new ArrayList<Favorites>();

    private static FavoritesManager favoritesManager = new FavoritesManager();

    private FavoritesManager()
    {
        mFavorites = new ArrayList<Favorites>();
    }

    public static FavoritesManager getInstance() {
        return favoritesManager;
    }

    public void addFavorites(Favorites favorites)
    {
        mFavorites.add(favorites);
    }

    public Favorites getFavorites(int position)
    {
       return  mFavorites.get(position);
    }

    public int getFavoritesCount()
    {
        return mFavorites.size();
    }
}
