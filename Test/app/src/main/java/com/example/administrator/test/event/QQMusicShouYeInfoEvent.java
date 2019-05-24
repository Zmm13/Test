package com.example.administrator.test.event;

import com.example.administrator.test.entity.QQMusicFocus;

import java.util.ArrayList;
import java.util.List;

/**
 * Create by zmm
 * Time 2019/5/23
 * PackageName com.example.administrator.test.event
 */
public class QQMusicShouYeInfoEvent {
    public ShouYeInfo shouYeInfo;

    public QQMusicShouYeInfoEvent(ShouYeInfo shouYeInfo) {
        this.shouYeInfo = shouYeInfo;
    }

    public List<String> getUrls() {
        if (shouYeInfo.getFocusList()!=null && shouYeInfo.getFocusList().size()>0) {
            List<String> list = new ArrayList<>();
            for(int i =0 ;i<shouYeInfo.getFocusList().size();i++){
                list.add(shouYeInfo.getFocusList().get(i).getPic_url());
            }
            return list;
        }
        return null;
    }
}
