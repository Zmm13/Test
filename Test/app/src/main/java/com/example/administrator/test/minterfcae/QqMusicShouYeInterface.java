package com.example.administrator.test.minterfcae;

import com.example.administrator.test.utils.StaticBaseInfo;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;

/**
 * Create by zmm
 * Time 2019/5/23
 * PackageName com.example.administrator.test.minterfcae
 */
public interface QqMusicShouYeInterface {

    @GET(StaticBaseInfo.QQ_MUSIC_SHOUYE_ADD_URL)
    Observable<ResponseBody> getInfos();

}
