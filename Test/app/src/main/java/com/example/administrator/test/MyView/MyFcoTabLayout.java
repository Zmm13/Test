package com.example.administrator.test.MyView;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.util.AttributeSet;

import com.flyco.tablayout.CommonTabLayout;

/**
 * Create by zmm
 * Time 2019/5/27
 * PackageName com.example.administrator.test.MyView
 */
public class MyFcoTabLayout extends CommonTabLayout {
    public MyFcoTabLayout(Context context) {
        super(context);
    }

    public MyFcoTabLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyFcoTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

//    @BindingAdapter("app:tl_textUnselectColor")
//    public static void setTextColor(CommonTabLayout commonTabLayout, int color) {
//       if(commonTabLayout.getTextUnselectColor() != color){
//           commonTabLayout.setTextSelectColor(color);
//       }
//    }
    @BindingAdapter("app:tl_textUnselectColor")
    public static void setTextSize(CommonTabLayout commonTabLayout, int color) {
       if(commonTabLayout.getTextUnselectColor() != color){
           commonTabLayout.setTextUnselectColor(color);
       }
    }
//    @BindingAdapter("app:tl_textUnselectColor")
//    public static void setTextSize(CommonTabLayout commonTabLayout, int color) {
//       if(commonTabLayout.getTextUnselectColor() != color){
//           commonTabLayout.setTextSelectColor(color);
//       }
//    }
}
