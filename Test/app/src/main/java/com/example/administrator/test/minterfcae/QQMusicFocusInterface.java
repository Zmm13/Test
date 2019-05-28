package com.example.administrator.test.minterfcae;

import com.example.administrator.test.utils.StaticBaseInfo;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Create by zmm
 * Time 2019/5/28
 * PackageName com.example.administrator.test.minterfcae
 */
public  interface QQMusicFocusInterface {
    @GET(StaticBaseInfo.QQ_MUSIC_FOCUS_ADD)
    Observable<ResponseBody> getInfos(@Query("albummid") String id);
}
