package com.example.administrator.test.presenter;

import com.example.administrator.test.entity.QQMuiscSinger;
import com.example.administrator.test.entity.QQMusic;
import com.example.administrator.test.entity.QQMusicAlbum;
import com.example.administrator.test.event.QQGeCiEvent;
import com.example.administrator.test.event.QQMusicGetKeyEvent;
import com.example.administrator.test.event.QQNewMusicEvent;
import com.example.administrator.test.minterfcae.QQGeCi;
import com.example.administrator.test.minterfcae.QQMusicFocusInterface;
import com.example.administrator.test.minterfcae.QQMusicUrlInterface;
import com.example.administrator.test.minterfcae.QqCoolMusicInterf;
import com.example.administrator.test.minterfcae.QqMusicListInterf;
import com.example.administrator.test.minterfcae.QqNewMusicTop100GetKeyInterf;
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
 * Time 2019/5/27
 * PackageName com.example.administrator.test.presenter
 */
public class InternetMusicPresenter  {

    public void getMusicInfo(QQMusic song) {
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
                            EventBus.getDefault().post(new QQMusicGetKeyEvent(s, song));
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

    public void getMusicUrl(QQMusic song) {
        QQMusicUrlInterface qqNewMusicTop100GetKeyInterf = RetrofitTool.getInstance().get(QQMusicUrlInterface.class, StaticBaseInfo.OTHER_BASE_URL, true, false);
        if (qqNewMusicTop100GetKeyInterf != null) {
            qqNewMusicTop100GetKeyInterf.getInfos(song.getMid(),"flac","1")
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
                            EventBus.getDefault().post(new QQMusicGetKeyEvent(s, song));
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

    public void getCoolMusics() {
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

    public void getMusicsList(String id,String period) {
        QqMusicListInterf top100Interface = RetrofitTool.getInstance().get(QqMusicListInterf.class, StaticBaseInfo.QQ_NEW_MUSIC_TOP_100_BASEURL, true, false);
        if (top100Interface != null)
            top100Interface.getInfos(StaticBaseInfo.QQ_NEW_MUSIC_DATA.replace(StaticBaseInfo.QQ_NEW_MUSIC_DATA_ID_REPLACE,id).replace(StaticBaseInfo.QQ_NEW_MUSIC_DATA_PERIOD_REPLACE,period))
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

    public void getMusicsList(String id) {
        QQMusicFocusInterface qqMusicFocusInterface = RetrofitTool.getInstance().get(QQMusicFocusInterface.class, StaticBaseInfo.QQ_MUSIC_FOCUS_BASE_URL, true, false);
        if (qqMusicFocusInterface != null)
            qqMusicFocusInterface.getInfos(id)
                    .map(new Function<ResponseBody, List<QQMusic>>() {
                        @Override
                        public List<QQMusic> apply(ResponseBody responseBody) throws Exception {
                            String result = responseBody.string();
                            if (JSONUtils.isCodeSuccess(result)) {
                                List<QQMusic> list = new ArrayList<>();
                                JSONArray array = new JSONObject(result).getJSONObject("data").getJSONArray("list");
                                if (array != null && array.length() > 0) {
                                    for (int i = 0; i < array.length(); i++) {
                                        JSONObject object = array.getJSONObject(i);
                                        QQMusic song =new QQMusic();
                                        song.setId(object.getString("songid"));
                                        song.setMid(object.getString("songmid"));
                                        song.setName(object.getString("songname"));
                                        song.setTitle(object.getString("songname"));
                                        song.setSubtitle(object.getString("songorig"));
                                        song.setType(object.getString("type"));

                                        JSONArray array1 = array.getJSONObject(i).getJSONArray("singer");
                                        if (array1 != null && array1.length() > 0) {
                                            List<QQMuiscSinger> list1 = new ArrayList<>();
                                            for (int j = 0; j < array1.length(); j++) {
                                                QQMuiscSinger singer = GsonTool.getInstance().getObject(QQMuiscSinger.class, array1.getJSONObject(j).toString());
                                                list1.add(singer);
                                            }
                                            song.setSingers(list1);
                                        }
                                        QQMusicAlbum album = new QQMusicAlbum();
                                        album.setId(object.getString("albumid"));
                                        album.setMid(object.getString("albummid"));
                                        album.setName(object.getString("albumname"));
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

}
