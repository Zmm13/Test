package com.example.administrator.test;


import android.support.v7.app.AppCompatActivity;

import com.example.administrator.test.minterfcae.MusiPlaycUpdateInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.http.PUT;

/**
 * Create by zmm
 * Time 2019/5/29
 * PackageName PACKAGE_NAME
 */
public class ActivityManager {
    //刷新ui接口集合
    private static List<AppCompatActivity> list;
    private static volatile ActivityManager manager = null;

    private ActivityManager() {
        list = new ArrayList<>();
    }

    public static ActivityManager getManager() {
        if (manager == null) {
            synchronized (ActivityManager.class) {
                if (manager == null) {
                    manager = new ActivityManager();
                }
            }
        }
        return manager;
    }

    public void add(AppCompatActivity activity) {
        list.add(activity);
    }

    public void remove(AppCompatActivity activity) {
        list.remove(activity);
    }

    public boolean isTop(AppCompatActivity activity) {
        return list.size()>0 ? list.indexOf(activity) == list.size() - 1 : false;
    }
}
