package com.example.administrator.test.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.administrator.test.daoJavaBean.Song;

/**
 * Create by zmm
 * Time 2019/5/7
 * PackageName com.example.administrator.test.utils
 */
public class SpTool {
    public static void saveBoolean(Context context, String name, String key, boolean value) {
        SharedPreferences sp = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static boolean getBoolean(Context context, String name, String key) {
        try {
            SharedPreferences sp = context.getSharedPreferences(name, Context.MODE_PRIVATE);
            return sp.getBoolean(key, false);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void savePlaySong(Context context,Song song,int progress) {
        SharedPreferences sp = context.getSharedPreferences(StaticBaseInfo.SP_PLAY_SONG, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putLong("ID", song.getId());
        editor.putLong("KEY", song.getKey());
        editor.putString("NAME", song.getName());
        editor.putString("SINGER", song.getSinger());
        editor.putLong("SIZE", song.getSize());
        editor.putInt("DURATION", song.getDuration());
        editor.putString("PATH", song.getPath());
        editor.putLong("ALBUMID", song.getAlbumId());
        editor.putInt("PROGRESS", progress);
        editor.commit();
    }


    public static Song getPlaySong(Context context) {
        Song song = new Song();
        SharedPreferences sp = context.getSharedPreferences(StaticBaseInfo.SP_PLAY_SONG, Context.MODE_PRIVATE);
        song.setId(sp.getLong("ID", -1));
        song.setKey(sp.getLong("KEY", -1));
        song.setName(sp.getString("NAME","null"));
        song.setSinger(sp.getString("SINGER","singer"));
        song.setSize(sp.getLong("SIZE",-1));
        song.setDuration(sp.getInt("DURATION",-1));
        song.setPath(sp.getString("PATH",""));
        song.setAlbumId(sp.getLong("ALBUMID",-1));
        return song;
    }

    public static int getPlaySongPogress(Context context) {
        SharedPreferences sp = context.getSharedPreferences(StaticBaseInfo.SP_PLAY_SONG, Context.MODE_PRIVATE);
        int progress = sp.getInt("PROGRESS",0);
        return progress;
    }
}
