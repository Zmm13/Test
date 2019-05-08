package com.example.administrator.test;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.test.adapter.FragmentViewPagerAdapter;
import com.example.administrator.test.daoJavaBean.Song;
import com.example.administrator.test.service.MyMusicSercive;
import com.example.administrator.test.singleton.MediaPlayerUtils;
import com.example.administrator.test.singleton.MusicListTool;
import com.example.administrator.test.utils.HomeMusicIconRotateTool;
import com.example.administrator.test.utils.LocalMusicUtils;
import com.example.administrator.test.utils.MusicTimeTool;
import com.example.administrator.test.utils.ScreenUtils;
import com.example.administrator.test.utils.SpTool;
import com.example.administrator.test.utils.StaticBaseInfo;
import com.example.administrator.test.utils.StatusBarUtils;

import com.example.administrator.test.databinding.ActivityMainBinding;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    private FragmentViewPagerAdapter adapter;//主页viewpager适配器
    private ActivityMainBinding binding;//主页databinding
    private MyMusicSercive.MusicBinder musicBinder;//服务绑定数据传递对象
    private boolean isTouchSeekBar = false;//主页seekbar是否触摸
    private Intent intent;//播放音乐服务的intent对象
    private long uiDuration = 100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //状态栏暗色图标
        super.onCreate(savedInstanceState);
        //必须先透明状态栏再设置状态栏亮色标记，状态栏图标才会变成暗色
        StatusBarUtils.StatusBarTransport(this);
        StatusBarUtils.statusBarLightMode(this);
        //初始化view



        initView();
        //初始化数据
        initData();
        //初始化监听
        initListener();




        //213213124
        //hello git
        //hello gitlab
    }

    private void initView() {
        //利用databinding设置布局文件并初始化databinding对象
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        //由于是全屏设置线性布局第一个子view与顶部的距离，避免状态栏和页面重合
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) binding.llItem.getLayoutParams();
        layoutParams.topMargin = ScreenUtils.getStatusHeight(this);
        binding.llItem.setLayoutParams(layoutParams);
//        setSeekBarClickable(0, binding.seekBar);
    }

    private void initData() {
        //设置viewpage默认第一页
        binding.setSelectedPosition(0);
        //设置viewpager缓存三页
        binding.vp.setOffscreenPageLimit(3);
        //初始化viewpager适配器
        adapter = new FragmentViewPagerAdapter(getSupportFragmentManager(), new int[]{1, 2, 3, 4});
        binding.vp.setAdapter(adapter);
        //初始化服务意图
        intent = new Intent();
        intent.setClass(this, MyMusicSercive.class);
        //获取音乐
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, StaticBaseInfo.MAINACTIVITY_REQUEST_CODE_READ_EXTERNAL_STORAGE);
        } else {
            MusicListTool.initList(this);
        }
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
                if (MusicListTool.getInstance().getList(MainActivity.this) == null || MusicListTool.getInstance().getList(MainActivity.this).size() == 0) {
                    Toast toast = Toast.makeText(MainActivity.this, "本地没有音乐文件！", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    return;
                }
                if (MediaPlayerUtils.mediaPlayer != null) {
                    //正在播放就暂停
                    if (musicBinder != null && musicBinder.isPlay()) {
                        musicBinder.pauseMusic();
                        pauseUI();
                        initPlayButton(false);
                    }
                    //没有播放就播放
                    else {
                        //绑定服务播放并且更新ui
                        bindService(intent, connection, Context.BIND_AUTO_CREATE);
                    }
                } else {
                    initService(null);
                }
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
            musicBinder = ((MyMusicSercive.MusicBinder) service);
            if (musicBinder.isPlayEnd()) {
                HomeMusicIconRotateTool.rotateView(true, false, binding.civ);
            }
            if (!musicBinder.isPlay()) {
                musicBinder.playMusic();
            }
            handler.postDelayed(runnable, uiDuration);
        }
    };

    //handler刷新ui
    public android.os.Handler handler = new android.os.Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public synchronized void run() {
            try {
                System.out.println("刷新ui");
                if (musicBinder == null) {
                    return;
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (isTouchSeekBar) {
                            return;
                        }
                        reflushMusicControlUI();
                    }
                });
                handler.postDelayed(runnable, uiDuration);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        reflushMusicControlUI();
    }

    @Override
    protected void onStop() {
        pauseUI();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        //置空动画
        HomeMusicIconRotateTool.objectAnimator = null;
        if (MusicListTool.getInstance().getPlaySong() != null) {
            SpTool.savePlaySong(this, MusicListTool.getInstance().getPlaySong(), MediaPlayerUtils.mediaPlayer.getCurrentPosition());
        }
        super.onDestroy();
    }


    /**
     * 刷新播放按钮
     *
     * @param isPlay
     */
    private void initPlayButton(boolean isPlay) {
        if (isPlay) {
            if (!binding.ivPlay.isSelected()) {
                binding.ivPlay.setSelected(true);
            }
        } else {
            if (binding.ivPlay.isSelected()) {
                binding.ivPlay.setSelected(false);
            }
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
                }
                break;
            case StaticBaseInfo.MAINACTIVITY_REQUEST_CODE_READ_EXTERNAL_STORAGE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    MusicListTool.initList(MainActivity.this);
                }
                break;
        }
    }

    /**
     * 暂停ui刷新
     */
    private void pauseUI() {
        //暂停动画
        HomeMusicIconRotateTool.pauseAnimator();
        //取消绑定
        musicBinder = null;
        try {
            unbindService(connection);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setPlayPosition(Song song) {
        try {
            if (MediaPlayerUtils.mediaPlayer == null) {
                initService(song);
            } else {
                MediaPlayerUtils.getInstance().setPlayPath(this, song);
                reflushMusicControlUI();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initService(Song song) {
        if (binding.civ.getRotation() != 0) {
            HomeMusicIconRotateTool.rotateView(true, false, binding.civ);
        }
        intent.putExtra("song", song);
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, StaticBaseInfo.MAINACTIVITY_REQUEST_CODE_WRITE_EXTERNAL_STORAGE);
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

    private void reflushMusicControlUI() {
        if (MediaPlayerUtils.mediaPlayer == null) {
            Song playSong = SpTool.getPlaySong(this);
            int progress = SpTool.getPlaySongPogress(this);
            binding.setShowName(playSong.getName() + "(" + playSong.getSinger() + ")");
            if (playSong.getKey() == -1 || playSong.getAlbumId() == -1) {
                binding.civ.setImageDrawable(getResources().getDrawable(R.drawable.girl_icon));
            } else {
                Bitmap bitmap = LocalMusicUtils.getArtwork(this, playSong.getKey(), playSong.getAlbumId(), true, false);
                if (bitmap != null) {
                    binding.civ.setImageBitmap(bitmap);
                } else {
                    binding.civ.setImageDrawable(getResources().getDrawable(R.drawable.girl_icon));
                }
            }
            binding.setDuration(MusicTimeTool.getMusicTime(playSong.duration));
            binding.setProgressTime(MusicTimeTool.getMusicTime(progress));
            initPlayButton(false);
            return;
        }
        if (MediaPlayerUtils.mediaPlayer.isPlaying() && musicBinder == null) {
            bindService(intent, connection, Context.BIND_AUTO_CREATE);
        }
        if (binding.getShowName() == null || !binding.getShowName().equals(MusicListTool.getInstance().getPlaySong().getName() + "(" + MusicListTool.getInstance().getPlaySong().getSinger() + ")")) {
            binding.setShowName(MusicListTool.getInstance().getPlaySong().getName() + "(" + MusicListTool.getInstance().getPlaySong().getSinger() + ")");
            Bitmap bitmap = LocalMusicUtils.getArtwork(this, MusicListTool.getInstance().getPlaySong().getKey(), MusicListTool.getInstance().getPlaySong().getAlbumId(), true, false);
            if (bitmap != null) {
                binding.civ.setImageBitmap(bitmap);
            } else {
                binding.civ.setImageDrawable(getResources().getDrawable(R.drawable.girl_icon));
            }
        }
        int max = MediaPlayerUtils.getInstance().getDuration();
        int progress = MediaPlayerUtils.getInstance().getProgress();
        if (max > 0 && progress >= 0) {
            binding.seekBar.setMax(max);
            binding.seekBar.setProgress(progress);
            binding.setDuration(MusicTimeTool.getMusicTime(max));
            binding.setProgressTime(MusicTimeTool.getMusicTime(progress));
        }
        HomeMusicIconRotateTool.rotateView(false, MediaPlayerUtils.getInstance().isPlaying(), binding.civ);
        initPlayButton(MediaPlayerUtils.getInstance().isPlaying());
        if (MediaPlayerUtils.getInstance().isEnd()) {
            try {
                unbindService(connection);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
