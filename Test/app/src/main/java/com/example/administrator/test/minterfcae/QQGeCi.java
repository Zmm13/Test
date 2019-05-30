package com.example.administrator.test.minterfcae;

import com.example.administrator.test.utils.StaticBaseInfo;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

/**
 * Create by zmm
 * Time 2019/5/30
 * PackageName com.example.administrator.test.minterfcae
 */
public interface QQGeCi {
    @Headers({ "Content-Type: application/x-www-form-urlencoded"})
    @GET(StaticBaseInfo.OTHER_TENCENT_GET_GE_CI)
    Observable<ResponseBody> getInfos(@Query("id") String id);
}
