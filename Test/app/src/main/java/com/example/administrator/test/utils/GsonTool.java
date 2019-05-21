package com.example.administrator.test.utils;

import com.google.gson.Gson;

import java.util.PropertyResourceBundle;

/**
 * Create by zmm
 * Time 2019/5/21
 * PackageName com.example.administrator.test.utils
 */
public class GsonTool {
    private static volatile GsonTool instance = null;
    private Gson gson = null;

    private GsonTool() {
        gson = new Gson();
    }

    public static GsonTool getInstance() {
        if (instance == null) {
            synchronized (GsonTool.class) {
                if (instance == null) {
                    instance = new GsonTool();
                }
            }
        }
        return instance;
    }

    public <T> T getObject(Class<T> c, String json) {
        return gson.fromJson(json, c);
    }
}
