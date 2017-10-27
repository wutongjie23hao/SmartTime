package com.zjxtwvf.smarttime.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

import com.zjxtwvf.smarttime.R;

public class UserActivity extends AppCompatActivity implements View.OnClickListener{

    private LinearLayout mLlSetting;
    Toolbar toolbar;
    AppBarLayout mAblBar;
    TextView title;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView( R.layout.activity_user);
		initView();
		initListener();
	}

    protected void initView() {
    	toolbar = (Toolbar) findViewById(R.id.toolbar);
    	title = (TextView) findViewById(R.id.main_tv_toolbar_title);
    	mAblBar =  (AppBarLayout) findViewById(R.id.main_abl_app_bar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        mLlSetting = (LinearLayout)findViewById(R.id.ll_setting);
    }

    protected void initListener() {
        mLlSetting.setOnClickListener(this);
        mAblBar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                int halfScroll = appBarLayout.getTotalScrollRange() / 2;
                int offSetAbs = Math.abs(verticalOffset);
                float percentage;
                if (offSetAbs < halfScroll) {
                    title.setText("SmartTime");
                    percentage = 1 - (float) offSetAbs / (float) halfScroll;
                    toolbar.setBackgroundColor(Color.TRANSPARENT);
                } else {
                    title.setText("个人中心");
                    toolbar.setBackgroundColor(getResources().getColor(R.color.main_color));
                    percentage = (float) (offSetAbs - halfScroll) / (float) halfScroll;
                }
                toolbar.setAlpha(percentage);
            }
        });
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, SettingActivity.class);
        startActivity(intent);
    }
}
