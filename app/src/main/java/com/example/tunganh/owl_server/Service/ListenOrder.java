package com.example.tunganh.owl_server.Service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import com.example.tunganh.owl_server.Model.Order_Details;
import com.example.tunganh.owl_server.Order_status.Order_status;
import com.example.tunganh.owl_server.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Random;

public class ListenOrder extends Service implements ChildEventListener {
    FirebaseDatabase db;
    DatabaseReference orders;

    @Override
    public void onCreate() {
        super.onCreate();
        db=FirebaseDatabase.getInstance();
        orders=db.getReference("Order_Details");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        orders.addChildEventListener(this);
        return super.onStartCommand(intent, flags, startId);
    }

    public ListenOrder() {

    }

    @Override
    public IBinder onBind(Intent intent) {
       return null;
    }

    @Override
    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            ///////////
        Order_Details order_details=dataSnapshot.getValue(Order_Details.class);
        if (order_details.getStatus().equals("0"))
            showNotification(dataSnapshot.getKey(),order_details);


    }

    private void showNotification(String key, Order_Details order_details) {

        Intent i = new Intent(getBaseContext(), Order_status.class);
        PendingIntent contentIntent = PendingIntent.getActivity(getBaseContext(),0,i,0);

        NotificationCompat.Builder builder=new NotificationCompat.Builder(getBaseContext());
        builder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setContentTitle("You have new order !!!")
                .setContentText("New Order:  #"+key)
                .setContentInfo("Info")
                .setContentIntent(contentIntent)
                .setSmallIcon(R.drawable.ic_notifications_black_24dp);

        NotificationManager manager=(NotificationManager)getBaseContext().getSystemService(Context.NOTIFICATION_SERVICE);

        ////// Nếu muốn show nhiều notification ---> give 1 Id cho mỗi thông báo
        int randomInt=new Random().nextInt(999-1)+1;
        manager.notify(randomInt,builder.build());

    }

    @Override
    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

    }

    @Override
    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

    }

    @Override
    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }
}
