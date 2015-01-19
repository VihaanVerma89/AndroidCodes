package com.example.justunfollowsampleapp.fragments;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.justunfollowsampleapp.Favorites;
import com.example.justunfollowsampleapp.FavoritesManager;
import com.example.justunfollowsampleapp.R;
import com.example.justunfollowsampleapp.adapters.PagerAdapter;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by vihaan on 19/1/15.
 */
public class PhotoContainerFragment extends Fragment  implements View.OnClickListener{

    private ViewPager mUpperViewPager, mLowerViewPager;
//    private PhotoFragment mUpperImageFragment, mLowerImageFragment;
    private ArrayList<Fragment> mUpperImageFragments, mLowerImageFragments;
    private PagerAdapter mUpperPagerAdapter, mLowerPagerAdapter;

    private ImageView mFavImageView;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_photo_container, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mFavImageView = (ImageView)view.findViewById(R.id.favImageView);
        mFavImageView.setOnClickListener(this);

        addFragments();
        setupViewPagers(view);
    }


    private void addFragments()
    {
        mUpperImageFragments = new ArrayList<Fragment>();
        mLowerImageFragments = new ArrayList<Fragment>();

        for(int i=0; i < 5 ; i++)
        {
            mUpperImageFragments.add(new PhotoFragment());
            mLowerImageFragments.add(new PhotoFragment());
        }
    }

    private void setupViewPagers(View view)
    {
        mUpperViewPager = (ViewPager)view.findViewById(R.id.upperViewPager);
        mLowerViewPager = (ViewPager)view.findViewById(R.id.lowerViewPager);


        mUpperPagerAdapter = new PagerAdapter(getActivity().getSupportFragmentManager(), mUpperImageFragments);
        mLowerPagerAdapter = new PagerAdapter(getActivity().getSupportFragmentManager(), mLowerImageFragments);

        mUpperViewPager.setAdapter(mUpperPagerAdapter);
        mLowerViewPager.setAdapter(mLowerPagerAdapter);

        mUpperViewPager.setOffscreenPageLimit(4);
        mLowerViewPager.setOffscreenPageLimit(4);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        List<Fragment> fragments = getChildFragmentManager().getFragments();
        if (fragments != null) {
            for (Fragment fragment : fragments) {
                fragment.onActivityResult(requestCode, resultCode, data);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.favImageView:
                if(imagesArePresent())
                {
                    addFavorites();
                    Toast.makeText(getActivity(), getString(R.string.added_to_fav), Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(getActivity(), getString(R.string.select_at_least_two_images), Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    private void addFavorites()
    {
        PhotoFragment upperPhotoFragment = (PhotoFragment)getActiveFragment(mUpperViewPager, mUpperViewPager.getCurrentItem());
        PhotoFragment lowerPhotoFragment = (PhotoFragment)getActiveFragment(mLowerViewPager, mLowerViewPager.getCurrentItem());

        Favorites favorites = new Favorites(upperPhotoFragment.getImageUri(), lowerPhotoFragment.getImageUri());
        FavoritesManager.getInstance().addFavorites(favorites);
    }

    public Fragment getActiveFragment(ViewPager container, int position) {
        String name = makeFragmentName(container.getId(), position);
        return  getActivity().getSupportFragmentManager().findFragmentByTag(name);
    }

    private static String makeFragmentName(int viewId, int index) {
        return "android:switcher:" + viewId + ":" + index;
    }

    private boolean imagesArePresent()
    {
        return upperImagePresent() && lowerImagePresent() ? true: false;
    }

    private boolean upperImagePresent()
    {
        PhotoFragment photoFragment = (PhotoFragment)getActiveFragment(mUpperViewPager, mUpperViewPager.getCurrentItem());

        return photoFragment.isImagePresent()?true:false;
    }

    private boolean lowerImagePresent()
    {
        PhotoFragment photoFragment = (PhotoFragment)getActiveFragment(mLowerViewPager, mLowerViewPager.getCurrentItem());

        return photoFragment.isImagePresent()?true:false;
    }

}
