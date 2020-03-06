package com.example.bookstore.activity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.bookstore.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class FCMActivity extends FirebaseMessagingService {
    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
            @Override
            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                if (!task.isSuccessful()) {
                    Log.w("Firebase Service", "getInstanceId Failed", task.getException());
                    return;
                }

                String token = task.getResult().getToken();
                Log.d("Firebase Service", token);
            }
        });


    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        sendMyNotification(remoteMessage.getNotification().getBody());
    }

    private void sendMyNotification(String body) {
        Intent intent = new Intent(FCMActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("message",body);
        PendingIntent pendingIntent = PendingIntent.getActivity(FCMActivity.this,0,intent,PendingIntent.FLAG_ONE_SHOT);

        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            //@SuppressLint("WrongConstant")
            NotificationChannel notificationChannel=new NotificationChannel("BookStore_notification","book_channel",NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setDescription("description");
            notificationChannel.setName("Channel Name");
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            if (manager != null) {
                manager.createNotificationChannel(notificationChannel);
            }
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,"channel");
        builder.setContentTitle("BookStore")
                .setContentText("New Message")
                .setSmallIcon(R.drawable.notification_icon)
                .setTicker("Ticker")
                .setStyle(new NotificationCompat.BigTextStyle()
                .bigText("New Message From BookStore Recieved"))
                .setContentIntent(pendingIntent)
                .setSound(soundUri)
                .setAutoCancel(true);


        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (manager != null) {
            manager.notify(1, builder.build());
        }

    }
}
