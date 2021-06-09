package com.hydertechno.mulven.DatabaseHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.view.View;

import androidx.annotation.Nullable;

import com.hydertechno.mulven.Adapters.CartAdapter;
import com.hydertechno.mulven.Models.CartProductModel;

import java.util.ArrayList;

public class Database_Helper extends SQLiteOpenHelper {

    private static String DB_NAME = "cartTable";
    private static String TABLE_NAME = "new_table";
    public static String ID = "id";
    public static String PRODUCT_NAME = "product_name";
    public static String SKU = "sku";
    public static String MRP_PRICE = "mrp_price";
    public static String UNIT_PRICE = "unit_price";
    public static String SIZE = "size";
    public static String COLOR = "color";
    public static String VARIANT = "variant";
    public static String SHOP_NAME = "shop_name";
    public static String QUANTITY = "quantity";
    public static String IMAGE = "image";
    public static String CAMPAIGN_ID = "campaign_id";
    public static String STORE_ID = "store_id";
    public static String CATEGORY_ID = "category_id";
    private static int VERSION = 4;
    private Context context;
    private String table = "CREATE TABLE " + TABLE_NAME + " (" + ID + " INTEGER," + SKU + " VARCHAR(255),"
            + MRP_PRICE + " VARCHAR(255)," + UNIT_PRICE + " INTEGER,"+ SIZE + " VARCHAR(255),"+COLOR + " VARCHAR(255),"
            +VARIANT + " VARCHAR(255),"+ PRODUCT_NAME + " VARCHAR(255),"
            + SHOP_NAME + " VARCHAR(255)," + QUANTITY + " INTEGER,"+ CAMPAIGN_ID + " VARCHAR(255),"
            + STORE_ID + " INTEGER,"+ CATEGORY_ID + " INTEGER," +  IMAGE + " VARCHAR)";

    public Database_Helper(@Nullable Context context) {
        super(context, DB_NAME, null, VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(table);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void addToCart(int id,String sku, String name, int mrp_price, int unit_price,String size,String color,
                          String variant, String shop_name, int quantity,String campaign_id,
                          int store_id, int category_id, String image) {

        SQLiteDatabase sq = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ID, id);
        values.put(SKU, sku);
        values.put(PRODUCT_NAME, name);
        values.put(MRP_PRICE, mrp_price);
        values.put(UNIT_PRICE, unit_price);
        values.put(SIZE, size);
        values.put(COLOR, color);
        values.put(VARIANT, variant);
        values.put(SHOP_NAME, shop_name);
        values.put(QUANTITY, quantity);
        values.put(CAMPAIGN_ID, campaign_id);
        values.put(STORE_ID, store_id);
        values.put(CATEGORY_ID, category_id);
        values.put(IMAGE, image);
        sq.insert(TABLE_NAME, null, values);

    }

    public boolean checkCart(int id, String size, String color, String variant) {
        SQLiteDatabase sq = getReadableDatabase();
        Cursor cursor = sq.rawQuery("select " + ID  +" from " + TABLE_NAME + " where " +
                ID + " = " + id  , (String[]) null);
        if (cursor.getCount() > 0) {
            return true;
        }
        return false;
    }
    public boolean checkProductExist(int id, String size, String color, String variant) {
        SQLiteDatabase sq = getReadableDatabase();

        Cursor cursor = sq.rawQuery("SELECT id FROM new_table WHERE id=? and size=? and color=? and variant=?",
                new String[] { String.valueOf(id), size,color,variant});
        if (cursor.getCount()>0){
            return true;
        }
        return false;
    }


    public int checkQuantity(int id){
        SQLiteDatabase sq = getReadableDatabase();
        Cursor cursor = sq.rawQuery("select " + QUANTITY + " from " + TABLE_NAME + " where " + ID + " = " + id, (String[]) null);
        if(cursor.moveToFirst())
        {
            return cursor.getInt(0);
        }
        return 0;
    }

    public Cursor getCart() {
        String sql = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        return cursor;
    }

    public void deleteData(int id, String size, String color, String variant) {
        getWritableDatabase().delete(TABLE_NAME, "id=? and size=? and color=? and variant=?",
                new String[]{String.valueOf(id), size,color,variant});
    }

    public void addQuantity(int id, int quantity) {

        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("update " + TABLE_NAME + " set " + QUANTITY + " = " + quantity + " where " + ID + " = " + id);
        db.close();
    }

    public Cursor numberOfrows() {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor data = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        return data;
    }

     public int columnSum() {
         SQLiteDatabase db = this.getWritableDatabase();
         Cursor cur = db.rawQuery("SELECT SUM(quantity * unit_price) FROM new_table", null);
         if(cur.moveToFirst())
         {
             return cur.getInt(0);
         }

         return 0;
     }

     public ArrayList<CartProductModel> getAllCartProducts() {
        ArrayList<CartProductModel> productModels = new ArrayList<CartProductModel>();
         Cursor cursor = getCart();
         if (cursor != null) {
             while (cursor.moveToNext()) {
                 int id = cursor.getInt(cursor.getColumnIndex(ID));
                 String name = cursor.getString(cursor.getColumnIndex(PRODUCT_NAME));
                 int mrp_price = cursor.getInt(cursor.getColumnIndex(MRP_PRICE));
                 int unit_price = cursor.getInt(cursor.getColumnIndex(UNIT_PRICE));
                 String shop_name = cursor.getString(cursor.getColumnIndex(SHOP_NAME));
                 int quantity = cursor.getInt(cursor.getColumnIndex(QUANTITY));
                 String image = cursor.getString(cursor.getColumnIndex(IMAGE));
                 String size = cursor.getString(cursor.getColumnIndex(SIZE));
                 String color = cursor.getString(cursor.getColumnIndex(COLOR));
                 String variant = cursor.getString(cursor.getColumnIndex(VARIANT));

                 CartProductModel cartProductsModel = new CartProductModel(id, name, mrp_price, unit_price, size, color, variant, shop_name, quantity, image);

                 productModels.add(cartProductsModel);
             }
         }
        return productModels;
     }

}
