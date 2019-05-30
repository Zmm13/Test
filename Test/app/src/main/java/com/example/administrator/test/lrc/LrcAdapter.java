package com.example.administrator.test.lrc;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.administrator.test.R;

/**
 * Create by zmm
 * Time 2019/5/30
 * PackageName com.example.administrator.test.lrc
 */
public class LrcAdapter extends BaseAdapter {

    private LrcEntity mData; // 数据源
    private int mCurrentPosition; // 当前播放的歌曲位置，一般会有突出的效果

    @Override
    public int getCount() {
        return mData == null ? 0 : mData.lineCount();
    }

    @Override
    public Object getItem(int position) {
        return mData == null ? null : mData.lrcLines.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.line_text, parent, false);
        }

        TextView textView = (TextView) convertView;

        textView.setText(mData.lrcLines.get(position).content);

        if (mCurrentPosition == position) {
            // 当前播放的歌词显示黑色
            textView.setTextColor(Color.BLACK);
        } else {
            // 其他情况显示灰色
            textView.setTextColor(Color.GRAY);
        }

        return textView;
    }

    public void setData(LrcEntity data) {
        this.mData = data;
        notifyDataSetChanged();
    }

    public LrcEntity getData() {
        return mData;
    }

    public void setCurrentPosition(int currentPosition) {
        this.mCurrentPosition = currentPosition;
        notifyDataSetChanged();
    }

    public int getCurrentPosition() {
        return mCurrentPosition;
    }
}