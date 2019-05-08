package com.example.administrator.test.net;

import okhttp3.Response;

/**
 * Create by zmm
 * Time 2019/4/26
 * PackageName com.example.administrator.test.net
 */
public class CallbackInfo {

    private String resultString;

    public String getResultString() {
        return resultString;
    }

//    public void setResultString(String resultString) {
//        this.resultString = resultString;
//    }


    public CallbackInfo setResultString(String resultString) {
        this.resultString = resultString;
        return this;
    }
}
