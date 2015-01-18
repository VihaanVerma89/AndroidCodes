package com.example.tinyowlsampleapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.example.tinyowlsampleapp.adapters.CartAdapter;
import com.example.tinyowlsampleapp.database.CartCommandManager;
import com.example.tinyowlsampleapp.database.CartManager;

import org.apache.http.conn.params.ConnRoutePNames;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;


public class CartActivity extends ActionBarActivity {

    private ListView mCartListView;
    private CartAdapter mCartAdapter;

    private CartManager mCartManager;
    private CartCommandManager mCartCommandManager;

    private BroadcastReceiver mCartUpdateReciever = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

//                int currentPosition = mCartAdapter.getCursor().getPosition();
            Cursor cursor = mCartManager.getCursor();
            if(mCartAdapter == null)
            {
                setCursor(cursor);

            }
            else
            {
                mCartAdapter.changeCursor(cursor);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        mCartManager = new CartManager();
        mCartManager.removeZeroQuantityItem();

        mCartCommandManager = CartCommandManager.getInstance();
        initViews();

        LocalBroadcastManager.getInstance(this).registerReceiver(mCartUpdateReciever, new IntentFilter("cart_update"));
    }

    private void initViews()
    {
        mCartListView = (ListView)findViewById(R.id.cartListView);
        Cursor cursor = mCartManager.getCursor();
        setCursor(cursor);
    }

    private void setCursor(Cursor cursor)
    {
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_cart, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.undo:
                if(mCartCommandManager.canUndo())
                {
                    mCartCommandManager.undo();
                }
                else
                {
                    Crouton.makeText(this, "Nothing left to undo.", Style.INFO).show();
                }
                break;

            case R.id.redo:
                if(mCartCommandManager.canRedo())
                {
                    mCartCommandManager.redo();
                }
                else
                {
                    Crouton.makeText(this, "Nothing left to redo.", Style.INFO).show();
                }
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
    }

    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mCartUpdateReciever);

        Crouton.cancelAllCroutons();
        super.onDestroy();
    }
}
