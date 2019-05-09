package com.example.administrator.test.utils;

import android.content.Context;
import android.graphics.Color;

/**
 * Create by zmm
 * Time 2019/5/5
 * PackageName com.example.administrator.test.utils
 */
public class StaticBaseInfo {
    public static int baseColor_light = Color.parseColor("#ffffff");
    public static int baseColor_dark = Color.parseColor("#000000");
    public static int color_divider_dark = Color.parseColor("#cacaca");
    public static int color_divider_light = Color.parseColor("#33ffffff");
    public static int color_item_light = Color.parseColor("#09000000");
    public static int color_item_dark = Color.parseColor("#11ffffff");

    public static boolean isLight(Context context) {
        return SpTool.getBoolean(context, SP_NAME, IS_LIGHT);
    }

    public static void fanIsLight(Context context) {
        SpTool.saveBoolean(context, SP_NAME, IS_LIGHT, !isLight(context));
    }

    public static final int MAINACTIVITY_REQUEST_CODE_WRITE_EXTERNAL_STORAGE = 1;
    public static final int MAINACTIVITY_REQUEST_CODE_READ_EXTERNAL_STORAGE = 2;

    public static final String SP_NAME = "SP_NAME";
    public static final String SP_INIT_MUSIC = "SP_INIT_MUSIC";
    public static final String SP_PLAY_SONG = "SP_PLAY_SONG";
    public static final String IS_LIGHT = "IS_LIGHT";


    public static int baseColor = Color.parseColor("#000000");

    public static int getBaseColor2() {
        return baseColor;
    }

    public static void setBaseColor(int baseColor) {
        StaticBaseInfo.baseColor = baseColor;
    }
}


