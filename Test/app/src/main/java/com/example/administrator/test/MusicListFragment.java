package com.example.administrator.test;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.administrator.test.adapter.MusicListAdapter;
import com.example.administrator.test.daoJavaBean.Song;
import com.example.administrator.test.databinding.FragmentMusicListBinding;
import com.example.administrator.test.event.MusicChangeEvent;
import com.example.administrator.test.event.ReflushEvent;
import com.example.administrator.test.singleton.MusicListTool;
import com.example.administrator.test.utils.LocalMusicUtils;
import com.example.administrator.test.utils.MainHandler;
import com.example.administrator.test.utils.SpTool;
import com.example.administrator.test.utils.StaticBaseInfo;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * Create by zmm
 * Time 2019/5/6
 * PackageName com.example.administrator.test
 */
public class MusicListFragment extends Fragment {
    private View view;
    private boolean isViewInitiated = false;
    private boolean isVisibleToUser = false;
    private boolean isLoadData = false;
    private Context context;
    private FragmentMusicListBinding binding;
    private MusicListAdapter adapter;
    private List<Song> list;
    private Song playSong = null;

    public MusicListFragment() {

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_music_list, container, false);
        view = binding.getRoot();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        binding.rv.setLayoutManager(linearLayoutManager);
        list = new ArrayList<>();
        adapter = new MusicListAdapter(context, list) {
            @Override
            protected void onItemClick(Song song) {
                ((MainActivity) getActivity()).setPlayPosition(song);
            }
        };
        binding.rv.setAdapter(adapter);
        return view;
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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        isViewInitiated = true;
        loadData();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        this.isVisibleToUser = isVisibleToUser;
        loadData();
    }


    public void prepareFetchData() {
        if (SpTool.getBoolean(context, StaticBaseInfo.SP_NAME, StaticBaseInfo.SP_INIT_MUSIC) && MusicListTool.getInstance().getList(context).size() > 0) {
            List<Song> songs = MusicListTool.getInstance().getList(context);
            this.list.clear();
            this.list.addAll(songs);
            adapter.notifyDataSetChanged();
            isLoadData = true;
            System.out.println("Loading......");
        }
    }

    private void loadData() {
        if (view != null && isVisibleToUser && isViewInitiated && !isLoadData) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, StaticBaseInfo.MAINACTIVITY_REQUEST_CODE_READ_EXTERNAL_STORAGE);
            } else {
                prepareFetchData();
            }
        } else if (isLoadData) {
            Toast.makeText(context, "isLoadData：" + isLoadData, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case StaticBaseInfo.MAINACTIVITY_REQUEST_CODE_READ_EXTERNAL_STORAGE:
                prepareFetchData();
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ReflushEvent event) {
        //处理逻辑
        if (event != null && this.getClass().getSimpleName().equals(event.getName()) && event.isRefulsh()) {
            prepareFetchData();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(MusicChangeEvent event) {
        //处理逻辑
        if (list != null && list.size() > 0) {
            if (playSong != null) {
                adapter.notifyItemChanged(playSong.getId().intValue() - 1);
            }
            adapter.notifyItemChanged(MusicListTool.getInstance().getPlaySong().getId().intValue() - 1);
        }
        playSong = MusicListTool.getInstance().getPlaySong();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
