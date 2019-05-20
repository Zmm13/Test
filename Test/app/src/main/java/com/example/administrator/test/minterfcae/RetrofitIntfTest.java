package com.example.administrator.test.minterfcae;

import com.example.administrator.test.Bean;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Create by zmm
 * Time 2019/4/26
 * PackageName com.example.administrator.test.minterfcae
 */
public interface RetrofitIntfTest {
    //        @GET("ads")
//    Call<ResponseBody> getAds();
    @FormUrlEncoded               //别丢了
    @POST("/HealthRecordCenter/XMLTest.ashx")
    //接口字段
    Call<ResponseBody> getInfo(@Field("strPage") String strPage, @Field("ItemTypeId") String ItemTypeId);

    Observable<ResponseBody> getInfos(@Field("strPage") String strPage, @Field("ItemTypeId") String ItemTypeId);
//    Observable<ResponseBody> getInfos();
}
