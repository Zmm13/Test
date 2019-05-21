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
import com.example.administrator.test.entity.QqNewMusicTop100;
import com.example.administrator.test.event.IsLightChangeEvent;
import com.example.administrator.test.event.MusicChangeEvent;
import com.example.administrator.test.minterfcae.MusicTop100Interface;
import com.example.administrator.test.minterfcae.MyAction;
import com.example.administrator.test.minterfcae.MyFunc;
import com.example.administrator.test.minterfcae.QqNewMusicTop100GetKeyInterf;
import com.example.administrator.test.minterfcae.QqNewMusicTop100Interf;
import com.example.administrator.test.singleton.MediaPlayerUtils;
import com.example.administrator.test.utils.GsonTool;
import com.example.administrator.test.utils.JSONUtils;
import com.example.administrator.test.utils.RetrofitTool;
import com.example.administrator.test.databinding.FragmentMyInternetBinding;
import com.example.administrator.test.utils.StaticBaseInfo;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
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
    private List<QqNewMusicTop100> list;
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
            protected void onItemClick(QqNewMusicTop100 song) {
//                MediaPlayerUtils.getInstance().changeMusic(song);
//                EventBus.getDefault().post(new MusicChangeEvent());
                QqNewMusicTop100GetKeyInterf qqNewMusicTop100GetKeyInterf = RetrofitTool.getInstance().get(QqNewMusicTop100GetKeyInterf.class, StaticBaseInfo.QQ_NEW_MUSIC_TOP_100_BASEURL, true, false);

                if(qqNewMusicTop100GetKeyInterf!=null){
                    qqNewMusicTop100GetKeyInterf.getInfos("getplaysongvkey7687284379381651",
                            "859231619",
                            "1359163679",
                            "0",
                            "json",
                            "utf8",
                            "utf-8",
                            "0",
                            "yqq.json",
                            "0",
                            StaticBaseInfo.QQ_NEW_MUSIC_TOP_100_GET_KEY_DATA.replace(StaticBaseInfo.QQ_NEW_MUSIC_TOP_100_GET_KEY_REPLACE_TAG,song.getAlbumMid()))
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
                                        Toast.makeText(getActivity(),s,Toast.LENGTH_SHORT).show();
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
                        .map(new Function<ResponseBody, List<QqNewMusicTop100>>() {
                            @Override
                            public List<QqNewMusicTop100> apply(ResponseBody responseBody) throws Exception {
                                String result = responseBody.string();
                                if (JSONUtils.isCodeSuccess(result)) {
                                    List<QqNewMusicTop100> list = new ArrayList<>();
                                    JSONArray array = new JSONObject(result).getJSONObject(StaticBaseInfo.SONG_DETAIL).getJSONObject(StaticBaseInfo.SONG_LIST_DATA_NAME).getJSONObject(StaticBaseInfo.SONG_LIST_DATA_NAME).getJSONArray(StaticBaseInfo.SONG_LIST_NAME);
                                    if (array != null && array.length() > 0) {
                                        for (int i = 0; i < array.length(); i++) {
                                            QqNewMusicTop100 song = GsonTool.getInstance().getObject(QqNewMusicTop100.class, array.getJSONObject(i).toString());
                                            song.setImagePath(StaticBaseInfo.QQ_NEW_MUSIC_TOP_100_IMAGE_PATH.replace(StaticBaseInfo.QQ_NEW_MUSIC_TOP_100_IMAGE_REPLCAE_TAG,song.getAlbumMid()));
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
                        .subscribe(new Observer<List<QqNewMusicTop100>>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onNext(List<QqNewMusicTop100> songs) {
                                if (songs != null && songs.size() > 0) {
                                   list.clear();
                                   list.addAll(songs);
                                   adapter.notifyDataSetChanged();
                                    Toast.makeText(getActivity(),songs.size()+"",Toast.LENGTH_SHORT).show();
                                }else {
                                   Toast.makeText(getActivity(),"没有获取到数据",Toast.LENGTH_SHORT).show();
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
        if(adapter!=null){
            adapter.notifyDataSetChanged();
        }
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
}
