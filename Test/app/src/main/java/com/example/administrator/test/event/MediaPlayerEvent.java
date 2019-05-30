package com.example.administrator.test.event;

/**
 * Create by zmm
 * Time 2019/5/30
 * PackageName com.example.administrator.test.event
 */
public class MediaPlayerEvent {
    public static final int STATE_INIT = 0;//初始状态
    public static final int STATE_STARTED = 1;//已经打开状态
    public static final int STATE_END = 2;//结束状态
    public static final int STATE_ERROR = 3;//异常状态
    public static final int STATE_PAUSE = 4;//暂停状态

    private int state;

    public MediaPlayerEvent(int state) {
        this.state = state;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
