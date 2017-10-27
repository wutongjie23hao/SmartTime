package com.zjxtwvf.smarttime.activity;

import com.baidu.location.BDLocation;
import com.zjxtwvf.smarttime.R;
import com.zjxtwvf.smarttime.db.dao.DiaryDao;
import com.zjxtwvf.smarttime.domain.DiaryMessage;
import com.zjxtwvf.smarttime.global.MyApplication;
import com.zjxtwvf.smarttime.service.LocationService.DoAffterListener;
import com.zjxtwvf.smarttime.utils.BitmapCacheUtils;
import com.zjxtwvf.smarttime.utils.UIUtils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.MutableShort;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class EditorActivity extends Activity implements OnClickListener {

	private static final int REQUEST_CODE_PICK_IMAGE = 0;
	private static final int STATE_LOCATION = 0;
	private static final int STATE_CAMERA = 1;
	private static final int STATE_WEATHER = 2;
	private static final int STATE_SHOWPIC = 3;
	private static final int STATE_NULL = 4;
	private static final int STATE_NEW = 5;
	private static final int STATE_MODIFY = 6;
	private static final int STATE_SHOW = 7;
	
	private double mLongitude;
	private double mLatitude;
	private String mAddr;
	private String mTime;
	
	
	private int mCurrentNewState = STATE_NEW;
	
	private int mCurrentState = STATE_NULL;
	private Uri mCurrentUri;
	private String [] mSaveUri = new String[4];
	private int mCurrentId = 0;
	private ImageView mCurrentSelect;

	private ImageView mImageView;
	private ImageView [] mSelectPic = new ImageView[4];
	private ImageView [] mShowPic = new ImageView[4];
	private ImageView mLocaiton;
	private ImageView mImageCamera;
	private ImageView mImageWea;
	private ImageView mSaveImage;
	
	private EditText mEditText;
	
	private LinearLayout mLayoutLocation;
	private LinearLayout mLayoutWeather;
	private RelativeLayout mLayoutCamera;
	private LinearLayout mSelectLayout;
	private TextView mLocaitonText;
	private LinearLayout mLayoutShowPics;
	private LinearLayout mSelectLayoutParent;
	private FrameLayout mFrameLayoutSelect;
	private TextView mIvEditLocation;
	private TextView mIvEditTime;
	private TextView mEditDate;
	private TextView mEditWeak;
	private TextView mEditWether;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_editor);
		initView();
		initEvent();
		mEditDate.setText("sjdfjksf");
		handleExtra(getIntent());
	}

	private void handleExtra(Intent intent) {
		final int dataBaseId = intent.getIntExtra("ID", -1);
		if(-1 == dataBaseId){
			getFocausAndlocateLast();
			return;
		}else{
			mCurrentState = STATE_SHOWPIC;
			mCurrentNewState = STATE_SHOW;
		}
		showRightPage();

		new Thread(new Runnable() {
			@Override
			public void run() {
				DiaryDao dao = new DiaryDao(UIUtils.getContext());
				final DiaryMessage message = dao.getOneById(dataBaseId);
				UIUtils.runOnUIThread(new Runnable() {
					@Override
					public void run() {
						mEditText.setText(message.getText());
						mEditText.setFocusable(false);
						mSaveUri = message.getUri();
						if(message.getAddr() == null || message.getAddr().equals("")){
							mIvEditLocation.setVisibility(View.GONE);
						}
						mIvEditLocation.setText(message.getAddr());
						mIvEditTime.setText(message.getDate());
						mEditDate.setText(message.getDate().split(" ")[0]);
						mEditWeak.setText(message.getWeek());

						for(int i=0;i<mSaveUri.length;i++){
							if(null == mSaveUri[i] || "".equals(mSaveUri[i])){
								mShowPic[i].setVisibility(View.GONE);
								continue;
							}
							mCurrentUri = Uri.parse(mSaveUri[i]);
							BitmapCacheUtils.getInstance().display(mSelectPic[i], mSaveUri[i]);
							BitmapCacheUtils.getInstance().display(mShowPic[i], mSaveUri[i]);
						}
						mCurrentId = message.getId();
					}
				});
			}
		}).start();
	}

	private void initView() {
		mImageView = (ImageView) findViewById(R.id.iv_congxiangce);
		mLocaiton = (ImageView) findViewById(R.id.iv_get_location);
		mLocaitonText = (TextView) findViewById(R.id.tv_location);
		mImageCamera = (ImageView) findViewById(R.id.iv_camarer);
		mImageWea = (ImageView) findViewById(R.id.iv_select_wea);
		mSaveImage = (ImageView) findViewById(R.id.iv_save);
		mIvEditLocation = (TextView) findViewById(R.id.iv_edit_location);
		mIvEditTime = (TextView) findViewById(R.id.tv_edit_date_full);

		mSelectPic[0] = (ImageView) findViewById(R.id.iv_select_pic0);
		mSelectPic[1] = (ImageView) findViewById(R.id.iv_select_pic1);
		mSelectPic[2] = (ImageView) findViewById(R.id.iv_select_pic2);
		mSelectPic[3] = (ImageView) findViewById(R.id.iv_select_pic3);

		mLayoutLocation = (LinearLayout) findViewById(R.id.ll_location);
		mLayoutWeather = (LinearLayout) findViewById(R.id.ll_weather);
		mSelectLayout = (LinearLayout) findViewById(R.id.ll_select_pic);
		mLayoutCamera = (RelativeLayout) findViewById(R.id.rl_camera);
		mFrameLayoutSelect = (FrameLayout) findViewById(R.id.fl_select_pic_loc_wea);
		mSelectLayoutParent = (LinearLayout) findViewById(R.id.ll_select);
		mLayoutShowPics = (LinearLayout) findViewById(R.id.ll_show_pic);

		mShowPic[0] = (ImageView) mLayoutShowPics.findViewById(R.id.iv_pic_four_of0);
		mShowPic[1] = (ImageView) mLayoutShowPics.findViewById(R.id.iv_pic_four_of1);
		mShowPic[2] = (ImageView) mLayoutShowPics.findViewById(R.id.iv_pic_four_of2);
		mShowPic[3] = (ImageView) mLayoutShowPics.findViewById(R.id.iv_pic_four_of3);

		mSelectLayoutParent.setVisibility(View.GONE);
		mEditText = (EditText) findViewById(R.id.ev_diary_text);
		mEditDate = (TextView) findViewById(R.id.tv_edit_date);
		mEditWeak = (TextView) findViewById(R.id.tv_edit_weak);
		mEditWether = (TextView) findViewById(R.id.tv_edit_wether);

		mCurrentSelect = mSelectPic[0];
		showRightPage();
	}
	
	private void initEvent() {
		mSaveImage.setOnClickListener(this);
		mImageView.setOnClickListener(this);
		mLocaiton.setOnClickListener(this);
		mImageCamera.setOnClickListener(this);
		mImageWea.setOnClickListener(this);
		mSelectPic[0].setOnClickListener(this);
		mSelectPic[1].setOnClickListener(this);
		mSelectPic[2].setOnClickListener(this);
		mSelectPic[3].setOnClickListener(this);
		mEditText.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View arg0, boolean hasFocus) {
				if(hasFocus){
					inputMethodToggle(true);
					getFocausAndlocateLast();

				}
			}
		});
		mEditText.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.iv_get_location:
			//�������
			inputMethodToggle(false);
			mFrameLayoutSelect.setVisibility(View.VISIBLE);
			mCurrentState = STATE_LOCATION;
			final MyApplication application = (MyApplication)getApplication();
			application.getLocationService().setDoAffterListener(new DoAffterListener() {
				@Override
				public void DoAffterGetLocation(BDLocation location) {
					application.getLocationService().stop();
					mLongitude = location.getLongitude();
					mLatitude = location.getLatitude();
					Toast.makeText(UIUtils.getContext(), "定位成功", Toast.LENGTH_SHORT).show();
				}
			});
			application.getLocationService().displayLocation(mLocaitonText);
			break;
		case R.id.iv_camarer:
			//�������
			inputMethodToggle(false);
			mFrameLayoutSelect.setVisibility(View.VISIBLE);
			if(0 == mSaveUri.length){
				mCurrentState = STATE_CAMERA;
			}else{
				mCurrentState = STATE_SHOWPIC;
			}
			break;
		case R.id.iv_select_wea:
			//�������
			inputMethodToggle(false);
			mFrameLayoutSelect.setVisibility(View.VISIBLE);
			mCurrentState = STATE_WEATHER;
			break;
		case R.id.iv_congxiangce:
			Intent intent = new Intent(Intent.ACTION_PICK);
			intent.setType("image/*");
			startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);
			break;
		case R.id.iv_select_pic0:
		case R.id.iv_select_pic1:
		case R.id.iv_select_pic2:
		case R.id.iv_select_pic3:
			mCurrentSelect = (ImageView) view;
			Intent intent1 = new Intent(Intent.ACTION_PICK);
			intent1.setType("image/*");
			startActivityForResult(intent1, REQUEST_CODE_PICK_IMAGE);
			break;
		case R.id.iv_save:
			if(mCurrentNewState == STATE_MODIFY || mCurrentNewState == STATE_NEW){
				saveDiary();
			}else if(mCurrentNewState == STATE_SHOW){
				getFocausAndlocateLast();
				mCurrentNewState = STATE_MODIFY;
			}
			break;
		case R.id.ev_diary_text:
			if(mCurrentNewState == STATE_MODIFY || mCurrentNewState == STATE_NEW){
				mLayoutShowPics.setVisibility(View.GONE);
				mSelectLayoutParent.setVisibility(View.VISIBLE);
				mFrameLayoutSelect.setVisibility(View.GONE);
			}
			break;
		default:
			break;
		}
	    showRightPage();
	}
	
	public void inputMethodToggle(boolean on){
		InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		if(on){
			if (inputMethodManager != null) {
				mEditText.requestFocus();
				inputMethodManager.showSoftInput(mEditText, 0);
			}
		}else{
			if (inputMethodManager != null) {
				inputMethodManager.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
			}
		}
	}

	private void getFocausAndlocateLast() {
		mEditText.setFocusable(true);
		mEditText.setFocusableInTouchMode(true);
		mEditText.requestFocus();
		mEditText.findFocus();
		mEditText.setSelection(mEditText.getText().length());
		mLayoutShowPics.setVisibility(View.GONE);
		mSelectLayoutParent.setVisibility(View.VISIBLE);
		mFrameLayoutSelect.setVisibility(View.GONE);
	}

	private void saveDiary() {
		DiaryDao dao = new DiaryDao(this);
        String text = mEditText.getText().toString();
        StringBuffer buffer = new StringBuffer();
        for(int i=0;i<mSaveUri.length;i++){
        	if(mSaveUri[i] != null && !mSaveUri[i].equals("")){
        		buffer.append(mSaveUri[i].toString());
        		buffer.append("#####");
        	}
        }
        if(mLocaitonText.getText().toString().equals("") || mLocaitonText.getText().toString() == null){
			mLocaitonText.setText(mIvEditLocation.getText());
		}else{
			mIvEditLocation.setText(mLocaitonText.getText());
			mIvEditLocation.setVisibility(View.VISIBLE);
		}
        if(STATE_MODIFY == mCurrentNewState){
        	dao.update(mCurrentId,text,buffer.toString(),mLocaitonText.getText().toString(),
        			String.valueOf(mLongitude),String.valueOf(mLatitude),"");
        }else if(STATE_NEW == mCurrentNewState){
        	dao.add(text,buffer.toString(),mLocaitonText.getText().toString(),
        			String.valueOf(mLongitude),String.valueOf(mLatitude),"");
        }
        mCurrentNewState = STATE_SHOW;
		inputMethodToggle(false);
        mSelectLayoutParent.setVisibility(View.GONE);
		mLayoutShowPics.setVisibility(View.VISIBLE);
        mEditText.setFocusable(false);
        Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT).show();
	}

	private void showRightPage() {
		mLayoutLocation.setVisibility(View.INVISIBLE);
		mLayoutWeather.setVisibility(View.INVISIBLE);
		mLayoutCamera.setVisibility(View.INVISIBLE);
		mSelectLayout.setVisibility(View.INVISIBLE);
		
		switch (mCurrentState) {
		case STATE_LOCATION:
			mLayoutLocation.setVisibility(View.VISIBLE);
			break;
		case STATE_CAMERA:
			mLayoutCamera.setVisibility(View.VISIBLE);
			break;
		case STATE_WEATHER:
			mLayoutWeather.setVisibility(View.VISIBLE);
			break;
		case STATE_SHOWPIC:
			mSelectLayout.setVisibility(View.VISIBLE);
			break;
		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		System.out.println("onActivityResult");
		if (REQUEST_CODE_PICK_IMAGE == requestCode) {
			if(null == data){
				System.out.println("null == data");
				return;
			}
			Uri uri = data.getData();
			if (null == uri) {
				mCurrentUri = null;
				System.out.println("null == uri");
				return;
			}else{
				mCurrentUri = uri;
				mSaveUri[getIndexByViewId(mCurrentSelect.getId())] = uri.toString();
			}
			BitmapCacheUtils.getInstance().display(mCurrentSelect, mCurrentUri.toString());
			BitmapCacheUtils.getInstance().display(mShowPic[getIndexByViewId(mCurrentSelect.getId())], mCurrentUri.toString());
			mCurrentState = STATE_SHOWPIC;
			showRightPage();
		}
	}
	
	private int getIndexByViewId(int id) {
		int result = 0;
		switch (id) {
		case R.id.iv_congxiangce:
			result = 0;
			break;
		case R.id.iv_select_pic0:
			result = 0;
			break;
		case R.id.iv_select_pic1:
			result = 1;
			break;
		case R.id.iv_select_pic2:
			result = 2;
			break;
		case R.id.iv_select_pic3:
			result = 3;
			break;
		default:
			break;
		}
		return result;
	}
}
