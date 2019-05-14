package com.example.administrator.test.service;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Create by zmm
 * Time 2019/5/14
 * PackageName com.example.administrator.test.service
 */
public class MyTestService extends Service {
    @Override
    public void onCreate() {
        super.onCreate();
        System.out.println("onCreate");
    }

    @Override
    public void onStart(Intent intent, int startId) {
        System.out.println("onStart");
        super.onStart(intent, startId);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        System.out.println("onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        System.out.println("onBind");
        return new MyBinder();
    }

    @Override
    public void unbindService(ServiceConnection conn) {
        System.out.println("unbindService");
        super.unbindService(conn);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        System.out.println("onDestroy");
    }
    class MyBinder extends Binder{

    }
}
