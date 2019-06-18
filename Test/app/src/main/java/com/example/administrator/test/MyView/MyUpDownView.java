package com.example.administrator.test.MyView;

import android.content.Context;
import android.media.AudioManager;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;


/**
 * Create by zmm
 * Time 2019/6/10
 * PackageName com.example.administrator.test.MyView
 */
public class MyUpDownView extends View {
    private float startY;
    private int currentLevel = 0;
    private int maxLevel = 0;
    private UpDateInterface upDateInterface;
    private int max;
    private int current;
    private long downTime;
    private long clickTime = 500;

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public void setUpDateInterface(UpDateInterface upDateInterface) {
        this.upDateInterface = upDateInterface;
    }


    public interface UpDateInterface {
        void update(MyUpDownView myUpDownView, float distance);

        void down(MyUpDownView myUpDownView);

        void up(MyUpDownView myUpDownView);

        void click(MyUpDownView myUpDownView);
    }


    public MyUpDownView(Context context) {
        super(context);
    }

    public MyUpDownView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyUpDownView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public MyUpDownView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downTime = System.currentTimeMillis();
                startY = event.getY();
                if (upDateInterface != null) {
                    upDateInterface.down(this);
                }
                return true;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (upDateInterface != null) {
                    upDateInterface.up(this);
                    if (System.currentTimeMillis() - downTime < clickTime) {
                        upDateInterface.click(this);
                    }
                }
                return true;
            case MotionEvent.ACTION_MOVE:
                if (upDateInterface != null&&System.currentTimeMillis() - downTime > clickTime) {
                    upDateInterface.update(this, startY - event.getY());
                }
                return true;
            default:
                return false;
        }
    }
}
