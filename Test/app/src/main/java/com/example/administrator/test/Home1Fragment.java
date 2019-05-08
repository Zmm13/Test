package com.example.administrator.test;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.PluralsRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.test.databinding.FragmentHome1Binding;
import com.example.administrator.test.service.MyMusicSercive;
import com.example.administrator.test.singleton.MediaPlayerUtils;

/**
 * Create by zmm
 * Time 2019/5/5
 * PackageName com.example.administrator.test
 */
public class Home1Fragment extends MyTestFragment implements View.OnClickListener {
    private View view;
    private boolean isViewInitiated = false;
    private boolean isVisibleToUser = false;
    private boolean isLoadData = false;
    private FragmentHome1Binding binding;
    private Context context;
    private Intent intent;

//    private MyMusicSercive.DownLoadBinder downLoadBinder;
//    private MyService myService;  //我们自己的service


    public Home1Fragment() {

    }

    public MyTestFragment build() {
        return this;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home1, container, false);
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


    public void prepareFetchData() {
        if (view != null && isVisibleToUser && isViewInitiated && !isLoadData) {
            binding = DataBindingUtil.bind(view);
            binding.setOnClickL(this);
            isLoadData = true;
            System.out.println("Loading......");
        } else if (isLoadData) {
            Toast.makeText(context, "isLoadData：" + isLoadData, Toast.LENGTH_SHORT).show();
        }
    }



    @Override
    public void onClick(View view) {
        intent = new Intent();
        intent.setClass(context, MyMusicSercive.class);
        intent.putExtra("path", Environment.getExternalStorageDirectory() + "/123.mp3");
        switch (view.getId()) {
            case R.id.bt_start:
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                } else {
                    if (Build.VERSION.SDK_INT >= 26) {
                        context.startForegroundService(intent);
                    } else {
                        // Pre-O behavior.
                        context.startService(intent);
                    }
                }
                break;
            case R.id.bt_pause:
                //结束Service
//                switch (MediaPlayerUtils.getInstance().getState()) {
//                    case 1:
//                        MediaPlayerUtils.getInstance().pauseMusic();
//                        break;
//                    case 2:
//                        MediaPlayerUtils.getInstance().contiuneMusic();
//                        break;
//                }
                break;
        }
    }




    @Override
    public void onDestroy() {
        super.onDestroy();
//        context.unbindService(connection);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (Build.VERSION.SDK_INT >= 26) {
                        context.startForegroundService(intent);
                    } else {
                        // Pre-O behavior.
                        context.startService(intent);
                    }
                }
                break;
        }
    }
}
