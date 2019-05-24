package com.example.administrator.test.event;

import com.example.administrator.test.entity.GeDanInfo;

import java.util.List;

/**
 * Create by zmm
 * Time 2019/5/24
 * PackageName com.example.administrator.test.event
 */
public class GeDanInfoEvent {
    public List<GeDanInfo> list;

    public GeDanInfoEvent(List<GeDanInfo> list) {
        this.list = list;
    }
}
