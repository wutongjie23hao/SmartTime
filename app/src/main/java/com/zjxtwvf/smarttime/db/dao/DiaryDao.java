package com.zjxtwvf.smarttime.db.dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import com.zjxtwvf.smarttime.db.DatabaseHelper;
import com.zjxtwvf.smarttime.domain.DiaryMessage;
import com.zjxtwvf.smarttime.utils.DateUtils;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DiaryDao {
    private DatabaseHelper helper;
    private Calendar mCalendar;

	public DiaryDao(Context context){
    	helper = new DatabaseHelper(context);
    	mCalendar = Calendar.getInstance();
    }
	
	public void add(String text,String uri,String addr,String longitude,String latitude,String voiceuri){
		SQLiteDatabase database = helper.getWritableDatabase();
		database.execSQL("insert into diary (text,picuri,addr,longitude,latitude,voiceuri) values (?,?,?,?,?,?)",
				new Object[]{text,uri,addr,longitude,latitude,voiceuri});
		database.close();
	}
	
	public Boolean isExit(int id){
		SQLiteDatabase database = helper.getReadableDatabase();
		Cursor cursor = database.rawQuery("select * from diary where id=?", new String[]{id+""});
		if(cursor.moveToNext()){
			return true;
		}
		cursor.close();
		database.close();
		return false;
	}
	
	public ArrayList<DiaryMessage> getAll(){
		Date curDate;
		ArrayList<DiaryMessage> arrayList = new ArrayList<DiaryMessage>();
		SQLiteDatabase database = helper.getReadableDatabase();
		Cursor cursor = database.rawQuery("select id,text,picuri,addr,longitude,latitude,voiceuri,datetime(date,'localtime') from diary", null);
		while(cursor.moveToNext()){
			int id = cursor.getInt(cursor.getColumnIndex("id"));
			String text = cursor.getString(cursor.getColumnIndex("text"));
			String uri = cursor.getString(cursor.getColumnIndex("picuri"));
			String addr = cursor.getString(cursor.getColumnIndex("addr"));
			String longitude = cursor.getString(cursor.getColumnIndex("longitude"));
			String latitude = cursor.getString(cursor.getColumnIndex("latitude"));
			String voiceuri = cursor.getString(cursor.getColumnIndex("voiceuri"));
			String date = cursor.getString(cursor.getColumnIndex("datetime(date,'localtime')"));
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String day = null;
			String week = null;
			String mouth = null;
			try {
				curDate = dateFormat.parse(date);
				mCalendar.setTime(curDate);
				day = String.valueOf(mCalendar.get(Calendar.DAY_OF_MONTH));
                day = DateUtils.formatDay(day);
				int temp = mCalendar.get(Calendar.DAY_OF_WEEK);
				week = String.valueOf(DateUtils.formatWeek(temp));
				temp = mCalendar.get(Calendar.MONTH);
				mouth = String.valueOf(DateUtils.formatMonth(temp));
				
			} catch (ParseException e) {
				e.printStackTrace();
			}finally{
				database.close();
			}
			String uris [];
			uris = uri.split("#####");
			DiaryMessage message = new DiaryMessage(id, text, uris,addr,Double.valueOf(longitude)
					,Double.valueOf(latitude),voiceuri,date,day,mouth,week);
			arrayList.add(message);
		}
		database.close();
		return arrayList;
	}
	
	public DiaryMessage getOneById(int index){
		Date curDate;
		DiaryMessage message = null;
		SQLiteDatabase database = helper.getReadableDatabase();
		Cursor cursor = database.rawQuery("select id,text,picuri,addr,longitude,latitude,voiceuri,datetime(date,'localtime') from diary where id=?",
				new String[]{String.valueOf(index)});
		if(cursor.moveToNext()){
			int id = cursor.getInt(cursor.getColumnIndex("id"));
			String text = cursor.getString(cursor.getColumnIndex("text"));
			String uri = cursor.getString(cursor.getColumnIndex("picuri"));
			String addr = cursor.getString(cursor.getColumnIndex("addr"));
			String longitude = cursor.getString(cursor.getColumnIndex("longitude"));
			String latitude = cursor.getString(cursor.getColumnIndex("latitude"));
			String voiceuri = cursor.getString(cursor.getColumnIndex("voiceuri"));
			String date = cursor.getString(cursor.getColumnIndex("datetime(date,'localtime')"));
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String day = null;
			String week = null;
			String mouth = null;
			try {
				curDate = dateFormat.parse(date);
				mCalendar.setTime(curDate);
				day = String.valueOf(mCalendar.get(Calendar.DAY_OF_MONTH));
                day = DateUtils.formatDay(day);
				int temp = mCalendar.get(Calendar.DAY_OF_WEEK);
				week = String.valueOf(DateUtils.formatWeek(temp));
				temp = mCalendar.get(Calendar.MONTH);
				mouth = String.valueOf(DateUtils.formatMonth(temp));
				
			} catch (ParseException e) {
				e.printStackTrace();
			}finally{
				database.close();
			}
			String uris [];
			String urisResult [] = new String[4];
			uris = uri.split("#####");
			for(int i=0;i<uris.length;i++){
				urisResult[i] = uris[i];
			}
			message = new DiaryMessage(id, text, urisResult,addr,Double.valueOf(longitude)
					,Double.valueOf(latitude),voiceuri,date,day,mouth,week);
		}
		
		database.close();
		return message;
	}
	
	public void update(int id,String text,String uri,String addr,String longitude,String latitude,String voiceuri){
		SQLiteDatabase database = helper.getWritableDatabase();
		database.execSQL("update diary set text=?,picuri=?,addr=?,longitude=?,latitude=?,voiceuri=? where id=?",
				 new Object[]{text,uri,addr,longitude,latitude,voiceuri,id});
		database.close();
	}
	
	public void delete(int id){
		SQLiteDatabase database = helper.getWritableDatabase();
		database.execSQL("delete from diary where id=?",new Object[]{id});
		database.close();
	}
}
