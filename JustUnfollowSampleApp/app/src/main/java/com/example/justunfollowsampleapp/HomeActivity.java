package com.example.justunfollowsampleapp;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.justunfollowsampleapp.fragments.FavoriteContainerFragment;
import com.example.justunfollowsampleapp.fragments.PhotoContainerFragment;

import java.util.ArrayList;


public class HomeActivity extends ActionBarActivity {
    protected static final int NAVDRAWER_ITEM_INVALID = -1;
    protected static final int NAVDRAWER_ITEM_PHOTOS = 0;
    protected static final int NAVDRAWER_ITEM_FAV = 1   ;

    private static final int[] NAVDRAWER_TITLE_RES_ID = new int[]{
            R.string.tab_photos,
            R.string.tab_fav};

    private static final int[] NAVDRAWER_ICON_RES_ID = new int[]{
            R.drawable.ic_insert_photo_grey600_24dp,
            R.drawable.ic_favorite_grey600_24dp};

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    private ArrayList<Integer> mNavDrawerItems = new ArrayList<Integer>();

    private ViewGroup mDrawerItemsListContainer;
    private View[] mNavDrawerItemViews = null;

    private int mCurrentTab = -1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        setupNavigationDrawer();
        displayTab(NAVDRAWER_ITEM_PHOTOS);
        setSelectedNavDrawerItem(NAVDRAWER_ITEM_PHOTOS);

    }

    private void setupNavigationDrawer()
    {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close);
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        mNavDrawerItems.add(NAVDRAWER_ITEM_PHOTOS);
        mNavDrawerItems.add(NAVDRAWER_ITEM_FAV);

        mDrawerItemsListContainer = (ViewGroup) findViewById(R.id.navdrawer_items_list);

        mNavDrawerItemViews = new View[mNavDrawerItems.size()];

        mDrawerItemsListContainer.removeAllViews();

        int i = 0;
        for (int itemId : mNavDrawerItems) {
            mNavDrawerItemViews[i] = makeNavDrawerItem(itemId, mDrawerItemsListContainer);
            mDrawerItemsListContainer.addView(mNavDrawerItemViews[i]);
            ++i;
        }


    }

    private View makeNavDrawerItem(final int itemId, ViewGroup container) {


        boolean selected = getSelfNavDrawerItem() == itemId;

        View view = getLayoutInflater().inflate(R.layout.navdrawer_item, container, false);



        ImageView iconView = (ImageView) view.findViewById(R.id.icon);
        TextView titleView = (TextView) view.findViewById(R.id.title);
        int iconId = itemId >= 0 && itemId < NAVDRAWER_ICON_RES_ID.length ?
                NAVDRAWER_ICON_RES_ID[itemId] : 0;
        int titleId = itemId >= 0 && itemId < NAVDRAWER_TITLE_RES_ID.length ?
                NAVDRAWER_TITLE_RES_ID[itemId] : 0;

        // set icon and text
        iconView.setVisibility(iconId > 0 ? View.VISIBLE : View.GONE);
        if (iconId > 0) {
            iconView.setImageResource(iconId);
        }
        titleView.setText(getString(titleId));

        formatNavDrawerItem(view, itemId, selected);


        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onNavDrawerItemClicked(itemId);
            }
        });

        return view;
    }

    protected int getSelfNavDrawerItem() {
        return NAVDRAWER_ITEM_INVALID;
    }

    private void formatNavDrawerItem(View view, int itemId, boolean selected) {


        ImageView iconView = (ImageView) view.findViewById(R.id.icon);
        TextView titleView = (TextView) view.findViewById(R.id.title);

        // configure its appearance according to whether or not it's selected
        titleView.setTextColor(selected ?
                getResources().getColor(R.color.navdrawer_text_color_selected) :
                getResources().getColor(R.color.navdrawer_text_color));
        iconView.setColorFilter(selected ?
                getResources().getColor(R.color.navdrawer_icon_tint_selected) :
                getResources().getColor(R.color.navdrawer_icon_tint));
    }

    protected void onNavDrawerItemClicked(final int itemId) {
        mDrawerLayout.closeDrawer(Gravity.START);
        setSelectedNavDrawerItem(itemId);
        displayTab(itemId);

    }

    private void setSelectedNavDrawerItem(int itemId) {
        if (mNavDrawerItemViews != null) {
            for (int i = 0; i < mNavDrawerItemViews.length; i++) {
                if (i < mNavDrawerItems.size()) {
                    int thisItemId = mNavDrawerItems.get(i);
                    formatNavDrawerItem(mNavDrawerItemViews[i], thisItemId, itemId == thisItemId);
                }
            }
        }
    }


    private void displayTab(int position) {
        Fragment fragment;
        if (mCurrentTab != position) {
            switch (position) {
                case NAVDRAWER_ITEM_PHOTOS:
                    fragment = new PhotoContainerFragment();
                    replaceFragment(fragment);
                    break;

                case NAVDRAWER_ITEM_FAV:
                    fragment = new FavoriteContainerFragment();
                    replaceFragment(fragment);
                    break;
            }
        }
    }

    private void replaceFragment(Fragment fragment)
    {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.container, fragment);
        ft.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
