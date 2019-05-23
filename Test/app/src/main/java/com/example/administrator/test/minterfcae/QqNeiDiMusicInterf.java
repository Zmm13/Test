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
public interface QqNeiDiMusicInterf {
    @GET(StaticBaseInfo.QQ_NEW_MUSIC_NEIDI)
    Observable<ResponseBody> getInfos();
}
