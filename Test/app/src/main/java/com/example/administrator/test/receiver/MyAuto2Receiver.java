package com.example.administrator.test.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Create by zmm
 * Time 2019/5/14
 * PackageName com.example.administrator.test.receiver
 */
public class MyAuto2Receiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        System.out.println("MyAuto2Receiver"+intent.getAction());
        abortBroadcast();
//        abortBroadcast();
//        switch (intent.getAction()){
//            case "android.intent.action.PACKAGE_ADDED":
//                Toast.makeText(context,"有应用安装了", Toast.LENGTH_SHORT).show();
//                break;
//            case "android.intent.action.PACKAGE_REMOVED":
//                Toast.makeText(context,"有应用卸载了", Toast.LENGTH_SHORT).show();
//                break;
//            case "android.intent.action.PACKAGE_REPLACED":
//                Toast.makeText(context,"有应用覆盖了", Toast.LENGTH_SHORT).show();
//                break;
//        }
    }
}
