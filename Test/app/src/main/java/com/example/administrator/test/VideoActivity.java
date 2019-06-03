package com.example.administrator.test;

import android.content.pm.ActivityInfo;
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
import com.example.administrator.test.utils.Res;
import com.example.administrator.test.utils.TextUtil;

import java.io.IOException;

import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

public class VideoActivity extends BaseActivity<ActivityVideoBinding> implements SurfaceHolder.Callback {

    private IjkMediaPlayer ijkMediaPlayer;
    private String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null){
            actionBar.hide();
        }
        //全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,  WindowManager.LayoutParams.FLAG_FULLSCREEN);
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
//        path = "http://v1.itooi.cn/tencent/mvUrl?id=f0030zht1hf&quality=1080";
        if (TextUtil.isEmpty(path))
            return;
        SurfaceHolder surfaceHolder = binding.sfv.getHolder();
        surfaceHolder.addCallback(this);
        ijkMediaPlayer = new IjkMediaPlayer();
        ijkMediaPlayer.setScreenOnWhilePlaying(true);
        ijkMediaPlayer.setKeepInBackground(false);
        try {
            ijkMediaPlayer.setDataSource(context, Uri.parse(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void initListener() {
     ijkMediaPlayer.setOnPreparedListener(new IMediaPlayer.OnPreparedListener() {
         @Override
         public void onPrepared(IMediaPlayer iMediaPlayer) {
             if (ijkMediaPlayer.isPlayable()) {
                 binding.seekBar.setMax((int) iMediaPlayer.getDuration());
                 ijkMediaPlayer.start();
             }
         }
     });
     ijkMediaPlayer.setOnErrorListener(new IMediaPlayer.OnErrorListener() {
         @Override
         public boolean onError(IMediaPlayer iMediaPlayer, int i, int i1) {
             Toast.makeText(VideoActivity.this,"faild", Toast.LENGTH_SHORT).show();
             return true;
         }
     });
     ijkMediaPlayer.setOnBufferingUpdateListener(new IMediaPlayer.OnBufferingUpdateListener() {
         @Override
         public void onBufferingUpdate(IMediaPlayer iMediaPlayer, int i) {
             binding.seekBar.setSecondaryProgress(i);
         }
     });
     ijkMediaPlayer.setOnCompletionListener(new IMediaPlayer.OnCompletionListener() {
         @Override
         public void onCompletion(IMediaPlayer iMediaPlayer) {
             Toast.makeText(VideoActivity.this,"播放完毕",Toast.LENGTH_SHORT).show();
         }
     });
        binding.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                ijkMediaPlayer.seekTo(binding.seekBar.getProgress());
            }
        });
        binding.ivStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ijkMediaPlayer!=null){
                    if(ijkMediaPlayer.isPlaying()){
                        ijkMediaPlayer.pause();
                        binding.ivStart.setSelected(true);
                    }else if(ijkMediaPlayer.isPlayable()){
                        ijkMediaPlayer.start();
                        binding.ivStart.setSelected(false);
                    }
                }
            }
        });
        binding.ivFullOrHalfScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int screenNum = getResources().getConfiguration().orientation;
                if(screenNum==1){
                    binding.ivFullOrHalfScreen.setSelected(true);
                    RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) binding.sfv.getLayoutParams();
                    layoutParams.height = RelativeLayout.LayoutParams.MATCH_PARENT;
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                }else {
                    binding.ivFullOrHalfScreen.setSelected(false);
                    RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) binding.sfv.getLayoutParams();
                    layoutParams.height = (int) Res.getDimen(R.dimen.x200,context);
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                }
            }
        });
    }


    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        ijkMediaPlayer.setDisplay(surfaceHolder);
        ijkMediaPlayer._prepareAsync();
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
        ijkMediaPlayer.release();
    }
}
