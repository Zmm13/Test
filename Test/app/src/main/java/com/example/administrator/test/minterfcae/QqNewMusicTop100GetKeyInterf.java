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
//    @GET(StaticBaseInfo.QQ_NEW_MUSIC_TOP_100_GET_KEY_ADD)
//    https://u.y.qq.com/cgi-bin/musicu.fcg?-=getplaysongvkey6054735472468298&g_tk=1019472139&loginUin=1359163679&hostUin=0&format=json&inCharset=utf8&outCharset=utf-8&notice=0&platform=yqq.json&needNewCode=0&data=%7B%22req_0%22%3A%7B%22module%22%3A%22vkey.GetVkeyServer%22%2C%22method%22%3A%22CgiGetVkey%22%2C%22param%22%3A%7B%22guid%22%3A%229733784294%22%2C%22songmid%22%3A%5B%22001LWYyL4NM5Is%22%5D%2C%22songtype%22%3A%5B0%5D%2C%22uin%22%3A%221359163679%22%2C%22loginflag%22%3A1%2C%22platform%22%3A%2220%22%7D%7D%2C%22comm%22%3A%7B%22uin%22%3A1359163679%2C%22format%22%3A%22json%22%2C%22ct%22%3A24%2C%22cv%22%3A0%7D%7D
    @GET("cgi-bin/musicu.fcg?getplaysongvkey")
    Observable<ResponseBody> getInfos(
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
