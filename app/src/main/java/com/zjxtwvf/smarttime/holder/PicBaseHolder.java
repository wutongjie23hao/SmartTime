package com.zjxtwvf.smarttime.holder;

import com.zjxtwvf.smarttime.R;
import com.zjxtwvf.smarttime.activity.PhotoViewActivity;
import com.zjxtwvf.smarttime.domain.DiaryMessage;
import com.zjxtwvf.smarttime.utils.UIUtils;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

public abstract class PicBaseHolder extends BaseHolder<DiaryMessage> implements View.OnClickListener{
	public FrameLayout mLayout;
	public View mPics;
	public TextView text;
	public TextView date;
	public TextView day;
	public TextView month;
	public TextView week;
	public TextView mLoction;
	
	public PicBaseHolder() {
        super.BaseHolder();
		mPics = initPicsView();
	}
	
	public void setData(DiaryMessage data){
		super.setData(data);
		refeshPicsView();
	}

	public void initRootView(){
		mRootView = View.inflate(UIUtils.getContext(),
				R.layout.list_item_view, null);
		mLayout = (FrameLayout) mRootView.findViewById(R.id.fl_pics);
		text = (TextView) mRootView.findViewById(R.id.tv_diary_text);
		date = (TextView) mRootView.findViewById(R.id.tv_date);
		day = (TextView) mRootView.findViewById(R.id.tv_day);
		month = (TextView) mRootView.findViewById(R.id.tv_month);
		week = (TextView) mRootView.findViewById(R.id.tv_week);
		mLoction = (TextView) mRootView.findViewById(R.id.iv_location);
	}
	
	public void refreshRootView(){
		text.setText(data.getText());
		date.setText(data.getDate());
		day.setText(data.getDay());
		month.setText(data.getMouth());
		week.setText(data.getWeek());
		mLoction.setText(data.getAddr());
		if(data.getAddr() == null || data.getAddr().equals("")){
			mLoction.setVisibility(View.GONE);
		}else{
			mLoction.setVisibility(View.VISIBLE);
		}
		mLayout.removeAllViews();
		if(null != mPics){
			mLayout.addView(mPics);
		}
	}

    public void statPhotoViewActivity(int position) {
        Intent intent = new Intent(UIUtils.getContext(), PhotoViewActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("dataBean", data.getUri());
        intent.putExtras(bundle);
        intent.putExtra("currentPosition", position);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		UIUtils.getContext().startActivity(intent);
    }

	public abstract View initPicsView();
	public abstract void refeshPicsView();

}
