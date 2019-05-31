package com.example.administrator.test;


import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.test.adapter.QqTop100Adapter;
import com.example.administrator.test.daoJavaBean.Song;
import com.example.administrator.test.databinding.FragmentInternetMusicBinding;
import com.example.administrator.test.entity.GeDanEvent;
import com.example.administrator.test.entity.QQMusic;
import com.example.administrator.test.event.EventInternetMusicEnd;
import com.example.administrator.test.event.IsLightChangeEvent;
import com.example.administrator.test.event.MusicChangeEvent;
import com.example.administrator.test.event.QQInternetMusicListChangeEvent;
import com.example.administrator.test.event.QQMusicFocuse10002Event;
import com.example.administrator.test.event.QQMusicGetKeyEvent;
import com.example.administrator.test.event.QQNewMusicEvent;
import com.example.administrator.test.event.SearchInternetMusicEvent;
import com.example.administrator.test.presenter.InternetMusicPresenter;
import com.example.administrator.test.presenter.MyInternetPresenter;
import com.example.administrator.test.singleton.MediaPlayerUtils;
import com.example.administrator.test.singleton.MusicListTool;
import com.example.administrator.test.utils.KeybordsUtils;
import com.example.administrator.test.utils.StaticBaseInfo;
import com.example.administrator.test.utils.TextUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class InternetMusicFragment extends Fragment {
    private View view;
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
        binding.editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                KeybordsUtils.closeKeybord(binding.editText,getActivity());
                String key = binding.editText.getText().toString();
                if(i == EditorInfo.IME_ACTION_SEARCH){
                   presenter.search(key);
                }
                return false;
            }
        });
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


    public void prepareFetchData() {
        if (view != null && isVisibleToUser && isViewInitiated && !isLoadData) {
//            presenter.getMusicsList("4", "2019-05-27");
            binding.setIsLight(StaticBaseInfo.isLight(getActivity()));
            isLoadData = true;
        } else if (isLoadData) {

        }
    }

    private void initRecyclerview() {
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

    }

    private void selectSong(QQMusic song) {
        if(MusicListTool.getInstance().getPlaySong() == null || (!song.getMid().equals(MusicListTool.getInstance().getPlaySong().getMid()))){
            presenter.getMusicInfo(song);
        }
//        presenter.getMusicUrl(song);
//        EventBus.getDefault().post(new QQMusicGetKeyEvent(StaticBaseInfo.OTHER_TENCENT_GET_URL_ADDRESS.replace(StaticBaseInfo.OTHER_TENCENT_GET_URL_ADDRESS_REPLACE,song.getMid()),song));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(QQNewMusicEvent event) {
        list.clear();
        list.addAll(event.getList());
        adapter.notifyDataSetChanged();
//        Toast.makeText(getActivity(), event.getList().size() + "", Toast.LENGTH_SHORT).show();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public synchronized void onEvent(MusicChangeEvent event) {
//        处理逻辑
        io.reactivex.Observable.create(new ObservableOnSubscribe<List<QQMusic>>() {
            @Override
            public void subscribe(ObservableEmitter<List<QQMusic>> e) {
                if (list != null && list.size() > 0) {
                    boolean a = false;
                    boolean b = false;
                    for (int i = 0; i < list.size(); i++) {
                        if (playSong != null) {
                            if (!a && list.get(i).getMid().equals(playSong.getMid())) {
                                adapter.notifyItemChanged(i);
                                a = true;
                            }
                        }
                        if(!MusicListTool.getInstance().getPlaySong().isInternet()){
                           b = true;
                        }
                        if (!b && list.get(i).getMid().equals(MusicListTool.getInstance().getPlaySong().getMid())) {
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
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<List<QQMusic>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(List<QQMusic> qqMusics) {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(EventInternetMusicEnd event) {
        QQMusic qqMusic = null;
        if (list != null && list.size() > 0) {
            if (MusicListTool.getInstance().getPlaySong() == null || !MusicListTool.getInstance().getPlaySong().isInternet()) {
                qqMusic = list.get(0);
            } else {
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).getMid().equals(MusicListTool.getInstance().getPlaySong().getMid())) {
                        qqMusic = list.get(i + 1 < list.size() ? i + 1 : 0);
                        break;
                    }
                    if (i == list.size() - 1) {
                        qqMusic = list.get(0);
                    }
                }
            }
        }
        if (qqMusic != null) {
            selectSong(qqMusic);
        }else {
            MediaPlayerUtils.getInstance().playMusic(getContext());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(IsLightChangeEvent event) {
        binding.setIsLight(StaticBaseInfo.isLight(getActivity()));
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(QQInternetMusicListChangeEvent event) {
        if (presenter != null) {
            presenter.getMusicsList(event.getInfo().getTopId() + "", event.getInfo().getPeriod());
        }
    }

 @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(GeDanEvent event) {
        if (presenter != null) {
            presenter.getCD(event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(QQMusicFocuse10002Event event) {
        presenter.getMusicsList(event.getS());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(SearchInternetMusicEvent event) {
        KeybordsUtils.openKeybord(binding.editText,getActivity());
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(QQMusicGetKeyEvent event) {
        if (TextUtil.isEmpty(event.getKey())) {
            Toast.makeText(getActivity(), "资源有误，无法播放！", Toast.LENGTH_SHORT).show();
            return;
        }
        String path = StaticBaseInfo.QQ_NEW_MUSIC_TOP_100_PLAY + event.getKey();
        Song song1 = new Song();
//        song1.setPath(event.getKey());
        song1.setPath(path);
        song1.setSinger(event.getSong().getSingerNames());
        song1.setName(event.getSong().getTitle());
        song1.setImageUrl(event.getSong().getSongImagePath());
        song1.setMid(event.getSong().getMid());
        song1.setInternet(true);
        MediaPlayerUtils.getInstance().changeMusic(song1);
    }

    @Override
    public void onStop() {
        super.onStop();
        if(KeybordsUtils.isSoftInputShow(getActivity())){
            KeybordsUtils.closeKeybord(binding.editText,getActivity());
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


}
