package com.example.bookstore;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.ByteArrayOutputStream;
import java.net.URI;
import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    private List<ListItem> listItems;
    private Context context;


    public Adapter(List<ListItem> listItems, Context context) {
        this.listItems = listItems;
        this.context=context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final ListItem listItem = listItems.get(position);

        holder.textHead.setText(listItem.getHead());
        holder.textDesc.setText(listItem.getDesc());
        holder.imageView.setImageDrawable(context.getResources().getDrawable(listItem.getmImage()));

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                String head = listItem.getHead();
                String desc = listItem.getDesc();
                /**BitmapDrawable bitmapDrawable =(BitmapDrawable)holder.imageView.getDrawable();
                Bitmap bitmap = bitmapDrawable.getBitmap();
                ByteArrayOutputStream stream=new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);
                byte[] bytes=stream.toByteArray();*/

                Intent intent = new Intent(context,BookActivity.class);
                intent.putExtra("head",head);
                intent.putExtra("desc",desc);
                intent.putExtra("image",listItem.getmImage());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView textHead;
        TextView textDesc;
        ImageView imageView;
        ItemClickListener itemClickListener;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);

            textHead = itemView.findViewById(R.id.textViewHead);
            textDesc = itemView.findViewById(R.id.Desc);
            imageView=itemView.findViewById(R.id.img);
            itemView.setOnClickListener(this);


        }

        @Override
        public void onClick(View v) {
            this.itemClickListener.onItemClick(v,getLayoutPosition());

        }
        public void setItemClickListener(ItemClickListener ic){
            this.itemClickListener = ic;
        }
    }


}
