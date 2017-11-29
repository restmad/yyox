package com.yyox.mvp.ui.activity;

import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.umeng.message.PushAgent;
import com.yyox.R;

import java.util.ArrayList;

public class GuideActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    private ImageView mPage0;
    private ImageView mPage1;
    private ImageView mPage2;
    private int currIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        mViewPager = (ViewPager) findViewById(R.id.welcome_viewpager);
        mViewPager.setOnPageChangeListener(new MyOnPageChangeListener());

        mPage0 = (ImageView) findViewById(R.id.page0);
        mPage1 = (ImageView) findViewById(R.id.page1);
        mPage2 = (ImageView) findViewById(R.id.page2);

        LayoutInflater mLi = LayoutInflater.from(this);
        View view1 = mLi.inflate(R.layout.welcome1, null);
        View view2 = mLi.inflate(R.layout.welcome2, null);
        View view3 = mLi.inflate(R.layout.welcome3, null);

        final ArrayList<View> views = new ArrayList<View>();
        views.add(view1);
        views.add(view2);
        views.add(view3);

        PagerAdapter mPagerAdapter = new PagerAdapter() {
            @Override
            public boolean isViewFromObject(View arg0, Object arg1) {
                return arg0 == arg1;
            }

            @Override
            public int getCount() {
                return views.size();
            }

            @Override
            public void destroyItem(View container, int position, Object object) {
                ((ViewPager) container).removeView(views.get(position));
            }

            @Override
            public Object instantiateItem(View container, int position) {
                ((ViewPager) container).addView(views.get(position));
                return views.get(position);
            }
        };
        //友盟设置
        PushAgent.getInstance(this).onAppStart();
        mViewPager.setAdapter(mPagerAdapter);
    }

    public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageSelected(int arg0) {
            switch (arg0) {
                case 0:
                    mPage0.setImageDrawable(getResources().getDrawable(R.mipmap.page_now));
                    mPage1.setImageDrawable(getResources().getDrawable(R.mipmap.page));
                    mPage2.setImageDrawable(getResources().getDrawable(R.mipmap.page));
                    break;
                case 1:
                    mPage0.setImageDrawable(getResources().getDrawable(R.mipmap.page));
                    mPage1.setImageDrawable(getResources().getDrawable(R.mipmap.page_now));
                    mPage2.setImageDrawable(getResources().getDrawable(R.mipmap.page));
                    break;
                case 2:
                    mPage0.setImageDrawable(getResources().getDrawable(R.mipmap.page));
                    mPage1.setImageDrawable(getResources().getDrawable(R.mipmap.page));
                    mPage2.setImageDrawable(getResources().getDrawable(R.mipmap.page_now));
                    break;
            }
            currIndex = arg0;
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }
    }

    public void startbutton(View v) {
        Intent intent = new Intent();
        intent.setClass(GuideActivity.this, HomeActivity.class);
        startActivity(intent);
        this.finish();
    }
}
