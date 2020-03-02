package com.example.bookstore.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.example.bookstore.model.ListItem;
import com.example.bookstore.model.UserList;

import java.util.ArrayList;
import java.util.List;


public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "Books";
    public static final String TABLE_NAME = "BooksTable";
    public static final String CART_TABLE = "CartTable";
    public static final String USER_TABLE = "UserTable";
    public static final String COL_Id = "Id";
    public static final String COL_Name = "Name";
    public static final String COL_Description = "Description";
    public static final String COL_Image = "Image";
    public static final String COL_Price = "Price";
    public static final String COL_Address = "Address";
    public static final String COL_Email = "Email";
    public static final String COL_Password = "Password";


    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE " + TABLE_NAME + " (Id Integer Primary Key AutoIncrement,Name TEXT,Description TEXT,Image BLOB,Price Integer)");
        db.execSQL("CREATE TABLE " + CART_TABLE + " (Id Integer Primary Key AutoIncrement,Name TEXT,Description TEXT,Image BLOB,Price Integer)");
        db.execSQL("CREATE TABLE " + USER_TABLE + "(Id Integer Primary Key AutoIncrement,Name Text,Email Text,Password Text,Address Text)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + CART_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE);
        onCreate(db);

    }

    public boolean insertBook(ListItem listItem) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_Name, listItem.getHead());
        values.put(COL_Description, listItem.getDesc());
        values.put(COL_Image, listItem.getmImage());
        values.put(COL_Price, listItem.getPrice());
        long result = db.insert(TABLE_NAME, null, values);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean insertCart(ListItem listItem) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_Name, listItem.getHead());
        values.put(COL_Description, listItem.getDesc());
        values.put(COL_Image, listItem.getmImage());
        values.put(COL_Price, listItem.getPrice());
        long result = db.insert(CART_TABLE, null, values);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean insertUser(UserList userList) {
        System.out.println("insert=====");
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_Name, userList.getUserName());
        values.put(COL_Email, userList.getUserEmail());
        values.put(COL_Password, userList.getUserPassword());
        values.put(COL_Address, userList.getUserAddress());
        long result = db.insert(USER_TABLE, null, values);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean checkUser(String Name, String Password) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor result = db.rawQuery(" SELECT Name,Password FROM " + USER_TABLE + " WHERE Name=? AND Password=? ", new String[]{Name, Password});
        if (result.moveToFirst()) {
            return true;
        } else {
            return false;
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
                    item.add(new ListItem(name, desc, image, price));
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
                    int price = res.getInt(4);
                    item.add(new ListItem(name, desc, image, price));
                } while (res.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            res.close();
        }
        return item;
    }

    public UserList getAllUser() {

        SQLiteDatabase db = this.getWritableDatabase();
        UserList item=null;
        Cursor res = db.rawQuery(" SELECT * FROM " + USER_TABLE, null);

        try {
            if (res.moveToFirst()) {
                do {
                    String name = res.getString(1);
                    String email = res.getString(2);
                    String password = res.getString(3);
                    String address = res.getString(4);
                    item = new UserList(name, email,password,address);
                } while (res.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            res.close();
        }
        return item;
    }

    public UserList getUser(String Name) {

        SQLiteDatabase db = this.getWritableDatabase();
        UserList item = null;
        Cursor res = db.rawQuery(" SELECT * FROM " + USER_TABLE + " WHERE Name=?", new String[]{Name});
        try {
            if (res.moveToFirst()) {
                do {
                    String name = res.getString(1);
                    String email = res.getString(2);
                    String pass = res.getString(3);
                    String address = res.getString(4);
                    item = new UserList(name, email, pass, address);
                } while (res.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            res.close();
        }
        return item;
    }

    public boolean updateUser(String tName, String name, String email, String address) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_Name, name);
        values.put(COL_Email, email);
        values.put(COL_Address, address);
        long result = db.update(USER_TABLE, values, "Name =?", new String[]{tName});
        if (result == -1) {
            return false;
        }
        return true;
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

    public String getPrice() {
        String amount = "";
        int netPrice = 0;
        SQLiteDatabase db = getReadableDatabase();
        Cursor result = db.rawQuery("SELECT Price FROM " + CART_TABLE, null);
        if (result.moveToFirst()) {
            do {
                netPrice = netPrice + result.getInt(0);
            } while (result.moveToNext());
            result.close();
            amount = String.valueOf(netPrice);
        }
        return amount;

    }

    public boolean checkEmpty() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor result = db.rawQuery("SELECT Count(*) FROM " + TABLE_NAME, null);
        result.moveToFirst();
        int count = result.getInt(0);
        result.close();
        if (count <= 0) {
            return true;

        } else {
            return false;
        }

    }

    public void setAddress(String address) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_Address, address);
        db.insert(USER_TABLE, null, values);

    }

    public String getAddress() {
        SQLiteDatabase db = getReadableDatabase();
        String address;
        Cursor result = db.rawQuery(" SELECT Address FROM " + USER_TABLE, null);
        if (result.moveToFirst()) {
            do {
                address = result.getString(0);
            } while (result.moveToNext());
            result.close();
            return address;
        } else {
            result.close();
            return null;
        }
    }
}

