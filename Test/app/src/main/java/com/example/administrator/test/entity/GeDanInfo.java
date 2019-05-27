package com.example.administrator.test.entity;

import android.databinding.BindingAdapter;
import android.net.Uri;
import android.widget.ImageView;

import com.example.administrator.test.R;
import com.example.administrator.test.utils.ShowLongNumberTools;
import com.squareup.picasso.Picasso;

/**
 * Create by zmm
 * Time 2019/5/24
 * PackageName com.example.administrator.test.entity
 */
public class GeDanInfo {
    private String access_num;
    private String album_pic_mid;
    private String cover_url_big;
    private String title;

    @BindingAdapter({"imagePath"})
    public static void loadimage(ImageView imageView, String url){
//        Glide.with(imageView.getContext()).load(Uri.parse(url)).into(imageView);
        Picasso.with(imageView.getContext()).load(Uri.parse(url)).into(imageView);
    }

    public String getAccess_num() {
        return access_num;
    }

    public void setAccess_num(String access_num) {
        this.access_num = access_num;
    }

    public String getAlbum_pic_mid() {
        return album_pic_mid;
    }

    public void setAlbum_pic_mid(String album_pic_mid) {
        this.album_pic_mid = album_pic_mid;
    }

    public String getCover_url_big() {
        return cover_url_big;
    }

    public void setCover_url_big(String cover_url_big) {
        this.cover_url_big = cover_url_big;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCount(){
        long count = Long.parseLong(access_num);
        return "播放量:"+ShowLongNumberTools.getShow(count);
    }
}
