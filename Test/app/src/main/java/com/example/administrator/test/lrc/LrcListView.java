package com.example.administrator.test.lrc;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ListView;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Create by zmm
 * Time 2019/5/30
 * PackageName com.example.administrator.test.lrc
 */
public class LrcListView extends ListView {
    private LrcAdapter mAdapter;
    private int mOldPosition = -1; // 用于优化，记录上次高亮位置
    private long mStartTime = 0; // 用于计算相对时间

    // mPauseTime - mStartTime就是已经播放的时间，主要用户pause之后再start，对mStartTime正确赋值
    private long mPauseTime = 0;

    private volatile boolean mIsStart = false;

    private Runnable mSetPositionRunnable = new Runnable() {
        @Override
        public void run() {
            if (!mIsStart) {
                // 因为在消息队列中，所以会有延迟，基于mIsStart进行判断
                return;
            }
            setPosition((int) (System.currentTimeMillis() - mStartTime));

            // 用于循环
            postDelayed(mSetPositionRunnable, 100);
        }
    };


    public LrcListView(Context context) {
        this(context, null);
    }

    public LrcListView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LrcListView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOverScrollMode(OVER_SCROLL_NEVER);
        mAdapter = new LrcAdapter();
        setAdapter(mAdapter);
    }


    public void setLrc(String lrcContent) {
        reset();
        mAdapter.setData(LrcEntity.parse(lrcContent));
    }

    public void setLrc(InputStream inputStream) throws IOException {
        reset();
        mAdapter.setData(LrcEntity.parseStream(inputStream));
    }

    /**
     * 根据已经播放的相对位置进行UI更新
     *
     * @param position 单位ms，已经播放的时间
     */
    private void setPosition(int position) {
        if (mAdapter.getData() == null) {
            return;
        }
        int currentPosition = mAdapter.getCurrentPosition();
        int newPosition = -1;
        List<LrcLineEntity> lrcLines = mAdapter.getData().lrcLines;
        int size = lrcLines.size();

        // 最常见的情况就是一句挨着一句，所以先从当前位置开始遍历
        for (int i = currentPosition + 1; i < size; i++) {
            LrcLineEntity entity = lrcLines.get(i);
            if (entity.startPosition > position) {
                newPosition = i - 1;
                break;
            }
        }

        if (newPosition == -1) {
            // 如果遍历不到，那么就从头开始遍历
            for (int i = 0; i < currentPosition; i++) {
                LrcLineEntity entity = lrcLines.get(i);
                if (entity.startPosition > position) {
                    newPosition = i - 1;
                    break;
                }
            }
        }


        if (newPosition == mOldPosition) {
            // 两次找到歌词是一样的位置，那么就不更新UI了
            return;
        }

        if (newPosition == -1) {
            // 当前位置已经超出所有歌词所在时间范围内，停留在最后一句歌词
            pause();
            return;
        }

        mAdapter.setCurrentPosition(newPosition);

        // 计算一半屏幕可容纳的View的数量
        int halfOfVisibleCount = (getLastVisiblePosition() - getFirstVisiblePosition()) / 2;

        int scrollPosition = 0;

        // 歌词居中操作
        if (mOldPosition + halfOfVisibleCount < getLastVisiblePosition()) {
            // 歌词从下滚到上面，要让高亮歌词居中则少滚动半屏幕行数
            scrollPosition = newPosition >= (halfOfVisibleCount) ? newPosition - halfOfVisibleCount : newPosition;
        } else {
            // 歌词从上滚到下面，要让高亮歌词居中则多滚动半屏幕行数
            scrollPosition = newPosition >= (halfOfVisibleCount) ? newPosition + halfOfVisibleCount : newPosition;
        }

        smoothScrollToPosition(scrollPosition);

        mOldPosition = newPosition;
    }

    /**
     * 重置
     */
    public void reset() {
        mIsStart = false;
        mStartTime = 0;
        mPauseTime = 0;
        mOldPosition = -1;
        mAdapter.setCurrentPosition(-1);
        setPosition(0);
    }

    /**
     * 开始播放歌词
     */
    public void start() {
        if (mIsStart) {
            return;
        }
        mStartTime = System.currentTimeMillis() - (mPauseTime - mStartTime);
        mIsStart = true;
        post(mSetPositionRunnable);
    }

    /**
     * 暂停播放歌词
     */
    public void pause() {
        if (!mIsStart) {
            return;
        }
        mPauseTime = System.currentTimeMillis();
        mIsStart = false;
    }

    /**
     * 移动相对位置
     *
     * @param position 单位ms，已经播放的时间
     */
    public void seekTo(int position) {
        if (position < 0 || position > mAdapter.getData().totalLength) {
            return;
        }

        mStartTime = System.currentTimeMillis() - position;
        mPauseTime = 0;

        // 下次更新UI强制刷新
        mOldPosition = -1;
        mAdapter.setCurrentPosition(-1);
    }
}
