package com.example.administrator.test.minterfcae;

import com.example.administrator.test.utils.StaticBaseInfo;

import org.greenrobot.greendao.annotation.Entity;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Create by zmm
 * Time 2019/5/20
 * PackageName com.example.administrator.test.minterfcae
 */
public interface QQMusicSelectGeDanByItemId {
    @GET(StaticBaseInfo.QQ_MUSIC_GE_DAN_SELECT_BY_ITEM_ID)
//    Call<ResponseBody> getInfo();
    Observable<ResponseBody> getInfos(@Query("data") String data);
}
