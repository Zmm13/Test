package com.example.administrator.test.MyView;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

import com.example.administrator.test.R;
import com.example.administrator.test.utils.Res;
import com.example.administrator.test.utils.ScreenUtils;


/**
 * Create by zmm
 * Time 2018/11/27
 * PackageName com.tbidea.xunyitongPatientv10.MyView
 */
@SuppressLint("AppCompatCustomView")
public class DragImageView extends CircleImageView {

    private int screenWidth;
    private int screenHeight;
    private int screenWidthHalf;
    private int statusHeight;
    private int virtualHeight;
    private long startTime;
    private int divider;

    public DragImageView(Context context) {
        super(context);
        init();
    }

    public DragImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray array=context.obtainStyledAttributes(attrs, R.styleable.DragImageView);
        divider = (int) array.getDimension(R.styleable.DragImageView_side_padding,0);
        init();
    }

    public DragImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        screenWidth = ScreenUtils.getScreenWidth(getContext());
        screenWidthHalf = screenWidth / 2;
        screenHeight = ScreenUtils.getScreenHeight(getContext());
        statusHeight = ScreenUtils.getStatusHeight(getContext());
        virtualHeight=ScreenUtils.getVirtualBarHeigh(getContext());
//        divider = (int) Res.getDimen(R.dimen.x24,getContext());
    }

    private int lastX;
    private int lastY;

    private boolean isDrag;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int rawX = (int) event.getRawX();
        int rawY = (int) event.getRawY();
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                startTime = System.currentTimeMillis();
                isDrag = false;
                getParent().requestDisallowInterceptTouchEvent(true);
                lastX = rawX;
                lastY = rawY;
                Log.e("down---->", "getX=" + getX() + "；screenWidthHalf=" + screenWidthHalf);
                break;
            case MotionEvent.ACTION_MOVE:
                isDrag = true;
                //计算手指移动了多少
                int dx = rawX - lastX;
                int dy = rawY - lastY;
                //这里修复一些手机无法触发点击事件的问题
                int distance= (int) Math.sqrt(dx*dx+dy*dy);
                Log.e("distance---->",distance+"");
                if(distance<3){//给个容错范围，不然有部分手机还是无法点击
                    isDrag=false;
                    break;
                }
                float x = getX() + dx;
                float y = getY() + dy;

                //检测是否到达边缘 左上右下
                x = x < divider ? divider : x > screenWidth - getWidth()-divider ? screenWidth - getWidth()-divider : x;
                // y = y < statusHeight ? statusHeight : (y + getHeight() >= screenHeight ? screenHeight - getHeight() : y);
                if (y<0){
                    y=0;
                }
                if (y>screenHeight-statusHeight-getHeight()){
                    y=screenHeight-statusHeight-getHeight();
                }
                setX(x);
                setY(y);

                lastX = rawX;
                lastY = rawY;
                Log.e("move---->", "getX=" + getX() + "；screenWidthHalf=" + screenWidthHalf + " " + isDrag+"  statusHeight="+statusHeight+ " virtualHeight"+virtualHeight+ " screenHeight"+ screenHeight+"  getHeight="+getHeight()+" y"+y);
                break;
            case MotionEvent.ACTION_UP:
                long thisTime = System.currentTimeMillis();
                if(thisTime - startTime < 150 &&!isDrag&&(getX()<=divider||getX()>=screenWidth-getWidth()-divider)){

                }else {
//                if (isDrag) {
                    //恢复按压效果
                    setPressed(false);
                    Log.e("ACTION_UP---->", "getX=" + getX() + "；screenWidthHalf=" + screenWidthHalf);
                    if (rawX >= screenWidthHalf) {
                        animate().setInterpolator(new DecelerateInterpolator())
                                .setDuration(500)
                                .xBy(screenWidth - getWidth() - getX()-divider)
                                .start();
                    } else {
                        ObjectAnimator oa = ObjectAnimator.ofFloat(this, "x", getX(), divider);
                        oa.setInterpolator(new DecelerateInterpolator());
                        oa.setDuration(500);
                        oa.start();
                    }
//                }
                }
                Log.e("up---->",isDrag+"");
                break;
        }
        //如果是拖拽则消耗事件，否则正常传递即可。
        return isDrag || super.onTouchEvent(event) ;

    }
}
