package com.example.administrator.test.base;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.administrator.test.R;
import com.example.administrator.test.adapter.MusicListAdapter;
import com.example.administrator.test.daoJavaBean.Song;
import com.example.administrator.test.databinding.FragmentHome1Binding;
import com.example.administrator.test.event.IsLightChangeEvent;
import com.example.administrator.test.event.MusicChangeEvent;
import com.example.administrator.test.singleton.MediaPlayerUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

/**
 * Create by zmm
 * Time 2019/5/21
 * PackageName com.example.administrator.test.base
 */
public abstract class BaseFragment<T extends ViewDataBinding> extends Fragment {
    public View view;
    private boolean isViewInitiated = false;
    private boolean isVisibleToUser = false;
    private boolean isLoadData = false;
    public Context context;
    public T t;

    public abstract T initDatabinding();

    public abstract void initView();

    public abstract void initData();

    public abstract void initListener();

    public abstract void isLightChange();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        initDatabinding();
        t.setLifecycleOwner(this);
        view = t.getRoot();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        isViewInitiated = true;
        prepareFetchData();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        this.isVisibleToUser = isVisibleToUser;
        prepareFetchData();
    }

    private void prepareFetchData() {
        if (isViewInitiated && isVisibleToUser && !isLoadData) {
            initView();
            initData();
            initListener();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            EventBus.getDefault().register(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(IsLightChangeEvent event) {
        isLightChange();
    }
}
