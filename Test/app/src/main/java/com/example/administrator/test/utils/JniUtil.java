package com.example.administrator.test.utils;

/**
 * Create by zmm
 * Time 2019/5/13
 * PackageName com.example.administrator.test.utils
 */
public class JniUtil {

    static {
        System.loadLibrary("JNISample");//名字注意，需要跟你的build.gradle ndk节点       下面的名字一样
    }

    public native String getSomeString();
}
