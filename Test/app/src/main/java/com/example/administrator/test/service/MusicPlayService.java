package com.example.administrator.test.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;

import com.example.administrator.test.minterfcae.MusiPlaycUpdateInterface;
import com.example.administrator.test.singleton.MediaPlayerUtils;
import com.example.administrator.test.singleton.MusicListTool;

import java.util.ArrayList;
import java.util.List;

/**
 * Create by zmm
 * Time 2019/5/10
 * PackageName com.example.administrator.test.service
 */
public class MusicPlayService extends Service {

    private final String CHANNEL_ID = "CHANNEL_ID_SERVICE_MUSIC";
    private final String CHANNEL_NAME = "CHANNEL_NAME_SERVICE_MUSIC";

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

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (MusicListTool.getInstance().haveUserfulSong(this)) {
            MediaPlayerUtils.getInstance().playMusic(this);
            return START_STICKY;
        } else {
            stopSelf();
            return START_NOT_STICKY;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new MusicPlayBinder();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
       if(MediaPlayerUtils.mediaPlayer!=null){
           MediaPlayerUtils.mediaPlayer.release();
       }
    }

    public class MusicPlayBinder extends Binder {
        List<MusiPlaycUpdateInterface> musiPlaycUpdateInterfaces;
        long duration = 100;
        boolean isUpdate = false;

        public MusicPlayBinder setDuration(long duration) {
            this.duration = duration;
            return this;
        }

//        public void stopUpdate() {
//            this.doUpdate = false;
//        }

        public void pause(MusiPlaycUpdateInterface musiPlaycUpdateInterface) {
            MediaPlayerUtils.getInstance().pauseMusic();
            removeUpdateListener(musiPlaycUpdateInterface);
        }

        public void play(Context context, MusiPlaycUpdateInterface musiPlaycUpdateInterface) {
            MediaPlayerUtils.getInstance().playMusic(context);
            addUpdateListener(musiPlaycUpdateInterface);
        }

        public boolean isPlay() {
            return MediaPlayerUtils.getInstance().isPlaying();
        }

        public MusicPlayBinder addUpdateListener(MusiPlaycUpdateInterface musiPlaycUpdateInterface) {
            if (musiPlaycUpdateInterfaces == null) {
                musiPlaycUpdateInterfaces = new ArrayList<>();
            }
            boolean b = false;
            if (musiPlaycUpdateInterfaces.size() == 0) {
                b = true;
            }
            if (!musiPlaycUpdateInterfaces.contains(musiPlaycUpdateInterface)) {
                musiPlaycUpdateInterfaces.add(musiPlaycUpdateInterface);
            }
            if (b) {
                doRefulsh();
            }
            return this;
        }

        public MusicPlayBinder removeUpdateListener(MusiPlaycUpdateInterface musiPlaycUpdateInterface) {
            if (musiPlaycUpdateInterfaces != null && musiPlaycUpdateInterfaces.size() > 0 && musiPlaycUpdateInterfaces.contains(musiPlaycUpdateInterface)) {
                musiPlaycUpdateInterfaces.remove(musiPlaycUpdateInterface);
            }
            return this;
        }

        public void doRefulsh() {
            if (isUpdate) {
                return;
            }
            isUpdate = true;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        try {
                            if (musiPlaycUpdateInterfaces != null && musiPlaycUpdateInterfaces.size() > 0) {
                                for (MusiPlaycUpdateInterface musiPlaycUpdateInterface : musiPlaycUpdateInterfaces) {
                                    musiPlaycUpdateInterface.update(MusicListTool.getInstance().getPlaySong(), MediaPlayerUtils.getInstance().getProgress(), MediaPlayerUtils.getInstance().getDuration());
//                                    System.out.println("刷新:" + "thread:" + Thread.currentThread().getId() + "////" + musiPlaycUpdateInterface.getClass().getName());
                                }
                                Thread.sleep(duration);
                            }
                            else {
                                break;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    isUpdate = false;
                }
            }).start();
        }
    }
}
