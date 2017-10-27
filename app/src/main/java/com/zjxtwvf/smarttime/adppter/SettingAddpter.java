package com.zjxtwvf.smarttime.adppter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.zjxtwvf.smarttime.holder.BaseHolder;
import com.zjxtwvf.smarttime.holder.SettingItemHolder;

import java.util.List;

/**
 * Created by zjxtwvf on 2017/7/31.
 */

public class SettingAddpter extends BaseAdapter{

    public List<String> mData;

    public SettingAddpter(List<String> data){
        mData = data;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BaseHolder holder;

        if(null == convertView){
            holder = new SettingItemHolder();
        }else{
            holder = (BaseHolder) convertView.getTag();
        }
        convertView = holder.getRootView();
        holder.setData(mData.get(position));

        return convertView;
    }
}
