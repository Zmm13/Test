package com.example.administrator.test.utils;

import android.os.Handler;
import android.os.Looper;

/**
 * 作者: MR.ZMM on 2017-06-15 12:52.
 * 描述:
 */

public class MainHandler extends Handler {
    private static volatile MainHandler instance;

    public static MainHandler getInstance() {
        if (null == instance) {
            synchronized (MainHandler.class) {
                if (null == instance) {
                    instance = new MainHandler();
                }
            }
        }
        return instance;
    }
    private MainHandler() {
        super(Looper.getMainLooper());
    }
}
