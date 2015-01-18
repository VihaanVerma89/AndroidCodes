package com.example.tinyowlsampleapp.database;

/**
 * Created by vihaan on 20/12/14.
 */
public class Tables {

    public static class RestaurantItems {
        public static final String TABLE_NAME = "restaurantItems";

        public static final String ITEMS = "items";
        public static final String NAME = "name";
        public static final String TYPE = "type";

        public static final String COLUMNS =
                NAME + " text not null, "
                        + TYPE + " text not null ";


        public static final String SCHEMA = "Create table " + TABLE_NAME
                + " ( " + COLUMNS + ")";

    }

    public static class RestaurantItemsCost {
        public static final String TABLE_NAME = "restuarantItemsCosts";

        public static final String ITEM_ID = "item_id";
        public static final String SIZE = "size";
        public static final String COST = "cost";


        public static final String COLUMNS =
                ITEM_ID + " text not null, "
                        + SIZE + " text not null, "
                        + COST + " text not null ";


        public static final String SCHEMA = "Create table " + TABLE_NAME
                + " ( " + COLUMNS + ")";

    }


    public static class Cart {
        public static final String TABLE_NAME = "cart";

        public static final String ITEM_ID = "item_id";
        public static final String NAME = "name";
        public static final String SIZE = "size";
        public static final String TYPE = "type";
        public static final String COST = "cost";
        public static final String QUANTITY = "quantity";


        public static final String COLUMNS =
                ITEM_ID + " text not null, "
                        + NAME + " text not null, "
                        + TYPE + " text not null, "
                        + SIZE + " text not null, "
                        + COST + " text not null, "
                        + QUANTITY + " text not null ";


        public static final String SCHEMA = "Create table " + TABLE_NAME
                + " ( " + COLUMNS + ")";

    }

}
