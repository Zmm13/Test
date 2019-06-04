package com.example.administrator.test.MyView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * 作者: zmm on 2017-08-24 10:21.
 * 描述:
 */

public class MyLoadingView extends View {
    private Paint mPaint;
    private int circle1Yoffset;
    private int circle2Yoffset;
    private int circle3Yoffset;
    private int offsetMaxValue = 35;
    private long looperTime = 350;
    private int distance = 20;
    private long speed = 10;
    boolean circle1Go = false;
    boolean circle2Go = false;
    boolean circle3Go = false;
    private int r = 5;
    private int textSize = 60;
    private String color = "#ffffff";
    private int circleDistance = 14;

    public void setCircleDistance(int circleDistance) {
        this.circleDistance = circleDistance;
    }

    public void setContent(String content) {
        this.content = content;
    }

    private int circle1Way = 0;
    private int circle2Way = 0;
    private int circle3Way = 0;
    private int width;
    private int height;
    private String content = "Loading";

    public MyLoadingView(Context context) {
        super(context);
        initPaint();
    }

    public MyLoadingView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initPaint();
    }

    public MyLoadingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //获取paint中的字体信息  settextSize要在他前面
        Paint.FontMetrics fontMetrics = mPaint.getFontMetrics();
// 计算文字高度
        float fontHeight = fontMetrics.bottom - fontMetrics.top;
//获取字体的长度
        float fontWidth = mPaint.measureText(content);
        canvas.drawText(content, width / 2 - fontWidth / 2, height / 2 + fontHeight / 4, mPaint);
        canvas.drawCircle(width / 2 + fontWidth / 2 + 2 * r, height / 2 + fontHeight / 4 - 2 * r + circle1Yoffset, r, mPaint);
        canvas.drawCircle(width / 2 + fontWidth / 2 + 4 * r + circleDistance, height / 2 + fontHeight / 4 - 2 * r + circle2Yoffset, r, mPaint);
        canvas.drawCircle(width / 2 + fontWidth / 2 + 6 * r + 2*circleDistance, height / 2 + fontHeight / 4 - 2 * r + circle3Yoffset, r, mPaint);
        postDelayed(runnable,5);
    }

    public void setOffsetMaxValue(int offsetMaxValue) {
        this.offsetMaxValue = offsetMaxValue;
    }

    public void setLooperTime(long looperTime) {
        this.looperTime = looperTime;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public void setSpeed(long speed) {
        this.speed = speed;
    }

    public void setR(int r) {
        this.r = r;
    }

    public void setTextSize(int textSize) {
        mPaint.setTextSize(textSize);
    }

    public void setColor(String color) {
        mPaint.setColor(Color.parseColor(color));
    }

    private void initPaint() {
        mPaint = new Paint();

        mPaint.setTextSize(textSize);
        mPaint.setColor(Color.parseColor(color));
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);
        circle1Yoffset = 0;
        circle2Yoffset = 0;
        circle3Yoffset = 0;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        width = getMeasuredWidth();//获取屏幕宽度
        height = getMeasuredHeight();//获取屏幕高度
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
                //判断运动方向
                if (circle1Yoffset == 0) {
                    circle1Way = -1;
                    if (circle2Yoffset != 0) {
                        circle1Go = false;
                    }
                } else if (circle1Yoffset == -offsetMaxValue) {
                    circle1Way = 1;
                }
                if (circle2Yoffset == 0) {
                    circle2Way = -1;
                    if (circle3Yoffset != 0 && circle1Yoffset == 0) {
                        circle2Go = false;
                    }
                } else if (circle2Yoffset == -offsetMaxValue) {
                    circle2Way = 1;
                }
                if (circle3Yoffset == 0) {
                    circle3Way = -1;
                    if (circle2Yoffset == 0 && circle1Yoffset == 0) {
                        circle3Go = false;
                    }
                } else if (circle3Yoffset == -offsetMaxValue) {
                    circle3Way = 1;
                }
                //判断第二个圆点启动
                if (circle1Yoffset == 0 && circle2Yoffset == 0 && circle3Yoffset == 0) {
                    //一轮结束间隔时间
                    try {
                        Thread.sleep(looperTime);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    circle1Go = true;
                }
                //判断第二个圆点启动
                if (circle1Yoffset == -distance) {
                    circle2Go = true;
                }
                //判断第三个圆点启动
                if (circle2Yoffset == -distance) {
                    circle3Go = true;
                }
                //第一个圆点位移
                if (circle1Go) {
                    circle1Yoffset = circle1Yoffset + circle1Way;
                }
                //第二个圆点位移
                if (circle2Go) {
                    circle2Yoffset = circle2Yoffset + circle2Way;
                }
                //第三个圆点位移
                if (circle3Go) {
                    circle3Yoffset = circle3Yoffset + circle3Way;
                }
                invalidate();
        }
    };
}
