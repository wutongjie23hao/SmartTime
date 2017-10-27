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

	/*����ģʽ*/
	public static synchronized BitmapHnadleUtils getInstance(){
		if(null == mBitmapUtils){
			mBitmapUtils = new BitmapHnadleUtils();
		}
		return mBitmapUtils;
	}
	
	/*�Ƚ��б���ѹ���ٽ�������ѹ��*/
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
			
			//һ��Ҫ����ʹ��option
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
        //ͼƬ�ֱ�����480x800Ϊ��׼
        float hh = 800f;//�������ø߶�Ϊ800f
        float ww = 320f;//�������ÿ��Ϊ480f
        //���űȡ������ǹ̶��������ţ�ֻ�ø߻��߿�����һ�����ݽ��м��㼴��
        int be = 1;//be=1��ʾ������
        if (originalWidth > originalHeight && originalWidth > ww) {//�����ȴ�Ļ����ݿ�ȹ̶���С����
            be = (int) (originalWidth / ww);
        } else if (originalWidth < originalHeight && originalHeight > hh) {//����߶ȸߵĻ����ݿ�ȹ̶���С����
            be = (int) (originalHeight / ww);
        }
        if (be <= 0)
            be = 1;
        //����ѹ��
        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inSampleSize = be;//�������ű���
        bitmapOptions.inDither = true;//optional
        bitmapOptions.inPreferredConfig = Bitmap.Config.RGB_565;//optional
        input = ac.getContentResolver().openInputStream(uri);
        Bitmap bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions);
        input.close();
 
        return compress(bitmap);//�ٽ�������ѹ��
    }
    

	/*����ѹ��*/
	public Bitmap compress(Bitmap bitmap){
		Bitmap reslutBitmap = null;
		int option = 100;
		
		if(null == bitmap){
			return null;
		}
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);  //����������
		//���ͼƬ��С��100kb �ͽ�������ѹ��
		while(baos.toByteArray().length/1024 > 100){
			baos.reset(); //һ��Ҫ�������� ������ѹ��ʧ��
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
