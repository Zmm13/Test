package com.example.administrator.test.event;

import com.example.administrator.test.entity.MVInfo;
import com.example.administrator.test.entity.QQMusic;

import java.util.List;

/**
 * Create by zmm
 * Time 2019/5/23
 * PackageName com.example.administrator.test.event
 */
public class QQMVEvent {
    private List<MVInfo> list;

    public QQMVEvent(List<MVInfo> list) {
        this.list = list;
    }

    public List<MVInfo> getList() {
        return list;
    }

    public void setList(List<MVInfo> list) {
        this.list = list;
    }
}
