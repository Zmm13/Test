package com.example.administrator.test.singleton;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.widget.Toast;

import com.example.administrator.test.daoJavaBean.Song;
import com.example.administrator.test.event.EventInternetMusicEnd;
import com.example.administrator.test.event.MusicChangeEvent;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;

/**
 * Create by zmm
 * Time 2019/5/5
 * PackageName com.example.administrator.test.singleton
 */
public class MediaPlayerUtils {
    public static volatile MediaPlayerUtils instance;
    public static MediaPlayer mediaPlayer;
    private int pauseProgress = -1;
    private boolean isEnd = true;

    public boolean isEnd() {
        return isEnd;
    }

    public int getProgress() {
        return mediaPlayer.getCurrentPosition();
    }

    public int getDuration() {
        return mediaPlayer.getDuration();
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

    private MediaPlayerUtils() {
        mediaPlayer = new MediaPlayer();
    }

    public static MediaPlayerUtils getInstance() {
        if (instance == null) {
            synchronized (MediaPlayerUtils.class) {
                if (instance == null) {
                    instance = new MediaPlayerUtils();
                }
            }
        }
        return instance;
    }

    public void playMusic(Context context) {
        try {
            if (isEnd) {
                initMediaPlayer(context,false);
            }
            mediaPlayer.start();
            isEnd = false;
            System.out.println("play");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void pauseMusic() {
        try {
            mediaPlayer.pause();
            pauseProgress = mediaPlayer.getCurrentPosition();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void contiuneMusic() {
        try {
            if (pauseProgress > 0) {
                mediaPlayer.start();
            }
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

    public void initMediaPlayer(Context context,boolean loop) {
        try {
            pauseProgress = -1;
            resetMusic();
            if(MusicListTool.getInstance().getPlaySong() == null ){
                MusicListTool.getInstance().setPlaySong(MusicListTool.getInstance().getList(context).get(0));
            }
            if(MusicListTool.getInstance().getPlaySong().getPath().contains("http")){
            mediaPlayer.setDataSource(context,Uri.parse(MusicListTool.getInstance().getPlaySong().getPath()));
            }else {
                File file = new File(MusicListTool.getInstance().getPlaySong().getPath());
                mediaPlayer.setDataSource(file.getPath());//指定音频文件路径
            }
            mediaPlayer.setLooping(loop);//设置为循环播放
            mediaPlayer.prepare();//初始化播放器MediaPlayer
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    isEnd = true;
                    if(MusicListTool.getInstance().getPlaySong().getPath().contains("http")){
                        EventBus.getDefault().post(new EventInternetMusicEnd());
                    }else {
                        Long position = MusicListTool.getInstance().getPlaySong().getId();
                        Long next = (position + 1) < MusicListTool.getInstance().getList(context).size() ? (position) : 0;
                        MusicListTool.getInstance().setPlaySong(MusicListTool.getInstance().getList(context).get((int) next.longValue()));
                        playMusic(context);
                    }
                }
            });
            mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                    resetMusic();
                    isEnd = true;
                    mediaPlayer.stop();
                    Toast.makeText(context,"资源不可用，播放失败！",Toast.LENGTH_SHORT).show();
                    return true;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void changeMusic(Song song){
         isEnd = true;
         MusicListTool.getInstance().setPlaySong(song);
    }

    public  void setPlayPath(Context context,Song song) {
        if(MusicListTool.getInstance().getPlaySong() == null || song.getId().longValue() != MusicListTool.getInstance().getPlaySong().getId().longValue()){
            MusicListTool.getInstance().setPlaySong(song);
            isEnd = true;
            playMusic(context);
        }
    }

}
