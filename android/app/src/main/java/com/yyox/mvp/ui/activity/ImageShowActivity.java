package com.yyox.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.yyox.R;
import com.yyox.consts.CodeDefine;
import com.yyox.mvp.model.entity.Images;

import java.util.ArrayList;
import java.util.List;

public class ImageShowActivity extends AppCompatActivity {

    private ViewPager iamgeViewPager = null;
    private List<ImageView> imageviewls = new ArrayList<ImageView>();

    private ViewGroup guidPview;
    private List<ImageView> guidviewls = new ArrayList<ImageView>();

    private Button delButton;

    private int cus = 0;

    private List<Images> mImages = new ArrayList<Images>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initData();

        initControl();
    }

    private void initControl(){
        /*set it to be no title*/
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        /*set it to be full screen*/
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_image_show);

        iamgeViewPager = (ViewPager) findViewById(R.id.image_pager);
        LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        iamgeViewPager.setLayoutParams(params);
        iamgeViewPager.setOnPageChangeListener(pageChangeListener);

        guidPview = (ViewGroup) findViewById(R.id.guid_point);
        setImageViewPager(false);

        delButton = (Button) findViewById(R.id.b_right_id);
        if (getIntent().getStringExtra("delete").equals("delete")) {
            delButton.setVisibility(View.GONE);
        }
        delButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mImages.size() > 1) {
                    mImages.remove(cus);
                    GlobalData.setImagesList(mImages);
                    setImageViewPager(false);
                } else {
                    mImages.remove(0);
                    GlobalData.setImagesList(mImages);
                    Intent intent = new Intent();
                    setResult(CodeDefine.IMAGE_SHOW_RESULT, intent);
                    finish();
                }
            }
        });
    }

    private void initData(){
        mImages = GlobalData.getImagesList();
    }

    private void setImageViewPager(boolean isAdd) {
        imageviewls.clear();
        iamgeViewPager.removeAllViews();

        guidviewls.clear();
        guidPview.removeAllViews();

        for (int i = 0; i < mImages.size(); i++) {
            ImageView view = new ImageView(this);
            LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            view.setLayoutParams(params);
            view.setScaleType(ImageView.ScaleType.FIT_CENTER);
            view.setImageBitmap(mImages.get(i).getBitmap());
            imageviewls.add(view);
        }

        for (int i = 0; i < mImages.size(); i++) {
            ImageView imageView = new ImageView(this);
            imageView.setLayoutParams(new LayoutParams(20, 20));
            imageView.setPadding(20, 0, 20, 0);
            guidviewls.add(imageView);
            if (i == 0) {
                // 默认选中第一张图片
                guidviewls.get(i).setBackgroundResource(R.drawable.page_indicator_focused);
            } else {
                guidviewls.get(i).setBackgroundResource(R.drawable.page_indicator);
            }
            guidPview.addView(imageView);
        }

        pageAdapter.notifyDataSetChanged();
        iamgeViewPager.setAdapter(pageAdapter);
        System.gc();
        int defse = 0;
        iamgeViewPager.setCurrentItem(defse);
        reFreshGuidView(defse);
    }

    private PagerAdapter pageAdapter = new PagerAdapter() {
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            if (position < imageviewls.size()) {
                container.removeView(imageviewls.get(position));
            }
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(imageviewls.get(position), 0);
            return imageviewls.get(position);
        }

        @Override
        public int getCount() {
            return imageviewls.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }
    };

    private ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageSelected(int arg0) {
            reFreshGuidView(arg0);
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }
    };

    private void reFreshGuidView(int arg0) {
        for (int i = 0; i < guidviewls.size(); i++) {
            guidviewls.get(i).setBackgroundResource(R.drawable.page_indicator);
        }
        guidviewls.get(arg0).setBackgroundResource(R.drawable.page_indicator_focused);
        cus = arg0;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            GlobalData.setImagesList(mImages);
            Intent intent = new Intent();
            setResult(CodeDefine.IMAGE_SHOW_RESULT, intent);
            this.finish();
        }
        return super.onKeyDown(keyCode, event);
    }

}
