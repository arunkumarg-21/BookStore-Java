package com.example.bookstore.api.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class MyReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        /*Intent i = new Intent(context,MainActivity.class);
        context.startActivity(i);*/
       Toast.makeText(context,"BroadCastMessage Received",Toast.LENGTH_SHORT).show();
    }
}
