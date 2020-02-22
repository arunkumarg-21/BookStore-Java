package com.example.bookstore.activity

import android.content.Context
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bookstore.model.ListItem
import com.example.bookstore.R

class CartAdapter(listItems: List<ListItem>?, context: Context, itemClickListen: ItemClickListen) : RecyclerView.Adapter<CartAdapter.ViewHolder>() {

    private var listItems: List<ListItem>? = listItems
    private var context: Context? = context
    private var itemClickListen: ItemClickListen? = itemClickListen

    interface ItemClickListen {
        fun onItemClick(v: View?, pos: Int)
        fun delete(v: View?, pos: Int)
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        /*fun bindItems(item:ListItem) {

           }*/
        val textName: TextView
        val textDesc: TextView
        val image: ImageView
        val price: TextView
        val deleteButton: Button
        var itemClickListen: ItemClickListen? = null

        init {
            textName = itemView.findViewById(R.id.product_name)
            textDesc = itemView.findViewById(R.id.product_description)
            image = itemView.findViewById(R.id.product_image)
            price = itemView.findViewById(R.id.product_price)
            deleteButton = itemView.findViewById(R.id.product_remove)
            itemView.setOnClickListener(this)
            deleteButton.setOnClickListener(this)
        }


        override fun onClick(v: View?) {
            println("onclick========")
            if(v?.id == R.id.product_remove) {
                this.itemClickListen?.delete(v, layoutPosition)
            }
                else this.itemClickListen?.onItemClick(v, layoutPosition)
            }

        fun setItemClickListener(ic: ItemClickListen?) {
            itemClickListen = ic
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.cart_items, parent, false)
        return ViewHolder(v)

    }

    override fun getItemCount(): Int {
        return listItems!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val listItem = listItems!![position]

        holder.textName.text = listItem.head
        holder.textDesc.text = listItem.desc
        holder.price.text = listItem.price.toString()

        val image = listItem.getmImage()
        val bitmap = BitmapFactory.decodeByteArray(image, 0, image.size)
        holder.image.setImageBitmap(bitmap)

        holder.setItemClickListener(itemClickListen)

    }

}