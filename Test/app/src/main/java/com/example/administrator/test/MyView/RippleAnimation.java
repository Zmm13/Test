package com.example.administrator.test.MyView;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.databinding.adapters.ViewGroupBindingAdapter;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.RelativeLayout;

import com.example.administrator.test.MainActivity;
import com.example.administrator.test.utils.StatusBarUtils;

/**
 * Create by zmm
 * Time 2019/5/9
 * PackageName com.example.administrator.test.MyView
 */
public class RippleAnimation extends View {
    public  static volatile RippleAnimation instance = null;

    private ViewGroup mRootView;//加载view的根布局
    private float mStartX;//圆心的x
    private float mStartY;//圆心的Y
    private float mMaxRadius;//圆的半径
    private float mStartRadius;//圆的半径
    private float mCurrentRadius;//圆的半径
    private Paint mPaint;
    private boolean isStarted = false;
    private Bitmap mBackground;
    private AnimatorListenerAdapter mAnimatorListener;
    private ValueAnimator.AnimatorUpdateListener mAnimatorUpdateListener;
//    private ViewGroupBindingAdapter.OnAnimationEnd mOnAnimationEndListener ;
    private long mDuration;

    public RippleAnimation(Context context) {
        super(context);
    }

    public RippleAnimation(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RippleAnimation(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @SuppressLint("NewApi")
    public RippleAnimation(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private RippleAnimation(Context context, float startX, float startY, int radius) {
        super(context);
        //获取activity的根视图,用来添加本View
            mRootView = (ViewGroup) ((Activity) getContext()).getWindow().getDecorView();
            mStartX = startX;
            mStartY = startY;
            mStartRadius = radius;
            mPaint = new Paint();
            mPaint.setAntiAlias(true);
            //设置为擦除模式
            mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
            updateMaxRadius();
            initListener();
    }

    public static RippleAnimation create(View onClickView) {
        if(instance == null){
            synchronized (RippleAnimation.class){
                if(instance == null){
                    Context context = onClickView.getContext();
                    int newWidth = onClickView.getWidth() / 2;
                    int newHeight = onClickView.getHeight() / 2;
                    //计算起点位置
                    int[] i = getAbsolute(onClickView);
                    float startX = i[0] + newWidth;
                    float startY = i[1] + newHeight;
//                    float startX = getAbsoluteX(onClickView) + newWidth;
//                    float startY = getAbsoluteY(onClickView) + newHeight;
                    //起始半径
                    //因为我们要避免遮挡按钮
                    int radius = Math.max(newWidth, newHeight);
                    instance =  new RippleAnimation(context, startX, startY, radius);
                }
            }
        }
        return instance;

    }

    /**
     * 根据起始点将屏幕分成4个小矩形,mMaxRadius就是取它们中最大的矩形的对角线长度
     * 这样的话, 无论起始点在屏幕中的哪一个位置上, 我们绘制的圆形总是能覆盖屏幕
     */
    private void updateMaxRadius() {
        //将屏幕分成4个小矩形
        RectF leftTop = new RectF(0, 0, mStartX + mStartRadius, mStartY + mStartRadius);
        RectF rightTop = new RectF(leftTop.right, 0, mRootView.getRight(), leftTop.bottom);
        RectF leftBottom = new RectF(0, leftTop.bottom, leftTop.right, mRootView.getBottom());
        RectF rightBottom = new RectF(leftBottom.right, leftTop.bottom, mRootView.getRight(), leftBottom.bottom);
        //分别获取对角线长度
        double leftTopHypotenuse = Math.sqrt(Math.pow(leftTop.width(), 2) + Math.pow(leftTop.height(), 2));
        double rightTopHypotenuse = Math.sqrt(Math.pow(rightTop.width(), 2) + Math.pow(rightTop.height(), 2));
        double leftBottomHypotenuse = Math.sqrt(Math.pow(leftBottom.width(), 2) + Math.pow(leftBottom.height(), 2));
        double rightBottomHypotenuse = Math.sqrt(Math.pow(rightBottom.width(), 2) + Math.pow(rightBottom.height(), 2));
        //取最大值
        mMaxRadius = (int) Math.max(
                Math.max(leftTopHypotenuse, rightTopHypotenuse),
                Math.max(leftBottomHypotenuse, rightBottomHypotenuse));
    }

    /**
     * 获取view在屏幕中的绝对x坐标
     */
    private static float getAbsoluteX(View view) {
        float x = view.getX();
        ViewParent parent = view.getParent();
        if (parent != null && parent instanceof View) {
            x += getAbsoluteX((View) parent);
        }
        return x;
    }

    private static int[] getAbsolute(View view){
        int[] location = new  int[2] ;
        view.getLocationInWindow(location); //获取在当前窗口内的绝对坐标
        return location;
    }

    /**
     * 获取view在屏幕中的绝对y坐标
     */
    private static float getAbsoluteY(View view) {
        float y = view.getY();
        ViewParent parent = view.getParent();
        if (parent != null && parent instanceof View) {
            y += getAbsoluteY((View) parent);
        }
        return y;
    }

    public void start() {
        if (!isStarted) {
            isStarted = true;
            updateBackground();
            attachToRootView();
            getAnimator().start();
        }
    }

    /**
     * 更新屏幕截图
     */
    private void updateBackground() {
        if (mBackground != null && !mBackground.isRecycled()) {
            mBackground.recycle();
        }
        mRootView.setDrawingCacheEnabled(true);
        mBackground = mRootView.getDrawingCache();
        mBackground = Bitmap.createBitmap(mBackground);
        mRootView.setDrawingCacheEnabled(false);
    }

    /**
     * 添加到根视图
     */
    private void attachToRootView() {
        setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        mRootView.addView(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onDraw(Canvas canvas) {
        //在新的图层上面绘制
        int layer = canvas.saveLayer(0, 0, getWidth(), getHeight(), null);
        canvas.drawBitmap(mBackground, 0, 0, null);
        canvas.drawCircle(mStartX, mStartY, mCurrentRadius, mPaint);
        canvas.restoreToCount(layer);
    }

    private ValueAnimator getAnimator() {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, mMaxRadius).setDuration(mDuration);
        valueAnimator.addUpdateListener(mAnimatorUpdateListener);
        valueAnimator.addListener(mAnimatorListener);
        return valueAnimator;
    }

    private void initListener() {
        mAnimatorListener = new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                //动画播放完毕, 移除本View
                detachFromRootView();
                isStarted = false;
                instance = null;
            }
        };
        mAnimatorUpdateListener = new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                //更新圆的半径
                mCurrentRadius = (int) (float) animation.getAnimatedValue() + mStartRadius;
                postInvalidate();
            }
        };
    }

    /**
     * 从根视图中移除
     */
    private void detachFromRootView() {
        mRootView.removeView(this);
    }

    public RippleAnimation setDuration(long duration) {
        mDuration = duration;
        return this;
    }

}
