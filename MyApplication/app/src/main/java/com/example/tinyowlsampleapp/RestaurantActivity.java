package com.example.tinyowlsampleapp;

import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tinyowlsampleapp.adapters.RestaurantsItemsAdapter;
import com.example.tinyowlsampleapp.async.BootStrapAsync;
import com.example.tinyowlsampleapp.database.CartManager;
import com.example.tinyowlsampleapp.interfaces.CartDataLoader;

import java.io.InputStream;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;


public class RestaurantActivity extends ActionBarActivity implements CartDataLoader, View.OnClickListener {

    private ListView mListView;
    private RestaurantsItemsAdapter mListAdapter;

    private TextView mCartItemTextsView, mTotalAmountTextView;
    private LinearLayout mCartContainerLL;

    private CartManager mCartManager;
    private BootStrapAsync mBootStrapAsync;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant);
        mListView = (ListView)findViewById(R.id.itemsListView);

        mCartItemTextsView = (TextView)findViewById(R.id.cartItemsTextView);
        mTotalAmountTextView = (TextView)findViewById(R.id.totalAmountTextView);

        mCartContainerLL = (LinearLayout)findViewById(R.id.cartContainerLL);
        mCartContainerLL.setOnClickListener(this);
        mCartManager = new CartManager();
        mCartManager.clearTable();
        bootStrap();
    }



    private void bootStrap()
    {
            mBootStrapAsync = new BootStrapAsync(this);
            mBootStrapAsync.execute(new String[]{getItemsString()});
    }

    private String getItemsString()
    {
        byte[] bytes = null;
        try {
            Resources res = getResources();
            InputStream in_s = res.openRawResource(R.raw.items);

            bytes = new byte[in_s.available()];
            in_s.read(bytes);
        } catch (Exception e) {
            Crouton.makeText(this, "Error reading items file", Style.ALERT).show();
        }
        return new String(bytes);
    }


    @Override
    public void onSuccess(Cursor cursor) {
        if(!isFinishing())
        {
            mListAdapter = new RestaurantsItemsAdapter(this, R.layout.list_item_restaurant, cursor, new String[]{}, new int[]{}, 0);
            mListView.setAdapter(mListAdapter);
        }
    }

    @Override
    public void onFailure() {
        if(!isFinishing())
        {
            Crouton.makeText(this, "Something went wrong", Style.ALERT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateCart();
    }

    @Override
    public void updateCart() {
        int cartCount = 0;
        try
        {
            cartCount = Integer.parseInt(mCartManager.getCartCount());
        }
        catch (Exception e)
        {
            Log.i("RestaurantActivity::updateCart ", e.getMessage());
        }
        if(cartCount > 0)
        {
            mCartItemTextsView.setText("Cart(" + mCartManager.getCartCount() + " items)");
            mTotalAmountTextView.setText(getResources().getString(R.string.rs) + " " + mCartManager.getCartTotal());
        }
        else
        {
            mCartItemTextsView.setText("Cart(0 items)");
            mTotalAmountTextView.setText(getResources().getString(R.string.rs) + "  0");
        }

        AlphaAnimation animation1 = new AlphaAnimation(0.2f, 1.0f);
        animation1.setDuration(1000);
//        animation1.setStartOffset(5000);
        animation1.setFillAfter(true);
        mCartItemTextsView.startAnimation(animation1);
        mTotalAmountTextView.startAnimation(animation1);

    }


    @Override
    protected void onDestroy() {
        if(mBootStrapAsync != null)
        {
            mBootStrapAsync.cancel(true);
        }

        Crouton.cancelAllCroutons();

        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.cartContainerLL:
                Intent intent = new Intent(this, CartActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_up,R.anim.slide_down);

                break;
        }
    }
}
