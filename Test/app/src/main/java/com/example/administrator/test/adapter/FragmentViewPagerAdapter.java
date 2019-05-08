package com.example.administrator.test.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.administrator.test.Home1Fragment;
import com.example.administrator.test.MusicListFragment;
import com.example.administrator.test.MyTestFragment;

import java.util.ArrayList;

public class FragmentViewPagerAdapter extends FragmentPagerAdapter {
    private int[] contents;

    public FragmentViewPagerAdapter(FragmentManager fm, int[] contents) {
        super(fm);
        this.contents = contents;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new MusicListFragment();
            default:
                return new MyTestFragment().build(position + 1);
        }

    }

    @Override
    public int getCount() {
        return contents.length;
    }
}
