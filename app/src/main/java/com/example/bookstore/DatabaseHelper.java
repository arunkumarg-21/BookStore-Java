package com.example.bookstore;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import java.util.ArrayList;
import java.util.List;


public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "Books";
    public static final String TABLE_NAME = "BooksTable";
    public static final String COL_1 = "Id";
    public static final String COL_2 = "Name";
    public static final String COL_3 = "Description";
    public static final String COL_4 = "Image";


    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE " + TABLE_NAME + " (Id Integer Primary Key AutoIncrement,Name TEXT,Description TEXT,Image BLOB)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
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
}

