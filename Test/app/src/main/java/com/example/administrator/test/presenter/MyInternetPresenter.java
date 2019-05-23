package com.example.administrator.test.presenter;

import com.example.administrator.test.entity.QQMuiscSinger;
import com.example.administrator.test.entity.QQMusic;
import com.example.administrator.test.entity.QQMusicAlbum;
import com.example.administrator.test.entity.QQMusicFocus;
import com.example.administrator.test.event.QQMusicFocusEvent;
import com.example.administrator.test.event.QQMusicGetKeyEvent;
import com.example.administrator.test.event.QQNewMusicEvent;
import com.example.administrator.test.minterfcae.QqCoolMusicInterf;
import com.example.administrator.test.minterfcae.QqHotMusicInterf;
import com.example.administrator.test.minterfcae.QqMusicShouYeInterface;
import com.example.administrator.test.minterfcae.QqNeiDiMusicInterf;
import com.example.administrator.test.minterfcae.QqNewMusicTop100GetKeyInterf;
import com.example.administrator.test.minterfcae.QqNewMusicInterf;
import com.example.administrator.test.minterfcae.QqOuMeiMusicInterf;
import com.example.administrator.test.minterfcae.QqRiBenMusicInterf;
import com.example.administrator.test.utils.GsonTool;
import com.example.administrator.test.utils.JSONUtils;
import com.example.administrator.test.utils.RetrofitTool;
import com.example.administrator.test.utils.StaticBaseInfo;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

/**
 * Create by zmm
 * Time 2019/5/23
 * PackageName com.example.administrator.test.presenter
 */
public class MyInternetPresenter {

    public void getShouYeInfo() {
        QqMusicShouYeInterface qqMusicShouYeInterface = RetrofitTool.getInstance().get(QqMusicShouYeInterface.class, StaticBaseInfo.QQ_NEW_MUSIC_TOP_100_BASEURL, true, false);
        if (qqMusicShouYeInterface != null) {
            qqMusicShouYeInterface.getInfos().map(new Function<ResponseBody, List<QQMusicFocus>>() {
                @Override
                public List<QQMusicFocus> apply(ResponseBody responseBody) throws Exception {
                    String result = responseBody.string();
                    JSONObject object1 = new JSONObject(result);
                    JSONArray array = object1.getJSONObject("focus").getJSONObject("data").getJSONArray("content");
                    if(array!=null && array.length()>0){
                        List<QQMusicFocus> list = new ArrayList<>();
                        for(int i = 0;i<array.length();i++){
                            JSONObject object11 = array.getJSONObject(i);
                            QQMusicFocus focus = new QQMusicFocus();
                            focus.setId(object11.getString("id"));
                            focus.setUrl(object11.getJSONObject("jump_info").getString("url"));
                            focus.setPic_url(object11.getJSONObject("pic_info").getString("url"));
                            list.add(focus);
                        }
                        return list;
                    }

                    return null;
                }
            }).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<List<QQMusicFocus>>() {
                @Override
                public void onSubscribe(Disposable d) {

                }

                @Override
                public void onNext(List<QQMusicFocus> qqMusicFoci) {
                      if(qqMusicFoci!=null&&qqMusicFoci.size()>0){
                          EventBus.getDefault().post(new QQMusicFocusEvent(qqMusicFoci));
                      }
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

    public void getNewMusics(){
        QqNewMusicInterf top100Interface = RetrofitTool.getInstance().get(QqNewMusicInterf.class, StaticBaseInfo.QQ_NEW_MUSIC_TOP_100_BASEURL, true, false);
        if (top100Interface != null)
            top100Interface.getInfos()
                    .map(new Function<ResponseBody, List<QQMusic>>() {
                        @Override
                        public List<QQMusic> apply(ResponseBody responseBody) throws Exception {
                            String result = responseBody.string();
                            if (JSONUtils.isCodeSuccess(result)) {
                                List<QQMusic> list = new ArrayList<>();
                                JSONArray array = new JSONObject(result).getJSONObject(StaticBaseInfo.SONG_DETAIL).getJSONObject(StaticBaseInfo.SONG_LIST_DATA_NAME).getJSONArray(StaticBaseInfo.SONG_INFO_LIST);
                                if (array != null && array.length() > 0) {
                                    for (int i = 0; i < array.length(); i++) {
                                        QQMusic song = GsonTool.getInstance().getObject(QQMusic.class, array.getJSONObject(i).toString());
                                        JSONArray array1 = array.getJSONObject(i).getJSONArray("singer");
                                        if (array1 != null && array1.length() > 0) {
                                            List<QQMuiscSinger> list1 = new ArrayList<>();
                                            for (int j = 0; j < array1.length(); j++) {
                                                QQMuiscSinger singer = GsonTool.getInstance().getObject(QQMuiscSinger.class, array1.getJSONObject(j).toString());
                                                list1.add(singer);
                                            }
                                            song.setSingers(list1);
                                        }
                                        QQMusicAlbum album = GsonTool.getInstance().getObject(QQMusicAlbum.class, array.getJSONObject(i).getJSONObject("album").toString());
                                        song.setQqMusicAlbum(album);
                                        list.add(song);
                                    }
                                    return list;
                                }
                            }
                            return null;
                        }
                    })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<List<QQMusic>>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(List<QQMusic> songs) {
                            if (songs != null && songs.size() > 0) {
                                EventBus.getDefault().post(new QQNewMusicEvent(songs));
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                        }

                        @Override
                        public void onComplete() {
                        }
                    });
    }

    public void getNeiDiMusics(){
        QqNeiDiMusicInterf top100Interface = RetrofitTool.getInstance().get(QqNeiDiMusicInterf.class, StaticBaseInfo.QQ_NEW_MUSIC_TOP_100_BASEURL, true, false);
        if (top100Interface != null)
            top100Interface.getInfos()
                    .map(new Function<ResponseBody, List<QQMusic>>() {
                        @Override
                        public List<QQMusic> apply(ResponseBody responseBody) throws Exception {
                            String result = responseBody.string();
                            if (JSONUtils.isCodeSuccess(result)) {
                                List<QQMusic> list = new ArrayList<>();
                                JSONArray array = new JSONObject(result).getJSONObject(StaticBaseInfo.SONG_DETAIL).getJSONObject(StaticBaseInfo.SONG_LIST_DATA_NAME).getJSONArray(StaticBaseInfo.SONG_INFO_LIST);
                                if (array != null && array.length() > 0) {
                                    for (int i = 0; i < array.length(); i++) {
                                        QQMusic song = GsonTool.getInstance().getObject(QQMusic.class, array.getJSONObject(i).toString());
                                        JSONArray array1 = array.getJSONObject(i).getJSONArray("singer");
                                        if (array1 != null && array1.length() > 0) {
                                            List<QQMuiscSinger> list1 = new ArrayList<>();
                                            for (int j = 0; j < array1.length(); j++) {
                                                QQMuiscSinger singer = GsonTool.getInstance().getObject(QQMuiscSinger.class, array1.getJSONObject(j).toString());
                                                list1.add(singer);
                                            }
                                            song.setSingers(list1);
                                        }
                                        QQMusicAlbum album = GsonTool.getInstance().getObject(QQMusicAlbum.class, array.getJSONObject(i).getJSONObject("album").toString());
                                        song.setQqMusicAlbum(album);
                                        list.add(song);
                                    }
                                    return list;
                                }
                            }
                            return null;
                        }
                    })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<List<QQMusic>>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(List<QQMusic> songs) {
                            if (songs != null && songs.size() > 0) {
                                EventBus.getDefault().post(new QQNewMusicEvent(songs));
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                        }

                        @Override
                        public void onComplete() {
                        }
                    });
    }
    public void getOuMeiMusics(){
        QqOuMeiMusicInterf top100Interface = RetrofitTool.getInstance().get(QqOuMeiMusicInterf.class, StaticBaseInfo.QQ_NEW_MUSIC_TOP_100_BASEURL, true, false);
        if (top100Interface != null)
            top100Interface.getInfos()
                    .map(new Function<ResponseBody, List<QQMusic>>() {
                        @Override
                        public List<QQMusic> apply(ResponseBody responseBody) throws Exception {
                            String result = responseBody.string();
                            if (JSONUtils.isCodeSuccess(result)) {
                                List<QQMusic> list = new ArrayList<>();
                                JSONArray array = new JSONObject(result).getJSONObject(StaticBaseInfo.SONG_DETAIL).getJSONObject(StaticBaseInfo.SONG_LIST_DATA_NAME).getJSONArray(StaticBaseInfo.SONG_INFO_LIST);
                                if (array != null && array.length() > 0) {
                                    for (int i = 0; i < array.length(); i++) {
                                        QQMusic song = GsonTool.getInstance().getObject(QQMusic.class, array.getJSONObject(i).toString());
                                        JSONArray array1 = array.getJSONObject(i).getJSONArray("singer");
                                        if (array1 != null && array1.length() > 0) {
                                            List<QQMuiscSinger> list1 = new ArrayList<>();
                                            for (int j = 0; j < array1.length(); j++) {
                                                QQMuiscSinger singer = GsonTool.getInstance().getObject(QQMuiscSinger.class, array1.getJSONObject(j).toString());
                                                list1.add(singer);
                                            }
                                            song.setSingers(list1);
                                        }
                                        QQMusicAlbum album = GsonTool.getInstance().getObject(QQMusicAlbum.class, array.getJSONObject(i).getJSONObject("album").toString());
                                        song.setQqMusicAlbum(album);
                                        list.add(song);
                                    }
                                    return list;
                                }
                            }
                            return null;
                        }
                    })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<List<QQMusic>>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(List<QQMusic> songs) {
                            if (songs != null && songs.size() > 0) {
                                EventBus.getDefault().post(new QQNewMusicEvent(songs));
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                        }

                        @Override
                        public void onComplete() {
                        }
                    });
    }

    public void getRiBenMusics(){
        QqRiBenMusicInterf top100Interface = RetrofitTool.getInstance().get(QqRiBenMusicInterf.class, StaticBaseInfo.QQ_NEW_MUSIC_TOP_100_BASEURL, true, false);
        if (top100Interface != null)
            top100Interface.getInfos()
                    .map(new Function<ResponseBody, List<QQMusic>>() {
                        @Override
                        public List<QQMusic> apply(ResponseBody responseBody) throws Exception {
                            String result = responseBody.string();
                            if (JSONUtils.isCodeSuccess(result)) {
                                List<QQMusic> list = new ArrayList<>();
                                JSONArray array = new JSONObject(result).getJSONObject(StaticBaseInfo.SONG_DETAIL).getJSONObject(StaticBaseInfo.SONG_LIST_DATA_NAME).getJSONArray(StaticBaseInfo.SONG_INFO_LIST);
                                if (array != null && array.length() > 0) {
                                    for (int i = 0; i < array.length(); i++) {
                                        QQMusic song = GsonTool.getInstance().getObject(QQMusic.class, array.getJSONObject(i).toString());
                                        JSONArray array1 = array.getJSONObject(i).getJSONArray("singer");
                                        if (array1 != null && array1.length() > 0) {
                                            List<QQMuiscSinger> list1 = new ArrayList<>();
                                            for (int j = 0; j < array1.length(); j++) {
                                                QQMuiscSinger singer = GsonTool.getInstance().getObject(QQMuiscSinger.class, array1.getJSONObject(j).toString());
                                                list1.add(singer);
                                            }
                                            song.setSingers(list1);
                                        }
                                        QQMusicAlbum album = GsonTool.getInstance().getObject(QQMusicAlbum.class, array.getJSONObject(i).getJSONObject("album").toString());
                                        song.setQqMusicAlbum(album);
                                        list.add(song);
                                    }
                                    return list;
                                }
                            }
                            return null;
                        }
                    })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<List<QQMusic>>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(List<QQMusic> songs) {
                            if (songs != null && songs.size() > 0) {
                                EventBus.getDefault().post(new QQNewMusicEvent(songs));
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                        }

                        @Override
                        public void onComplete() {
                        }
                    });
    }

    public void getHotMusics(){
        QqHotMusicInterf top100Interface = RetrofitTool.getInstance().get(QqHotMusicInterf.class, StaticBaseInfo.QQ_NEW_MUSIC_TOP_100_BASEURL, true, false);
        if (top100Interface != null)
            top100Interface.getInfos()
                    .map(new Function<ResponseBody, List<QQMusic>>() {
                        @Override
                        public List<QQMusic> apply(ResponseBody responseBody) throws Exception {
                            String result = responseBody.string();
                            if (JSONUtils.isCodeSuccess(result)) {
                                List<QQMusic> list = new ArrayList<>();
                                JSONArray array = new JSONObject(result).getJSONObject(StaticBaseInfo.SONG_DETAIL).getJSONObject(StaticBaseInfo.SONG_LIST_DATA_NAME).getJSONArray(StaticBaseInfo.SONG_INFO_LIST);
                                if (array != null && array.length() > 0) {
                                    for (int i = 0; i < array.length(); i++) {
                                        QQMusic song = GsonTool.getInstance().getObject(QQMusic.class, array.getJSONObject(i).toString());
                                        JSONArray array1 = array.getJSONObject(i).getJSONArray("singer");
                                        if (array1 != null && array1.length() > 0) {
                                            List<QQMuiscSinger> list1 = new ArrayList<>();
                                            for (int j = 0; j < array1.length(); j++) {
                                                QQMuiscSinger singer = GsonTool.getInstance().getObject(QQMuiscSinger.class, array1.getJSONObject(j).toString());
                                                list1.add(singer);
                                            }
                                            song.setSingers(list1);
                                        }
                                        QQMusicAlbum album = GsonTool.getInstance().getObject(QQMusicAlbum.class, array.getJSONObject(i).getJSONObject("album").toString());
                                        song.setQqMusicAlbum(album);
                                        list.add(song);
                                    }
                                    return list;
                                }
                            }
                            return null;
                        }
                    })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<List<QQMusic>>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(List<QQMusic> songs) {
                            if (songs != null && songs.size() > 0) {
                                EventBus.getDefault().post(new QQNewMusicEvent(songs));
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                        }

                        @Override
                        public void onComplete() {
                        }
                    });
    }

    public void getCoolMusics(){
        QqCoolMusicInterf top100Interface = RetrofitTool.getInstance().get(QqCoolMusicInterf.class, StaticBaseInfo.QQ_NEW_MUSIC_TOP_100_BASEURL, true, false);
        if (top100Interface != null)
            top100Interface.getInfos()
                    .map(new Function<ResponseBody, List<QQMusic>>() {
                        @Override
                        public List<QQMusic> apply(ResponseBody responseBody) throws Exception {
                            String result = responseBody.string();
                            if (JSONUtils.isCodeSuccess(result)) {
                                List<QQMusic> list = new ArrayList<>();
                                JSONArray array = new JSONObject(result).getJSONObject(StaticBaseInfo.SONG_DETAIL).getJSONObject(StaticBaseInfo.SONG_LIST_DATA_NAME).getJSONArray(StaticBaseInfo.SONG_INFO_LIST);
                                if (array != null && array.length() > 0) {
                                    for (int i = 0; i < array.length(); i++) {
                                        QQMusic song = GsonTool.getInstance().getObject(QQMusic.class, array.getJSONObject(i).toString());
                                        JSONArray array1 = array.getJSONObject(i).getJSONArray("singer");
                                        if (array1 != null && array1.length() > 0) {
                                            List<QQMuiscSinger> list1 = new ArrayList<>();
                                            for (int j = 0; j < array1.length(); j++) {
                                                QQMuiscSinger singer = GsonTool.getInstance().getObject(QQMuiscSinger.class, array1.getJSONObject(j).toString());
                                                list1.add(singer);
                                            }
                                            song.setSingers(list1);
                                        }
                                        QQMusicAlbum album = GsonTool.getInstance().getObject(QQMusicAlbum.class, array.getJSONObject(i).getJSONObject("album").toString());
                                        song.setQqMusicAlbum(album);
                                        list.add(song);
                                    }
                                    return list;
                                }
                            }
                            return null;
                        }
                    })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<List<QQMusic>>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(List<QQMusic> songs) {
                            if (songs != null && songs.size() > 0) {
                                EventBus.getDefault().post(new QQNewMusicEvent(songs));
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                        }

                        @Override
                        public void onComplete() {
                        }
                    });
    }



    public void getMusicInfo(QQMusic song){
        QqNewMusicTop100GetKeyInterf qqNewMusicTop100GetKeyInterf = RetrofitTool.getInstance().get(QqNewMusicTop100GetKeyInterf.class, StaticBaseInfo.QQ_NEW_MUSIC_TOP_100_BASEURL, true, false);
        if (qqNewMusicTop100GetKeyInterf != null) {
            qqNewMusicTop100GetKeyInterf.getInfos(
                    "1019472139",
                    "1359163679",
                    "0",
                    "json",
                    "utf8",
                    "utf-8",
                    "0",
                    "yqq.json",
                    "0",
                    StaticBaseInfo.QQ_NEW_MUSIC_TOP_100_GET_KEY_DATA.replace(StaticBaseInfo.QQ_NEW_MUSIC_TOP_100_GET_KEY_REPLACE_TAG, song.getMid())
            )
                    .map(new Function<ResponseBody, String>() {
                        @Override
                        public String apply(ResponseBody responseBody) throws Exception {
                            String s = responseBody.string();
                            String key = new JSONObject(s).getJSONObject("req_0").getJSONObject("data").getJSONArray("midurlinfo").getJSONObject(0).getString("purl");
                            return key;
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
                           EventBus.getDefault().post(new QQMusicGetKeyEvent(s,song));
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
