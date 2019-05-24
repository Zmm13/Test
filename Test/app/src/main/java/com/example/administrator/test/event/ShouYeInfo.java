package com.example.administrator.test.event;

import com.example.administrator.test.entity.QQMusicFocus;

import java.util.ArrayList;
import java.util.List;

/**
 * Create by zmm
 * Time 2019/5/24
 * PackageName com.example.administrator.test.event
 */
public class ShouYeInfo {
    private List<QQMusicFocus> focusList;
    private List<GeDan> geDans;

    public List<QQMusicFocus> getFocusList() {
        return focusList;
    }

    public void setFocusList(List<QQMusicFocus> focusList) {
        this.focusList = focusList;
    }

    public List<GeDan> getGeDans() {
        return geDans;
    }

    public void setGeDans(List<GeDan> geDans) {
        this.geDans = geDans;
    }

    public List<String> getNames(){
        List<String> list = null;
        if(geDans!=null&&geDans.size()>0){
            list = new ArrayList<>();
            for(int i = 0;i<geDans.size();i++){
                list.add(geDans.get(i).getItem_name());
            }
        }
        return list;
    }
}
