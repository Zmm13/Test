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
import android.widget.ImageView;
import android.widget.Toast;

import com.example.administrator.test.adapter.GeDanAdapter;
import com.example.administrator.test.adapter.ItemInernetAdapter;
import com.example.administrator.test.adapter.QqTop100Adapter;
import com.example.administrator.test.daoJavaBean.Song;
import com.example.administrator.test.entity.QQMusic;
import com.example.administrator.test.event.EventInternetMusicEnd;
import com.example.administrator.test.event.GeDanInfoEvent;
import com.example.administrator.test.event.IsLightChangeEvent;
import com.example.administrator.test.event.MusicChangeEvent;
import com.example.administrator.test.event.QQMusicShouYeInfoEvent;
import com.example.administrator.test.event.QQMusicGetKeyEvent;
import com.example.administrator.test.event.QQNewMusicEvent;
import com.example.administrator.test.presenter.MyInternetPresenter;
import com.example.administrator.test.singleton.MediaPlayerUtils;
import com.example.administrator.test.singleton.MusicListTool;
import com.example.administrator.test.utils.Res;
import com.example.administrator.test.databinding.FragmentMyInternetBinding;
import com.example.administrator.test.utils.StaticBaseInfo;
import com.example.administrator.test.utils.TextUtil;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.bingoogolapple.bgabanner.BGABanner;

public class MyInternetFragment extends Fragment {
    private View view;
    private boolean isViewInitiated = false;
    private boolean isVisibleToUser = false;
    private boolean isLoadData = false;
    private FragmentMyInternetBinding binding;
    private QqTop100Adapter adapter;
    private ItemInernetAdapter itemInernetAdapter;
    private ItemInernetAdapter itemInernetAdapter1;
    private List<QQMusic> list;
    private List<String> items;
    private Song playSong = null;
    private final String TAG = "MyInternetFragment";
    private MyInternetPresenter presenter;

    private String[] itemsName = new String[]{"流行榜", "热歌榜", "新歌榜", "内地榜", "欧美榜", "日本榜"};

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
        binding.setIsLight(StaticBaseInfo.isLight(getActivity()));
        presenter = new MyInternetPresenter();
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


        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(getActivity());
        linearLayoutManager1.setOrientation(LinearLayoutManager.HORIZONTAL);
        binding.rvItem.setLayoutManager(linearLayoutManager1);
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(getActivity());
        linearLayoutManager2.setOrientation(LinearLayoutManager.HORIZONTAL);
        binding.rvItemGedanTuijian.setLayoutManager(linearLayoutManager2);

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

            presenter.getShouYeInfo();

            presenter.getCoolMusics();

            itemInernetAdapter = new ItemInernetAdapter(Arrays.asList(itemsName), getActivity()) {
                @Override
                protected void oItemClick(int position) {
                    itemInernetAdapter.setSelectedItem(position);
                    switch (position) {
                        case 0:
                            presenter.getCoolMusics();
                            break;
                        case 1:
                            presenter.getHotMusics();
                            break;
                        case 2:
                            presenter.getNewMusics();
                            break;
                        case 3:
                            presenter.getNeiDiMusics();
                            break;
                        case 4:
                            presenter.getOuMeiMusics();
                            break;
                        case 5:
                            presenter.getRiBenMusics();
                            break;
                    }
                }
            };
            binding.rvItem.setAdapter(itemInernetAdapter);

            isLoadData = true;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(IsLightChangeEvent event) {
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
        if (itemInernetAdapter != null) {
            itemInernetAdapter.notifyDataSetChanged();
        }
        if (binding != null) {
            binding.setIsLight(StaticBaseInfo.isLight(getActivity()));
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
                        qqMusic = list.get(i + 1 < list.size() ? i + 1 : 0);
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(QQMusicShouYeInfoEvent event) {

        try {
            binding.bannerGuideContent.setAdapter(new BGABanner.Adapter<ImageView, String>() {
                @Override
                public void fillBannerItem(BGABanner banner, ImageView itemView, String model, int position) {
                    Transformation transformation = new RoundedTransformationBuilder()
                            .cornerRadiusDp(Res.getDimen(R.dimen.x2, getActivity()))
                            .oval(false)
                            .build();
                    Picasso.with(getActivity())
                            .load(model)
                            .fit()
                            .transform(transformation)
                            .into(itemView);
                }
            });
            binding.bannerGuideContent.setData(event.getUrls(), null);

            itemInernetAdapter1 = new ItemInernetAdapter(event.shouYeInfo.getNames(), getActivity()) {
                @Override
                protected void oItemClick(int position) {
                    itemInernetAdapter1.setSelectedItem(position);
                    presenter.getGeDan(event.shouYeInfo.getGeDans().get(position).getItem_id());
                }
            };
            binding.rvItemGedanTuijian.setAdapter(itemInernetAdapter1);

            presenter.getGeDan(event.shouYeInfo.getGeDans().get(0).getItem_id());
        } catch (Exception e) {

        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(QQNewMusicEvent event) {
        list.clear();
        list.addAll(event.getList());
        adapter.notifyDataSetChanged();
        Toast.makeText(getActivity(), event.getList().size() + "", Toast.LENGTH_SHORT).show();
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(GeDanInfoEvent event) {
        GeDanAdapter geDanAdapter = new GeDanAdapter(getActivity(),event.list);
        binding.mrvGedan.setAdapter(geDanAdapter);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(QQMusicGetKeyEvent event) {
        if (TextUtil.isEmpty(event.getKey())) {
            Toast.makeText(getActivity(), "资源有误，无法播放！", Toast.LENGTH_SHORT).show();
            return;
        }
        String path = StaticBaseInfo.QQ_NEW_MUSIC_TOP_100_PLAY + event.getKey();
        Song song1 = new Song();
        song1.setPath(path);
        song1.setSinger(event.getSong().getSingerNames());
        song1.setName(event.getSong().getTitle());
        song1.setImageUrl(event.getSong().getSongImagePath());
        MediaPlayerUtils.getInstance().changeMusic(song1);
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
    public void onStart() {
        super.onStart();
        binding.bannerGuideContent.setAutoPlayAble(true);
    }

    @Override
    public void onStop() {
        super.onStop();
        binding.bannerGuideContent.setAutoPlayAble(false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    private void selectSong(QQMusic song) {
        presenter.getMusicInfo(song);
    }
}
