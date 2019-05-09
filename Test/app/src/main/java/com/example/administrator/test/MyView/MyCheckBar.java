package com.example.administrator.test.MyView;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.example.administrator.test.R;

/**
 * Create by zmm
 * Time 2019/5/9
 * PackageName com.example.administrator.test.MyView
 */
public class MyCheckBar extends View {

    private boolean isCheck;//是否选中
    private boolean isChange = false;//是否正在变化
    private float width_height = 2f;//长宽比例
    private float r_R = 0.8f;//长宽比例
    private float mwidth = 120;//长宽比例
    private float mheight = mwidth / width_height;//长宽比例
    private Paint paint;
    private int bg_color_default;
    private int bg_color_checked;
    private int circle_color_default;
    private int circle_color_checked;
    private long mDuration = 1500;
    private float width_change;

    private AnimatorListenerAdapter mAnimatorListener;
    private ValueAnimator.AnimatorUpdateListener mAnimatorUpdateListener;

    private final String TAG = "MyCheckBar";

    public MyCheckBar(Context context) {
        super(context);
        initPaint();
    }

    public MyCheckBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.MyCheckBar);
        bg_color_default = array.getColor(R.styleable.MyCheckBar_bg_color_default, Color.parseColor("#E0E0E0"));
        bg_color_checked = array.getColor(R.styleable.MyCheckBar_bg_color_checked, Color.parseColor("#66CD00"));
        circle_color_default = array.getColor(R.styleable.MyCheckBar_circle_color_default, Color.parseColor("#E0EEEE"));
        circle_color_checked = array.getColor(R.styleable.MyCheckBar_circle_color_checked, Color.parseColor("#1afa29"));
        isCheck = array.getBoolean(R.styleable.MyCheckBar_isCheck, false);
        initPaint();
    }

    public MyCheckBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
    }

    @SuppressLint("NewApi")
    public MyCheckBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initPaint();
    }

    private void initPaint() {
        paint = new Paint();
        paint.setAntiAlias(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);   //获取宽的模式
        int heightMode = MeasureSpec.getMode(heightMeasureSpec); //获取高的模式
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);   //获取宽的尺寸
        int heightSize = MeasureSpec.getSize(heightMeasureSpec); //获取高的尺寸
        int width;
        int height;
        if (widthMode == MeasureSpec.EXACTLY) {
            //如果match_parent或者具体的值，直接赋值
            width = widthSize;
            int h = (int) Math.ceil(width / width_height);
            setMeasuredDimension(width, (int) h);
            return;
        }
        //高度跟宽度处理方式一样
        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
            int w = (int) Math.ceil(height * width_height);
            setMeasuredDimension(w, height);
            return;
        }
        setMeasuredDimension((int) mwidth, (int) (mheight));
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mwidth = getWidth();
        mheight = mwidth / width_height;
        width_change = (isCheck ? mwidth - mheight / 2 : mheight / 2);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setColor(bg_color_default);
        float r = mheight / 2 * r_R;
        float d = mheight / 2 - r * r_R;
        RectF oval1 = new RectF(d, d, mwidth - d, mheight - d);// 设置个新的长方形
        canvas.drawRoundRect(oval1, r, r, paint);//第二个参数是x半径，第三个参数是y半径
        paint.setColor(bg_color_checked);
        RectF oval2 = new RectF(d, d, isChange ? width_change + r : (isCheck ? mwidth - mheight / 2 : mheight / 2 + r), mheight - d);// 设置个新的长方形
        canvas.drawRoundRect(oval2, r, r, paint);//第二个参数是x半径，第三个参数是y半径
        paint.setColor(isChange ? (width_change >= Math.ceil(mheight / 2) ? circle_color_checked : circle_color_default) : (isCheck ? circle_color_checked : circle_color_default));
        canvas.drawCircle(isChange ? width_change : (isCheck ? mwidth - mheight / 2 : mheight / 2), mheight / 2, mheight / 2, paint);

    }

    public boolean isCheck() {
        return isCheck;
    }

    public boolean isChange() {
        return isChange;
    }

    private void initListener() {
        mAnimatorListener = new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                //动画播放完毕, 移除本View
                isChange = false;
            }
        };
        mAnimatorUpdateListener = new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                //更进度
                width_change = (int) (float) animation.getAnimatedValue();
                postInvalidate();
            }
        };

    }

    private ValueAnimator getAnimator() {
        float start = isCheck ? mheight / 2 : mwidth - mheight / 2;
        float end = isCheck ? mwidth - mheight / 2 : mheight / 2;
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(start, end).setDuration(mDuration);
        valueAnimator.addUpdateListener(mAnimatorUpdateListener);
        valueAnimator.addListener(mAnimatorListener);
        return valueAnimator;
    }

    public void setIsCheck(boolean isCheck) {
        this.isCheck = isCheck;
        postInvalidate();
    }

    public void setCheck(boolean isCheck) {
        if (!(this.isCheck & isCheck) && !isChange) {
            this.isCheck = isCheck;
            isChange = true;
            initListener();
            getAnimator().start();
        }
    }
}
