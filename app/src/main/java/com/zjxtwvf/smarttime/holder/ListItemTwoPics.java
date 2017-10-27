package com.zjxtwvf.smarttime.holder;

import com.zjxtwvf.smarttime.R;
import com.zjxtwvf.smarttime.utils.BitmapCacheUtils;
import com.zjxtwvf.smarttime.utils.UIUtils;

import android.view.View;
import android.widget.ImageView;

public class ListItemTwoPics extends PicBaseHolder {
	private ImageView mView0;
	private ImageView mView1;
	
	public ListItemTwoPics() {
		super();
	}

	@Override
	public View initPicsView() {
		View view = UIUtils.inflate(R.layout.list_item_two_pic);
		mView0 = (ImageView) view.findViewById(R.id.iv_pic_two_of0);
		mView1 = (ImageView) view.findViewById(R.id.iv_pic_two_of1);
		mView0.setOnClickListener(this);
		mView1.setOnClickListener(this);
		return view;
	}

	@Override
	public void refeshPicsView() {
        BitmapCacheUtils.getInstance().display(mView0, data.getUri()[0]);		
        BitmapCacheUtils.getInstance().display(mView1, data.getUri()[1]);	
	}

	@Override
	public void onClick(View view) {
		int position = 0;
		switch(view.getId()) {
			case R.id.iv_pic_four_of0:
				position = 0;
				break;
			case R.id.iv_pic_four_of1:
				position = 1;
				break;
		}
		statPhotoViewActivity(position);
	}
}
