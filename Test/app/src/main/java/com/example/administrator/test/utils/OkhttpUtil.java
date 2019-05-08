package com.example.administrator.test.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.example.administrator.test.minterfcae.NetCallBack;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Create by zmm
 * Time 2019/4/26
 * PackageName com.example.administrator.test.utils
 */
public class OkhttpUtil {
    /**
     * okhttpclient单例
     */
    private static OkHttpClient okHttpClient = null;

    private static Handler mainHandler = null;

    /**
     * 获取okhttpclient单例
     *
     * @return
     */
    private static OkHttpClient getOkHttpClient() {
        if (okHttpClient == null) {
            synchronized (OkhttpUtil.class) {
                okHttpClient = new OkHttpClient.Builder()
                        .connectTimeout(10, TimeUnit.SECONDS)//链接超时时间
                        .writeTimeout(10, TimeUnit.SECONDS)//写入超时时间
                        .readTimeout(10, TimeUnit.SECONDS).build();//读取超时时间
                mainHandler = new Handler(Looper.getMainLooper());
            }
        }
        return okHttpClient;
    }

    /**
     * get异步请求
     */
    public static void _getAsyn(Context context,String url, NetCallBack netCallBack) {
        if(!NetUtils.isConnected(context)){
            netCallBack.onNetworkError();
            return;
        }
        Request request = new Request.Builder()
                .url(url)
                .build();
        handleRequest(request,netCallBack);
    }

    /**
     * post异步
     */
    public static void _postAsyn() {

    }

    /**
     * 处理请求
     *
     * @param request
     * @param netCallBack
     */
    private static void handleRequest(final Request request, final NetCallBack netCallBack) {
        getOkHttpClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
               netCallBack.onError(call.request(),e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    final String string = response.body().string();
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            netCallBack.onResponse(netCallBack.callbackInfo.setResultString(string));
                        }
                    });
                } catch (Exception e) {

                }
            }
        });
    }
}
