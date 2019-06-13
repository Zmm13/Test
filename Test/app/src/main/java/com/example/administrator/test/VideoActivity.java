package com.example.administrator.test;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Toast;

import com.example.administrator.test.MyView.VideoControlView;
import com.example.administrator.test.MyView.MyUpDownView;
import com.example.administrator.test.base.BaseActivity;
import com.example.administrator.test.databinding.ActivityVideoBinding;
import com.example.administrator.test.daoJavaBean.Song;
import com.example.administrator.test.utils.ActivityBrightnessManager;
import com.example.administrator.test.utils.AudioUtil;
import com.example.administrator.test.utils.MusicTimeTool;
import com.example.administrator.test.utils.Res;
import com.example.administrator.test.utils.ScreenUtils;
import com.example.administrator.test.utils.StaticBaseInfo;
import com.example.administrator.test.utils.TextUtil;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

public class VideoActivity extends BaseActivity<ActivityVideoBinding> implements SurfaceHolder.Callback {

    private IjkMediaPlayer ijkMediaPlayer;
    private String path;
    private String title;
    private boolean isTouchSeekBar = false;
    private Timer timer;
    private TimerTask task;
    private TimerTask task2;
    private boolean exitIsPlay = false;
    private int levelDistance = 100;
    private int levelDistance2 = 100;

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
        if (TextUtil.isEmpty(path))
            finish();
        //设置标题
        title = getIntent().getStringExtra("title");
        binding.setTitle(TextUtil.isEmpty(title) ? "无标题" : title);
        //设置是否显示控制器
        binding.setShowControl(true);
        //初始化播放器
        initPlayer();
//        Calendar.getInstance().get(Calendar.YEAR);
    }

    /**
     * 初始化播放器
     */
    private void initPlayer() {
        ijkMediaPlayer = new IjkMediaPlayer();
        ijkMediaPlayer.setScreenOnWhilePlaying(true);
        ijkMediaPlayer.setKeepInBackground(false);
        try {
            ijkMediaPlayer.setDataSource(context, Uri.parse(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        //监听视频大小改变
        ijkMediaPlayer.setOnVideoSizeChangedListener(new IMediaPlayer.OnVideoSizeChangedListener() {
            @Override
            public void onVideoSizeChanged(IMediaPlayer iMediaPlayer, int i, int i1, int i2, int i3) {
                binding.seekBar.setMax((int) iMediaPlayer.getDuration());
                binding.setDuration(MusicTimeTool.getMusicTime(iMediaPlayer.getDuration()));
            }
        });
        //准备完成后监听
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
        //异常监听
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
        //消息监听
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
        //缓存监听
        ijkMediaPlayer.setOnBufferingUpdateListener(new IMediaPlayer.OnBufferingUpdateListener() {
            @Override
            public void onBufferingUpdate(IMediaPlayer iMediaPlayer, int i) {
                binding.seekBar.setSecondaryProgress(binding.seekBar.getMax() / 100 * i);
            }
        });
        //完成监听
        ijkMediaPlayer.setOnCompletionListener(new IMediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(IMediaPlayer iMediaPlayer) {
                Toast.makeText(VideoActivity.this, "播放完毕!", Toast.LENGTH_SHORT).show();
                reflushUI(false);
                binding.ivStart.setSelected(true);
            }
        });
        //初始化surface
        SurfaceHolder surfaceHolder = binding.sfv.getHolder();
        surfaceHolder.addCallback(this);
    }

    @Override
    protected void initListener() {
        //返回按钮点击事件
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
        //滑动指示器监听
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
        //播放按钮点击事件
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
        //全屏按钮点击事件
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
        //左边上下滑监听
        binding.myUpDownLeft.setUpDateInterface(new MyUpDownView.UpDateInterface() {
            @Override
            public void update(MyUpDownView myUpDownView, float distance) {
                binding.llLevel.setVisibility(View.VISIBLE);
                int value = (int) (myUpDownView.getCurrent() + distance / levelDistance2);
                int max = myUpDownView.getMax();
                int level = value >= max ? max : (value <= 0 ? 0 : value);
                ActivityBrightnessManager.setActivityBrightness((float) level / 255f, VideoActivity.this);
                binding.setLevelProgress((level * 100) / max + "%");
            }

            @Override
            public void down(MyUpDownView myUpDownView) {
                myUpDownView.setMax(255);
                levelDistance2 = (int) ((ScreenUtils.getScreenHeight(context) - Res.getDimen(R.dimen.x70, context)) / myUpDownView.getMax());
                myUpDownView.setCurrent((int) (ActivityBrightnessManager.getActivityBrightness(VideoActivity.this) * 255f));
                binding.setLevelState(StaticBaseInfo.TAG_ISLIGHT);
            }

            @Override
            public void up(MyUpDownView myUpDownView) {
                binding.llLevel.setVisibility(View.GONE);
            }

            @Override
            public void click(MyUpDownView myUpDownView) {
                binding.setShowControl(!binding.getShowControl());
            }
        });
        //右边上下滑监听
        binding.myUpDownRight.setUpDateInterface(new MyUpDownView.UpDateInterface() {
            @Override
            public void update(MyUpDownView myUpDownView, float distance) {
                binding.llLevel.setVisibility(View.VISIBLE);
                int value = (int) (myUpDownView.getCurrent() + distance / levelDistance);
                int max = myUpDownView.getMax();
                int level = value >= max ? max : (value <= 0 ? 0 : value);
                AudioUtil.getInstance(context).setMediaVolume(level);
                binding.setLevelProgress((level * 100) / max + "%");
            }

            @Override
            public void down(MyUpDownView myUpDownView) {
                myUpDownView.setMax(AudioUtil.getInstance(context).getMediaMaxVolume());
                levelDistance = (int) ((ScreenUtils.getScreenHeight(context) - Res.getDimen(R.dimen.x70, context)) / myUpDownView.getMax());
                myUpDownView.setCurrent(AudioUtil.getInstance(context).getMediaVolume());
                binding.setLevelState(StaticBaseInfo.TAG_ISVOICE);
            }

            @Override
            public void up(MyUpDownView myUpDownView) {
                binding.llLevel.setVisibility(View.GONE);
            }

            @Override
            public void click(MyUpDownView myUpDownView) {
                binding.setShowControl(!binding.getShowControl());
            }
        });
        //控制器触摸监听设置定时任务隐藏控制器
        binding.llControl.setTouchInterface(new VideoControlView.TouchInterface() {
            @Override
            public void doTouch() {
                closeControlView();
            }
        });
    }

    /**
     * 横竖屏改变监听
     * @param newConfig
     */
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
//        overridePendingTransition(R.anim.animaition_activity_end,0);
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

    /**
     * 刷新ui的线程
     *
     * @param b
     */
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

    /**
     * 返回键监听
     */
    @Override
    public void onBackPressed() {
        binding.ivBack.performClick();
    }

    /**
     * 关闭控制view
     */
    private void closeControlView() {
        if (timer == null) {
            timer = new Timer();
        }
        if (task2 != null) {
            task2.cancel();
            task2 = null;
        }
        task2 = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        binding.setShowControl(false);
                    }
                });

            }
        };
        timer.schedule(task2, 3000);
    }
}
