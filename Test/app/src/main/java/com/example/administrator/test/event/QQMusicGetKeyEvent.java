package com.example.administrator.test.event;

import com.example.administrator.test.entity.QQMusic;

/**
 * Create by zmm
 * Time 2019/5/23
 * PackageName com.example.administrator.test.event
 */
public class QQMusicGetKeyEvent {
    private String key;
    private QQMusic song;
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public QQMusic getSong() {
        return song;
    }

    public void setSong(QQMusic song) {
        this.song = song;
    }

    public QQMusicGetKeyEvent(String key, QQMusic song) {
        this.key = key;
        this.song = song;
    }
}
