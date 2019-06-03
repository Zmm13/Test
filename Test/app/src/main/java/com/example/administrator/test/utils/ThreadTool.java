package com.example.administrator.test.utils;

/**
 * Create by zmm
 * Time 2019/6/3
 * PackageName com.example.administrator.test.utils
 */
public class ThreadTool {
    public static Thread  doSomething(Runnable runnable){
        return  new Thread(runnable);
    }
}
