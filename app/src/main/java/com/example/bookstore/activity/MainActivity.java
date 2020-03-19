package com.example.bookstore.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookstore.R;
import com.example.bookstore.adapter.MyAdapter;
import com.example.bookstore.model.ListItem;
import com.example.bookstore.model.UserList;
import com.example.bookstore.util.CircleImage;
import com.example.bookstore.util.DatabaseHelper;
import com.example.bookstore.util.SharedPreferenceHelper;
import com.google.android.material.navigation.NavigationView;

import java.io.ByteArrayOutputStream;
import java.util.List;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, MyAdapter.ItemClickListener {

    Toolbar toolbar;
    DatabaseHelper myDb;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle toggle;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    List<ListItem> listItems;
    ImageButton cartButton;
    ImageView drawerImage;
    NavigationView navigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initializing widgets in the activity
        initialize();
        //initializing toolbar
        toolbarInitialize();
        //initializing drawer layout with toggle enabled
        drawerLayoutInit();
        //profile initializing on the navigation layout
        profileInitialize();
        //inserting local data to the database
        bookInsert();
        //recycler view to display all the data from database in linear layout
        recyclerViewInit();
    }


    private void initialize() {
        myDb = new DatabaseHelper(this);
        toolbar = findViewById(R.id.toolBar);
        cartButton = findViewById(R.id.cart_button);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigationView);
        recyclerView = findViewById(R.id.recyclerView);
        navigationView.setNavigationItemSelectedListener(this);
        drawerImage = findViewById(R.id.drawer_image);
    }

    //assigning user data from the database to drawer header layout and converting image into circle shape

    public void profileInitialize(){
        CircleImage circleImage = new CircleImage();
        View headerView = navigationView.getHeaderView(0);
        ImageView profilePicture = headerView.findViewById(R.id.drawer_image);
        TextView userName = headerView.findViewById(R.id.user_name);
        TextView userAccount = headerView.findViewById(R.id.user_account);
        SharedPreferenceHelper sph = new SharedPreferenceHelper();
        String name = sph.getSharedName(getApplicationContext());
        UserList userList = myDb.getUser(name);
        profilePicture.setImageBitmap(circleImage.transform(bitImage(userList.getUserImage())));
        userName.setText(userList.getUserName());
        userAccount.setText(userList.getUserEmail());
    }

    private Bitmap bitImage(byte[] userImage){
        return BitmapFactory.decodeByteArray(userImage, 0, userImage.length);
    }

    private void toolbarInitialize() {
        setSupportActionBar(toolbar);
    }

    private void drawerLayoutInit() {

        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_opened,
                R.string.navigation_drawer_closed);
        drawerLayout.addDrawerListener(toggle);
        toggle.setDrawerIndicatorEnabled(true);
        toggle.syncState();
    }

    private void bookInsert() {

        boolean checkEmpty = myDb.checkEmpty();
        if (checkEmpty) {
            ListItem listItem = new ListItem("Gone With The Wind", "A manipulative woman and a roguish man conduct a turbulent romance during the American Civil War and Reconstruction periods.", drawableToByte(getResources().getDrawable(R.drawable.book_1)), 120);
            ListItem listItem1 = new ListItem("The Monk Who Sold His Ferrari", "The Monk Who Sold His Ferrari tells the extraordinary story of Julian Mantle, and the subsequent wisdom that he gains on a life-changing odyssey that enables him to create a life of passion, purpose and peace.", drawableToByte(getResources().getDrawable(R.drawable.book_2)), 130);
            ListItem listItem2 = new ListItem("Rich Dad Poor Dad", "Rich Dad Poor Dad is about Robert Kiyosaki and his two dads—his real father (poor dad) and the father of his best friend (rich dad)—and the ways in which both men shaped his thoughts about money and investing.", drawableToByte(getResources().getDrawable(R.drawable.book_3)), 190);
            ListItem listItem3 = new ListItem("Life Is What You Make It", "Life Is What You Make It is based on a love story that has been set in India in the 90s.", drawableToByte(getResources().getDrawable(R.drawable.book_4)), 250);
            ListItem listItem4 = new ListItem("The Subtle Art Of Not Giving", "A Counterintuitive Approach to Living a Good Life is the second book by blogger and author Mark Manson.", drawableToByte(getResources().getDrawable(R.drawable.book_5)), 225);
            ListItem listItem5 = new ListItem("To Kill A Mocking Bird", "It follows three years in the life of 8-year-old Scout Finch, her brother, Jem, and their father, Atticus--three years punctuated by the arrest and eventual trial of a young black man accused of raping a white woman.", drawableToByte(getResources().getDrawable(R.drawable.book_6)), 170);
            ListItem listItem6 = new ListItem("Vekkai", "Vekkai explores the vulnerability of a bucolic existence damaged irreparably by violent conflicts over class and poverty.", drawableToByte(getResources().getDrawable(R.drawable.book_7)), 165);
            ListItem listItem7 = new ListItem("Ottran", "It is a satire mingling travel and fiction. The author ably records the events, without taking part in them, with an eye of a camera.", drawableToByte(getResources().getDrawable(R.drawable.book_8)), 200);
            myDb.insertBook(listItem);
            myDb.insertBook(listItem1);
            myDb.insertBook(listItem2);
            myDb.insertBook(listItem3);
            myDb.insertBook(listItem4);
            myDb.insertBook(listItem5);
            myDb.insertBook(listItem6);
            myDb.insertBook(listItem7);
        }
    }

    private void recyclerViewInit() {

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        listItems = myDb.getAllData();
        if (listItems.size() > 0) {
            adapter = new MyAdapter(listItems, this, this);
            recyclerView.setAdapter(adapter);

        } else {
            Toast.makeText(MainActivity.this, "No Record", Toast.LENGTH_SHORT).show();
        }
    }


    //this method converts the byte image data from the database to drawable bitmap

    private byte[] drawableToByte(Drawable book) {
        Bitmap bitmap = ((BitmapDrawable) book).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        long size = bitmap.getByteCount();
        if (size > 20000) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 20, stream);
        } else {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        }
        return stream.toByteArray();
    }

    //when user presses back button app will go to on pause state and doesn't close

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            Intent a = new Intent(Intent.ACTION_MAIN);
            a.addCategory(Intent.CATEGORY_HOME);
            a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(a);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.mCart) {
            Intent i = new Intent(MainActivity.this, CartActivity.class);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_profile:
                drawerLayout.closeDrawer(GravityCompat.START);
                startActivity(new Intent(MainActivity.this, ViewProfileActivity.class));
                break;

            case R.id.nav_cart:
                drawerLayout.closeDrawer(GravityCompat.START);
                Intent i = new Intent(MainActivity.this, CartActivity.class);
                startActivity(i);
                break;

            case R.id.nav_logout:
                drawerLayout.closeDrawer(GravityCompat.START);
                SharedPreferenceHelper sph = new SharedPreferenceHelper();
                sph.removeSharedPreference(getApplicationContext());
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                break;

            case R.id.nav_share:
                drawerLayout.closeDrawer(GravityCompat.START);
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT, "Text Message From BookStore");
                intent.setType("text/plain");

                Intent shareIntent = Intent.createChooser(intent, null);
                startActivity(shareIntent);
                break;

            case R.id.nav_contactUs:
                drawerLayout.closeDrawer(GravityCompat.START);
                Toast.makeText(MainActivity.this, "send", Toast.LENGTH_SHORT).show();
                break;
        }
        return false;
    }


    //this method passes the selected data information to the next activity
    @Override
    public void onItemClick(View v, int pos) {
        ListItem listItem = listItems.get(pos);
        String head = listItem.getHead();
        String desc = listItem.getDesc();
        byte[] image = listItem.getmImage();
        double price = listItem.getPrice();


        Intent intent = new Intent(MainActivity.this, BookActivity.class);
        intent.putExtra("head", head);
        intent.putExtra("desc", desc);
        intent.putExtra("image", image);
        intent.putExtra("price", price);
        startActivity(intent);
    }

    //adds the selected items to the cart
    @Override
    public void cartInsert(View v, int pos) {

        boolean result = myDb.insertCart(listItems.get(pos));
        if (result) {
            Toast.makeText(MainActivity.this, "Added successful", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(MainActivity.this, "Added failed", Toast.LENGTH_SHORT).show();
        }
    }
}
