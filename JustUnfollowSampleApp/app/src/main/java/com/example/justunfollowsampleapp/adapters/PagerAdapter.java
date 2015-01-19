package com.example.justunfollowsampleapp.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class PagerAdapter extends FragmentPagerAdapter {

    private ArrayList<Fragment> mFragments;
    private Fragment mFragment = null;

    public PagerAdapter(android.support.v4.app.FragmentManager fm,
                        ArrayList<Fragment> fragments) {
        super(fm);
        mFragments = fragments;
    }


    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public Fragment getItem(int position) {
        mFragment = mFragments.get(position);
        return mFragment;
    }
}