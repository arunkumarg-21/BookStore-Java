package com.example.bookstore;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    Toolbar toolbar;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle toggle;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<ListItem> listItems;



    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        toolbar = (Toolbar) findViewById(R.id.toolBar);
//        toolbar.setOverflowIcon(getDrawable(R.drawable.ic_more_options_icon_24dp));
//        toolbar./**/

        setSupportActionBar(toolbar);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

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

        listItems = new ArrayList<>();
        listItems.add(new ListItem("Gone With The Wind","A manipulative woman and a roguish man conduct a turbulent romance during the American Civil War and Reconstruction periods.",R.drawable.book_1));
        listItems.add(new ListItem("The Monk Who Sold His Ferrari","The Monk Who Sold His Ferrari tells the extraordinary story of Julian Mantle, and the subsequent wisdom that he gains on a life-changing odyssey that enables him to create a life of passion, purpose and peace.",R.drawable.book_5));
        listItems.add(new ListItem("Rich Dad Poor Dad","Rich Dad Poor Dad is about Robert Kiyosaki and his two dads—his real father (poor dad) and the father of his best friend (rich dad)—and the ways in which both men shaped his thoughts about money and investing.",R.drawable.book_3));
        listItems.add(new ListItem("Life Is What You Make It","Life Is What You Make It is based on a love story that has been set in India in the 90s.",R.drawable.book_4));
        listItems.add(new ListItem("The Subtle Art Of Not Giving a F*ck","A Counterintuitive Approach to Living a Good Life is the second book by blogger and author Mark Manson.",R.drawable.book_2));
        listItems.add(new ListItem("To Kill A Mocking Bird","It follows three years in the life of 8-year-old Scout Finch, her brother, Jem, and their father, Atticus--three years punctuated by the arrest and eventual trial of a young black man accused of raping a white woman.",R.drawable.book_6));
        listItems.add(new ListItem("Vekkai","Vekkai explores the vulnerability of a bucolic existence damaged irreparably by violent conflicts over class and poverty.",R.drawable.book_7));
        listItems.add(new ListItem("Ottran","It is a satire mingling travel and fiction. The author ably records the events, without taking part in them, with an eye of a camera.",R.drawable.book_8));

        adapter = new Adapter(listItems,this);
        recyclerView.setAdapter(adapter);




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater= getMenuInflater();
        inflater.inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.miCompose:
                startActivity(new Intent(getApplicationContext(),UploadActivity.class));
                break;
            case R.id.miProfile:
                Toast.makeText(this,"profile is opted",Toast.LENGTH_SHORT).show();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.nav_cart:
                Toast.makeText(this,"Cart is opted",Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_book:
                Toast.makeText(this,"Book is opted",Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_logout:
                Toast.makeText(this,"Logging out is opted",Toast.LENGTH_SHORT).show();
                break;
        }
        return false;
    }



}
