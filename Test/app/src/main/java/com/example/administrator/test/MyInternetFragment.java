package com.example.administrator.test;


import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.administrator.test.adapter.MusicListAdapter;
import com.example.administrator.test.daoJavaBean.Song;
import com.example.administrator.test.event.MusicChangeEvent;
import com.example.administrator.test.minterfcae.MusicTop100Interface;
import com.example.administrator.test.minterfcae.MyAction;
import com.example.administrator.test.minterfcae.MyFunc;
import com.example.administrator.test.singleton.MediaPlayerUtils;
import com.example.administrator.test.utils.RetrofitTool;
import com.example.administrator.test.databinding.FragmentMyInternetBinding;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
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
    private MusicListAdapter adapter;
    private List<Song> list;
    private Song playSong = null;

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
        adapter = new MusicListAdapter(getActivity(), list) {
            @Override
            protected void onItemClick(Song song) {
                MediaPlayerUtils.getInstance().changeMusic(song);
                EventBus.getDefault().post(new MusicChangeEvent());
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
//
//            RetrofitIntfTest retrofitIntfTest = RetrofitTool.getInstance().get(RetrofitIntfTest.class, "http://182.140.132.172", false, false);
            MusicTop100Interface top100Interface = RetrofitTool.getInstance().get(MusicTop100Interface.class, "https://c.y.qq.com/", false, false);
            if (top100Interface != null) {
                Call<ResponseBody> call = top100Interface.getInfo();
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        try {
                            String result = response.body().string();
                            Observable.just(result)
                                    .map(new MyFunc<List<Song>, String>() {
                                        @Override
                                        public Object apply(Object o) throws Exception {
                                            return null;
                                        }

                                        @Override
                                        public List<Song> call(String s) {
                                            return null;
                                        }
                                    })
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new Consumer() {
                                        @Override
                                        public void accept(Object o) throws Exception {

                                        }
                                    })
                            binding.setContent(result);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(getActivity(), "请求失败！", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            isLoadData = true;
        }
    }
}
