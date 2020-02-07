package com.example.bookstore;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


public class BuyActivity extends AppCompatActivity {

    private static final String CHANNEL_ID ="channel" ;
    int notificationId=1;
    private NotificationManagerCompat notificationManager;
    NotificationManager manager=null;
    ImageView iV;
    TextView tV,price;
    EditText address;
    Button mBuy;
    int img;
    String Name;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy);

        iV = findViewById(R.id.image);
        tV  =findViewById(R.id.name);
        price=findViewById(R.id.price);
        mBuy=findViewById(R.id.buy);

        price.setText("Price: 120");




        Intent intent = getIntent();
        Name = intent.getStringExtra("name");
        tV.setText(Name);
        img = intent.getIntExtra("img",0);
        iV.setImageResource(img);


        notificationManager = NotificationManagerCompat.from(BuyActivity.this);

    }

    public void notify(View v){
        Intent intent = new Intent(this,BuyActivity.class);
        intent.putExtra("name",Name);
        intent.putExtra("img",img);
        TaskStackBuilder stackBuilder=TaskStackBuilder.create(BuyActivity.this);
        stackBuilder.addNextIntentWithParentStack(intent);
        PendingIntent pendingIntent=stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,CHANNEL_ID);
        builder.setContentTitle("BookStore")
                .setContentText("Book Order Placed")
                .setSmallIcon(R.drawable.notification_icon)
                .setTicker("Ticker")
                .setStyle(new NotificationCompat.BigTextStyle()
                .bigText("New Message from the BookStore App."))
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(notificationId,builder.build());
    }

    /*private void createNotificationChannel() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
        
    }
*/
}
