package com.hydertechno.mulven.DatabaseHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class Database_Helper extends SQLiteOpenHelper {

    private static String DB_NAME = "cart_table";
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
    private static int VERSION = 4;
    private Context context;
    private String table = "CREATE TABLE " + TABLE_NAME + " (" + ID + " INTEGER," + SKU + " VARCHAR(255),"
            + MRP_PRICE + " VARCHAR(255)," + UNIT_PRICE + " INTEGER,"+ SIZE + " VARCHAR(255),"+COLOR + " VARCHAR(255),"
            +VARIANT + " VARCHAR(255),"+ PRODUCT_NAME + " VARCHAR(255),"
            + SHOP_NAME + " VARCHAR(255)," + QUANTITY + " INTEGER," + IMAGE + " VARCHAR)";

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
    public void delete(){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("delete from "+ TABLE_NAME);
    }

    public void addToCart(int id, String name, int mrp_price, int unit_price,String size,String color,String variant, String shop_name, int quantity, String image) {

        SQLiteDatabase sq = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ID, id);
        values.put(PRODUCT_NAME, name);
        values.put(MRP_PRICE, mrp_price);
        values.put(UNIT_PRICE, unit_price);
        values.put(SIZE, size);
        values.put(COLOR, color);
        values.put(VARIANT, variant);
        values.put(SHOP_NAME, shop_name);
        values.put(QUANTITY, quantity);
        values.put(IMAGE, image);
        sq.insert(TABLE_NAME, null, values);

    }

    public boolean checkCart(int id) {
        SQLiteDatabase sq = getReadableDatabase();
        Cursor cursor = sq.rawQuery("select " + ID + " from " + TABLE_NAME + " where " + ID + " = " + id, (String[]) null);
        if (cursor.getCount() > 0) {
            return true;
        }
        return false;
    }

    public Cursor getCart() {
        String sql = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        return cursor;
    }

    public void deleteData(int id) {
        getWritableDatabase().delete(TABLE_NAME, "id=?", new String[]{String.valueOf(id)});
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




}
