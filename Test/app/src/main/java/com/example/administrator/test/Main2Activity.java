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
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
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

import com.example.administrator.test.MyView.RippleAnimation;
import com.example.administrator.test.adapter.FragmentViewPagerAdapter;
import com.example.administrator.test.daoJavaBean.Song;
import com.example.administrator.test.databinding.ActivityMainBinding;
import com.example.administrator.test.event.IsLightChangeEvent;
import com.example.administrator.test.event.MusicChangeEvent;
import com.example.administrator.test.minterfcae.MusiPlaycUpdateInterface;
import com.example.administrator.test.service.MusicPlayService;
import com.example.administrator.test.service.MyTestService;
import com.example.administrator.test.service.ReceiverService;
import com.example.administrator.test.singleton.MediaPlayerUtils;
import com.example.administrator.test.singleton.MusicListTool;
import com.example.administrator.test.utils.HomeMusicIconRotateTool;
import com.example.administrator.test.utils.LocalMusicUtils;
import com.example.administrator.test.utils.MusicTimeTool;
import com.example.administrator.test.utils.ScreenUtils;
import com.example.administrator.test.utils.StaticBaseInfo;
import com.example.administrator.test.utils.StatusBarUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import static android.view.View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR;
import static android.view.View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;


public class Main2Activity extends AppCompatActivity {
    private FragmentViewPagerAdapter adapter;//主页viewpager适配器
    private ActivityMainBinding binding;//主页databinding
    private MusicPlayService.MusicPlayBinder musicPlayBinder;//服务绑定数据传递对象
    private boolean isTouchSeekBar = false;//主页seekbar是否触摸
    private Intent intent;//播放音乐服务的intent对象


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //状态栏暗色图标
        super.onCreate(savedInstanceState);
        //必须先透明状态栏再设置状态栏亮色标记，状态栏图标才会变成暗色
        StatusBarUtils.StatusBarTransport(this);
        StatusBarUtils.statusBarLightMode(this, StaticBaseInfo.isLight(this) ? SYSTEM_UI_FLAG_LIGHT_STATUS_BAR : SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
        startReceiverService();
        //初始化view
        initView();
//        initStaticService();
        //初始化数据


        initData();
        //初始化监听
        initListener();
        EventBus.getDefault().register(this);
//        Toast.makeText(Main2Activity.this, new JniUtil().getSomeString(),Toast.LENGTH_SHORT).show();
    }

    private void initView() {
        //利用databinding设置布局文件并初始化databinding对象
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setIsLight(StaticBaseInfo.isLight(this));
        //由于是全屏设置线性布局第一个子view与顶部的距离，避免状态栏和页面重合
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) binding.llItem.getLayoutParams();
        layoutParams.topMargin = ScreenUtils.getStatusHeight(this);
        binding.llItem.setLayoutParams(layoutParams);
    }

    private void initData() {
        //设置viewpage默认第一页
        binding.setSelectedPosition(0);
        //设置viewpager缓存三页
        binding.vp.setOffscreenPageLimit(3);
        //初始化viewpager适配器
        adapter = new FragmentViewPagerAdapter(getSupportFragmentManager(), new int[]{1, 2, 3, 4});
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
        if (MediaPlayerUtils.mediaPlayer != null && MusicListTool.getInstance().getPlaySong() != null) {
            if (binding.getShowName() == null || !binding.getShowName().equals(MusicListTool.getInstance().getPlaySong().getName() + "(" + MusicListTool.getInstance().getPlaySong().singer + ")")) {
                binding.setShowName(MusicListTool.getInstance().getPlaySong().getName() + "(" + MusicListTool.getInstance().getPlaySong() + ")");
                Bitmap bitmap = LocalMusicUtils.getArtwork(Main2Activity.this, MusicListTool.getInstance().getPlaySong().getKey(), MusicListTool.getInstance().getPlaySong().getAlbumId(), true, false);
                if (bitmap != null) {
                    binding.civ.setImageBitmap(bitmap);
                } else {
                    binding.civ.setImageDrawable(getResources().getDrawable(R.drawable.girl_icon));
                }
            }
            binding.ivPlay.setSelected(MediaPlayerUtils.getInstance().isPlaying());
            binding.setShowName(MusicListTool.getInstance().getPlaySong().name + "(" + MusicListTool.getInstance().getPlaySong().singer + ")");
            binding.ivPlay.setSelected(MediaPlayerUtils.getInstance().isPlaying());
            binding.seekBar.setMax(MusicListTool.getInstance().getPlaySong().duration);
            binding.setDuration(MusicTimeTool.getMusicTime(MusicListTool.getInstance().getPlaySong().duration));
            if (!isTouchSeekBar) {
                binding.seekBar.setProgress(MediaPlayerUtils.getInstance().getProgress());
                binding.setProgressTime(MusicTimeTool.getMusicTime(MediaPlayerUtils.getInstance().getProgress()));
            }
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(Main2Activity.this, MyTestService.class);
                bindService(intent, connectiontest, Context.BIND_AUTO_CREATE);
                startService(intent);
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                unbindService(connectiontest);
                stopService(intent);
            }
        }).start();

    }

    private void initListener() {
        //设置viewpage页面切换监听
        binding.vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                binding.setSelectedPosition(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        //设置模块点击事件
        TextView[] items = new TextView[]{binding.tv1, binding.tv2, binding.tv3, binding.tv4};
        for (int i = 0; i < items.length; i++) {
            int finalI = i;
            items[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    binding.vp.setCurrentItem(finalI, false);
                    binding.setSelectedPosition(finalI);
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
                        musicPlayBinder.pause();
                        binding.ivPlay.setSelected(false);
                        HomeMusicIconRotateTool.rotateView(false, binding.civ);
                    } else {
                        musicPlayBinder.play(Main2Activity.this);
                        musicPlayBinder.doRefulsh();
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
                Intent intent = new Intent();
                intent.setAction("MY_BROADCAST");
//                intent.setComponent(new ComponentName(Main2Activity.this,
//                        "com.example.administrator.test.receiver.MyAutoReceiver"));
//                if (Build.VERSION.SDK_INT >= 26) {
//                    intent.addFlags(0x01000000);
//                }
//                sendOrderedBroadcast(intent,null);
                sendBroadcast(intent);
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
            musicPlayBinder.setDoUpdate(false);
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
            musicPlayBinder.setUpdateListener(new MusiPlaycUpdateInterface() {
                @Override
                public void update(Song song, int progress, int duration) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (binding.getShowName() == null || !binding.getShowName().equals(song.getName() + "(" + song.singer + ")")) {
                                binding.setShowName(song.getName() + "(" + song + ")");
                                Bitmap bitmap = LocalMusicUtils.getArtwork(Main2Activity.this, song.getKey(), song.getAlbumId(), true, false);
                                if (bitmap != null) {
                                    binding.civ.setImageBitmap(bitmap);
                                } else {
                                    binding.civ.setImageDrawable(getResources().getDrawable(R.drawable.girl_icon));
                                }
                            }
                            binding.ivPlay.setSelected(musicPlayBinder.isPlay());
                            binding.setShowName(song.getName() + "(" + song.singer + ")");
                            binding.ivPlay.setSelected(musicPlayBinder.isPlay());
                            binding.seekBar.setMax(duration);
                            binding.setDuration(MusicTimeTool.getMusicTime(duration));
                            if (!isTouchSeekBar) {
                                binding.seekBar.setProgress(progress);
                                binding.setProgressTime(MusicTimeTool.getMusicTime(progress));
                            }
                            HomeMusicIconRotateTool.rotateView(musicPlayBinder.isPlay(), binding.civ);
                        }
                    });
                }
            });
            if (musicPlayBinder.isPlay()) {
                musicPlayBinder.doRefulsh();
            }
        }
    };


    private ServiceConnection connectiontest = new ServiceConnection() {
        /**
         * 服务解除绑定时候调用
         */
        @Override
        public void onServiceDisconnected(ComponentName name) {
            // TODO Auto-generated method stub
            System.out.println("onServiceDisconnected");
        }

        /**
         * 绑定服务的时候调用
         */
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // TODO Auto-generated method stub
            System.out.println("onServiceConnected");
        }
    };


    @Override
    protected void onStart() {
        super.onStart();
        if (MediaPlayerUtils.getInstance().isPlaying()) {
            if (musicPlayBinder != null) {
                musicPlayBinder.doRefulsh();
            } else {
                bindService(intent, connection, Context.BIND_AUTO_CREATE);
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (musicPlayBinder != null && musicPlayBinder.isPlay()) {
            musicPlayBinder.setDoUpdate(false);
        }
        HomeMusicIconRotateTool.rotateView(false, binding.civ);
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        unbindService(connection);
        HomeMusicIconRotateTool.objectAnimator = null;
        super.onDestroy();
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
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(IsLightChangeEvent event) {
        binding.setIsLight(StaticBaseInfo.isLight(this));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(MusicChangeEvent event) {
        if (musicPlayBinder != null) {
            musicPlayBinder.setDoUpdate(false);
            musicPlayBinder.play(Main2Activity.this);
            musicPlayBinder.doRefulsh();
        } else {
            initService();
        }
    }

    private void startReceiverService() {
        Intent intent = new Intent();
        intent.setClass(this, ReceiverService.class);
        //开启服务播放音乐
        if (Build.VERSION.SDK_INT >= 26) {
            startForegroundService(intent);
        } else {
            startService(intent);
        }
    }

    private void initStaticService() {
        Intent intent = new Intent("com.example.administrator.test.receiver.MyStatic2Receiver");
        ComponentName componentName = new ComponentName(this, "com.example.administrator.test.receiver.MyStatic2Receiver");
        intent.setComponent(componentName);
        sendBroadcast(intent);
    }

}
