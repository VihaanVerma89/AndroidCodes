package com.example.tinyowlsampleapp.interfaces;

import android.util.Log;
import android.support.v4.widget.SimpleCursorAdapter;

import com.example.tinyowlsampleapp.CartActions;
import com.example.tinyowlsampleapp.adapters.CartAdapter;
import com.example.tinyowlsampleapp.adapters.RestaurantsItemsAdapter;
import com.example.tinyowlsampleapp.beans.Item;
import com.example.tinyowlsampleapp.database.CartManager;

/**
 * Created by vihaan on 27/12/14.
 */
public class CartCommand implements Command {

    //    private ReloadAdapter mAdapter;
    private CartAdapter mAdapter;

    private CartManager mCartManager;
    private Item mItem;
    private String mCartAction;

    public CartCommand(CartManager cartManager, Item item, String action) {
        mCartManager = cartManager;
        mItem = item;
        mCartAction = action;
    }

    public CartCommand(CartAdapter cartAdapter, CartManager cartManager, Item item, String action)

    {
        mAdapter = cartAdapter;
        mCartManager = cartManager;
        mItem = item;
        mCartAction = action;
    }

    @Override
    public void undo() {

        if (mCartAction.equals(CartActions.increase)) {
            mCartManager.removeFromCart(mItem);
            Log.i("CartCommand", " removing " + mItem.toString());
            mCartAction = CartActions.decrease;

        } else if (mCartAction.equals(CartActions.decrease)) {
            mCartManager.addToCart(mItem);
            Log.i("CartCommand", " adding " + mItem.toString());

            mCartAction = CartActions.increase;

        }

    }

    @Override
    public void redo() {

        if (mCartAction.equals(CartActions.increase)) {
            mCartManager.removeFromCart(mItem);
            Log.i("CartCommand", " removing " + mItem.toString());

            mCartAction = CartActions.decrease;
        } else if (mCartAction.equals(CartActions.decrease)) {
            mCartManager.addToCart(mItem);
            Log.i("CartCommand", " adding " + mItem.toString());

            mCartAction = CartActions.increase;

        }

    }


}
