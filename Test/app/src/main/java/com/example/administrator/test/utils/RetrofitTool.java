package com.example.administrator.test.utils;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Create by zmm
 * Time 2019/5/20
 * PackageName com.example.administrator.test.utils
 */
public class RetrofitTool {

    private static volatile RetrofitTool instance = null;
    private Retrofit retrofit;
    private Retrofit.Builder builder;

    public static RetrofitTool getInstance() {
        if (instance == null) {
            synchronized (RetrofitTool.class) {
                if (instance == null) {
                    instance = new RetrofitTool();
                }
            }
        }
        return instance;
    }

    public RetrofitTool() {
        builder = new Retrofit.Builder();
    }

    public <T> T get(final Class<T> service, String baseUrl, boolean rxjava, boolean gson) {
        if (TextUtil.isEmpty(baseUrl)) {
            return null;
        }
        builder.baseUrl(baseUrl);
//        builder.validateEagerly(true);是否提前将接口里面的方法解析成为ServiceMethod
        if(gson)
        builder.addConverterFactory(GsonConverterFactory.create());

        if (rxjava)
        builder.addCallAdapterFactory(RxJava2CallAdapterFactory.create());
        retrofit = builder.build();
        T t = retrofit.create(service);
        return t;
    }
}
