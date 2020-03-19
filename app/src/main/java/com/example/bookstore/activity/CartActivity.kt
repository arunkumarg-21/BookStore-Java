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
import kotlinx.android.synthetic.main.toolbar.*


class CartActivity : AppCompatActivity(), CartAdapter.ItemClickListen {

    private lateinit var recycler: RecyclerView
    private lateinit var listItems:List<ListItem>
    private lateinit var myDb: DatabaseHelper
    private var amount:Double = 0.0
    private val total = "TOTAL:"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        myDb = DatabaseHelper(this)
        toolbarInitialize()
        recyclerViewInit()
        amount = myDb.price
        net_amount.text = total.plus(amount)
        place_order.setOnClickListener {
            val i = Intent(this@CartActivity, PayTmActivity::class.java)
            i.putExtra("total",amount.toString())
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
        listItems = myDb.cart
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
        val id:Int = listItem.id
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Delete")
                .setMessage("Are you sure want to delete?")
                .setNegativeButton("cancel",null)
                .setPositiveButton("yes"){ dialog, _ ->
                    dialog.dismiss()
                    myDb.deleteCart(id)
                    amount = myDb.price
                    net_amount.text = total.plus( amount)
                    listItems = myDb.cart
                    if(listItems.isNotEmpty()){
                        recycler.adapter = CartAdapter(listItems, this, this)
                    }
                }
        builder.show()
    }

    override fun minusPrice(v: View?, pos: Int) {
        val listItem = listItems[pos]
        val price:Double = listItem.price
        amount -= price
        net_amount.text = total.plus(amount)
    }

    override fun addPrice(v: View?, pos: Int) {
        val listItem = listItems[pos]
        val price:Double = listItem.price
        amount += price
        net_amount.text = total.plus(amount)
    }
}
