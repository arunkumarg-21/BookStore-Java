package com.example.bookstore;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    public interface ItemClickListener {
        void onItemClick(View v, int pos);
        void onItemLongClick(View v,int pos);
    }

    private List<ListItem> listItems;
    private Context context;
    private ItemClickListener itemClickListener,itemLongClickListener;
    private DatabaseHelper myDb;

    public Adapter(List<ListItem> listItems, Context context, ItemClickListener itemClickListener) {
        this.listItems = listItems;
        this.context = context;
        this.itemClickListener = itemClickListener;
        this.itemLongClickListener=itemClickListener;
        myDb = new DatabaseHelper(context);
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

        holder.textHead.setText(listItem.getHead());
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

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener {

        TextView textHead;
        TextView textDesc;
        ImageView imageView;
        ItemClickListener itemClickListener,itemLongClickListener;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);

            textHead = itemView.findViewById(R.id.textViewHead);
            textDesc = itemView.findViewById(R.id.Desc);
            imageView = itemView.findViewById(R.id.img);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);


        }

        @Override
        public void onClick(View v) {
            this.itemClickListener.onItemClick(v, getLayoutPosition());
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
