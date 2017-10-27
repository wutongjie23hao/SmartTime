package com.zjxtwvf.smarttime.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.net.Uri;

public class BitmapHnadleUtils {

	private static BitmapHnadleUtils mBitmapUtils;
	
	private BitmapHnadleUtils(){
	}

	/*单例模式*/
	public static synchronized BitmapHnadleUtils getInstance(){
		if(null == mBitmapUtils){
			mBitmapUtils = new BitmapHnadleUtils();
		}
		return mBitmapUtils;
	}
	
	/*先进行比例压缩再进行质量压缩*/
	public Bitmap getBitmapFromUri(Context context,Uri uri){
		Bitmap resultBitmap = null;
		try {
			InputStream inputStream = context.getContentResolver().openInputStream(uri);
			BitmapFactory.Options options = new Options();
			options.inJustDecodeBounds  = true;
			options.inDither = true;
			options.inPreferredConfig = Config.ARGB_8888;
			BitmapFactory.decodeStream(inputStream, null, options);
			inputStream.close();
			
			
			int width = options.outWidth;
			int height = options.outHeight;
			
			int be = 1;
			
			if(width > height && width > 400){
			    be = width/400; 
			}else if(height > width && height > 800){
				be = height/800;
			}
			
			//一定要重新使用option
			BitmapFactory.Options bitMapOptions = new Options();
			inputStream = context.getContentResolver().openInputStream(uri);
			bitMapOptions.inSampleSize = be;
			bitMapOptions.inDither = true;
			bitMapOptions.inJustDecodeBounds = false;
			bitMapOptions.inPreferredConfig = Config.ARGB_8888;
			
			resultBitmap = BitmapFactory.decodeStream(inputStream, null, options);
			inputStream.close();
			
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	
		return compress(resultBitmap);
	}
	
    public  Bitmap getBitmapFormUri(Context ac, Uri uri) throws FileNotFoundException, IOException {
        InputStream input = ac.getContentResolver().openInputStream(uri);
        BitmapFactory.Options onlyBoundsOptions = new BitmapFactory.Options();
        onlyBoundsOptions.inJustDecodeBounds = true;
        onlyBoundsOptions.inDither = true;//optional
        onlyBoundsOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;//optional
        BitmapFactory.decodeStream(input, null, onlyBoundsOptions);
        input.close();
        int originalWidth = onlyBoundsOptions.outWidth;
        int originalHeight = onlyBoundsOptions.outHeight;
        if ((originalWidth == -1) || (originalHeight == -1))
            return null;
        //图片分辨率以480x800为标准
        float hh = 800f;//这里设置高度为800f
        float ww = 320f;//这里设置宽度为480f
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (originalWidth > originalHeight && originalWidth > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (originalWidth / ww);
        } else if (originalWidth < originalHeight && originalHeight > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (originalHeight / ww);
        }
        if (be <= 0)
            be = 1;
        //比例压缩
        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inSampleSize = be;//设置缩放比例
        bitmapOptions.inDither = true;//optional
        bitmapOptions.inPreferredConfig = Bitmap.Config.RGB_565;//optional
        input = ac.getContentResolver().openInputStream(uri);
        Bitmap bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions);
        input.close();
 
        return compress(bitmap);//再进行质量压缩
    }
    

	/*质量压缩*/
	public Bitmap compress(Bitmap bitmap){
		Bitmap reslutBitmap = null;
		int option = 100;
		
		if(null == bitmap){
			return null;
		}
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);  //不降低质量
		//如果图片不小于100kb 就进行质量压缩
		while(baos.toByteArray().length/1024 > 100){
			baos.reset(); //一定要进行重置 否则导致压缩失败
			bitmap.compress(Bitmap.CompressFormat.JPEG, option, baos);
			option -= 10;
		}
		
		ByteArrayInputStream inputStream = new ByteArrayInputStream(baos.toByteArray());
		reslutBitmap = BitmapFactory.decodeStream(inputStream,null,null);
		
		return reslutBitmap;
	}
	
	public  byte[] toByteArray(InputStream input) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];
        int n = 0;
        while (-1 != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
        }
        return output.toByteArray();
    }

}
