package com.example.tinyowlsampleapp.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tinyowlsampleapp.CartActions;
import com.example.tinyowlsampleapp.R;
import com.example.tinyowlsampleapp.beans.Item;
import com.example.tinyowlsampleapp.database.CartCommandManager;
import com.example.tinyowlsampleapp.database.CartManager;
import com.example.tinyowlsampleapp.database.RestaurantItemsCostManager;
import com.example.tinyowlsampleapp.database.Tables;
import com.example.tinyowlsampleapp.interfaces.CartCommand;
import com.example.tinyowlsampleapp.interfaces.CartDataLoader;
import com.example.tinyowlsampleapp.interfaces.Command;
import com.example.tinyowlsampleapp.interfaces.ReloadAdapter;

import java.nio.charset.Charset;
import java.util.ArrayList;

/**
 * Created by vihaan on 20/12/14.
 */
//public class RestaurantsItemsAdapter extends SimpleCursorAdapter implements ReloadAdapter{
public class RestaurantsItemsAdapter extends SimpleCursorAdapter {


    private Context mContext;
    private RestaurantItemsCostManager mRestaurantItemsCostManager;
    private CartManager mCartManager;
    private CartDataLoader mCartDataLoader;
    private CartCommandManager mCartCommandManager;

    public RestaurantsItemsAdapter(CartDataLoader cartDataLoader, int layout, Cursor c, String[] from, int[] to, int flags) {
        super((Context)cartDataLoader, layout, c, from, to, flags);
        mContext = (Context)cartDataLoader;
        mCartDataLoader = cartDataLoader;
        mRestaurantItemsCostManager = new RestaurantItemsCostManager();
        mCartManager = new CartManager();
        mCartCommandManager = CartCommandManager.getInstance();
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = super.newView(context, cursor, parent);

        ViewHolder viewHolder = new ViewHolder();

        viewHolder.itemNameTextView = (TextView) view.findViewById(R.id.itemNameTextView);
        viewHolder.itemCostTextView = (TextView) view.findViewById(R.id.itemCostTextView);
        viewHolder.addButton = (Button) view.findViewById(R.id.addButton);

        view.setTag(viewHolder);

        return view;
    }


    @Override
    public void bindView(View view, Context context, final Cursor cursor) {
        super.bindView(view, context, cursor);

        final ViewHolder viewHolder = (ViewHolder) view.getTag();

        final long itemId = cursor.getLong(cursor.getColumnIndex("_id"));
        final String itemName = cursor.getString(cursor.getColumnIndex(Tables.RestaurantItems.NAME));
        final String itemType = cursor.getString(cursor.getColumnIndex(Tables.RestaurantItems.TYPE));

        if(itemType.equalsIgnoreCase("veg"))
        {
            viewHolder.itemNameTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.veg, 0, 0, 0);
        }
        else if(itemType.equalsIgnoreCase("nonveg"))
        {
            viewHolder.itemNameTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.nonveg, 0, 0, 0);
        }

        viewHolder.itemNameTextView.setText(itemName);

        final Cursor itemInfoCursor = mRestaurantItemsCostManager.getCursor(itemId + "");

//        String itemCost;
//
        itemInfoCursor.moveToFirst();
        final String itemCost = itemInfoCursor.getString(itemInfoCursor.getColumnIndex(Tables.RestaurantItemsCost.COST));
//
        final String itemSize;

        itemSize = itemInfoCursor.getString(itemInfoCursor.getColumnIndex(Tables.RestaurantItemsCost.SIZE));

        viewHolder.itemCostTextView.setText(mContext.getResources().getString(R.string.rs) + " " + itemCost);


        final ArrayList<String> itemSizeOptions = new ArrayList<String>();

        itemSizeOptions.add(itemSize + " for Rs." + itemCost);

        String tempItemSize, tempItemCost;

        while (itemInfoCursor.moveToNext()) {
            tempItemSize = itemInfoCursor.getString(itemInfoCursor.getColumnIndex(Tables.RestaurantItemsCost.SIZE));
            tempItemCost = itemInfoCursor.getString(itemInfoCursor.getColumnIndex(Tables.RestaurantItemsCost.COST));
            itemSizeOptions.add(tempItemSize + " size for Rs " + tempItemCost);
        }


        viewHolder.addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

                builder.setTitle(itemName);
                CharSequence options[] = itemSizeOptions.toArray(new CharSequence[itemSizeOptions.size()]);


                builder.setItems(options,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                Log.i("adapter", which + "");
                                itemInfoCursor.moveToPosition(which);
                                String cost = itemInfoCursor.getString(itemInfoCursor.getColumnIndex(Tables.RestaurantItemsCost.COST));
                                String size = itemInfoCursor.getString(itemInfoCursor.getColumnIndex(Tables.RestaurantItemsCost.SIZE));

                                int temp = 1 ;
                                if(mCartManager.presentInCart(itemId +"", cost))
                                {
                                    temp = Integer.parseInt(mCartManager.getQuantity(itemId +"", itemCost));
                                }

                                Item item = new Item(itemId+"",itemName, itemType, size, cost, temp+"");
                                Command cartCommand = new CartCommand(mCartManager, item, CartActions.increase);
                                mCartCommandManager.pushInUndoStack(cartCommand);

                                mCartManager.addToCart(itemId +"", itemName, itemType,  size, cost, "1");
                                mCartDataLoader.updateCart();
                            }
                        });
                builder.show();

            }
        });

    }

    static class ViewHolder {
        TextView itemNameTextView;
        TextView itemCostTextView;
        Button addButton;
    }

}
