package com.example.tinyowlsampleapp;

import android.database.Cursor;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.example.tinyowlsampleapp.adapters.CartAdapter;
import com.example.tinyowlsampleapp.database.CartManager;

import org.apache.http.conn.params.ConnRoutePNames;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;


public class CartActivity extends ActionBarActivity {

    private ListView mCartListView;
    private CartAdapter mCartAdapter;

    private CartManager mCartManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        mCartManager = new CartManager();
        mCartManager.removeZeroQuantityItem();

        initViews();
    }

    private void initViews()
    {
        mCartListView = (ListView)findViewById(R.id.cartListView);
        Cursor cursor = mCartManager.getCursor();
        if(cursor.getCount() > 0)
        {
            mCartAdapter = new CartAdapter(this,R.layout.list_item_cart, cursor, new String[] {} , new int []{}, 0);
            mCartListView.setAdapter(mCartAdapter);
        }
        else
        {
            Crouton.makeText(this, "No items in cart.", Style.INFO).show();
        }

    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
    }

    @Override
    protected void onDestroy() {
        Crouton.cancelAllCroutons();
        super.onDestroy();
    }
}
