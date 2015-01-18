package com.example.tinyowlsampleapp.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.tinyowlsampleapp.CartActions;
import com.example.tinyowlsampleapp.R;
import com.example.tinyowlsampleapp.beans.Item;
import com.example.tinyowlsampleapp.database.CartCommandManager;
import com.example.tinyowlsampleapp.database.CartManager;
import com.example.tinyowlsampleapp.database.Tables;
import com.example.tinyowlsampleapp.interfaces.CartCommand;
import com.example.tinyowlsampleapp.interfaces.Command;
import com.example.tinyowlsampleapp.interfaces.ReloadAdapter;

/**
 * Created by vihaan on 21/12/14.
 */
//public class CartAdapter extends SimpleCursorAdapter implements ReloadAdapter{
public class CartAdapter extends SimpleCursorAdapter {

    private Context mContext;
    private CartManager mCartManager;
    private CartCommandManager mCartCommandManager;

    public CartAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);
        mContext = context;
        mCartManager = new CartManager();
        mCartCommandManager = CartCommandManager.getInstance();
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = super.newView(context, cursor, parent);

        ViewHolder viewHolder = new ViewHolder();

        viewHolder.itemNameTextView = (TextView) view.findViewById(R.id.itemNameTextView);
        viewHolder.itemSizeTextView = (TextView) view.findViewById(R.id.itemSizeTextView);

        viewHolder.itemCostTextView = (TextView) view.findViewById(R.id.itemCostTextView);
        viewHolder.minusImageButton = (ImageButton) view.findViewById(R.id.minusImageButton);
        viewHolder.itemQuantityTextView = (TextView) view.findViewById(R.id.itemQuantityTextView);

        viewHolder.plusImageButton = (ImageButton) view.findViewById(R.id.plusImageButton);

        view.setTag(viewHolder);

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        super.bindView(view, context, cursor);
        final ViewHolder viewHolder = (ViewHolder) view.getTag();

        final String itemId = cursor.getString(cursor.getColumnIndex(Tables.Cart.ITEM_ID));

        final String itemName = cursor.getString(cursor.getColumnIndex(Tables.Cart.NAME));
        final String itemType= cursor.getString(cursor.getColumnIndex(Tables.Cart.TYPE));

        final String itemSize = cursor.getString(cursor.getColumnIndex(Tables.Cart.SIZE));

        final String itemCost = cursor.getString(cursor.getColumnIndex(Tables.Cart.COST));

        final String itemQuantity = cursor.getString(cursor.getColumnIndex(Tables.Cart.QUANTITY));

        if(itemType.equalsIgnoreCase("veg"))
        {
            viewHolder.itemNameTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.veg, 0, 0, 0);
        }
        else if(itemType.equalsIgnoreCase("nonveg"))
        {
            viewHolder.itemNameTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.nonveg, 0, 0, 0);
        }

        viewHolder.itemNameTextView.setText(itemName);
        viewHolder.itemSizeTextView.setText(itemSize);
        viewHolder.itemCostTextView.setText(mContext.getResources().getString(R.string.rs) + " " + itemCost);
        viewHolder.itemQuantityTextView.setText(itemQuantity);

        viewHolder.plusImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int temp = Integer.parseInt(mCartManager.getQuantity(itemId, itemCost));

                temp++;

                Item item = new Item(itemId,itemName, itemType, itemSize, itemCost, temp+"");
                Command cartCommand = new CartCommand(CartAdapter.this, mCartManager, item, CartActions.increase);
                mCartCommandManager.pushInUndoStack(cartCommand);

                mCartManager.increaseQuantity(itemId, itemCost);


                viewHolder.itemQuantityTextView.setText(temp+"");
            }
        });

        viewHolder.minusImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int temp = Integer.parseInt(mCartManager.getQuantity(itemId, itemCost));
                if(temp >=1)
                {
                    Item item = new Item(itemId,itemName, itemType, itemSize, itemCost, temp+"");
                    --temp;
                    Command cartCommand = new CartCommand(CartAdapter.this, mCartManager, item, CartActions.decrease);
                    mCartCommandManager.pushInUndoStack(cartCommand);
                    mCartManager.decreaseQuantity(itemId, itemCost);
                }

                if(temp == 0)
                {
                    swapCursor(mCartManager.getCursor());
                }
                viewHolder.itemQuantityTextView.setText(temp+"");
            }
        });
    }


    static class ViewHolder {
        TextView itemNameTextView;
        TextView itemSizeTextView;
        TextView itemCostTextView;

        ImageButton minusImageButton;
        TextView itemQuantityTextView;
        ImageButton plusImageButton;

    }
}
