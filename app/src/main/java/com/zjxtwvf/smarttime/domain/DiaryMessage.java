package com.zjxtwvf.smarttime.domain;

public class DiaryMessage {
	
	public static final int TYPE_ONE = 0;
	public static final int TYPE_TWO = 1;
	public static final int TYPE_THREE = 2;
	public static final int TYPE_FOUR = 3;
	
	public int id;
	public String text;
	public String [] uri;
	public String date;
	public String day;
	public String mouth;
	public int type;
	public int picsNum;
	public String addr;
	public double longitude;
	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public String getVoiceuri() {
		return voiceuri;
	}

	public void setVoiceuri(String voiceuri) {
		this.voiceuri = voiceuri;
	}

	public double latitude;
	public String voiceuri;
	
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public DiaryMessage(int id, String text, String[] uri,String addr,double longitude,double latitude,String voiceuri, String date,
			String day, String mouth, String week) {
		super();
		this.id = id;
		this.text = text;
		this.uri = uri;
		this.date = date;
		this.day = day;
		this.mouth = mouth;
		this.week = week;
		this.addr = addr;
		this.longitude = longitude;
		this.latitude = latitude;
		this.voiceuri = voiceuri;
		this.picsNum= uri.length; 
	}

	public int getPicsNum() {
		return picsNum;
	}

	public void setPicsNum(int picsNum) {
		this.picsNum = picsNum;
	}

	public String getMouth() {
		return mouth;
	}

	public void setMouth(String mouth) {
		this.mouth = mouth;
	}

	public String getWeek() {
		return week;
	}

	public void setWeek(String week) {
		this.week = week;
	}

	public String week;
	
	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public DiaryMessage() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String[] getUri() {
		return uri;
	}

	public void setUri(String[] uri) {
		this.uri = uri;
	}
}
