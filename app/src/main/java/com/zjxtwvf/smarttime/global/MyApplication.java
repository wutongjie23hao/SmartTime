package com.zjxtwvf.smarttime.global;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.utils.CoordinateConverter.CoordType;
import com.zjxtwvf.smarttime.service.LocationService;

import android.app.Application;
import android.content.Context;
import android.os.Handler;

public class MyApplication extends Application{
	private static Context context;
	private static int mainThreadId;
	private static Handler handler;
	private LocationService locationService;
	
	public LocationService getLocationService() {
		return locationService;
	}

	public static Context getContext() {
		return context;
	}

	public static Handler getHandler() {
		return handler;
	}
	
	@Override
	public void onCreate() {
		context = getApplicationContext();
		handler = new Handler();
		SDKInitializer.initialize(this);
		//SDKInitializer.setCoordType(CoordType.BD09LL);
		mainThreadId =  android.os.Process.myTid();
		locationService = new LocationService(context);
		super.onCreate();
	}

	public static int getMainThreadId() {
		return mainThreadId;
	}
}
