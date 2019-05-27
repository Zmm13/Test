package com.example.administrator.test.event;

/**
 * Create by zmm
 * Time 2019/5/27
 * PackageName com.example.administrator.test.event
 */
public class HomeFragmentChangeEvent {
    private int toPosition = 0;

    public HomeFragmentChangeEvent(int toPosition) {
        this.toPosition = toPosition;
    }

    public int getToPosition() {
        return toPosition;
    }

    public void setToPosition(int toPosition) {
        this.toPosition = toPosition;
    }
}
