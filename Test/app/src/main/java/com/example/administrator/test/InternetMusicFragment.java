package com.example.administrator.test;


import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.test.adapter.QqTop100Adapter;
import com.example.administrator.test.daoJavaBean.Song;
import com.example.administrator.test.databinding.FragmentInternetMusicBinding;
import com.example.administrator.test.entity.QQMusic;
import com.example.administrator.test.event.EventInternetMusicEnd;
import com.example.administrator.test.event.IsLightChangeEvent;
import com.example.administrator.test.event.MusicChangeEvent;
import com.example.administrator.test.event.QQInternetMusicListChangeEvent;
import com.example.administrator.test.event.QQNewMusicEvent;
import com.example.administrator.test.presenter.InternetMusicPresenter;
import com.example.administrator.test.presenter.MyInternetPresenter;
import com.example.administrator.test.singleton.MusicListTool;
import com.example.administrator.test.utils.StaticBaseInfo;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class InternetMusicFragment extends Fragment {
    private  View view;
    private boolean isViewInitiated = false;
    private boolean isVisibleToUser = false;
    private boolean isLoadData = false;
    private Context context;

    private List<QQMusic> list;
    private Song playSong = null;
    private QqTop100Adapter adapter;
    private InternetMusicPresenter presenter;
    private FragmentInternetMusicBinding binding;

    public InternetMusicFragment() {

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        presenter = new InternetMusicPresenter();
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_internet_music, container, false);
        view = binding.getRoot();
        initRecyclerview();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.context = getActivity();
        isViewInitiated = true;
        prepareFetchData();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        this.isVisibleToUser = isVisibleToUser;
        prepareFetchData();
    }


    public void prepareFetchData(){
          if(view!=null&&isVisibleToUser&&isViewInitiated&&!isLoadData){
              presenter.getMusicsList("4","2019-05-27");
              isLoadData = true;
          }else if(isLoadData){

          }
    }

    private void initRecyclerview(){  LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
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
        binding.rv.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                adapter.setFirstPosition(linearLayoutManager.findFirstVisibleItemPosition());
                adapter.setLastPosition(linearLayoutManager.findLastVisibleItemPosition());
            }
        });

    }

    private void selectSong(QQMusic song) {
        presenter.getMusicInfo(song);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(QQNewMusicEvent event) {
        list.clear();
        list.addAll(event.getList());
        adapter.notifyDataSetChanged();
//        Toast.makeText(getActivity(), event.getList().size() + "", Toast.LENGTH_SHORT).show();
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(EventInternetMusicEnd event) {
        QQMusic qqMusic = null;
        if (list != null && list.size() > 0) {
            if (playSong == null) {
                qqMusic = list.get(0);
            } else {
                for (int i = 0; i < list.size(); i++) {
                    if (playSong.getPath().contains(list.get(i).getMid())) {
                        qqMusic = list.get(i + 1 < list.size() ? i + 1 : 0);
                        break;
                    }
                    if(i == list.size()-1){
                        qqMusic = list.get(0);
                    }
                }
            }
        }
        if (qqMusic != null)
            selectSong(qqMusic);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(IsLightChangeEvent event) {
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(QQInternetMusicListChangeEvent event) {
       if(presenter!=null){
           presenter.getMusicsList(event.getInfo().getTopId()+"",event.getInfo().getPeriod());
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
