package com.example.administrator.test.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.administrator.test.Home1Fragment;
import com.example.administrator.test.InternetMusicFragment;
import com.example.administrator.test.MusicListFragment;
import com.example.administrator.test.MyInternetFragment;
import com.example.administrator.test.MyTestFragment;
import com.example.administrator.test.SettingFragment;

import java.util.ArrayList;

public class FragmentViewPagerAdapter extends FragmentPagerAdapter {
    private int[] contents;

    public FragmentViewPagerAdapter(FragmentManager fm, int[] contents) {
        super(fm);
        this.contents = contents;
    }

    public FragmentViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new MyInternetFragment();
            case 1:
                return new InternetMusicFragment();
            case 2:
                return new MusicListFragment();
            case 3:
                return new SettingFragment();
            default:
                return new MyTestFragment().build(position + 1);
        }

    }

    @Override
    public int getCount() {
        return 4;
    }
}
