package com.zjxtwvf.smarttime.holder;

import android.view.View;

/**
 * Created by zjxtwvf on 2017/7/31.
 */

public abstract class BaseHolder<T> {

    public View mRootView;
    public T data;

    public void BaseHolder(){
        initRootView();
        mRootView.setTag(this);
    }

    public void setData(T data){
        this.data = data;
        refreshRootView();
    }

    public View getRootView(){
        return mRootView;
    }

    public abstract void initRootView();
    public abstract void refreshRootView();
}
