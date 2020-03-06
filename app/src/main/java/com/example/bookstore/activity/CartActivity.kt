package com.example.bookstore.activity


import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.bookstore.util.DatabaseHelper
import com.example.bookstore.model.ListItem
import com.example.bookstore.R
import com.example.bookstore.adapter.CartAdapter


class CartActivity : AppCompatActivity(), CartAdapter.ItemClickListen {

    lateinit var toolbar: androidx.appcompat.widget.Toolbar
    private lateinit var recycler: RecyclerView
    private lateinit var listItems:List<ListItem>
    lateinit var myDb: DatabaseHelper
    private lateinit var netAmount:TextView
    private lateinit var placeOreder: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        initializeView()
        toolbarInitialize()
        recyclerViewInit()

        netAmount.setText("Total: "+myDb.getPrice())

    }


    private fun initializeView() {
        toolbar = findViewById(R.id.toolBar)
        netAmount= findViewById(R.id.net_amount)
        placeOreder = findViewById(R.id.place_order)
        myDb = DatabaseHelper(this)
    }

    private fun toolbarInitialize() {

        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

        toolbar.setNavigationOnClickListener{finish()}
    }

    private fun recyclerViewInit() {
        recycler = findViewById(R.id.recyclerView)
        recycler.setHasFixedSize(true)
        recycler.layoutManager = LinearLayoutManager(this)
        listItems = mutableListOf()
        listItems = myDb.getCart()
        if(listItems.isNotEmpty()){
            recycler.adapter = CartAdapter(listItems, this, this)
        }
    }

    override fun onItemClick(v: View?, pos: Int) {
       finish()
    }

    override fun delete(v: View?, pos: Int) {
        val listItem = listItems[pos]
        val name:String = listItem.head
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Delete")
                .setMessage("Are you sure want to delete?")
                .setNegativeButton("cancel",null)
                .setPositiveButton("yes"){ dialog, _ ->
                    dialog.dismiss()
                    myDb.deleteCart(name)
                    listItems = myDb.getCart()
                    if(listItems.isNotEmpty()){
                        recycler.adapter = CartAdapter(listItems, this, this)
                    }
                }
        builder.show()
    }
}
