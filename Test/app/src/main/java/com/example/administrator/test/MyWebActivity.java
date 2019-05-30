package com.example.administrator.test;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RelativeLayout;

import com.example.administrator.test.base.BaseActivity;
import com.example.administrator.test.daoJavaBean.Song;
import com.example.administrator.test.databinding.ActivityMyWebBinding;
import com.example.administrator.test.event.IsLightChangeEvent;
import com.example.administrator.test.utils.ScreenUtils;
import com.example.administrator.test.utils.StaticBaseInfo;
import com.example.administrator.test.utils.TextUtil;
import com.just.agentweb.AgentWeb;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class MyWebActivity extends BaseActivity<ActivityMyWebBinding> {
    private AgentWeb agentWeb;
    private String path = "https://www.baidu.com";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        eventBusEnable = true;
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int setLayout() {
        return R.layout.activity_my_web;
    }

    @Override
    protected void initView() {
        binding.setIsLight(StaticBaseInfo.isLight(context));
        binding.rlMain.setPadding(0, ScreenUtils.getStatusHeight(context), 0, 0);
    }

    @Override
    protected void initData() {
        String s = getIntent().getStringExtra("path");
        agentWeb = AgentWeb.with(this)
                .setAgentWebParent(binding.rlMain, new RelativeLayout.LayoutParams(-1, -1))
                .useDefaultIndicator()
                .createAgentWeb()
                .ready()
                .go(TextUtil.isEmpty(s) ? path : s);
    }

    @Override
    protected void initListener() {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(IsLightChangeEvent event) {
        binding.setIsLight(StaticBaseInfo.isLight(context));
    }

    @Override
    public void onBackPressed() {
        if (agentWeb.back()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        agentWeb.clearWebCache();
        agentWeb.destroy();
        super.onDestroy();
    }

    @Override
    public void update(Song song, int progress, int duration) {

    }
}
