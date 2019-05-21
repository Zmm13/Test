package com.example.administrator.test.minterfcae;

import com.example.administrator.test.utils.StaticBaseInfo;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Create by zmm
 * Time 2019/5/21
 * PackageName com.example.administrator.test.minterfcae
 */
public interface QqNewMusicTop100GetKeyInterf {
    @GET(StaticBaseInfo.QQ_NEW_MUSIC_TOP_100_GET_KEY_ADD)
    Observable<ResponseBody> getInfos(@Query("-") String s,
                                      @Query("g_tk") String g_tk,
                                      @Query("loginUin") String loginUin,
                                      @Query("hostUin") String hostUin,
                                      @Query("format") String format,
                                      @Query("inCharset") String inCharset,
                                      @Query("outCharset") String  outCharset,
                                      @Query("notice") String  notice,
                                      @Query("platform") String  platform,
                                      @Query("needNewCode") String  needNewCode,
                                      @Query("data") String  data
                                      );
}
