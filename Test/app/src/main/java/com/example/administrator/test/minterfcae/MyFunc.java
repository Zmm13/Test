package com.example.administrator.test.minterfcae;

import com.example.administrator.test.daoJavaBean.Song;

import io.reactivex.functions.Function;


/**
 * Create by zmm
 * Time 2019/5/20
 * PackageName com.example.administrator.test.minterfcae
 */
public interface MyFunc<R, T> extends Function {
    R call(T t);
}
