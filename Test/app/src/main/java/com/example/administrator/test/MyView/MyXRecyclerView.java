package com.example.administrator.test.MyView;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * Create by zmm
 * Time 2019/5/24
 * PackageName com.example.administrator.test.MyView
 */
public class MyXRecyclerView extends RecyclerView {
    public MyXRecyclerView(Context context) {
        super(context);
        init();
    }

    public MyXRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyXRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init(){
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        setLayoutManager(manager);
    }
}
