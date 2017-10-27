package com.zjxtwvf.smarttime.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import com.zjxtwvf.smarttime.R;
import com.zjxtwvf.smarttime.adppter.PhtoViewAdppter;
import com.zjxtwvf.smarttime.utils.StringUtils;
import com.zjxtwvf.smarttime.views.PhotoViewPager;
import java.util.List;

/**
 * Created by zjxtwvf on 2017/7/16.
 */

public class PhotoViewActivity extends Activity implements View.OnClickListener {

    public static final String TAG = PhotoViewActivity.class.getSimpleName();
    private PhotoViewPager mViewPager;
    private int currentPosition;
    private PhtoViewAdppter adapter;
    private TextView mTvImageCount;
    private List<String> Urls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_view);
        initView();
        initData();
    }

    private void initView() {
        mViewPager = (PhotoViewPager) findViewById(R.id.view_pager_photo);
        mTvImageCount = (TextView) findViewById(R.id.tv_image_count);
    }

    private void initData() {
        Intent intent = getIntent();
        currentPosition = intent.getIntExtra("currentPosition", 0);
        String [] urls = (String [])intent.getSerializableExtra("dataBean");
        Urls = StringUtils.fromString(urls);
        adapter = new PhtoViewAdppter(Urls, this);
        mViewPager.setAdapter(adapter);
        mViewPager.setCurrentItem(currentPosition, false);
        mTvImageCount.setText(currentPosition + 1 + "/" + Urls.size());
        mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                currentPosition = position;
                mTvImageCount.setText(currentPosition + 1 + "/" + Urls.size());
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            //case R.id.tv_save_image_photo:
               // break;
        }
    }
}
