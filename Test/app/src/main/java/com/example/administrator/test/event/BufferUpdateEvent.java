package com.example.administrator.test.event;

/**
 * Create by zmm
 * Time 2019/5/23
 * PackageName com.example.administrator.test.event
 */
public class BufferUpdateEvent {
    private  int percent ;

    public BufferUpdateEvent(int percent){
        this.percent = percent;
    }

    public int getPercent() {
        return percent;
    }

    public void setPercent(int percent) {
        this.percent = percent;
    }
}
