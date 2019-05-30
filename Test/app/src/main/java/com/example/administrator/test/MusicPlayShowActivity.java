package com.example.administrator.test;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.databinding.BindingAdapter;
import android.databinding.BindingMethod;
import android.databinding.BindingMethods;
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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Toast;

import com.example.administrator.test.MyView.BlurTransformation;
import com.example.administrator.test.base.BaseActivity;
import com.example.administrator.test.daoJavaBean.Song;
import com.example.administrator.test.databinding.ActivityMusicPlayShowBinding;
import com.example.administrator.test.event.BufferUpdateEvent;
import com.example.administrator.test.event.EventInternetMusicEnd;
import com.example.administrator.test.event.IsLightChangeEvent;
import com.example.administrator.test.event.MediaPlayerEvent;
import com.example.administrator.test.event.MusicChangeEvent;
import com.example.administrator.test.event.QQGeCiEvent;
import com.example.administrator.test.lrc.LrcAdapter;
import com.example.administrator.test.lrc.LrcEntity;
import com.example.administrator.test.lrc.LrcLineEntity;
import com.example.administrator.test.lrc.LrcListView;
import com.example.administrator.test.minterfcae.MusiPlaycUpdateInterface;
import com.example.administrator.test.presenter.MusicPlayShowPresenter;
import com.example.administrator.test.service.MusicPlayService;
import com.example.administrator.test.singleton.MediaPlayerUtils;
import com.example.administrator.test.singleton.MusicListTool;
import com.example.administrator.test.utils.ActivityUtils;
import com.example.administrator.test.utils.BitmapTool;
import com.example.administrator.test.utils.HomeMusicIconRotateTool;
import com.example.administrator.test.utils.LocalMusicUtils;
import com.example.administrator.test.utils.MusicTimeTool;
import com.example.administrator.test.utils.Res;
import com.example.administrator.test.utils.ScreenUtils;
import com.example.administrator.test.utils.StaticBaseInfo;
import com.example.administrator.test.utils.TextUtil;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.BufferedInputStream;
import java.io.IOException;

import me.wcy.lrcview.LrcView;

public class MusicPlayShowActivity extends BaseActivity<ActivityMusicPlayShowBinding> {

    private MusicPlayService.MusicPlayBinder musicPlayBinder;//服务绑定数据传递对象
    private boolean isTouchSeekBar = false;//主页seekbar是否触摸
    private Intent intent;//播放音乐服务的intent对象
    private HomeMusicIconRotateTool homeMusicIconRotateTool;//播放音乐服务的intent对象
    private boolean firstInitService = false;
    private MusicPlayShowPresenter presenter;
    //    private LrcAdapter lrcAdapter;
    private String geci = "";

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
        binding.setIsAllGeCi(false);
    }

    @Override
    protected void initData() {
        intent = new Intent();
        presenter = new MusicPlayShowPresenter();
        intent.setClass(this, MusicPlayService.class);
        homeMusicIconRotateTool = new HomeMusicIconRotateTool();

        if (MusicListTool.getInstance().getPlaySong() != null && MusicListTool.getInstance().getPlaySong().isInternet()) {
            presenter.getMusicGeCi(MusicListTool.getInstance().getPlaySong().getMid());
        }

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
//                        changeMusicUi(MusicListTool.getInstance().getPlaySong(), false);
                        musicPlayBinder.pause(MusicPlayShowActivity.this);
                    } else {
//                        changeMusicUi(MusicListTool.getInstance().getPlaySong(), true);
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
        binding.civ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.setIsAllGeCi(true);
            }
        });
        binding.lrcView2.setOnPlayClickListener(new LrcView.OnPlayClickListener() {
            @Override
            public boolean onPlayClick(long time) {
                binding.setIsAllGeCi(false);
                return false;
            }
        });

        binding.ivNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MusicListTool.getInstance().getPlaySong() != null) {
                    if (MusicListTool.getInstance().getPlaySong().isInternet()) {
                        EventBus.getDefault().post(new EventInternetMusicEnd());
                    } else {
                        Long position = MusicListTool.getInstance().getPlaySong().getId();
                        Long next = (position + 1) < MusicListTool.getInstance().getList(context).size() ? (position) : 0;
                        MediaPlayerUtils.getInstance().changeMusic(MusicListTool.getInstance().getList(context).get((int) next.longValue()));
                    }
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
            if (firstInitService) {
                musicPlayBinder.removeUpdateListener(MusicPlayShowActivity.this);
                musicPlayBinder.play(context, MusicPlayShowActivity.this);
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
        if (musicPlayBinder != null) {
            if (ActivityManager.getManager().isTop(this)) {
                musicPlayBinder.removeUpdateListener(MusicPlayShowActivity.this);
                musicPlayBinder.play(MusicPlayShowActivity.this, MusicPlayShowActivity.this);
            }
        } else {
            initService();
        }
        if (MusicListTool.getInstance().getPlaySong().isInternet()) {
            presenter.getMusicGeCi(MusicListTool.getInstance().getPlaySong().getMid());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(BufferUpdateEvent event) {
        int i = binding.seekBar.getMax() * event.getPercent() / 100;
        System.out.println("SecondaryProgress:" + i);
        binding.seekBar.setSecondaryProgress(i);
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(QQGeCiEvent event) {
//        Toast.makeText(MusicPlayShowActivity.this,event.getGeci(),Toast.LENGTH_SHORT).show();
//        LrcEntity entity = LrcEntity.parse(event.getGeci());
//        lrcAdapter = new LrcAdapter();
//        lrcAdapter.setData(entity);
//        binding.llv.setVisibility(View.VISIBLE);
//        binding.llv.setAdapter(lrcAdapter);
//        binding.llv.start();
        geci = event.getGeci();
        binding.lrcView.loadLrc(geci);
        binding.lrcView2.loadLrc(geci);
    }

    @Override
    public void update(Song song, int progress, int duration) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setProgress(duration, progress);
//                if(lrcAdapter !=  null){
//                    binding.llv.seekTo(progress);
//                }
                if (binding.lrcView.hasLrc()) {
                    binding.lrcView.updateTime(progress);
                }
                if (binding.lrcView2.hasLrc()) {
                    binding.lrcView2.updateTime(progress);
                }
            }
        });
    }

    private void setPic(Song song) {
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
        if (!song.isInternet() || TextUtil.isEmpty(song.getImageUrl())) {
            Bitmap bitmap = LocalMusicUtils.getArtwork(MusicPlayShowActivity.this, song.getKey(), song.getAlbumId(), true, false);
            if (bitmap != null) {
                binding.civ.setImageBitmap(bitmap);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    Bitmap bitmap1 = BitmapTool.toMoHu(bitmap, context);
                    binding.llMain.setBackground(new BitmapDrawable(getResources(), bitmap1));
//                    bitmap1.recycle();
                }
//                bitmap.recycle();
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


    private void changeMusicUi(Song song, boolean isPlay) {
        if (song == null)
            return;
        setPic(song);
        binding.ivPlay.setSelected(isPlay);
        binding.setSongName(song.getName());
        binding.setSinger(song.singer);
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
        }
    }

    @BindingAdapter(value = {"lrcNormalTextColor"}, requireAll = false)
    public static void setActionClickListener(LrcView view, int color) {
        view.setNormalColor(color);
    }

}
