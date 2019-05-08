package com.example.administrator.test.minterfcae;

import android.support.annotation.CallSuper;

import com.example.administrator.test.net.CallbackInfo;
import com.example.administrator.test.utils.GenericSuperclassUtil;

import okhttp3.Request;

/**
 * Create by zmm
 * Time 2019/4/26
 * PackageName com.example.administrator.test.minterfcae
 */
public abstract class NetCallBack {
    //是否处理完成事件
    private boolean isDealComplete = false;

    public CallbackInfo callbackInfo;
    public NetCallBack() {
        callbackInfo = new CallbackInfo() ;
    }
    public void onRequestComplete() {

    }

    @CallSuper
    public void onNetworkError() {
        if (!isDealComplete) {
            onRequestComplete();
            isDealComplete = true;
        }
    }

    @CallSuper
    public void onError(Request request, Exception e) {
        if (!isDealComplete) {
            onRequestComplete();
            isDealComplete = true;
        }
    }

    @CallSuper
    public void onResponse(CallbackInfo callbackInfo) {
        if (!isDealComplete) {
            onRequestComplete();
            isDealComplete = true;
        }
    }

}
