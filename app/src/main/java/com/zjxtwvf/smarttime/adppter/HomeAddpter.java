package com.zjxtwvf.smarttime.adppter;

import java.util.ArrayList;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import com.zjxtwvf.smarttime.domain.DiaryMessage;
import com.zjxtwvf.smarttime.holder.PicBaseHolder;
import com.zjxtwvf.smarttime.holder.ListItemFourPic;
import com.zjxtwvf.smarttime.holder.ListItemNullPic;
import com.zjxtwvf.smarttime.holder.ListItemOnePic;
import com.zjxtwvf.smarttime.holder.ListItemThreePic;
import com.zjxtwvf.smarttime.holder.ListItemTwoPics;

public class HomeAddpter extends BaseAdapter{
	private ArrayList<DiaryMessage> mArrayList;
	
	public HomeAddpter(ArrayList<DiaryMessage> data) {
		mArrayList = data;
	}
	
	@Override
	public View getView(int index, View contentView, ViewGroup arg2) {
		int position = mArrayList.size() - index - 1;
		PicBaseHolder holder;
		int type;
		if(null == contentView){

			type = mArrayList.get(position).getPicsNum();
			if(1 == type){
				holder = new ListItemOnePic();
			}else if(2 == type){
				holder = new ListItemTwoPics();
			}else if(3 == type){
				holder = new ListItemThreePic();
			}else if(4 == type){
				holder = new ListItemFourPic();
			}else{
				holder = new ListItemNullPic();
			}

			contentView = holder.getRootView();
		}else{
			holder = (PicBaseHolder) contentView.getTag();
		}
		
		holder.setData(mArrayList.get(position));
		return contentView;
	}
	@Override
	public long getItemId(int arg0) {
		return arg0;
	}
	@Override
	public Object getItem(int arg0) {
		return mArrayList.get(arg0);
	}
	@Override
	public int getCount() {
		return mArrayList.size();
	}
	
	@Override
	public int getItemViewType(int position) {
		return mArrayList.get(mArrayList.size() - position - 1).getPicsNum();
	}
	
	@Override
	public int getViewTypeCount() {
		return 5;
	}
}