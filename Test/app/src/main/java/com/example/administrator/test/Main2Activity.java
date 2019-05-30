package com.example.administrator.test;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.test.MyView.BlurTransformation;
import com.example.administrator.test.MyView.RippleAnimation;
import com.example.administrator.test.adapter.FragmentViewPagerAdapter;
import com.example.administrator.test.base.BaseActivity;
import com.example.administrator.test.daoJavaBean.Song;
import com.example.administrator.test.databinding.ActivityMainBinding;
import com.example.administrator.test.event.BufferUpdateEvent;
import com.example.administrator.test.event.HomeFragmentChangeEvent;
import com.example.administrator.test.event.IsLightChangeEvent;
import com.example.administrator.test.event.MediaPlayerEvent;
import com.example.administrator.test.event.MusicChangeEvent;
import com.example.administrator.test.event.QQMusicFocuse10002Event;
import com.example.administrator.test.minterfcae.MusiPlaycUpdateInterface;
import com.example.administrator.test.service.MusicPlayService;
import com.example.administrator.test.service.MyTestService;
import com.example.administrator.test.service.ReceiverService;
import com.example.administrator.test.singleton.MediaPlayerUtils;
import com.example.administrator.test.singleton.MusicListTool;
import com.example.administrator.test.utils.ActivityUtils;
import com.example.administrator.test.utils.BitmapTool;
import com.example.administrator.test.utils.FcoTablayoutTools;
import com.example.administrator.test.utils.HomeMusicIconRotateTool;
import com.example.administrator.test.utils.LocalMusicUtils;
import com.example.administrator.test.utils.MusicTimeTool;
import com.example.administrator.test.utils.ScreenUtils;
import com.example.administrator.test.utils.StaticBaseInfo;
import com.example.administrator.test.utils.StatusBarUtils;
import com.example.administrator.test.utils.TextUtil;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import static android.view.View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR;
import static android.view.View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;


public class Main2Activity extends BaseActivity<ActivityMainBinding> {
    private FragmentViewPagerAdapter adapter;//主页viewpager适配器
    private MusicPlayService.MusicPlayBinder musicPlayBinder;//服务绑定数据传递对象
    private boolean isTouchSeekBar = false;//主页seekbar是否触摸
    private Intent intent;//播放音乐服务的intent对象
    private HomeMusicIconRotateTool homeMusicIconRotateTool;
//    private String[] titles = new String[]{"发现","音乐","本地","设置"};
    private boolean firstInitService = false;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        eventBusEnable = true;
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int setLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        //利用databinding设置布局文件并初始化databinding对象
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setContext(this);
        binding.setIsLight(StaticBaseInfo.isLight(this));
        //由于是全屏设置线性布局第一个子view与顶部的距离，避免状态栏和页面重合
        binding.llItem.setPadding(0, ScreenUtils.getStatusHeight(this), 0, 0);

    }

    @Override
    protected void initData() {
        //设置viewpage默认第一页
        binding.setSelectedPosition(0);
//        binding.ctl.setCurrentTab(0);
        //设置viewpager缓存三页
        binding.vp.setOffscreenPageLimit(3);
        //初始化viewpager适配器
        adapter = new FragmentViewPagerAdapter(getSupportFragmentManager());
        binding.vp.setAdapter(adapter);
        //获取音乐
        if (ContextCompat.checkSelfPermission(Main2Activity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Main2Activity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, StaticBaseInfo.MAINACTIVITY_REQUEST_CODE_READ_EXTERNAL_STORAGE);
        } else {
            MusicListTool.initList(this);
        }
        intent = new Intent();
        intent.setClass(this, MusicPlayService.class);

        //初始化界面
        homeMusicIconRotateTool = new HomeMusicIconRotateTool();
    }

    @Override
    protected void initListener() {
        //设置viewpage页面切换监听
        binding.vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                binding.setSelectedPosition(position);
//                binding.ctl.setCurrentTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
//        设置模块点击事件
        TextView[] items = new TextView[]{binding.tv1, binding.tv2, binding.tv3, binding.tv4};
        for (int i = 0; i < items.length; i++) {
            int finalI = i;
            items[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    binding.vp.setCurrentItem(finalI, false);
                }
            });
        }
        //设置seekbar音乐进度条改变监听
        binding.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                binding.setProgressTime(MusicTimeTool.getMusicTime(i));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                isTouchSeekBar = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                try {
                    MediaPlayerUtils.getInstance().seekTo(seekBar.getProgress());
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    isTouchSeekBar = false;
                }
            }
        });
        //设置播放按钮点击事件
        binding.ivPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public synchronized void onClick(View view) {
                if (MusicListTool.getInstance().getList(Main2Activity.this) == null || MusicListTool.getInstance().getList(Main2Activity.this).size() == 0) {
                    Toast toast = Toast.makeText(Main2Activity.this, "本地没有音乐文件！", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    return;
                }
                if (musicPlayBinder != null) {
                    if (musicPlayBinder.isPlay()) {
                        musicPlayBinder.pause(Main2Activity.this);
                    } else {
                        musicPlayBinder.play(Main2Activity.this, Main2Activity.this);
                    }
                } else {
                    initService();
                }
            }
        });
        binding.div.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                RippleAnimation.create(binding.div).setDuration(1500).start();
                StaticBaseInfo.fanIsLight(Main2Activity.this);
                StatusBarUtils.statusBarLightMode(Main2Activity.this, StaticBaseInfo.isLight(Main2Activity.this) ? SYSTEM_UI_FLAG_LIGHT_STATUS_BAR : SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
                StaticBaseInfo.setBaseColor(Color.parseColor("#FF0000"));
                binding.setIsLight(StaticBaseInfo.isLight(Main2Activity.this));
                EventBus.getDefault().post(new IsLightChangeEvent());
            }
        });
        binding.civ.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View view) {

                ActivityUtils.startActivity(Main2Activity.this, MusicPlayShowActivity.class);
            }
        });
    }

    //初始化服务连接对象
    private ServiceConnection connection = new ServiceConnection() {
        /**
         * 服务解除绑定时候调用
         */
        @Override
        public void onServiceDisconnected(ComponentName name) {
            // TODO Auto-generated method stub
            musicPlayBinder.removeUpdateListener(Main2Activity.this);
            musicPlayBinder = null;
        }

        /**
         * 绑定服务的时候调用
         */
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // TODO Auto-generated method stub
            /*
             * 调用DownLoadBinder的方法实现参数的传递
             */
            musicPlayBinder = (MusicPlayService.MusicPlayBinder) service;
            if (musicPlayBinder.isPlay()) {
                musicPlayBinder.removeUpdateListener(Main2Activity.this);
                musicPlayBinder.addUpdateListener(Main2Activity.this);
//                musicPlayBinder.play(context,Main2Activity.this);
            }
            if(firstInitService){
                musicPlayBinder.removeUpdateListener(Main2Activity.this);
                musicPlayBinder.play(context,Main2Activity.this);
                firstInitService = false;
            }
        }
    };


    @Override
    protected void onStart() {
        super.onStart();
        if (MediaPlayerUtils.getInstance().isPlaying()) {
            if (musicPlayBinder != null) {
                musicPlayBinder.removeUpdateListener(this);
                musicPlayBinder.addUpdateListener(this);
            } else {
                bindService(intent, connection, Context.BIND_AUTO_CREATE);
                firstInitService = true;
            }
        }
        changeMusicUi(MusicListTool.getInstance().getPlaySong(), MediaPlayerUtils.getInstance().isPlaying());
        //初始化界面
        if (MediaPlayerUtils.mediaPlayer != null && MusicListTool.getInstance().getPlaySong() != null) {
            setProgress(MusicListTool.getInstance().getPlaySong().duration, MediaPlayerUtils.getInstance().getProgress());
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (musicPlayBinder != null && musicPlayBinder.isPlay()) {
            musicPlayBinder.removeUpdateListener(Main2Activity.this);
        }
        homeMusicIconRotateTool.rotateView(false, binding.civ);
        System.out.println("onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            unbindService(connection);
        } catch (Exception e) {

        }
    }


    /**
     * 动态获取权限结果
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case StaticBaseInfo.MAINACTIVITY_REQUEST_CODE_WRITE_EXTERNAL_STORAGE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (Build.VERSION.SDK_INT >= 26) {
                        startForegroundService(intent);
                    } else {
                        startService(intent);
                    }
                    bindService(intent, connection, Context.BIND_AUTO_CREATE);
                    firstInitService = true;
                }
                break;
            case StaticBaseInfo.MAINACTIVITY_REQUEST_CODE_READ_EXTERNAL_STORAGE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    MusicListTool.initList(Main2Activity.this);
                }
                break;
        }
    }

    private void initService() {
        if (ContextCompat.checkSelfPermission(Main2Activity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Main2Activity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, StaticBaseInfo.MAINACTIVITY_REQUEST_CODE_WRITE_EXTERNAL_STORAGE);
        } else {
            //开启服务播放音乐
            if (Build.VERSION.SDK_INT >= 26) {
                startForegroundService(intent);
            } else {
                startService(intent);
            }
            //绑定服务更新ui
            bindService(intent, connection, Context.BIND_AUTO_CREATE);
        }
        firstInitService = true;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(IsLightChangeEvent event) {
        binding.setIsLight(StaticBaseInfo.isLight(this));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(MusicChangeEvent event) {
        if (musicPlayBinder != null) {
            if (ActivityManager.getManager().isTop(this)) {
                musicPlayBinder.removeUpdateListener(Main2Activity.this);
                musicPlayBinder.play(Main2Activity.this, Main2Activity.this);
            }
        } else {
            initService();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(BufferUpdateEvent event) {
        int i = binding.seekBar.getMax() * event.getPercent() / 100;
        System.out.println("SecondaryProgress:" + i);
        binding.seekBar.setSecondaryProgress(i);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(HomeFragmentChangeEvent event) {
        int i = event.getToPosition();
        binding.vp.setCurrentItem(i, false);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(MediaPlayerEvent event) {
        switch (event.getState()) {
            case MediaPlayerEvent.STATE_STARTED:
                changeMusicUi(MusicListTool.getInstance().getPlaySong(), true);
                break;
            case MediaPlayerEvent.STATE_PAUSE:
                changeMusicUi(MusicListTool.getInstance().getPlaySong(), false);
                break;
        }
    }


    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        ActivityUtils.goBackHome(context);
    }

    @Override
    public void update(Song song, int progress, int duration) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
              setProgress(duration,progress);
            }
        });
    }

    private void setPic(Song song) {
        if (!song.isInternet() || TextUtil.isEmpty(song.getImageUrl())) {
            Bitmap bitmap = LocalMusicUtils.getArtwork(Main2Activity.this, song.getKey(), song.getAlbumId(), true, false);
            if (bitmap != null) {
                binding.civ.setImageBitmap(bitmap);
//                bitmap.recycle();
            } else {
                Picasso.with(this).load(R.drawable.girl_icon).into(binding.civ);
            }
        } else {

            Picasso.with(Main2Activity.this).load(Uri.parse(song.getImageUrl())).into(binding.civ);
        }
    }


    private void changeMusicUi(Song song, boolean isPlay) {
        if (song == null)
            return;
        setPic(song);
        binding.ivPlay.setSelected(isPlay);
        binding.setShowName(song.getName()+"/"+song.getSinger());
        binding.ivPlay.setSelected(isPlay);
        homeMusicIconRotateTool.rotateView(isPlay, binding.civ);
    }

    private void setProgress(int duration, int progress) {
        if (binding.seekBar.getMax() != duration) {
            binding.seekBar.setMax(duration);
            binding.setDuration(MusicTimeTool.getMusicTime(duration));
        }
        if (!isTouchSeekBar) {
            binding.seekBar.setProgress(progress);
            binding.setProgressTime(MusicTimeTool.getMusicTime(progress));
            System.out.println("update");
        }
    }


}
