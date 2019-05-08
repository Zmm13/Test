package com.example.administrator.test.utils;

import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;

/**
 * Created by Administrator on 2016/8/2 0002.
 */
public class TextUtil {

    /**
      *判断字符串是否是空字符串
      *@auther Leo
      *created at 2016/8/2 0002 12:57
      */
    public static boolean isEmpty(String str){
        if(str == null || "".equals(str) || "null".equals(str)){
            return true ;
        }else{
            return false ;
        }
    }
    public static SpannableStringBuilder getChargeRecordContent(String text, String colorText, int color){
        SpannableStringBuilder style=new SpannableStringBuilder(text);
        int index = text.indexOf(colorText) ;
        style.setSpan(new ForegroundColorSpan(color),
                index, colorText.length()+index, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        return style ;
    }
    public static String filterOffUtf8Mb4(String buff) {
        String regEx = "[^0-9\u4E00-\u9FA5]";
        return buff.replaceAll(regEx,"").trim() ;
    }
    /**
     * 半角转换为全角
     *
     * @param input
     * @return
     */
    public static String ToDBC(String input) {
        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == 12288) {// 全角空格为12288，半角空格为32
                c[i] = (char) 32;
                continue;
            }
            if (c[i] > 65280 && c[i] < 65375)// 其他字符半角(33-126)与全角(65281-65374)的对应关系是：均相差65248
                c[i] = (char) (c[i] - 65248);
        }
        return new String(c);
    }
}
