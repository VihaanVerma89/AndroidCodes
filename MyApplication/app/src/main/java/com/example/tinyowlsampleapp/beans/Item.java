package com.example.tinyowlsampleapp.beans;

/**
 * Created by vihaan on 26/12/14.
 */
public class Item {

    private String mItemId, mName, mType, mSize, mCost, mQuantity;

    public Item(String itemId, String name, String type, String size, String cost, String quantity)
    {
        mItemId = itemId;
        mName = name;
        mType = type;
        mSize = size;
        mCost = cost;
        mQuantity = quantity;
    }

    public String getItemId() {
        return mItemId;
    }

    public String getName() {
        return mName;
    }

    public String getType() {
        return mType;
    }

    public String getSize() {
        return mSize;
    }

    public String getCost() {
        return mCost;
    }

    public String getQuantity() {
        return mQuantity;
    }


}

