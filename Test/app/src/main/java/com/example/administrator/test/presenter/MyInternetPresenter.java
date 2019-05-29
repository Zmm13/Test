package com.example.administrator.test.presenter;

import com.example.administrator.test.entity.GeDanInfo;
import com.example.administrator.test.entity.QQMuiscSinger;
import com.example.administrator.test.entity.QQMusic;
import com.example.administrator.test.entity.QQMusicAlbum;
import com.example.administrator.test.entity.QQMusicFocus;
import com.example.administrator.test.entity.QQTopGroup;
import com.example.administrator.test.entity.QQTopListInfo;
import com.example.administrator.test.event.GeDan;
import com.example.administrator.test.event.GeDanInfoEvent;
import com.example.administrator.test.event.QQMusicFocuse10002Event;
import com.example.administrator.test.event.QQMusicShouYeInfoEvent;
import com.example.administrator.test.event.QQMusicGetKeyEvent;
import com.example.administrator.test.event.QQNewMusicEvent;
import com.example.administrator.test.event.ShouYeInfo;
import com.example.administrator.test.minterfcae.QQMusicFocusInterface;
import com.example.administrator.test.minterfcae.QQMusicSelectGeDanByItemId;
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
            qqMusicShouYeInterface.getInfos().map(new Function<ResponseBody, ShouYeInfo>() {
                @Override
                public ShouYeInfo apply(ResponseBody responseBody) throws Exception {
                    String result = responseBody.string();
                    JSONObject object1 = new JSONObject(result);
                    ShouYeInfo shouYeInfo = new ShouYeInfo();
//                    解析歌单信息
                    if(object1.getInt("code") != StaticBaseInfo.CODE_SUCCESS){
                         return null;
                    }
                    if(object1.getJSONObject("category").getInt("code") == StaticBaseInfo.CODE_SUCCESS){
                        JSONArray array1 = object1.getJSONObject("category").getJSONObject("data").getJSONArray("category").getJSONObject(0).getJSONArray("items");
                        if (array1 != null && array1.length() > 0) {
                            List<GeDan> list = new ArrayList<>();
                            for (int i = 0; i < array1.length(); i++) {
                                JSONObject object = array1.getJSONObject(i);
                                list.add(GsonTool.getInstance().getObject(GeDan.class, object.toString()));
                            }
                            shouYeInfo.setGeDans(list);
                        }
                    }
                    //解析精彩推荐
                    if(object1.getJSONObject("focus").getInt("code") == StaticBaseInfo.CODE_SUCCESS) {
                        JSONArray array = object1.getJSONObject("focus").getJSONObject("data").getJSONArray("content");
                        if (array != null && array.length() > 0) {
                            List<QQMusicFocus> list = new ArrayList<>();
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject object11 = array.getJSONObject(i);
                                QQMusicFocus focus = new QQMusicFocus();
                                focus.setId(object11.getString("id"));
                                focus.setUrl(object11.getJSONObject("jump_info").getString("url"));
                                focus.setPic_url(object11.getJSONObject("pic_info").getString("url"));
                                focus.setType(object11.getInt("type"));
                                list.add(focus);
                            }
                            shouYeInfo.setFocusList(list);
                        }
                    }
                    //排行榜
                    if(object1.getJSONObject("toplist").getInt("code") == StaticBaseInfo.CODE_SUCCESS) {
                        JSONArray array2 = object1.getJSONObject("toplist").getJSONObject("data").getJSONArray("group");
                        if (array2 != null && array2.length() > 0) {
                            List<QQTopGroup> list = new ArrayList<>();
                            for (int i = 0; i < array2.length(); i++) {
                                JSONObject object = array2.getJSONObject(i);
                                JSONArray array = object.getJSONArray("toplist") ;
                                QQTopGroup group = GsonTool.getInstance().getObject(QQTopGroup.class,object.toString());
                                if(group != null&&array!=null&&array.length()>0){
                                    List<QQTopListInfo> qqTopListInfos = new ArrayList<>();
                                    for(int j = 0;j<array.length();j++){
                                          QQTopListInfo qqTopListInfo = GsonTool.getInstance().getObject(QQTopListInfo.class,array.getJSONObject(j).toString());
                                    qqTopListInfos.add(qqTopListInfo);
                                    }
                                    group.setQqTopListInfos(qqTopListInfos);
                                }
                                list.add(group);
                            }
                            shouYeInfo.setQqTopGroups(list);
                        }
                    }

                    return shouYeInfo;
                }
            }).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<ShouYeInfo>() {
                @Override
                public void onSubscribe(Disposable d) {

                }

                @Override
                public void onNext(ShouYeInfo shouYeInfo) {
                    EventBus.getDefault().post(new QQMusicShouYeInfoEvent(shouYeInfo));
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



    public void getGeDan(String itemId) {
        QQMusicSelectGeDanByItemId qqMusicSelectGeDanByItemId = RetrofitTool.getInstance().get(QQMusicSelectGeDanByItemId.class, StaticBaseInfo.QQ_NEW_MUSIC_TOP_100_BASEURL, true, false);
        if (qqMusicSelectGeDanByItemId != null) {
            qqMusicSelectGeDanByItemId.getInfos(StaticBaseInfo.QQ_MUSIC_GE_DAN_SELECT_BY_ITEM_ID_DATA.replace(StaticBaseInfo.QQ_MUSIC_GE_DAN_SELECT_BY_ITEM_ID_REPLACE, itemId))
                    .map(new Function<ResponseBody, List<GeDanInfo>>() {
                        @Override
                        public List<GeDanInfo> apply(ResponseBody responseBody) throws Exception {
                            String s = responseBody.string();
                            List<GeDanInfo> list = null;
                            JSONObject object = new JSONObject(s);
                            JSONArray array = object.getJSONObject("playlist").getJSONObject("data").getJSONArray("v_playlist");
                            if(array!=null && array.length()>0){
                                list = new ArrayList<>();
                                for(int i = 0;i<array.length();i++){
                                    list.add(GsonTool.getInstance().getObject(GeDanInfo.class,array.getJSONObject(i).toString()));
                                }
                            }
                            return list;
                        }
                    })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<List<GeDanInfo>>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(List<GeDanInfo> geDanInfos) {
                           EventBus.getDefault().post(new GeDanInfoEvent(geDanInfos));
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
