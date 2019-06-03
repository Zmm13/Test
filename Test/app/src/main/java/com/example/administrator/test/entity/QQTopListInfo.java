package com.example.administrator.test.entity;

import android.databinding.BindingAdapter;
import android.net.Uri;
import android.widget.ImageView;

import com.example.administrator.test.utils.ShowLongNumberTools;
import com.squareup.picasso.Picasso;

/**
 * Create by zmm
 * Time 2019/5/27
 * PackageName com.example.administrator.test.entity
 */
public class QQTopListInfo {
    private int topId;
    private String title;
    private String period;
    private String listenNum;
    private String headPicUrl;
    private int recType;

    @BindingAdapter({"imagePath"})
    public static void loadimage(ImageView imageView, String url){
//        Glide.with(imageView.getContext()).load(Uri.parse(url)).into(imageView);
        Picasso.with(imageView.getContext()).load(Uri.parse(url)).into(imageView);
    }

    public int getRecType() {
        return recType;
    }

    public void setRecType(int recType) {
        this.recType = recType;
    }

    public int getTopId() {
        return topId;
    }

    public void setTopId(int topId) {
        this.topId = topId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getListenNum() {
        return listenNum;
    }

    public void setListenNum(String listenNum) {
        this.listenNum = listenNum;
    }

    public String getHeadPicUrl() {
        return headPicUrl;
    }

    public void setHeadPicUrl(String headPicUrl) {
        this.headPicUrl = headPicUrl;
    }

    public String getCount(){
        long count = Long.parseLong(listenNum);
        return "播放量:"+ ShowLongNumberTools.getShow(count);
    }
}
