package com.example.administrator.test.event;

/**
 * Create by zmm
 * Time 2019/5/7
 * PackageName com.example.administrator.test.event
 */
public class ReflushEvent {
    private String name;
    private boolean refulsh = false;

    public ReflushEvent(String name, boolean refulsh) {
        this.name = name;
        this.refulsh = refulsh;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isRefulsh() {
        return refulsh;
    }

    public void setRefulsh(boolean refulsh) {
        this.refulsh = refulsh;
    }
}
