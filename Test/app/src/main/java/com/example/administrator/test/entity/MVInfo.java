package com.example.administrator.test.entity;

import android.databinding.BindingAdapter;
import android.net.Uri;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Create by zmm
 * Time 2019/6/3
 * PackageName com.example.administrator.test.entity
 */
public class MVInfo {
    private MVInfoDetail mvInfoDetail;
    private List<QQMuiscSinger> singers;

    public List<QQMuiscSinger> getSingers() {
        return singers;
    }

    public void setSingers(List<QQMuiscSinger> singers) {
        this.singers = singers;
    }

    public MVInfoDetail getMvInfoDetail() {
        return mvInfoDetail;
    }

    public void setMvInfoDetail(MVInfoDetail mvInfoDetail) {
        this.mvInfoDetail = mvInfoDetail;
    }

    public String getShowSinger(){
        StringBuilder  builder = new StringBuilder();
        if (singers != null && singers.size()>0) {
            for(int i = 0;i<singers.size();i++){
                if(i == 0){
                    builder.append(singers.get(i).getName());
                }else {
                    builder.append("/"+singers.get(i).getName());
                }
            }
        }
        return builder.toString();
    }
    @BindingAdapter({"imagePath"})
    public static void loadimage(ImageView imageView, String url){
//        Glide.with(imageView.getContext()).load(Uri.parse(url)).into(imageView);
        Picasso.with(imageView.getContext()).load(Uri.parse(url)).into(imageView);
    }
}
