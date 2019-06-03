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


/**
 * Create by zmm
 * Time 2019/5/5
 * PackageName com.example.administrator.test.singleton
 */
public class MediaPlayer2Utils {
    public static volatile MediaPlayer2Utils instance;
    public static MediaPlayer mediaPlayer;
    private int pauseProgress = -1;
    private boolean isEnd = true;

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

    private MediaPlayer2Utils() {
        mediaPlayer = new MediaPlayer();
    }

    public static MediaPlayer2Utils getInstance() {
        if (instance == null) {
            synchronized (MediaPlayer2Utils.class) {
                if (instance == null) {
                    instance = new MediaPlayer2Utils();
                }
            }
        }
        return instance;
    }

    public void playMusic(Context context) {
        try {
            if (isEnd) {
                initMediaPlayer(context, false);
            } else {
                mediaPlayer.start();
                isEnd = false;
                System.out.println("play");
                EventBus.getDefault().post(new MediaPlayerEvent(MediaPlayerEvent.STATE_STARTED));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void pauseMusic() {
        try {
            mediaPlayer.pause();
            pauseProgress = (int) mediaPlayer.getCurrentPosition();
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

    public synchronized void initMediaPlayer(Context context, boolean loop) {
        try {
            System.out.println("initMediaPlayer");
            pauseProgress = -1;
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
            mediaPlayer.setLooping(loop);//设置为循环播放
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
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
            mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                    resetMusic();
                    isEnd = true;
                    Toast.makeText(context, "资源不可用，播放失败！", Toast.LENGTH_SHORT).show();
                    EventBus.getDefault().post(new MediaPlayerEvent(MediaPlayerEvent.STATE_ERROR));
                    return true;
                }
            });
            mediaPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
                @Override
                public void onBufferingUpdate(MediaPlayer mediaPlayer, int i) {
                    EventBus.getDefault().post(new BufferUpdateEvent(i));
                }
            });
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    mediaPlayer.start();
                    isEnd = false;
                    System.out.println("play");
                    EventBus.getDefault().post(new MediaPlayerEvent(MediaPlayerEvent.STATE_STARTED));
                }
            });
            mediaPlayer.prepare();//初始化播放器MediaPlayer
        } catch (Exception e) {
            e.printStackTrace();
            initMediaPlayer(context, loop);
        }
    }

    public void setEnd(boolean end) {
        isEnd = end;
    }

    public void changeMusic(Song song) {
        isEnd = true;
        MusicListTool.getInstance().setPlaySong(song);
    }

}
