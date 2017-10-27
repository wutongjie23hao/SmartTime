package com.zjxtwvf.smarttime.holder;

import android.view.View;
import android.widget.TextView;

import com.zjxtwvf.smarttime.R;
import com.zjxtwvf.smarttime.utils.UIUtils;

/**
 * Created by zjxtwvf on 2017/7/31.
 */

public class SettingItemHolder extends BaseHolder<String> {

    public TextView mText;

    public SettingItemHolder(){
        super.BaseHolder();
    }

    @Override
    public void initRootView() {
        mRootView = View.inflate(UIUtils.getContext(), R.layout.setting_list_item,null);
        mText = (TextView) mRootView.findViewById(R.id.tv_setting_item);
    }

    @Override
    public void refreshRootView() {
        mText.setText(data);
    }
}
