package com.example.administrator.test;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class MyTestFragment extends Fragment {
    private  View view;
    private boolean isViewInitiated = false;
    private boolean isVisibleToUser = false;
    private boolean isLoadData = false;
    private TextView textView;
    private int content;
    private Context context;

    public MyTestFragment() {

    }

    public MyTestFragment build(int content) {
        this.content = content;
        return this;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_test, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        isViewInitiated = true;
        prepareFetchData();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        this.isVisibleToUser = isVisibleToUser;
        prepareFetchData();
    }


    public void prepareFetchData(){
          if(view!=null&&isVisibleToUser&&isViewInitiated&&!isLoadData){
              textView = (TextView) view.findViewById(R.id.tv);
              textView.setText(content+"");
              isLoadData=true;
              System.out.println("Loading......");
          }else if(isLoadData){
              Toast.makeText(context,"isLoadData："+isLoadData,Toast.LENGTH_SHORT).show();
          }
    }
}
