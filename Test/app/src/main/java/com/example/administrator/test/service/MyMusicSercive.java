package com.example.administrator.test.service;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;

import com.example.administrator.test.daoJavaBean.Song;
import com.example.administrator.test.singleton.MediaPlayerUtils;
import com.example.administrator.test.singleton.MusicListTool;
import com.example.administrator.test.utils.LocalMusicUtils;
import com.example.administrator.test.utils.MusicTimeTool;
import com.example.administrator.test.utils.SpTool;

import java.util.ArrayList;

/**
 * Create by zmm
 * Time 2019/5/5
 * PackageName com.example.administrator.test.service
 */
public class MyMusicSercive extends Service {
    private final int START_TYPE = 1;
    private final String CHANNEL_ID = "11";
    private final String CHANNEL_NAME = "CHANNEL_NAME";
    private MusicBinder musicBinder = new MusicBinder();

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate() {
        super.onCreate();
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH);

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.createNotificationChannel(channel);

        Notification notification = new Notification.Builder(getApplicationContext(), CHANNEL_ID).build();
        startForeground(1, notification);
    }


    @SuppressLint("WrongConstant")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Song song = intent.getParcelableExtra("song");
        if(song != null){
            MusicListTool.getInstance().setPlaySong(song);
            MediaPlayerUtils.getInstance().playMusic(this);
        }
        return START_TYPE;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.createNotificationChannel(channel);
        Notification notification = new Notification.Builder(getApplicationContext(), CHANNEL_ID).build();
        startForeground(1, notification);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return musicBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     * 内部类继承Binder
     *
     * @author lenovo
     */
    public class MusicBinder extends Binder {
        //获取歌曲长度
        public int getMusicDuration() {
            int rtn = 0;
            rtn = MediaPlayerUtils.getInstance().getDuration();
            return rtn;
        }

        //获取当前播放进度
        public int getMusicCurrentPosition() {
            int rtn = 0;
            rtn = MediaPlayerUtils.getInstance().getProgress();
            return rtn;
        }

        //跳转进度
        public void seekTo(int position) {
            MediaPlayerUtils.getInstance().seekTo(position);
        }

        //是否处于播放状态
        public boolean isPlay() {
            boolean b = false;
            b = MediaPlayerUtils.getInstance().isPlaying();
            return b;
        }

        //是否处于循环状态
        public boolean isLooping() {
            boolean b = false;
            b = MediaPlayerUtils.mediaPlayer.isLooping();

            return b;
        }

        //暂停播放
        public void pauseMusic() {
            MediaPlayerUtils.getInstance().pauseMusic();
        }

        //播放
        public void playMusic() {
            MediaPlayerUtils.getInstance().playMusic(MyMusicSercive.this);
        }

        //播放结束
        public boolean isPlayEnd() {
            return MediaPlayerUtils.getInstance().isEnd();
        }

        //播放结束
        public Bitmap getIcon() {
            return LocalMusicUtils.getArtwork(MyMusicSercive.this, MusicListTool.getInstance().getPlaySong().key, MusicListTool.getInstance().getPlaySong().getAlbumId(), true, false);
        }

    }
}
