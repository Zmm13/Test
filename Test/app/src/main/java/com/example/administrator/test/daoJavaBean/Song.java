package com.example.administrator.test.daoJavaBean;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Transient;

/**
 * Create by zmm
 * Time 2019/5/6
 * PackageName com.example.administrator.test
 */

@Entity
public class Song implements Parcelable {
    @Id(autoincrement = true)
    public Long id;
    @Property(nameInDb = "KEY")
    public long key;//歌曲id
    @Property(nameInDb = "NAME")
    public String name;//歌曲名
    @Property(nameInDb = "SINGER")
    public String singer;//歌手
    @Property(nameInDb = "SIZE")
    public long size;//歌曲所占空间大小
    @Property(nameInDb = "DURATION")
    public int duration;//歌曲时间长度
    @Property(nameInDb = "PATH")
    public String path;//歌曲地址
    @Property(nameInDb = "ALBUMID")
    public long albumId;//图片id
    @Transient
    private String imageUrl;

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Song() {

    }


    protected Song(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        key = in.readLong();
        name = in.readString();
        singer = in.readString();
        size = in.readLong();
        duration = in.readInt();
        path = in.readString();
        albumId = in.readLong();
    }


    @Generated(hash = 1799090712)
    public Song(Long id, long key, String name, String singer, long size,
            int duration, String path, long albumId) {
        this.id = id;
        this.key = key;
        this.name = name;
        this.singer = singer;
        this.size = size;
        this.duration = duration;
        this.path = path;
        this.albumId = albumId;
    }

    public static final Creator<Song> CREATOR = new Creator<Song>() {
        @Override
        public Song createFromParcel(Parcel in) {
            return new Song(in);
        }

        @Override
        public Song[] newArray(int size) {
            return new Song[size];
        }
    };

    public long getAlbumId() {
        return albumId;
    }

    public void setAlbumId(long albumId) {
        this.albumId = albumId;
    }

    public long getKey() {
        return key;
    }

    public void setKey(long key) {
        this.key = key;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        if (id == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeLong(id);
        }
        parcel.writeLong(key);
        parcel.writeString(name);
        parcel.writeString(singer);
        parcel.writeLong(size);
        parcel.writeInt(duration);
        parcel.writeString(path);
        parcel.writeLong(albumId);
    }
}
