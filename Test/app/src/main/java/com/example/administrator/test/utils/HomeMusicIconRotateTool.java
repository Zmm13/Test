package com.example.administrator.test.utils;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.os.Build;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;

/**
 * Create by zmm
 * Time 2019/5/6
 * PackageName com.example.administrator.test.utils
 */
public class HomeMusicIconRotateTool {

    public static ObjectAnimator objectAnimator = null;

    public static boolean isRotate = false;

    public static void rotateView(boolean isRotate, View view) {
        if (objectAnimator == null) {
            initAnimator(view);
        }
        if (isRotate) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                if (objectAnimator.isPaused()) {
                    objectAnimator.resume();
                    return;
                }
            }
            if (!objectAnimator.isRunning()) {
                objectAnimator.start();
            }
        } else {
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    objectAnimator.pause();
                } else {
                    objectAnimator.end();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void rotateView(boolean reset, boolean isPlay, View view) {
        if (reset) {
            objectAnimator = null;
            view.setRotation(0);
            return;
        }
        if (isPlay) {
            if (objectAnimator == null) {
                initAnimator(view);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                if (objectAnimator.isPaused()) {
                    objectAnimator.resume();
                    return;
                }
            }
            if (!objectAnimator.isRunning()) {
                objectAnimator.start();
            }
        } else {
            try {
                if (objectAnimator != null) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        objectAnimator.pause();
                    } else {
                        objectAnimator.end();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void pauseAnimator() {
        if (objectAnimator != null && objectAnimator.isRunning()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                objectAnimator.pause();
            } else {
                objectAnimator.end();
            }
        }
    }

    @SuppressLint("WrongConstant")
    private static void initAnimator(View view) {
        objectAnimator = ObjectAnimator.ofFloat(view, "rotation", 0f, 360f);
        objectAnimator.setDuration(15000);
        objectAnimator.setRepeatCount(Animation.INFINITE);
        objectAnimator.setRepeatMode(Animation.RESTART);
        objectAnimator.setInterpolator(new LinearInterpolator());
        objectAnimator.setRepeatCount(-1);
    }

}
