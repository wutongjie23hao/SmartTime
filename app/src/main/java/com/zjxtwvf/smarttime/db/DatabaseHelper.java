package com.zjxtwvf.smarttime.db;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper{

	public DatabaseHelper(Context context) {
		super(context, "diary.db", null, 1);
	}
	
	
	//增：insert into diary (text,picuri) values('skjdfhksjfhksjf','/sfs/fsfs/sfsf')
	//查：select * from diary where text='sdkjf'
	//改：update diary set text='sdjkfskjf' where text='sdf'
	//删：delete from diary where text='sjfjsfhk'

	//第一次创建数据库的时候  回调该方法   获取读数据库的时候  如果没有数据库  同样会回调该方法
	@Override
	public void onCreate(SQLiteDatabase database) {
		//字段长度其实没有严格限制
		String sql = "create table diary(id integer primary key autoincrement,text varchar(34)" +
				",picuri varchar(20),addr varchar(50),longitude varchar(20),latitude varchar(20)," +
				"voiceuri varchar(20),date DATETIME DEFAULT CURRENT_TIMESTAMP)";
		database.execSQL(sql);  //此时并没有打开数据库存  直接打开数据可读完的数据的时候  数据才被真正创建
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		
	}

}
