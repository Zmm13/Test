package com.example.administrator.test.event;

import com.example.administrator.test.entity.QQTopListInfo;

/**
 * Create by zmm
 * Time 2019/5/27
 * PackageName com.example.administrator.test.event
 */
public class QQInternetMusicListChangeEvent {
    private QQTopListInfo info;

    public QQInternetMusicListChangeEvent(QQTopListInfo info) {
        this.info = info;
    }

    public QQTopListInfo getInfo() {
        return info;
    }

    public void setInfo(QQTopListInfo info) {
        this.info = info;
    }
}
