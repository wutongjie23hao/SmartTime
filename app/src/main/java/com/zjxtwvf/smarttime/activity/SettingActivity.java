package com.zjxtwvf.smarttime.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.zjxtwvf.smarttime.R;
import com.zjxtwvf.smarttime.adppter.SettingAddpter;

import java.util.ArrayList;
import java.util.List;

public class SettingActivity extends AppCompatActivity{

	public ListView mList;
	public List<String> mData = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView( R.layout.activity_settings);
		initView();
		initData();
		initEvent();
	}

	public void initView(){
		mList = (ListView) findViewById(R.id.settings_listview);
	}

	public void initData(){
		mData.add("检查版本");
		mData.add("重置密码");
		mData.add("用户协议");
		mData.add("关于我们");

		mList.setAdapter(new SettingAddpter(mData));
	}

	public void initEvent(){
		mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if(position == 1){
					Intent intent = new Intent(SettingActivity.this,GestureEditActivity.class);
					startActivity(intent);
				}
			}
		});
	}
}
