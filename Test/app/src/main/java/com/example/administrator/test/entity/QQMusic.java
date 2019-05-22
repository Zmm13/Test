package com.example.administrator.test.entity;

import android.databinding.BindingAdapter;
import android.net.Uri;
import android.widget.ImageView;

import com.example.administrator.test.utils.StaticBaseInfo;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Create by zmm
 * Time 2019/5/22
 * PackageName com.example.administrator.test.entity
 */
public class QQMusic {
    private String id;
    private String type;
    private String mid;
    private String name;
    private String title;
    private String subtitle;
    private List<QQMuiscSinger> singers;
    private QQMusicAlbum qqMusicAlbum;

    public String getSongImagePath() {
        String result = "";
        if (qqMusicAlbum != null) {
            result = StaticBaseInfo.QQ_NEW_MUSIC_TOP_100_IMAGE_PATH.replace(StaticBaseInfo.QQ_NEW_MUSIC_TOP_100_IMAGE_REPLCAE_TAG, qqMusicAlbum.getMid());
        }
        return result;
    }
    @BindingAdapter({"imagePath"})
    public static void loadimage(ImageView imageView, String url){
//        Glide.with(imageView.getContext()).load(Uri.parse(url)).into(imageView);
        Picasso.with(imageView.getContext()).load(Uri.parse(url)).into(imageView);
    }

    public String getSingerNames() {
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

    public String getId() {
        return id;
    }


    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public List<QQMuiscSinger> getSingers() {
        return singers;
    }

    public void setSingers(List<QQMuiscSinger> singers) {
        this.singers = singers;
    }

    public QQMusicAlbum getQqMusicAlbum() {
        return qqMusicAlbum;
    }

    public void setQqMusicAlbum(QQMusicAlbum qqMusicAlbum) {
        this.qqMusicAlbum = qqMusicAlbum;
    }
}
