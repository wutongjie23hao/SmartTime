package com.zjxtwvf.smarttime.utils;

import java.util.Calendar;
import java.util.Date;

public class DateUtils {
	
	public static class DateTime{
		public String day;
		public String week;
		public String month;
		public String year;
	}
	
	public static DateTime getCurrenTime(){
		DateTime dateTime = new DateTime();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		
		return dateTime;
	}

	public static String formatDay(String day){
		String temp = null;
		if(day.length() == 1){
			temp = "0" + day;
			return temp;
		}else{
			return day;
		}
	}
	
	public static String formatWeek(int temp) {
	    String [] arr = {"周日","周一","周二","周三","周四","周五","周六"};
		return arr[temp-1];
	}

	public static String formatMonth(int temp) {
		String month = String.valueOf(temp+1);
		if(month.length() == 1){
			month = "/0" + month;
		}
		return month;
	}
}
