package com.example.bookstore.activity


import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bookstore.R
import com.example.bookstore.adapter.CartAdapter
import com.example.bookstore.model.ListItem
import com.example.bookstore.util.DatabaseHelper
import kotlinx.android.synthetic.main.activity_cart.*


class CartActivity : AppCompatActivity(), CartAdapter.ItemClickListen {

    private lateinit var recycler: RecyclerView
    private lateinit var listItems:List<ListItem>
    lateinit var myDb: DatabaseHelper
    private lateinit var amount:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        myDb = DatabaseHelper(this)
        toolbarInitialize()
        recyclerViewInit()
        amount = myDb.price
        net_amount.text = "Total:" + amount

        place_order.setOnClickListener {
            val i = Intent(this@CartActivity,ProcessPaytm::class.java)
            i.putExtra("total",amount)
            startActivity(i)

        }
    }



    private fun toolbarInitialize() {

        setSupportActionBar(toolBar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

        toolBar.setNavigationOnClickListener{finish()}
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
        val listItem = listItems[pos]
        val head = listItem.head
        val desc = listItem.desc
        val image = listItem.getmImage()
        val price = listItem.price


        val intent = Intent(this@CartActivity, BookActivity::class.java)
        intent.putExtra("head", head)
        intent.putExtra("desc", desc)
        intent.putExtra("image", image)
        intent.putExtra("price", price)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
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
                    amount = myDb.price
                    net_amount.text = "Total: " + amount
                    listItems = myDb.getCart()
                    if(listItems.isNotEmpty()){
                        recycler.adapter = CartAdapter(listItems, this, this)
                    }
                }
        builder.show()
    }
}
