package com.zjxtwvf.smarttime.activity;

import java.util.ArrayList;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapStatusChangeListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.MarkerOptions.MarkerAnimateType;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.model.LatLng;
import com.zjxtwvf.smarttime.db.dao.DiaryDao;
import com.zjxtwvf.smarttime.domain.DiaryMessage;
import com.zjxtwvf.smarttime.utils.UIUtils;

import com.zjxtwvf.smarttime.R;

public class BaiduMapActivity extends Activity implements SensorEventListener,
		OnClickListener {

	// 定位相关
	LocationClient mLocClient;
	public MyLocationListenner myListener = new MyLocationListenner();
	private LocationMode mCurrentMode;
	BitmapDescriptor mCurrentMarker;
	private SensorManager mSensorManager;
	private Double lastX = 0.0;
	private int mCurrentDirection = 0;
	private double mCurrentLat = 0.0;
	private double mCurrentLon = 0.0;
	private float mCurrentAccracy;

	private Boolean mLocationFlag = false;

	MapView mMapView;
	BaiduMap mBaiduMap;


	// 初始化全局 bitmap 信息，不用时及时 recycle
	BitmapDescriptor diary = BitmapDescriptorFactory
			.fromResource(R.drawable.diary);

	// UI相关
	OnCheckedChangeListener radioButtonListener;
	Button requestLocButton;
	boolean isFirstLoc = true; // 是否首次定位
	private MyLocationData locData;
	private ImageView mSetCenterButton;
	private ArrayList<DiaryMessage> mArrayList;
	protected LatLng mCenterLatLng;
	private Button mMapDiary;
	private LinearLayout mMapDialog;
	private int mMapDialogHeight;
	private TextView mMapDialogLocation;
	private TextView mMapDialogMessage;
	private TextView mMapDialogTime;
	private TextView mMapDialogTime0;
	private Button mMapDialogBt;
	private int mClickId;
	private long mCurrenTime;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_location);
		initView();
		initOverlay();
		initEvent();
	}

	@Override
	public void onSensorChanged(SensorEvent sensorEvent) {
		double x = sensorEvent.values[SensorManager.DATA_X];
		if (Math.abs(x - lastX) > 1.0) {
			mCurrentDirection = (int) x;
			locData = new MyLocationData.Builder().accuracy(mCurrentAccracy)
					// 此处设置开发者获取到的方向信息，顺时针0-360
					.direction(mCurrentDirection).latitude(mCurrentLat)
					.longitude(mCurrentLon).build();
			mBaiduMap.setMyLocationData(locData);
		}
		lastX = x;
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int i) {
	}

	private void initView(){
		requestLocButton = (Button) findViewById(R.id.bt_map_mode);
		mSetCenterButton = (ImageView) findViewById(R.id.bt_location);
		mMapDiary = (Button) findViewById(R.id.bt_map_diary);
		mMapDialog = (LinearLayout) findViewById(R.id.laout_map_dialog);
		mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);// 获取传感器管理服务
		mCurrentMode = LocationMode.NORMAL;

		mMapDialogLocation = (TextView) mMapDialog.findViewById(R.id.tv_map_dialog_location);
		mMapDialogMessage = (TextView) mMapDialog.findViewById(R.id.tv_map_dialog_message);
		mMapDialogTime = (TextView) mMapDialog.findViewById(R.id.tv_map_dialog_time);
		mMapDialogTime0 = (TextView) mMapDialog.findViewById(R.id.tv_map_dialog_time0);

		mMapDialogBt = (Button) mMapDialog.findViewById(R.id.bt_map_dialog);

		int w = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
		int h = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);

		mMapDialog.measure(w,h);
		mMapDialogHeight = mMapDialog.getMeasuredHeight();

		mMapDialog.setPadding(mMapDialog.getPaddingLeft(),-mMapDialogHeight,
				mMapDialog.getPaddingRight(),mMapDialog.getPaddingBottom());

		// 地图初始化
		mMapView = (MapView) findViewById(R.id.bmapView);
		mBaiduMap = mMapView.getMap();

		// 开启定位图层
		mBaiduMap.setMyLocationEnabled(true);
		// 定位初始化
		mLocClient = new LocationClient(this);

		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true); // 打开gps
		option.setCoorType("bd09ll"); // 设置坐标类型
		option.setScanSpan(1000);
		mLocClient.setLocOption(option);
		mLocClient.start();
	}

	private void initEvent(){
		mBaiduMap.setOnMarkerClickListener(new OnMarkerClickListener() {
			public boolean onMarkerClick(final Marker marker) {
				mClickId = marker.getExtraInfo().getInt("id");
				DiaryDao dao = new DiaryDao(UIUtils.getContext());
				DiaryMessage message = dao.getOneById(mClickId);
				mMapDialogLocation.setText(message.getAddr());
				if(message.getText() == null || message.getText().equals("")){
					mMapDialogMessage.setText("这家伙很懒，没留下支言片语...");
				}else{
					mMapDialogMessage.setText(message.getText());
				}
				mMapDialogTime.setText(message.getDate().split(" ")[0]);
				mMapDialogTime0.setText(message.getWeek());
				popAddView(true);
				MapStatus.Builder builder = new MapStatus.Builder();
				builder.zoom(20.0f);
				mBaiduMap.animateMapStatus(MapStatusUpdateFactory
						.newMapStatus(builder.build()));
				return true;
			}
		});

		mBaiduMap.setOnMapStatusChangeListener(new OnMapStatusChangeListener() {
			@Override
			public void onMapStatusChangeStart(MapStatus arg0) {
			}
			@Override
			public void onMapStatusChangeFinish(MapStatus status) {
				mCenterLatLng = status.target;
			}
			@Override
			public void onMapStatusChange(MapStatus arg0) {
			}
		});

		mBaiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
			@Override
			public void onMapClick(LatLng latLng) {
				if(mMapDialogHeight == mMapDialog.getMeasuredHeight()){
					popAddView(false);
				}
				MapStatus.Builder builder = new MapStatus.Builder();
				builder.zoom(18.0f);
				mBaiduMap.animateMapStatus(MapStatusUpdateFactory
						.newMapStatus(builder.build()));
			}
			@Override
			public boolean onMapPoiClick(MapPoi mapPoi) {
				return false;
			}
		});

		mLocClient.registerLocationListener(myListener);
		requestLocButton.setOnClickListener(this);
		mSetCenterButton.setOnClickListener(this);
		mMapDiary.setOnClickListener(this);
		mMapDialogBt.setOnClickListener(this);
	}

	public void initOverlay() {
		//从数据库获取数据
		DiaryDao dao = new DiaryDao(UIUtils.getContext());
		mArrayList = dao.getAll();

		// add marker overlay
		new Thread(new Runnable() {
			@Override
			public void run() {
				for(int i=0;i<mArrayList.size();i++){
					if(mArrayList.get(i).getLatitude() == 0.0 || mArrayList.get(i).getLongitude() == 0.0){
						continue;
					}
					LatLng llA = new LatLng(mArrayList.get(i).getLatitude(), mArrayList.get(i).getLongitude());
					MarkerOptions ooA = new MarkerOptions().position(llA).icon(diary)
							.zIndex(9).draggable(false);
					ooA.animateType(MarkerAnimateType.grow);
					Bundle bundle = new Bundle();
					bundle.putInt("id",mArrayList.get(i).getId());
					Marker marker = (Marker) mBaiduMap.addOverlay(ooA);
					marker.setExtraInfo(bundle);
				}
			}
		}).start();
	}

	/**
	 * 定位SDK监听函数
	 */
	public class MyLocationListenner implements BDLocationListener {
		@Override
		public void onReceiveLocation(BDLocation location) {
			// map view 销毁后不在处理新接收的位置
			if (location == null || mMapView == null) {
				return;
			}
			mCurrentLat = location.getLatitude();
			mCurrentLon = location.getLongitude();
			mCurrentAccracy = location.getRadius();
			locData = new MyLocationData.Builder()
					.accuracy(location.getRadius())
					// 此处设置开发者获取到的方向信息，顺时针0-360
					.direction(mCurrentDirection)
					.latitude(location.getLatitude())
					.longitude(location.getLongitude()).build();
			mBaiduMap.setMyLocationData(locData);
			if (isFirstLoc) {
				isFirstLoc = false;
				LatLng ll = new LatLng(location.getLatitude(),
						location.getLongitude());
				MapStatus.Builder builder = new MapStatus.Builder();
				builder.target(ll).zoom(20.0f);
				mBaiduMap.animateMapStatus(MapStatusUpdateFactory
						.newMapStatus(builder.build()));
			}
		}
		@Override
		public void onConnectHotSpotMessage(String arg0, int arg1) {
		}
	}

	@Override
	protected void onPause() {
		mMapView.onPause();
		super.onPause();
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onResume() {
		mMapView.onResume();
		super.onResume();
		// 为系统的方向传感器注册监听器
		mSensorManager.registerListener(this,
				mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
				SensorManager.SENSOR_DELAY_UI);
	}

	@Override
	protected void onStop() {
		// 取消注册传感器监听
		mSensorManager.unregisterListener(this);
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		// 退出时销毁定位
		mLocClient.stop();
		// 关闭定位图层
		mBaiduMap.setMyLocationEnabled(false);
		mMapView.onDestroy();
		mMapView = null;
		super.onDestroy();

		// 回收 bitmap 资源
		diary.recycle();
	}

	public void popAddView(boolean open) {
		if(mMapDialogHeight == 0){
			mMapDialogHeight = mMapDialog.getMeasuredHeight();
		}

		ValueAnimator animator;
		if(open && mMapDialog.getMeasuredHeight() == 0){
			 animator = ValueAnimator.ofInt(0,
					 mMapDialogHeight);
		}else if(!open && mMapDialog.getMeasuredHeight() == mMapDialogHeight){
			 animator = ValueAnimator.ofInt(mMapDialogHeight,
					0);
		}else{
			return;
		}
		animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator valueAnimator) {
				int top = (Integer) valueAnimator.getAnimatedValue();
				mMapDialog.setPadding(mMapDialog.getPaddingLeft(), top - mMapDialogHeight,
						mMapDialog.getPaddingRight(),
						mMapDialog.getPaddingBottom());
			}
		});
		animator.setDuration(200);
		animator.start();
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.bt_map_mode:
				if (LocationMode.NORMAL == mCurrentMode) {
					requestLocButton.setText("跟随");
					mCurrentMode = LocationMode.FOLLOWING;
					mBaiduMap
							.setMyLocationConfigeration(new MyLocationConfiguration(
									mCurrentMode, true, mCurrentMarker));
					MapStatus.Builder builder = new MapStatus.Builder();
					builder.overlook(0);
					mBaiduMap.animateMapStatus(MapStatusUpdateFactory
							.newMapStatus(builder.build()));
				} else {
					requestLocButton.setText("普通");
					mCurrentMode = LocationMode.NORMAL;
					mBaiduMap
							.setMyLocationConfigeration(new MyLocationConfiguration(
									mCurrentMode, true, mCurrentMarker));
					MapStatus.Builder builder1 = new MapStatus.Builder();
					builder1.overlook(0);
					mBaiduMap.animateMapStatus(MapStatusUpdateFactory
							.newMapStatus(builder1.build()));
				}
				break;
			case R.id.bt_location:
				if(!isFirstLoc){
					MapStatus.Builder builder = new MapStatus.Builder();
					builder.zoom(18.0f);
					mBaiduMap.animateMapStatus(MapStatusUpdateFactory
							.newMapStatus(builder.build()));
					mLocClient.start();
					mCurrenTime = System.currentTimeMillis();
					isFirstLoc = true;
				}
				break;
			case R.id.bt_map_diary:
				Intent intent = new Intent(this, EditorActivity.class);
				startActivity(intent);
				break;
			case R.id.bt_map_dialog:
				intent = new Intent(BaiduMapActivity.this,
						EditorActivity.class);
				intent.putExtra("ID", mClickId);
				startActivity(intent);
				break;
			default:
				break;
		}
	}
}
