package com.zjxtwvf.smarttime.activity;

import java.util.ArrayList;

import com.zjxtwvf.smarttime.adppter.HomeAddpter;
import com.zjxtwvf.smarttime.db.dao.DiaryDao;
import com.zjxtwvf.smarttime.domain.DiaryMessage;
import com.zjxtwvf.smarttime.utils.AnimationUitls;
import com.zjxtwvf.smarttime.utils.UIUtils;

import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout.LayoutParams;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.zjxtwvf.smarttime.R;

public class MainActivity extends Activity implements OnClickListener {
    private ListView mListView;
    private ArrayList<DiaryMessage> mArrayList = new ArrayList<DiaryMessage>();
    private HomeAddpter mAddpter;
    private RelativeLayout mOptionRLayout;
    private FrameLayout mOptionFLayout;
    private GridLayout mOptionGLayout;
    private Boolean mOutOrIn = true; // in״̬
    private ImageView mOptionAdd;

    private int mClickAddHeight;
    private int mClickAddTotalHeight;
    private ImageView mShowMode;
    private ImageView mWriteDiary;
    private FrameLayout mOptionFLayoutRoot;
    private ImageView mSetting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
        initEvent();
    }

    private void initEvent() {
        mListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int position, long arg3) {
                if (position == 0) {
                    return;
                }
                Intent intent = new Intent(MainActivity.this,
                        EditorActivity.class);
                intent.putExtra("ID", mArrayList.size() - position + 1);
                startActivity(intent);
            }
        });
        mOptionRLayout.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (mOutOrIn) {
                    return false;
                } else {
                    hideOption();
                    return true;
                }
            }
        });
        mOptionFLayout.setOnClickListener(this);
        mShowMode.setOnClickListener(this);
        mWriteDiary.setOnClickListener(this);
        mSetting.setOnClickListener(this);
    }

    private void initData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                DiaryDao diaryDao = new DiaryDao(MainActivity.this);
                mArrayList = diaryDao.getAll();
                mAddpter = new HomeAddpter(mArrayList);
                UIUtils.runOnUIThread(new Runnable() {
                    @Override
                    public void run() {
                        mListView.setAdapter(mAddpter);
                    }
                });
            }
        }).start();
    }

    private void initView() {
        mListView = (ListView) findViewById(R.id.lv_view);
        View view = View.inflate(MainActivity.this, R.layout.list_item_header,
                null);
        mListView.addHeaderView(view);

        mShowMode = (ImageView) findViewById(R.id.iv_map_show_click);
        mWriteDiary = (ImageView) findViewById(R.id.iv_write_diary);
        mSetting = (ImageView) findViewById(R.id.iv_setting);

        mOptionAdd = (ImageView) findViewById(R.id.iv_dab_plus);
        mOptionRLayout = (RelativeLayout) findViewById(R.id.rl_option_back);
        mOptionFLayoutRoot = (FrameLayout) findViewById(R.id.fl_option_back);
        mOptionFLayout = (FrameLayout) findViewById(R.id.fl_option);
        mOptionGLayout = (GridLayout) findViewById(R.id.gl_option);
        mOptionGLayout.measure(0, 0);
        mClickAddHeight = mOptionFLayout.getMeasuredHeight();
        mClickAddTotalHeight = mOptionGLayout.getMeasuredHeight();
        mOptionGLayout.setPadding(mOptionGLayout.getPaddingLeft(),
                mClickAddHeight - mClickAddTotalHeight,
                mOptionGLayout.getPaddingRight(),
                mOptionGLayout.getPaddingBottom());
        mOptionRLayout.startAnimation(AnimationUitls.getAlphaAnimation(0.0f,
                0.0f, 0));
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
        if(mAddpter != null){
            mAddpter.notifyDataSetChanged();
        }
        if (mOutOrIn == false) {
            hideOption();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_camera:
                break;
            case R.id.action_writing:
                Intent intent = new Intent(this, EditorActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void openOption() {
        mOutOrIn = false;
        mOptionRLayout.startAnimation(AnimationUitls.getAlphaAnimation(0.1f,
                0.9f, 200));
        mOptionAdd.startAnimation(AnimationUitls
                .getRoateAnimation(0, -135, 200));
        ValueAnimator animator = ValueAnimator.ofInt(mClickAddHeight,
                mClickAddTotalHeight);
        animator.addUpdateListener(new AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int top = (Integer) valueAnimator.getAnimatedValue();
                mOptionGLayout.setPadding(mOptionGLayout.getPaddingLeft(), top
                                - mClickAddTotalHeight,
                        mOptionGLayout.getPaddingRight(),
                        mOptionGLayout.getPaddingBottom());
            }
        });
        animator.setDuration(200);
        animator.start();
    }

    public void hideOption() {
        mOutOrIn = true;
        mOptionRLayout.startAnimation(AnimationUitls.getAlphaAnimation(0.9f,
                0.0f, 200));
        mOptionAdd.startAnimation(AnimationUitls
                .getRoateAnimation(-135, 0, 200));
        mOptionGLayout.setPadding(mOptionGLayout.getPaddingLeft(),
                mClickAddHeight - mClickAddTotalHeight,
                mOptionGLayout.getPaddingRight(),
                mOptionGLayout.getPaddingBottom());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fl_option:
                if (mOutOrIn == true) {
                    openOption();
                } else {
                    hideOption();
                }
                // popAddView();
                DisplayMetrics metrics = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(metrics);

                int width = metrics.widthPixels;
                int height = metrics.heightPixels;
                RelativeLayout.LayoutParams params = new LayoutParams(width, height);
                mOptionFLayoutRoot.setLayoutParams(params);
                break;

            case R.id.iv_map_show_click:
                Intent intent = new Intent(UIUtils.getContext(),
                        BaiduMapActivity.class);
                startActivity(intent);
                break;
            case R.id.iv_write_diary:
                intent = new Intent(this, EditorActivity.class);
                startActivity(intent);
                break;
            case R.id.iv_setting:
                intent = new Intent(this, UserActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (mOutOrIn == true) {
            super.onBackPressed();
        } else {
            hideOption();
        }
    }
}
