package com.example.administrator.test.entity;

import android.databinding.BindingAdapter;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.administrator.test.utils.FrescoUtils;
import com.squareup.picasso.Picasso;

/**
 * Create by zmm
 * Time 2019/5/21
 * PackageName com.example.administrator.test.entity
 */
public class QqNewMusicTop100 {
    private String rank;
    private String rankType;
    private String rankValue;
    private String recType;
    private String songId;
    private String vid;
    private String albumMid;
    private String title;
    private String singerName;
    private String singerMid;

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    @BindingAdapter({"imagePath"})
    public static void loadimage(ImageView imageView, String url){
//        Glide.with(imageView.getContext()).load(Uri.parse(url)).into(imageView);
        Picasso.with(imageView.getContext()).load(Uri.parse(url)).into(imageView);
    }
    private String imagePath;

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getRankType() {
        return rankType;
    }

    public void setRankType(String rankType) {
        this.rankType = rankType;
    }

    public String getRankValue() {
        return rankValue;
    }

    public void setRankValue(String rankValue) {
        this.rankValue = rankValue;
    }

    public String getRecType() {
        return recType;
    }

    public void setRecType(String recType) {
        this.recType = recType;
    }

    public String getSongId() {
        return songId;
    }

    public void setSongId(String songId) {
        this.songId = songId;
    }

    public String getVid() {
        return vid;
    }

    public void setVid(String vid) {
        this.vid = vid;
    }

    public String getAlbumMid() {
        return albumMid;
    }

    public void setAlbumMid(String albumMid) {
        this.albumMid = albumMid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSingerName() {
        return singerName;
    }

    public void setSingerName(String singerName) {
        this.singerName = singerName;
    }

    public String getSingerMid() {
        return singerMid;
    }

    public void setSingerMid(String singerMid) {
        this.singerMid = singerMid;
    }
}
