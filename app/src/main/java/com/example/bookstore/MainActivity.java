package com.example.bookstore;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;



public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, Adapter.ItemClickListener{

    Toolbar toolbar;
    DatabaseHelper myDb;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle toggle;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<ListItem> listItems;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myDb = new DatabaseHelper(this);


        toolbar = findViewById(R.id.toolBar);
//        toolbar.setOverflowIcon(getDrawable(R.drawable.ic_more_options_icon_24dp));
//        toolbar./**/

        setSupportActionBar(toolbar);


        drawerLayout = findViewById(R.id.drawer_layout);

        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_opened,
                R.string.navigation_drawer_closed);
        drawerLayout.addDrawerListener(toggle);
        toggle.setDrawerIndicatorEnabled(true);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this);

        recyclerView = findViewById(R.id.recyclerView);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

       // boolean isInserted =

        myDb.insertData("Gone With The Wind", "A manipulative woman and a roguish man conduct a turbulent romance during the American Civil War and Reconstruction periods.",drawableToByte(getResources().getDrawable(R.drawable.book_1)));
        myDb.insertData("The Monk Who Sold His Ferrari", "The Monk Who Sold His Ferrari tells the extraordinary story of Julian Mantle, and the subsequent wisdom that he gains on a life-changing odyssey that enables him to create a life of passion, purpose and peace.", drawableToByte(getResources().getDrawable(R.drawable.book_5)));
        myDb.insertData("Rich Dad Poor Dad", "Rich Dad Poor Dad is about Robert Kiyosaki and his two dads—his real father (poor dad) and the father of his best friend (rich dad)—and the ways in which both men shaped his thoughts about money and investing.",drawableToByte(getResources().getDrawable(R.drawable.book_3)));
        myDb.insertData("Life Is What You Make It", "Life Is What You Make It is based on a love story that has been set in India in the 90s.", drawableToByte(getResources().getDrawable(R.drawable.book_4)));
        myDb.insertData("The Subtle Art Of Not Giving a F*ck", "A Counterintuitive Approach to Living a Good Life is the second book by blogger and author Mark Manson.", drawableToByte(getResources().getDrawable(R.drawable.book_2)));
        myDb.insertData("To Kill A Mocking Bird", "It follows three years in the life of 8-year-old Scout Finch, her brother, Jem, and their father, Atticus--three years punctuated by the arrest and eventual trial of a young black man accused of raping a white woman.", drawableToByte(getResources().getDrawable(R.drawable.book_6)));
        myDb.insertData("Vekkai", "Vekkai explores the vulnerability of a bucolic existence damaged irreparably by violent conflicts over class and poverty.", drawableToByte(getResources().getDrawable(R.drawable.book_7)));
        myDb.insertData("Ottran", "It is a satire mingling travel and fiction. The author ably records the events, without taking part in them, with an eye of a camera.", drawableToByte(getResources().getDrawable(R.drawable.book_8)));


       /*adapter = new Adapter(listItems, MainActivity.this, this);
        recyclerView.setAdapter(adapter);*/

    }

    protected void onResume() {
        super.onResume();

        listItems = new ArrayList<>();

        listItems = myDb.getAllData();


        if(listItems.size()>0){


            adapter = new Adapter(listItems,this,this);
            recyclerView.setAdapter(adapter);

        }else{

            Toast.makeText(getApplicationContext(),"No Record",Toast.LENGTH_SHORT).show();
        }

    }

    private byte[] drawableToByte(Drawable book) {
        Bitmap bitmap = ((BitmapDrawable)book).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);
        byte[] bytes = stream.toByteArray();
        return bytes;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.miCompose:
                startActivity(new Intent(getApplicationContext(), UploadActivity.class));
                break;
            case R.id.miProfile:
                Toast.makeText(this, "profile is opted", Toast.LENGTH_SHORT).show();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_cart:
                Toast.makeText(this, "Cart is opted", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_book:
                Toast.makeText(this, "Book is opted", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_logout:
                Toast.makeText(this, "Logging out is opted", Toast.LENGTH_SHORT).show();
                break;
        }
        return false;
    }


    @Override
    public void onItemClick(View v, int pos) {
        ListItem listItem = listItems.get(pos);
        String head = listItem.getHead();
        String desc = listItem.getDesc();
        byte[] image = listItem.getmImage();


        Intent intent = new Intent(this, BookActivity.class);
        intent.putExtra("head", head);
        intent.putExtra("desc", desc);
        intent.putExtra("image",image);
        startActivity(intent);
    }

    @Override
    public void onItemLongClick(View v,int pos){
        final ListItem listItem = listItems.get(pos);
        final String name = listItem.getHead();
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Are You Sure Want to Delete?");
        builder.setMessage("Select your choice");
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                DatabaseHelper db = new DatabaseHelper(MainActivity.this);
                dialog.dismiss();
                myDb.delete(name);
                System.out.println("dddddd=======");
                adapter.notifyDataSetChanged();
            }
        });
        builder.show();
        System.out.println("Finished=======");
        adapter.notifyDataSetChanged();
    }


}
