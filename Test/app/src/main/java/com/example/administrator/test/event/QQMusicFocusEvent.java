package com.example.administrator.test.event;

import com.example.administrator.test.entity.QQMusicFocus;

import java.util.ArrayList;
import java.util.List;

/**
 * Create by zmm
 * Time 2019/5/23
 * PackageName com.example.administrator.test.event
 */
public class QQMusicFocusEvent {
    private List<QQMusicFocus> focusList;

    public QQMusicFocusEvent(List<QQMusicFocus> focusList) {
        this.focusList = focusList;
    }

    public List<QQMusicFocus> getFocusList() {
        return focusList;
    }

    public void setFocusList(List<QQMusicFocus> focusList) {
        this.focusList = focusList;
    }

    public List<String> getUrls() {
        if (focusList!=null && focusList.size()>0) {
            List<String> list = new ArrayList<>();
            for(int i =0 ;i<focusList.size();i++){
                list.add(focusList.get(i).getPic_url());
            }
            return list;
        }
        return null;
    }
}
