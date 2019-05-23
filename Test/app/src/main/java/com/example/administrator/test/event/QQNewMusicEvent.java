package com.example.administrator.test.event;

import com.example.administrator.test.entity.QQMusic;

import java.util.List;

/**
 * Create by zmm
 * Time 2019/5/23
 * PackageName com.example.administrator.test.event
 */
public class QQNewMusicEvent {
    private List<QQMusic> list;

    public QQNewMusicEvent(List<QQMusic> list) {
        this.list = list;
    }

    public List<QQMusic> getList() {
        return list;
    }

    public void setList(List<QQMusic> list) {
        this.list = list;
    }
}
