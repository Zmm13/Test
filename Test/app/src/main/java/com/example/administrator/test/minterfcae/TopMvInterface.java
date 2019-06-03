package com.example.administrator.test.minterfcae;

import com.example.administrator.test.utils.StaticBaseInfo;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;

/**
 * Create by zmm
 * Time 2019/5/20
 * PackageName com.example.administrator.test.minterfcae
 */
public interface TopMvInterface {
    @GET(StaticBaseInfo.QQ_MUSIC_TOP_MV_ADD)
//    Call<ResponseBody> getInfo();
    Observable<ResponseBody> getInfos();
}
