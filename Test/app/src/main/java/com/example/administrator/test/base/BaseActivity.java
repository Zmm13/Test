package com.example.administrator.test.base;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.administrator.test.ActivityManager;
import com.example.administrator.test.minterfcae.MusiPlaycUpdateInterface;
import com.example.administrator.test.utils.StaticBaseInfo;
import com.example.administrator.test.utils.StatusBarUtils;

import org.greenrobot.eventbus.EventBus;

import static android.view.View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR;
import static android.view.View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;

/**
 * Create by zmm
 * Time 2019/5/28
 * PackageName com.example.administrator.test.base
 */
public abstract class BaseActivity<T extends ViewDataBinding> extends AppCompatActivity implements MusiPlaycUpdateInterface{

    public Context context;

    public T binding;

    public boolean eventBusEnable = false;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityManager.getManager().add(this);
        context = this;
        //必须先透明状态栏再设置状态栏亮色标记，状态栏图标才会变成暗色
        StatusBarUtils.StatusBarTransport(this);
        StatusBarUtils.statusBarLightMode(this, StaticBaseInfo.isLight(this) ? SYSTEM_UI_FLAG_LIGHT_STATUS_BAR : SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
        binding = DataBindingUtil.setContentView(this, setLayout());
        initView();
        initData();
        initListener();
        if(eventBusEnable){
            EventBus.getDefault().register(this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityManager.getManager().remove(this);
        if(eventBusEnable){
            EventBus.getDefault().unregister(this);
        }
    }

    protected abstract int setLayout();

    protected abstract void initView();

    protected abstract void initData();

    protected abstract void initListener();
}
