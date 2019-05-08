package com.example.administrator.test.utils;

import com.example.administrator.test.Bean;
import com.example.administrator.test.minterfcae.RetrofitIntfTest;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.Subject;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

/**
 * Create by zmm
 * Time 2019/4/26
 * PackageName com.example.administrator.test.utils
 */
public class RetrofitUtil {
    public static void post() {
        String baseUrl = "http://182.140.132.172";
//        String baseUrl = "https://www.baidu.com/";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitIntfTest retrofitIntfTest = retrofit.create(RetrofitIntfTest.class);
        //    http://182.140.132.172/HealthRecordCenter/XMLTest.ashx?strPage=APPCommentSelectItemJSON&ItemTypeId=1
        retrofitIntfTest.getInfos("APPCommentSelectItemJSON", "1")
//        retrofitIntfTest.getInfos()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext( ResponseBody responseBody) {
                        try {
                            String result = responseBody.string();
                            System.out.println(result);
                            Observable.just(result.substring(1,result.length()-1))
                                   .map(new Function<String, List<Bean>>() {
                                       @Override
                                       public List<Bean> apply(String s) throws Exception {
                                           return JSONUtils.jsonToArrayObj(s,Bean.class);
                                       }
                                   }).subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new Observer<List<Bean>>() {
                                @Override
                                public void onSubscribe(Disposable d) {

                                }

                                @Override
                                public void onNext(List<Bean> beans) {

                                }

                                @Override
                                public void onError(Throwable e) {

                                }

                                @Override
                                public void onComplete() {

                                }
                            });
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        System.out.println("faild:"+"11111111111111");
                    }

                    @Override
                    public void onComplete() {

                    }
                });
//        call.enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                try {
//                    System.out.println(response.body().string());
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                t.printStackTrace();
//            }
//        });
    }
}
