package com.example.administrator.test.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
 * Create by zmm
 * Time 2019/5/28
 * PackageName com.example.administrator.test.utils
 */
public class ActivityUtils {

    public static void startActivity(Context context, Class c) {
        Intent intent = new Intent(context, c);
        context.startActivity(intent);
    }

    public static void startActivity(Context context, Class c, Bundle bundle) {
        Intent intent = new Intent(context, c);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }
}
