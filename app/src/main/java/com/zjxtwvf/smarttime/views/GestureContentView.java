package com.zjxtwvf.smarttime.views;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.zjxtwvf.smarttime.R;
import com.zjxtwvf.smarttime.domain.GesturePoint;
import com.zjxtwvf.smarttime.utils.AppUtil;
import com.zjxtwvf.smarttime.utils.UIUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 手势密码容器类
 *
 */
public class GestureContentView extends ViewGroup {

	private int baseNum = 6;

	private int[] screenDispaly;

	/**
	 * 每个点区域的宽度
	 */
	private int blockWidth;
	/**
	 * 声明一个集合用来封装坐标集合
	 */
	private List<ImageView> list;
	private Context context;
	private boolean isVerify;
	private GestureDrawline gestureDrawline;
	private LayoutParams mLayoutParams;
	private String mPassword;
	private GestureDrawline.GestureCallBack mCallBack;
	private ViewGroup mParent;


	/**
	 * 包含9个ImageView的容器，初始化
	 * @param context
	 * @param isVerify 是否为校验手势密码
	 * @param passWord 用户传入密码
	 * @param callBack 手势绘制完毕的回调
	 */
	public GestureContentView(Context context, boolean isVerify, ViewGroup parent,
							  String passWord, GestureDrawline.GestureCallBack callBack) {
		super(context);
		this.context = context;
		this.isVerify = isVerify;
		this.mPassword = passWord;
		this.mCallBack = callBack;
		this.mParent = parent;
        this.list = new ArrayList<ImageView>();
		addChild();
	}

	private void addChild(){
		for (int i = 0; i < 9; i++) {
			ImageView image = new ImageView(UIUtils.getContext());
			image.setBackgroundResource(R.drawable.unlock_static);
			image.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
			this.addView(image);
			list.add(image);
		}
	}

	public void setParentView(ViewGroup parent){
		mLayoutParams = parent.getLayoutParams();
		this.setLayoutParams(mLayoutParams);
		// 初始化一个可以画线的view  占位
		gestureDrawline = new GestureDrawline(context,isVerify, list,mPassword, mCallBack);
		gestureDrawline.setLayoutParams(mLayoutParams);
		parent.addView(gestureDrawline);
		parent.addView(this);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		for (int i = 0; i < getChildCount(); i++) {
			//第几行
			int row = i/3;
			//第几列
			int col = i%3;
			View v = getChildAt(i);
			v.layout(l+col*blockWidth+blockWidth/baseNum, t+row*blockWidth+blockWidth/baseNum,
					l+col*blockWidth+blockWidth-blockWidth/baseNum, t+row*blockWidth+blockWidth-blockWidth/baseNum);
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int width = MeasureSpec.getSize(widthMeasureSpec) - getPaddingLeft()
				- getPaddingRight();
		int height = MeasureSpec.getSize(heightMeasureSpec) - getPaddingTop()
				- getPaddingBottom();

		blockWidth = width/3;

		// 遍历设置每个子view的大小
		for (int i = 0; i < getChildCount(); i++) {
			View child = getChildAt(i);
			int widthSpec = MeasureSpec.makeMeasureSpec(2*blockWidth/3, MeasureSpec.EXACTLY);
			int hSpeceight = MeasureSpec.makeMeasureSpec(2*blockWidth/3, MeasureSpec.EXACTLY);
			child.measure(widthSpec, hSpeceight);
		}

		setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec),
				MeasureSpec.getSize(heightMeasureSpec));
	}

	/**
	 * 保留路径delayTime时间长
	 * @param delayTime
	 */
	public void clearDrawlineState(long delayTime) {
		gestureDrawline.clearDrawlineState(delayTime);
	}

}
