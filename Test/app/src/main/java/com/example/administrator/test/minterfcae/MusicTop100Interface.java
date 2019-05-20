package com.example.administrator.test.minterfcae;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Create by zmm
 * Time 2019/5/20
 * PackageName com.example.administrator.test.minterfcae
 */
public interface MusicTop100Interface {
    @GET("v8/fcg-bin/fcg_v8_toplist_cp.fcg?g_tk=5381&uin=0&format=json&inCharset=utf-8&outCharset=utf-8%C2%ACice=0&platform=h5&needNewCode=1&tpl=3&page=detail&type=top&topid=27&_=1519963122923")
    Call<ResponseBody> getInfo();
}
