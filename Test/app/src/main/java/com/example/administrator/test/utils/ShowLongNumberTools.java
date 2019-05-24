package com.example.administrator.test.utils;

import java.text.DecimalFormat;

/**
 * Create by zmm
 * Time 2019/5/24
 * PackageName com.example.administrator.test.utils
 */
public class ShowLongNumberTools {
    public static String getShow(long i){
        String result = "";
        if(i<1000L){
            result  = i+"";
        }else if(i<10000L){
            result  =division(i,1000L)+"千";
        }else if(i<100000000L){
            result =division(i,10000L) +"万";
        }else if(i<1000000000000L){
            result = division(i,100000000L) +"亿";
        }else {
            result =division(i,1000000000000L) +"兆";
        }
        return result;
    }

    //整数相除 保留一位小数
    public static String division(long a ,long b){
        String result = "";
        float num =(float)a/b;

        DecimalFormat df = new DecimalFormat("0.0");

        result = df.format(num);

        return result;

    }
}
