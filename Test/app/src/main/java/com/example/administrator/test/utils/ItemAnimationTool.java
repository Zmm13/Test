package com.example.administrator.test.utils;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.example.administrator.test.R;

/**
 * Create by zmm
 * Time 2019/5/24
 * PackageName com.example.administrator.test.utils
 */
public class ItemAnimationTool {
    /**
     * item上滑动画
     * @param view
     * @param context
     */
    public static void loadAnimationUp(View view, Context context){
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.animaition_list_scroll_up );
            view.startAnimation(animation);
    }
    /**
     * item下滑动画
     * @param view
     * @param context
     */
    public static void loadAnimationDown(View view, Context context){
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.animaition_list_scroll_down );
            view.startAnimation(animation);
    }
}
