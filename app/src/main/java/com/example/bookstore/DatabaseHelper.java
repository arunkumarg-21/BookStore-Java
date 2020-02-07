package com.example.bookstore;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.jar.Attributes;

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
        // addData(db);


    }
/*
    private void addData(SQLiteDatabase db) {
        MainActivity main=new MainActivity();
        db.execSQL("INSERT INTO " + TABLE_NAME + "(" + COL_2 +"," +COL_3+ "," +COL_4+ ")" + "VALUES('Gone With The Wind', 'A manipulative woman and a roguish man conduct a turbulent romance during the American Civil War and Reconstruction periods.'," + drawableToByte(main.getResources().getDrawable(R.drawable.book_1)) + ")");
       *//* myDb.insertData("The Monk Who Sold His Ferrari", "The Monk Who Sold His Ferrari tells the extraordinary story of Julian Mantle, and the subsequent wisdom that he gains on a life-changing odyssey that enables him to create a life of passion, purpose and peace.", drawableToByte(getResources().getDrawable(R.drawable.book_5)));
        myDb.insertData("Rich Dad Poor Dad", "Rich Dad Poor Dad is about Robert Kiyosaki and his two dads—his real father (poor dad) and the father of his best friend (rich dad)—and the ways in which both men shaped his thoughts about money and investing.",drawableToByte(getResources().getDrawable(R.drawable.book_3)));
        myDb.insertData("Life Is What You Make It", "Life Is What You Make It is based on a love story that has been set in India in the 90s.", drawableToByte(getResources().getDrawable(R.drawable.book_4)));
        myDb.insertData("The Subtle Art Of Not Giving a F*ck", "A Counterintuitive Approach to Living a Good Life is the second book by blogger and author Mark Manson.", drawableToByte(getResources().getDrawable(R.drawable.book_2)));
        myDb.insertData("To Kill A Mocking Bird", "It follows three years in the life of 8-year-old Scout Finch, her brother, Jem, and their father, Atticus--three years punctuated by the arrest and eventual trial of a young black man accused of raping a white woman.", drawableToByte(getResources().getDrawable(R.drawable.book_6)));
        System.out.println("beyotch....");
        myDb.insertData("Vekkai", "Vekkai explores the vulnerability of a bucolic existence damaged irreparably by violent conflicts over class and poverty.", drawableToByte(getResources().getDrawable(R.drawable.book_7)));
        myDb.insertData("Ottran", "It is a satire mingling travel and fiction. The author ably records the events, without taking part in them, with an eye of a camera.", drawableToByte(getResources().getDrawable(R.drawable.book_8)));
*//*

    }*/

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);

    }

  /* private byte[] drawableToByte(Drawable book) {
        Bitmap bitmap = ((BitmapDrawable)book).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);
        byte[] bytes = stream.toByteArray();
        return bytes;

    }*/

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
        /*try (Cursor cursor = db.rawQuery(" SELECT * FROM " + TABLE_NAME + " WHERE Name = '" + name + "'", null)) {

            if(cursor.moveToFirst()) {

            }
        }catch (Exception e){
            System.out.println(e);
        }
*/
    }
}

