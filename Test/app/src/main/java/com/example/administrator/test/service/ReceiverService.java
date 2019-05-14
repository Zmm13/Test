package com.example.administrator.test.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;

import com.example.administrator.test.receiver.MyAuto2Receiver;
import com.example.administrator.test.receiver.MyAutoReceiver;

/**
 * Create by zmm
 * Time 2019/5/14
 * PackageName com.example.administrator.test.service
 */
public class ReceiverService extends Service {
    private final String CHANNEL_ID = "CHANNEL_ID_SERVICE_RECEIVER";
    private final String CHANNEL_NAME = "CHANNEL_NAME_SERVICE_RECEIVER";
    private MyAutoReceiver myStaticReceiver;
    private MyAuto2Receiver myStaticReceiver1;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate() {
        super.onCreate();
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH);

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.createNotificationChannel(channel);

        Notification notification = new Notification.Builder(getApplicationContext(), CHANNEL_ID).build();
        startForeground(1, notification);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        IntentFilter filter = new IntentFilter();
        filter.addAction("MY_BROADCAST");
//        filter.addAction("android.intent.action.PACKAGE_ADDED");
//        filter.addAction("android.intent.action.PACKAGE_REMOVED");
//        filter.addAction("android.intent.action.PACKAGE_REPLACED");
//        filter.addDataScheme("package");
        filter.setPriority(0);
        myStaticReceiver = new MyAutoReceiver();
//        注册广播接收
        registerReceiver(myStaticReceiver, filter);

        IntentFilter filter1 = new IntentFilter();
        filter1.addAction("MY_BROADCAST");
//        filter1.addAction("android.intent.action.PACKAGE_ADDED");
//        filter1.addAction("android.intent.action.PACKAGE_REMOVED");
//        filter1.addAction("android.intent.action.PACKAGE_REPLACED");
//        filter1.addDataScheme("package");
        filter1.setPriority(1);
        myStaticReceiver1 = new MyAuto2Receiver();
        //注册广播接收
        registerReceiver(myStaticReceiver1, filter1);

        System.out.println("myStaticReceiver is ok");
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(myStaticReceiver);
        unregisterReceiver(myStaticReceiver1);
    }
}
