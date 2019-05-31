package com.example.administrator.test;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.test.MyView.MyCheckBar;
import com.example.administrator.test.MyView.RippleAnimation;
import com.example.administrator.test.databinding.FragmentSettingBinding;
import com.example.administrator.test.event.IsLightChangeEvent;
import com.example.administrator.test.service.MusicPlayService;
import com.example.administrator.test.utils.StaticBaseInfo;
import com.example.administrator.test.utils.StatusBarUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import static android.view.View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR;
import static android.view.View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;

/**
 * Create by zmm
 * Time 2019/5/9
 * PackageName com.example.administrator.test
 */
public class SettingFragment extends Fragment {
    private View view;
    private boolean isViewInitiated = false;
    private boolean isVisibleToUser = false;
    private boolean isLoadData = false;
    private Context context;
    private FragmentSettingBinding binding;

    public SettingFragment() {

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_setting, container, false);
        binding.setIsLight(StaticBaseInfo.isLight(getActivity()));
        view = binding.getRoot();
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(IsLightChangeEvent event) {
        binding.cb.setCheck(!StaticBaseInfo.isLight(getActivity()));
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

    public void prepareFetchData(){
        if(view!=null&&isVisibleToUser&&isViewInitiated&&!isLoadData){
            binding.setIsLight(StaticBaseInfo.isLight(getActivity()));
            binding.cb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(binding.cb.isChange()){
                        return;
                    }
                    RippleAnimation.create(binding.cb).setDuration(1000).start();
                    StaticBaseInfo.fanIsLight(getActivity());
                    binding.setIsLight( StaticBaseInfo.isLight(getActivity()));
                    StatusBarUtils.statusBarLightMode(getActivity(), StaticBaseInfo.isLight(getActivity())? SYSTEM_UI_FLAG_LIGHT_STATUS_BAR :SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
                    EventBus.getDefault().post(new IsLightChangeEvent());
                }
            });
            binding.btExit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ActivityManager.getManager().exit();
                    android.os.Process.killProcess(android.os.Process.myPid());
                    try {
                        Intent intent = new Intent(getActivity(), MusicPlayService.class);
                        getActivity().stopService(intent);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });
            isLoadData=true;
            System.out.println("Loading......");
        }else if(isLoadData){
//            Toast.makeText(context,"isLoadData："+isLoadData,Toast.LENGTH_SHORT).show();
        }
    }

//    public void onCheckBarClick(View view){
//        MyCheckBar checkBar = (MyCheckBar)view;
//        checkBar.setCheck(checkBar.isCheck());
//    }
}
