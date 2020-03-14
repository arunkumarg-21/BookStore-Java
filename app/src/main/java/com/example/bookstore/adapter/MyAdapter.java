package com.example.bookstore.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookstore.util.DatabaseHelper;
import com.example.bookstore.R;
import com.example.bookstore.model.ListItem;

import java.util.List;

 public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    public interface ItemClickListener {
        void onItemClick(View v, int pos);
        void onItemLongClick(View v,int pos);
        void cartInsert(View v,int pos);
    }

    private List<ListItem> listItems;
    private Context context;
    private ItemClickListener itemClickListener,itemLongClickListener;

    public MyAdapter(List<ListItem> listItems, Context context, ItemClickListener itemClickListener) {
        this.listItems = listItems;
        this.context = context;
        this.itemClickListener = itemClickListener;
        this.itemLongClickListener=itemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final ListItem listItem = listItems.get(position);

        holder.textName.setText(listItem.getHead());
        holder.textDesc.setText(listItem.getDesc());

        byte[] image  = listItem.getmImage();
        Bitmap bitmap  = BitmapFactory.decodeByteArray(image,0,image.length);
        holder.imageView.setImageBitmap(bitmap);

        holder.setItemClickListener(itemClickListener);
        holder.setItemLongClickListener(itemLongClickListener);
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener {

        TextView textName;
        TextView textDesc;
        ImageView imageView;
        ImageButton cartButton;
        RatingBar ratingBar;
        ItemClickListener itemClickListener,itemLongClickListener;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);

            textName = itemView.findViewById(R.id.name);
            textDesc = itemView.findViewById(R.id.Desc);
            imageView = itemView.findViewById(R.id.bookImage);
            cartButton = itemView.findViewById(R.id.cart_button);
            ratingBar = itemView.findViewById(R.id.rating);
            ratingBar.setRating(3.5f);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            cartButton.setOnClickListener(this);
            imageView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.bookImage:
                    break;
                case R.id.cart_button:
                    this.itemClickListener.cartInsert(v, getLayoutPosition());
                    break;
                default:
                    this.itemClickListener.onItemClick(v, getLayoutPosition());
            }
        }

        @Override
        public boolean onLongClick(View v){
            this.itemLongClickListener.onItemLongClick(v,getLayoutPosition());
            return true;
        }

        public void setItemClickListener(ItemClickListener ic) {
            this.itemClickListener = ic;

        }

        public void setItemLongClickListener(ItemClickListener ilc){
            this.itemLongClickListener = ilc;
        }

    }


}
