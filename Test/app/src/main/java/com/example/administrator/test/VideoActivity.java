package com.example.administrator.test;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Toast;

import com.example.administrator.test.base.BaseActivity;
import com.example.administrator.test.databinding.ActivityVideoBinding;
import com.example.administrator.test.daoJavaBean.Song;
import com.example.administrator.test.utils.MusicTimeTool;
import com.example.administrator.test.utils.Res;
import com.example.administrator.test.utils.TextUtil;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;
import tv.danmaku.ijk.media.player.IjkTimedText;

public class VideoActivity extends BaseActivity<ActivityVideoBinding> implements SurfaceHolder.Callback {

    private IjkMediaPlayer ijkMediaPlayer;
    private String path;
    private String title;
    private boolean isTouchSeekBar = false;
    private Timer timer;
    private TimerTask task;
    private SurfaceHolder surfaceHolder;
    private boolean exitIsPlay = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        //全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    protected int setLayout() {
        return R.layout.activity_video;
    }

    @Override
    protected void initView() {
        binding.setIsLight(false);
    }

    @Override
    protected void initData() {
        path = getIntent().getStringExtra("path");
        title = getIntent().getStringExtra("title");
        binding.setTitle(TextUtil.isEmpty(title) ? "无标题" : title);
//        path = "http://v1.itooi.cn/tencent/mvUrl?id=f0030zht1hf&quality=1080";
        if (TextUtil.isEmpty(path))
            return;
        initPlayer();
    }

    private void initPlayer() {
        ijkMediaPlayer = new IjkMediaPlayer();
        ijkMediaPlayer.setScreenOnWhilePlaying(true);
        ijkMediaPlayer.setKeepInBackground(false);
        try {
            ijkMediaPlayer.setDataSource(context, Uri.parse(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        SurfaceHolder surfaceHolder = binding.sfv.getHolder();
        surfaceHolder.addCallback(this);
    }

    @Override
    protected void initListener() {
        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                } else {
                    finish();
                }
            }
        });
        ijkMediaPlayer.setOnPreparedListener(new IMediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(IMediaPlayer iMediaPlayer) {
                if (ijkMediaPlayer.isPlayable()) {
                    ijkMediaPlayer.start();
                    reflushUI(true);
                    binding.loadView.setVisibility(View.GONE);
                    binding.ivStart.setSelected(false);
//                 new Thread(new SeekThread()).start();
                }
            }
        });
        ijkMediaPlayer.setOnErrorListener(new IMediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(IMediaPlayer iMediaPlayer, int i, int i1) {
                Toast.makeText(VideoActivity.this, "播放失败！", Toast.LENGTH_SHORT).show();
                reflushUI(false);
                binding.ivStart.setSelected(true);
                binding.loadView.setVisibility(View.GONE);
                iMediaPlayer.reset();
                initPlayer();
                return true;
            }
        });
        ijkMediaPlayer.setOnTimedTextListener(new IMediaPlayer.OnTimedTextListener() {
            @Override
            public void onTimedText(IMediaPlayer iMediaPlayer, IjkTimedText ijkTimedText) {
                System.out.println("setOnTimedTextListener");
            }
        });
        ijkMediaPlayer.setOnInfoListener(new IMediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(IMediaPlayer iMediaPlayer, int i, int i1) {
                switch (i) {
                    case IjkMediaPlayer.MEDIA_INFO_BUFFERING_START:
                        binding.loadView.setVisibility(View.VISIBLE);
                        return true;
                    case IjkMediaPlayer.MEDIA_INFO_BUFFERING_END:
                        binding.loadView.setVisibility(View.GONE);
                        return true;
                }
                System.out.println("setOnInfoListener:" + i);
                return false;
            }
        });
        ijkMediaPlayer.setOnBufferingUpdateListener(new IMediaPlayer.OnBufferingUpdateListener() {
            @Override
            public void onBufferingUpdate(IMediaPlayer iMediaPlayer, int i) {
                binding.seekBar.setSecondaryProgress(binding.seekBar.getMax() / 100 * i);
            }
        });
        ijkMediaPlayer.setOnCompletionListener(new IMediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(IMediaPlayer iMediaPlayer) {
                Toast.makeText(VideoActivity.this, "播放完毕!", Toast.LENGTH_SHORT).show();
                reflushUI(false);
                binding.ivStart.setSelected(true);
            }
        });
        ijkMediaPlayer.setOnVideoSizeChangedListener(new IMediaPlayer.OnVideoSizeChangedListener() {
            @Override
            public void onVideoSizeChanged(IMediaPlayer iMediaPlayer, int i, int i1, int i2, int i3) {
                binding.seekBar.setMax((int) iMediaPlayer.getDuration());
                binding.setDuration(MusicTimeTool.getMusicTime(iMediaPlayer.getDuration()));
            }
        });
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
                isTouchSeekBar = false;
                ijkMediaPlayer.seekTo(binding.seekBar.getProgress());
            }
        });
        binding.ivStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ijkMediaPlayer != null) {
                    if (ijkMediaPlayer.isPlaying()) {
                        ijkMediaPlayer.pause();
                        binding.ivStart.setSelected(true);
                        reflushUI(false);
                        System.out.println("pause");
                    } else if (ijkMediaPlayer.isPlayable()) {
                        ijkMediaPlayer.start();
                        binding.ivStart.setSelected(false);
                        reflushUI(true);
                        System.out.println("play");
                    }
                }
            }
        });
        binding.ivFullOrHalfScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int screenNum = getResources().getConfiguration().orientation;
                if (screenNum == 1) {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                } else {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                }
            }
        });
        binding.llControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.llControl.setVisibility(View.INVISIBLE);
            }
        });
        binding.sfv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.llControl.setVisibility(View.VISIBLE);
            }
        });
        binding.loadView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {//切换为横屏
            binding.ivFullOrHalfScreen.setSelected(true);
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) binding.sfv.getLayoutParams();
            layoutParams.height = RelativeLayout.LayoutParams.MATCH_PARENT;
        } else {
            binding.ivFullOrHalfScreen.setSelected(false);
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) binding.sfv.getLayoutParams();
            layoutParams.height = (int) Res.getDimen(R.dimen.x200, context);
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        ijkMediaPlayer.setDisplay(surfaceHolder);
        binding.loadView.setVisibility(View.VISIBLE);
        ijkMediaPlayer.prepareAsync();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

    }

    @Override
    public void update(Song song, int progress, int duration) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (ijkMediaPlayer.isPlaying()) {
            ijkMediaPlayer.stop();
        }
        ijkMediaPlayer.release();
        if (timer != null) {
            timer.cancel();
            timer.purge();
            timer = null;
        }
    }

    public class SeekThread implements Runnable {
        @Override
        public void run() {
            while (true) {
                if (ijkMediaPlayer != null && ijkMediaPlayer.isPlaying()) {
                    if (!isTouchSeekBar) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                binding.seekBar.setProgress((int) ijkMediaPlayer.getCurrentPosition());
                            }
                        });
                    }
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (ijkMediaPlayer.isPlaying()) {
            binding.ivStart.performClick();
            exitIsPlay = true;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (exitIsPlay) {
            binding.ivStart.performClick();
            exitIsPlay = false;
        }
    }

    private void reflushUI(boolean b) {
        if (b) {
            if (timer == null) {
                timer = new Timer();
            }
            task = new TimerTask() {
                @Override
                public void run() {
                    if (!isTouchSeekBar) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                binding.seekBar.setProgress((int) ijkMediaPlayer.getCurrentPosition());
                            }
                        });
                    }
                }
            };
            timer.schedule(task, 100, 100);
        } else {
            if (task != null) {
                task.cancel();
                task = null;
            }
        }
    }

    @Override
    public void onBackPressed() {
        binding.ivBack.performClick();
    }
}
