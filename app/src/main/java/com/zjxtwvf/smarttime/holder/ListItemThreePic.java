package com.zjxtwvf.smarttime.holder;

import android.view.View;
import android.widget.ImageView;

import com.zjxtwvf.smarttime.R;
import com.zjxtwvf.smarttime.utils.BitmapCacheUtils;
import com.zjxtwvf.smarttime.utils.UIUtils;

public class ListItemThreePic extends PicBaseHolder {
	private ImageView mView0;
	private ImageView mView1;
	private ImageView mView2;
	
	public ListItemThreePic() {
		super();
	}

	@Override
	public View initPicsView() {
		View view = UIUtils.inflate(R.layout.list_item_three_pic);
		mView0 = (ImageView) view.findViewById(R.id.iv_pic_three_of0);
		mView1 = (ImageView) view.findViewById(R.id.iv_pic_three_of1);
		mView2 = (ImageView) view.findViewById(R.id.iv_pic_three_of2);
		mView0.setOnClickListener(this);
		mView1.setOnClickListener(this);
		mView2.setOnClickListener(this);
		return view;
	}

	@Override
	public void refeshPicsView() {
        BitmapCacheUtils.getInstance().display(mView0, data.getUri()[0]);		
        BitmapCacheUtils.getInstance().display(mView1, data.getUri()[1]);	
        BitmapCacheUtils.getInstance().display(mView2, data.getUri()[2]);	
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
			case R.id.iv_pic_four_of2:
				position = 2;
				break;
		}
		statPhotoViewActivity(position);
	}
}
