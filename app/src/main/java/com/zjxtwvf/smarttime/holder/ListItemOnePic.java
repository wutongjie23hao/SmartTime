package com.zjxtwvf.smarttime.holder;

import com.zjxtwvf.smarttime.R;
import com.zjxtwvf.smarttime.utils.BitmapCacheUtils;
import com.zjxtwvf.smarttime.utils.UIUtils;

import android.view.View;
import android.widget.ImageView;

public class ListItemOnePic extends PicBaseHolder {
	public ImageView mImage;
	
	public ListItemOnePic() {
		super();
	}

	@Override
	public View initPicsView() {
		View view = UIUtils.inflate(R.layout.list_item_one_pic);
		mImage = (ImageView) view.findViewById(R.id.iv_diary_image);
		mImage.setOnClickListener(this);
		return view;
	}

	@Override
	public void refeshPicsView() {
		BitmapCacheUtils.getInstance().display(mImage, data.getUri()[0]);
	}

	@Override
	public void onClick(View view) {
		int position = 0;
		switch(view.getId()) {
			case R.id.iv_pic_four_of0:
				position = 0;
				break;
		}
		statPhotoViewActivity(position);
	}
}
