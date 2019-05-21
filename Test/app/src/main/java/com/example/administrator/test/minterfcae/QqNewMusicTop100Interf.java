package com.example.administrator.test.minterfcae;

import com.example.administrator.test.utils.StaticBaseInfo;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;

/**
 * Create by zmm
 * Time 2019/5/21
 * PackageName com.example.administrator.test.minterfcae
 */
public interface QqNewMusicTop100Interf {
    @GET(StaticBaseInfo.QQ_NEW_MUSIC_TOP_100_GET_ADD)
    Observable<ResponseBody> getInfos();
}
