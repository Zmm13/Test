package com.example.administrator.test.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.administrator.test.Main2Activity;

/**
 * Create by zmm
 * Time 2019/5/14
 * PackageName com.example.administrator.test.receiver
 */
public class MyStatic2Receiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
            Intent intent1 = new Intent(context, Main2Activity.class);
            intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent1);
            abortBroadcast();
        }
    }
}
