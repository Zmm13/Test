package com.example.administrator.test.minterfcae;

import io.reactivex.functions.Function;


/**
 * Create by zmm
 * Time 2019/5/20
 * PackageName com.example.administrator.test.minterfcae
 */
public interface MyAction<T> extends Function {
    void call(T t);
}
