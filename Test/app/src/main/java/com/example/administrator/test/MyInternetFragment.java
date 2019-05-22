package com.example.administrator.test;


import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.administrator.test.adapter.MusicListAdapter;
import com.example.administrator.test.adapter.QqTop100Adapter;
import com.example.administrator.test.daoJavaBean.Song;
import com.example.administrator.test.entity.QQMuiscSinger;
import com.example.administrator.test.entity.QQMusic;
import com.example.administrator.test.entity.QQMusicAlbum;
import com.example.administrator.test.entity.QqNewMusicTop100;
import com.example.administrator.test.event.EventInternetMusicEnd;
import com.example.administrator.test.event.IsLightChangeEvent;
import com.example.administrator.test.event.MusicChangeEvent;
import com.example.administrator.test.minterfcae.MusicTop100Interface;
import com.example.administrator.test.minterfcae.MyAction;
import com.example.administrator.test.minterfcae.MyFunc;
import com.example.administrator.test.minterfcae.QqNewMusicTop100GetKeyInterf;
import com.example.administrator.test.minterfcae.QqNewMusicTop100Interf;
import com.example.administrator.test.singleton.MediaPlayerUtils;
import com.example.administrator.test.singleton.MusicListTool;
import com.example.administrator.test.utils.GsonTool;
import com.example.administrator.test.utils.JSONUtils;
import com.example.administrator.test.utils.RetrofitTool;
import com.example.administrator.test.databinding.FragmentMyInternetBinding;
import com.example.administrator.test.utils.StaticBaseInfo;
import com.example.administrator.test.utils.TextUtil;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyInternetFragment extends Fragment {
    private View view;
    private boolean isViewInitiated = false;
    private boolean isVisibleToUser = false;
    private boolean isLoadData = false;
    private FragmentMyInternetBinding binding;
    private QqTop100Adapter adapter;
    private List<QQMusic> list;
    private Song playSong = null;
    private final String TAG = "MyInternetFragment";

    public MyInternetFragment() {

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_internet, container, false);
        view = binding.getRoot();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        binding.rv.setLayoutManager(linearLayoutManager);
        list = new ArrayList<>();
        adapter = new QqTop100Adapter(getActivity(), list) {
            @Override
            protected void onItemClick(QQMusic song, int p) {
                selectSong(song);
            }
        };
        binding.rv.setAdapter(adapter);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        isViewInitiated = true;
        prepareFetchData();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        this.isVisibleToUser = isVisibleToUser;
        prepareFetchData();
    }


    public void prepareFetchData() {
        if (isViewInitiated && isVisibleToUser && !isLoadData) {
//          RetrofitIntfTest retrofitIntfTest = RetrofitTool.getInstance().get(RetrofitIntfTest.class, "http://182.140.132.172", false, false);
            QqNewMusicTop100Interf top100Interface = RetrofitTool.getInstance().get(QqNewMusicTop100Interf.class, StaticBaseInfo.QQ_NEW_MUSIC_TOP_100_BASEURL, true, false);
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
                                    list.clear();
                                    list.addAll(songs);
                                    adapter.notifyDataSetChanged();
                                    Toast.makeText(getActivity(), songs.size() + "", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getActivity(), "没有获取到数据", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                            }

                            @Override
                            public void onComplete() {
                            }
                        });
            isLoadData = true;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(IsLightChangeEvent event) {
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(EventInternetMusicEnd event) {
        QQMusic qqMusic = null;
        if (list != null && list.size() > 0) {
            if (playSong == null) {
                qqMusic = list.get(0);
            } else {
                for (int i = 0; i < list.size(); i++) {
                    if (playSong.getPath().contains(list.get(i).getMid())) {
                        qqMusic = list.get(i+1 < list.size() ? i+1 : 0);
                        break;
                    }
                }
            }
        }
        if (qqMusic != null)
            selectSong(qqMusic);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(MusicChangeEvent event) {
        //处理逻辑
            if (list != null && list.size() > 0) {
                boolean a = false;
                boolean b = false;
                for (int i = 0; i < list.size(); i++) {
                    if (playSong != null) {
                        if (!a && playSong.getPath().contains(list.get(i).getMid())) {
                            adapter.notifyItemChanged(i);
                            a = true;
                        }
                    }
                    if (!b && MusicListTool.getInstance().getPlaySong().getPath().contains(list.get(i).getMid())) {
                        adapter.notifyItemChanged(i);
                        b = true;
                    }
                    if (a && b) {
                        break;
                    }
                }
            }
            playSong = MusicListTool.getInstance().getPlaySong();
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            EventBus.getDefault().register(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void selectSong(QQMusic song) {
        //                MediaPlayerUtils.getInstance().changeMusic(song);
//                EventBus.getDefault().post(new MusicChangeEvent());
        QqNewMusicTop100GetKeyInterf qqNewMusicTop100GetKeyInterf = RetrofitTool.getInstance().get(QqNewMusicTop100GetKeyInterf.class, StaticBaseInfo.QQ_NEW_MUSIC_TOP_100_BASEURL, true, false);
//    https://u.y.qq.com/cgi-bin/musicu.fcg?-=getplaysongvkey6054735472468298
// &g_tk=1019472139
// &loginUin=1359163679
// &hostUin=0
// &format=json
// &inCharset=utf8
// &outCharset=utf-8
// &notice=0
// &platform=yqq.json
// &needNewCode=0
// &data=%7B%22req_0%22%3A%7B%22module%22%3A%22vkey.GetVkeyServer%22%2C%22method%22%3A%22CgiGetVkey%22%2C%22param%22%3A%7B%22guid%22%3A%229733784294%22%2C%22songmid%22%3A%5B%22001LWYyL4NM5Is%22%5D%2C%22songtype%22%3A%5B0%5D%2C%22uin%22%3A%221359163679%22%2C%22loginflag%22%3A1%2C%22platform%22%3A%2220%22%7D%7D%2C%22comm%22%3A%7B%22uin%22%3A1359163679%2C%22format%22%3A%22json%22%2C%22ct%22%3A24%2C%22cv%22%3A0%7D%7D
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
//                            song.getAlbumMid()
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
                            if(TextUtil.isEmpty(s)){
                                Toast.makeText(getActivity(), "资源有误，无法播放！", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            String path = StaticBaseInfo.QQ_NEW_MUSIC_TOP_100_PLAY + s;
                            Song song1 = new Song();
                            song1.setPath(path);
                            song1.setSinger(song.getSingerNames());
                            song1.setName(song.getTitle());
                            song1.setImageUrl(song.getSongImagePath());
                            MediaPlayerUtils.getInstance().changeMusic(song1);
//                                    Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
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
