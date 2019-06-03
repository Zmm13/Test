package com.example.administrator.test.singleton;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.widget.Toast;

import com.example.administrator.test.daoJavaBean.Song;
import com.example.administrator.test.event.BufferUpdateEvent;
import com.example.administrator.test.event.EventInternetMusicEnd;
import com.example.administrator.test.event.MediaPlayerEvent;

import org.greenrobot.eventbus.EventBus;

import java.io.File;

import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

/**
 * Create by zmm
 * Time 2019/6/3
 * PackageName com.example.administrator.test.singleton
 */
public class IjkPlayerUtils {
    public static volatile IjkPlayerUtils instance = null;
    private IjkMediaPlayer mediaPlayer;
    private boolean isEnd = true;

    private IjkPlayerUtils() {
        mediaPlayer = new IjkMediaPlayer();
        mediaPlayer.setLogEnabled(true);
        mediaPlayer.setLooping(false);
//        mediaPlayer.setScreenOnWhilePlaying(true);//屏幕常亮
//        mediaPlayer.setKeepInBackground(false);//后台播放
    }

    public static IjkPlayerUtils getInstance() {
        if (instance == null) {
            synchronized (IjkPlayerUtils.class) {
                if (instance == null) {
                    instance = new IjkPlayerUtils();
                }
            }
        }
        return instance;
    }

    /**
     * 初始化数据
     */
    private void initData(Context context) {
        try {
            resetMusic();
            if (MusicListTool.getInstance().getPlaySong() == null) {
                MusicListTool.getInstance().setPlaySong(MusicListTool.getInstance().getList(context).get(0));
            }
            if (MusicListTool.getInstance().getPlaySong().isInternet()) {
                mediaPlayer.setDataSource(context, Uri.parse(MusicListTool.getInstance().getPlaySong().getPath()));
            } else {
                File file = new File(MusicListTool.getInstance().getPlaySong().getPath());
                mediaPlayer.setDataSource(file.getPath());//指定音频文件路径
            }
            mediaPlayer.setOnCompletionListener(new IMediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(IMediaPlayer iMediaPlayer) {
                    isEnd = true;
                    if (MusicListTool.getInstance().getPlaySong().isInternet()) {
                        EventBus.getDefault().post(new EventInternetMusicEnd());
                    } else {
                        Long position = MusicListTool.getInstance().getPlaySong().getId();
                        Long next = (position + 1) < MusicListTool.getInstance().getList(context).size() ? (position) : 0;
                        MusicListTool.getInstance().setPlaySong(MusicListTool.getInstance().getList(context).get((int) next.longValue()));
                    }
                }
            });
            mediaPlayer.setOnErrorListener(new IMediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(IMediaPlayer iMediaPlayer, int i, int i1) {
                    resetMusic();
                    isEnd = true;
                    Toast.makeText(context, "资源不可用，播放失败！", Toast.LENGTH_SHORT).show();
                    EventBus.getDefault().post(new MediaPlayerEvent(MediaPlayerEvent.STATE_ERROR));
                    return true;
                }
            });
            mediaPlayer.setOnBufferingUpdateListener(new IMediaPlayer.OnBufferingUpdateListener() {
                @Override
                public void onBufferingUpdate(IMediaPlayer iMediaPlayer, int i) {
                    EventBus.getDefault().post(new BufferUpdateEvent(i));
                }
            });
            mediaPlayer.setOnPreparedListener(new IMediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(IMediaPlayer iMediaPlayer) {
                    if(mediaPlayer.isPlayable()){
                        mediaPlayer.start();
                        isEnd = false;
                        System.out.println("play");
                        EventBus.getDefault().post(new MediaPlayerEvent(MediaPlayerEvent.STATE_STARTED));
                    }
                }
            });
            mediaPlayer.prepareAsync();//初始化播放器MediaPlayer
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setEnd(boolean end) {
        isEnd = end;
    }

    public void changeMusic(Song song) {
        isEnd = true;
        MusicListTool.getInstance().setPlaySong(song);
    }

    public void playMusic(Context context) {
        try {
            if (isEnd) {
                initData(context);
            } else {
                if(mediaPlayer.isPlayable()){
                    mediaPlayer.start();
                    isEnd = false;
                    System.out.println("play");
                    EventBus.getDefault().post(new MediaPlayerEvent(MediaPlayerEvent.STATE_STARTED));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void pauseMusic() {
        try {
            mediaPlayer.pause();
            EventBus.getDefault().post(new MediaPlayerEvent(MediaPlayerEvent.STATE_PAUSE));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void resetMusic() {
        try {
            mediaPlayer.reset();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isEnd() {
        return isEnd;
    }

    public int getProgress() {
        return (int) mediaPlayer.getCurrentPosition();
    }

    public int getDuration() {
        return (int) mediaPlayer.getDuration();
    }

    public void seekTo(int position) {
        mediaPlayer.seekTo(position);
    }

    public boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }

    public boolean isLooping() {
        return mediaPlayer.isLooping();
    }

}
