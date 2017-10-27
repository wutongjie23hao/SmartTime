package com.zjxtwvf.smarttime.utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

import com.zjxtwvf.smarttime.manager.ThreadManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v4.util.LruCache;
import android.view.View;
import android.widget.ImageView;

public class BitmapCacheUtils {
	
	private LruCache<String, Bitmap> mCache;
	private HashMap<ImageView, String> mHashMap = new HashMap<ImageView, String>();
	private static BitmapCacheUtils mCacheUtils;
    	
	private BitmapCacheUtils(){
		long maxSize = Runtime.getRuntime().freeMemory();
		mCache = new LruCache<String, Bitmap>((int)(maxSize/2)){
			@Override
			protected int sizeOf(String key, Bitmap value) {
				return value.getRowBytes()*value.getHeight();
			}
		};
	}
	
	public static BitmapCacheUtils getInstance(){
		if(mCacheUtils != null){
			return mCacheUtils;
		}
		mCacheUtils = new BitmapCacheUtils();
		return mCacheUtils;
	}

	public void display(ImageView imageView,String url){
		Bitmap bitmap = null;
		
		if(url.equals("")){
			imageView.setVisibility(View.GONE);
			return;
		}else{
			imageView.setVisibility(View.VISIBLE);
		}
		
		//??????????????
		//imageView.setImageBitmap(mDefaultImage);
		//
		mHashMap.put(imageView, url);
		//???????
		bitmap = getFromMap(url);
		if(bitmap != null){
			imageView.setImageBitmap(bitmap);
			System.out.println("??map???????????????????????????");
		}
		//??File???
		if(null == bitmap){
			System.out.println("??file???????????????????????????");
			getFromFile(imageView,url);
		}
	}

	private void setBitmapToMap(Bitmap bitmap, String url) {
		mCache.put(Md5Utils.md5(url), bitmap);
	}

	private void getFromFile(final ImageView imageView,final String url) {
		ThreadManager.getThreadPool().execute(new Runnable() {
			Bitmap bitmap = null;
			@Override
			public void run() {
				Uri uri = Uri.parse(url);
				try {
					bitmap = BitmapHnadleUtils.getInstance().getBitmapFormUri(UIUtils.getContext(), uri);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				if(null != bitmap){
					setBitmapToMap(bitmap,url);
				}
				UIUtils.runOnUIThread(new Runnable() {
					@Override
					public void run() {
						if(mHashMap.get(imageView).equals(url)){
							imageView.setImageBitmap(bitmap);	
						}
					}
				});
			}
		});
	}

	private Bitmap getFromMap(String url) {
		return mCache.get(Md5Utils.md5(url));
	}
	
}
