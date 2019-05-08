package com.example.administrator.test.singleton;

import android.content.Context;

import com.example.administrator.test.MainActivity;
import com.example.administrator.test.daoJavaBean.DBHelper;
import com.example.administrator.test.daoJavaBean.Song;
import com.example.administrator.test.daoJavaBean.SongDao;
import com.example.administrator.test.event.MusicChangeEvent;
import com.example.administrator.test.event.ReflushEvent;
import com.example.administrator.test.utils.LocalMusicUtils;
import com.example.administrator.test.utils.SpTool;
import com.example.administrator.test.utils.StaticBaseInfo;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Create by zmm
 * Time 2019/5/7
 * PackageName com.example.administrator.test.singleton
 */
public class MusicListTool {
    private List<Song> list = null;
    public static volatile MusicListTool instance = null;
    private Song playSong = null;

    private SongDao songDao;
    private DBHelper dbHelper = DBHelper.getInstance();
    private String TABLE_NAME = "song.db";//数据表的名称



    private MusicListTool() {
        list = new ArrayList<>();
    }

    public static MusicListTool getInstance() {
        if (instance == null) {
            synchronized (MusicListTool.class) {
                if (instance == null) {
                    instance = new MusicListTool();
                }
            }
        }
        return instance;
    }

    public List<Song> getList(Context context) {
        synchronized (MusicListTool.class){
            if(list == null || list.size() == 0){
                MusicListTool.getInstance().songDao =  MusicListTool.getInstance().dbHelper.initDatabase(context,  MusicListTool.getInstance().TABLE_NAME).getSongDao();
                list = MusicListTool.getInstance().songDao.loadAll();
            }
//            list = new ArrayList<>();
        }
        return list;
    }

    public Song getPlaySong() {
        return playSong;
    }

    public void setPlaySong(Song playSong) {
        this.playSong = playSong;
        EventBus.getDefault().post(new MusicChangeEvent());
    }

    public static void initList(Context context) {
        if(!SpTool.getBoolean(context,StaticBaseInfo.SP_NAME,StaticBaseInfo.SP_INIT_MUSIC)){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    ArrayList<Song> list = LocalMusicUtils.getmusic(context);
                    if (list != null && list.size() > 0) {
                        MusicListTool.getInstance().songDao =  MusicListTool.getInstance().dbHelper.initDatabase(context,  MusicListTool.getInstance().TABLE_NAME).getSongDao();
                        MusicListTool.getInstance().songDao.deleteAll();
                        for(Song song : list){
                            MusicListTool.getInstance().songDao.insert(song);
                        }
                        SpTool.saveBoolean(context, StaticBaseInfo.SP_NAME,StaticBaseInfo.SP_INIT_MUSIC,true);
                        EventBus.getDefault().post(new ReflushEvent("MusicListFragment",true));
                        System.out.println("音乐加载完咯");
                    }
                }
            }).start();
        }
    }
}
