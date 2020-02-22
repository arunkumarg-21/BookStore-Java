package com.example.bookstore;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.example.bookstore.model.ListItem;

import java.util.ArrayList;
import java.util.List;


public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "Books";
    public static final String TABLE_NAME = "BooksTable";
    public static final String CART_TABLE = "CartTable";
    public static final String COL_1 = "Id";
    public static final String COL_2 = "Name";
    public static final String COL_3 = "Description";
    public static final String COL_4 = "Image";
    public static final String COL_5 = "Price";


    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE " + TABLE_NAME + " (Id Integer Primary Key AutoIncrement,Name TEXT,Description TEXT,Image BLOB)");
        db.execSQL("CREATE TABLE "+ CART_TABLE + " (Id Integer Primary Key AutoIncrement,Name TEXT,Description TEXT,Image BLOB,Price Integer)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + CART_TABLE);
        onCreate(db);

    }

    public boolean insertData(String name, String desc, byte[] image) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_2, name);
        values.put(COL_3, desc);
        values.put(COL_4, image);
        long result = db.insert(TABLE_NAME, null, values);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean insertCart(String name,String desc,byte[] image,int price){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_2,name);
        values.put(COL_3, desc);
        values.put(COL_4, image);
        values.put(COL_5,price);
        long result = db.insert(CART_TABLE,null,values);
        if(result == -1){
            return false;
        }else{
            return true;
        }
    }

    public List<ListItem> getCart() {
        SQLiteDatabase db = this.getWritableDatabase();
        List<ListItem> item = new ArrayList<>();
        Cursor res = db.rawQuery(" SELECT * FROM " + CART_TABLE, null);

        try {
            if (res.moveToFirst()) {
                do {
                    String name = res.getString(1);
                    String desc = res.getString(2);
                    byte[] image = res.getBlob(3);
                    int price = res.getInt(4);
                    item.add(new ListItem(name, desc, image,price));
                } while (res.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            res.close();
        }
        return item;
    }

    public List<ListItem> getAllData() {

        SQLiteDatabase db = this.getWritableDatabase();
        List<ListItem> item = new ArrayList<>();
        Cursor res = db.rawQuery(" SELECT * FROM " + TABLE_NAME, null);

        try {
            if (res.moveToFirst()) {
                do {
                    String name = res.getString(1);
                    String desc = res.getString(2);
                    byte[] image = res.getBlob(3);
                    item.add(new ListItem(name, desc, image));
                } while (res.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            res.close();
        }
        return item;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void delete(String name) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_NAME, " Name=? ", new String[]{name});
    }

    public void deleteCart(String name) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(CART_TABLE, " Name=? ", new String[]{name});
    }

    public String getPrice(){
        String amount="";
        int netPrice = 0;
        SQLiteDatabase db = getReadableDatabase();
        Cursor result = db.rawQuery("SELECT price FROM "+CART_TABLE,null);
        if(result.moveToFirst()){
            do{
                netPrice = netPrice + result.getInt(0);
            }while (result.moveToNext());

            amount = String.valueOf(netPrice);
        }
        System.out.println("amount======="+amount);
        return amount;

    }
}

