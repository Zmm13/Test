package com.example.administrator.test;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Toast;

import com.example.administrator.test.MyView.BlurTransformation;
import com.example.administrator.test.base.BaseActivity;
import com.example.administrator.test.daoJavaBean.Song;
import com.example.administrator.test.databinding.ActivityMusicPlayShowBinding;
import com.example.administrator.test.event.BufferUpdateEvent;
import com.example.administrator.test.event.IsLightChangeEvent;
import com.example.administrator.test.event.MusicChangeEvent;
import com.example.administrator.test.minterfcae.MusiPlaycUpdateInterface;
import com.example.administrator.test.service.MusicPlayService;
import com.example.administrator.test.singleton.MediaPlayerUtils;
import com.example.administrator.test.singleton.MusicListTool;
import com.example.administrator.test.utils.ActivityUtils;
import com.example.administrator.test.utils.BitmapTool;
import com.example.administrator.test.utils.HomeMusicIconRotateTool;
import com.example.administrator.test.utils.LocalMusicUtils;
import com.example.administrator.test.utils.MusicTimeTool;
import com.example.administrator.test.utils.ScreenUtils;
import com.example.administrator.test.utils.StaticBaseInfo;
import com.example.administrator.test.utils.TextUtil;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.BufferedInputStream;
import java.io.IOException;

public class MusicPlayShowActivity extends BaseActivity<ActivityMusicPlayShowBinding> {

    private MusicPlayService.MusicPlayBinder musicPlayBinder;//服务绑定数据传递对象
    private boolean isTouchSeekBar = false;//主页seekbar是否触摸
    private Intent intent;//播放音乐服务的intent对象
    private HomeMusicIconRotateTool homeMusicIconRotateTool;//播放音乐服务的intent对象
    private boolean changePic = true;
    private boolean firstInitService = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        eventBusEnable = true;
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int setLayout() {
        return R.layout.activity_music_play_show;
    }

    @Override
    protected void initView() {
        binding.llMain.setPadding(0, ScreenUtils.getStatusHeight(context), 0, 0);
        binding.setIsLight(StaticBaseInfo.isLight(this));
    }

    @Override
    protected void initData() {
        intent = new Intent();
        intent.setClass(this, MusicPlayService.class);
        //初始化界面
        if (MediaPlayerUtils.mediaPlayer != null && MusicListTool.getInstance().getPlaySong() != null) {
            if (binding.getSongName() == null || !binding.getSongName().equals(MusicListTool.getInstance().getPlaySong().getName())) {
                binding.setSongName(MusicListTool.getInstance().getPlaySong().getName());
                binding.setSinger(MusicListTool.getInstance().getPlaySong().singer);
                setPic(MusicListTool.getInstance().getPlaySong());
            }
            binding.ivPlay.setSelected(MediaPlayerUtils.getInstance().isPlaying());
            binding.setSongName(MusicListTool.getInstance().getPlaySong().name);
            binding.setSinger(MusicListTool.getInstance().getPlaySong().singer);
            binding.ivPlay.setSelected(MediaPlayerUtils.getInstance().isPlaying());
            binding.seekBar.setMax(MusicListTool.getInstance().getPlaySong().duration);
            binding.setDuration(MusicTimeTool.getMusicTime(MusicListTool.getInstance().getPlaySong().duration));
            if (!isTouchSeekBar) {
                binding.seekBar.setProgress(MediaPlayerUtils.getInstance().getProgress());
                binding.setProgressTime(MusicTimeTool.getMusicTime(MediaPlayerUtils.getInstance().getProgress()));
            }
        }
        homeMusicIconRotateTool = new HomeMusicIconRotateTool();
    }

    @Override
    protected void initListener() {
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
                if (MusicListTool.getInstance().getList(MusicPlayShowActivity.this) == null || MusicListTool.getInstance().getList(context).size() == 0) {
                    Toast toast = Toast.makeText(MusicPlayShowActivity.this, "本地没有音乐文件！", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    return;
                }
                if (musicPlayBinder != null) {
                    if (musicPlayBinder.isPlay()) {
                        musicPlayBinder.pause(MusicPlayShowActivity.this);
                        binding.ivPlay.setSelected(false);
                        homeMusicIconRotateTool.rotateView(false, binding.civ);
                    } else {
                        musicPlayBinder.play(MusicPlayShowActivity.this, MusicPlayShowActivity.this);
                    }
                } else {
                    initService();
                }
            }
        });
        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
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
            musicPlayBinder.removeUpdateListener(MusicPlayShowActivity.this);
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
                musicPlayBinder.removeUpdateListener(MusicPlayShowActivity.this);
                musicPlayBinder.addUpdateListener(MusicPlayShowActivity.this);
//                musicPlayBinder.play(context,MusicPlayShowActivity.this);
            }
            if(firstInitService){
                musicPlayBinder.removeUpdateListener(MusicPlayShowActivity.this);
                musicPlayBinder.play(context,MusicPlayShowActivity.this);
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
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (musicPlayBinder != null && musicPlayBinder.isPlay()) {
            musicPlayBinder.removeUpdateListener(MusicPlayShowActivity.this);
        }
        homeMusicIconRotateTool.rotateView(false, binding.civ);
        System.out.println("onStop22222222");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            unbindService(connection);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initService() {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, StaticBaseInfo.MAINACTIVITY_REQUEST_CODE_WRITE_EXTERNAL_STORAGE);
        } else {
            //开启服务播放音乐
            if (Build.VERSION.SDK_INT >= 26) {
                startForegroundService(intent);
            } else {
                startService(intent);
            }
            //绑定服务更新ui
            bindService(intent, connection, Context.BIND_AUTO_CREATE);
            firstInitService = true;
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
                    MusicListTool.initList(context);
                }
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(IsLightChangeEvent event) {
        binding.setIsLight(StaticBaseInfo.isLight(this));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(MusicChangeEvent event) {
        changePic = true;
        if (musicPlayBinder != null) {
            if (ActivityManager.getManager().isTop(this)) {
                musicPlayBinder.removeUpdateListener(MusicPlayShowActivity.this);
                musicPlayBinder.play(MusicPlayShowActivity.this, MusicPlayShowActivity.this);
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

    @Override
    public void update(Song song, int progress, int duration) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setPic(song);
                binding.ivPlay.setSelected(musicPlayBinder.isPlay());
                binding.setSongName(song.getName());
                binding.setSinger(song.singer);
                binding.ivPlay.setSelected(musicPlayBinder.isPlay());
                binding.seekBar.setMax(duration);
                binding.setDuration(MusicTimeTool.getMusicTime(duration));
                if (!isTouchSeekBar) {
                    binding.seekBar.setProgress(progress);
                    binding.setProgressTime(MusicTimeTool.getMusicTime(progress));
                }
                homeMusicIconRotateTool.rotateView(musicPlayBinder.isPlay(), binding.civ);
            }
        });
    }

    private void setPic(Song song) {
        if (!changePic) {
            return;
        }
        changePic = false;
        Target target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    binding.llMain.setBackground(new BitmapDrawable(bitmap));
                }
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
            }
        };
        if (!song.isInternet()||TextUtil.isEmpty(song.getImageUrl())) {
            Bitmap bitmap = LocalMusicUtils.getArtwork(MusicPlayShowActivity.this, song.getKey(), song.getAlbumId(), true, false);
            if (bitmap != null) {
                binding.civ.setImageBitmap(bitmap);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    binding.llMain.setBackground(new BitmapDrawable(getResources(), BitmapTool.toMoHu(bitmap,context)));
                }
            } else {
                Picasso.with(this).load(R.drawable.girl_icon).into(binding.civ);
                Picasso.with(this).load(R.drawable.girl_icon).transform(new BlurTransformation(this)).into(target);
            }
        } else {
            Picasso.with(MusicPlayShowActivity.this).load(Uri.parse(song.getImageUrl())).into(binding.civ);
            Picasso.with(this).load(Uri.parse(song.getImageUrl())).transform(new BlurTransformation(this)).into(target);
        }
//                Bitmap bitmap = LocalMusicUtils.getArtwork(context, MusicListTool.getInstance().getPlaySong().getKey(), MusicListTool.getInstance().getPlaySong().getAlbumId(), true, false);
//                if (bitmap != null) {
//                    binding.civ.setImageBitmap(bitmap);
//                } else {
//                    binding.civ.setImageDrawable(getResources().getDrawable(R.drawable.girl_icon));
//                }
    }
}
