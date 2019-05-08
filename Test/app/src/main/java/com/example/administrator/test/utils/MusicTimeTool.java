package com.example.administrator.test.utils;

import java.text.SimpleDateFormat;

/**
 * Create by zmm
 * Time 2019/5/6
 * PackageName com.example.administrator.test.utils
 */
public class MusicTimeTool {
    /**
     * 获取音乐显示时间
     *
     * @param time
     * @return
     */
    public static String getMusicTime(long time) {
        long second = time / 1000;
        long yuSecond = second % 60;
        long minute = second / 60;
        String yuSe = yuSecond + "";
        if (yuSecond < 10) {
            yuSe = "0" + yuSecond;
        }
        return minute + ":" + yuSe;
    }
}
