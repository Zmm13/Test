package com.example.administrator.test.presenter;

import com.example.administrator.test.entity.QQMusic;
import com.example.administrator.test.event.QQGeCiEvent;
import com.example.administrator.test.minterfcae.QQGeCi;
import com.example.administrator.test.utils.RetrofitTool;
import com.example.administrator.test.utils.StaticBaseInfo;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

/**
 * Create by zmm
 * Time 2019/5/30
 * PackageName com.example.administrator.test.presenter
 */
public class MusicPlayShowPresenter {
    public void getMusicGeCi(String mid) {
        QQGeCi qqGeCi = RetrofitTool.getInstance().get(QQGeCi.class, StaticBaseInfo.OTHER_BASE_URL, true, false);
        if (qqGeCi != null) {
            qqGeCi.getInfos(mid)
                    .map(new Function<ResponseBody, String>() {
                        @Override
                        public String apply(ResponseBody responseBody) throws Exception {
                            String s = responseBody.string();
                            return s;
                        }
                    })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<String>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(String s) {
                            EventBus.getDefault().post(new QQGeCiEvent(s));
                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        }
    }
}
