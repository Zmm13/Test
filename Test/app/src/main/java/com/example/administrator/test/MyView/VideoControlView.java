package com.example.administrator.test.MyView;

import android.content.Context;
import android.media.Image;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * Create by zmm
 * Time 2019/6/10
 * PackageName com.example.administrator.test.MyView
 */
public class VideoControlView extends LinearLayout {
    private TouchInterface touchInterface;

    public void setTouchInterface(TouchInterface touchInterface) {
        this.touchInterface = touchInterface;
    }

    public VideoControlView(Context context) {
        super(context);
    }

    public VideoControlView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public VideoControlView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public VideoControlView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (touchInterface != null) {
            touchInterface.doTouch();
        }
        return super.onInterceptTouchEvent(ev);
    }

    public interface TouchInterface {
        void doTouch();
    }
}
