package com.yyox.mvp.ui.activity;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;

import com.jess.arms.utils.UiUtils;
import com.tbruyelle.rxpermissions.RxPermissions;
import com.yyox.R;
import com.yyox.Utils.MyToast;
import com.yyox.di.component.DaggerHomeComponent;
import com.yyox.di.module.HomeModule;
import com.yyox.mvp.contract.HomeContract;
import com.yyox.mvp.presenter.HomePresenter;

import java.util.Dictionary;

import butterknife.BindView;
import common.AppComponent;
import common.WEActivity;
import q.rorbin.badgeview.Badge;

public class HomeActivity extends WEActivity<HomePresenter> implements HomeContract.View, SwipeRefreshLayout.OnRefreshListener{

    @Nullable
    @BindView(R.id.tabLayout)
    TabLayout mTablayout;

    @Nullable
    @BindView(R.id.viewPager)
    ViewPager mViewPager;

    private TabLayout.Tab one;
    private TabLayout.Tab two;
    private TabLayout.Tab three;
    private TabLayout.Tab four;

    private RxPermissions mRxPermissions;

    private BuyFragment buyFragment;
    private MineFragment mineFragment;
    private OrderFragment orderFragment;
    private ServiceFragment serviceFragment;
    private Dictionary<Object, Object> mBadgeViews;
    private Badge mBadge_Pending;
    private boolean tabSelect = true;

    @Override
    protected View initView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_home, null, false);
    }

    @Override
    protected void initData() {

        orderFragment = new OrderFragment();
        buyFragment = new BuyFragment();
        serviceFragment = new ServiceFragment();
        mineFragment = new MineFragment();

        mViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            private String[] mTitles = new String[]{"运单", "海淘", "服务", "我的"};

            @Override
            public Fragment getItem(int position) {
                if (position == 1) {
                    return buyFragment;
                } else if (position == 2) {
                    return serviceFragment;
                } else if (position == 3) {
                    return mineFragment;
                }
                return orderFragment;
            }

            @Override
            public int getCount() {
                return mTitles.length;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return mTitles[position];
            }

        });

        mTablayout.setupWithViewPager(mViewPager);

        one = mTablayout.getTabAt(0);
        two = mTablayout.getTabAt(1);
        three = mTablayout.getTabAt(2);
        four = mTablayout.getTabAt(3);

        one.setIcon(getResources().getDrawable(R.mipmap.tab_order_select));
        two.setIcon(getResources().getDrawable(R.mipmap.tab_buy_normal));

        if (tabSelect) {
            three.setIcon(getResources().getDrawable(R.mipmap.tab_service_norml_bot));
        }else {
            three.setIcon(getResources().getDrawable(R.mipmap.tab_service_normal));
        }
        four.setIcon(getResources().getDrawable(R.mipmap.tab_mine_normal));
        initEvents();
    }

    private void initEvents() {
        mTablayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab == mTablayout.getTabAt(0)) {
                    orderFragment.refreshOrderCount();
                    one.setIcon(getResources().getDrawable(R.mipmap.tab_order_select));
                    mViewPager.setCurrentItem(0);
                } else if (tab == mTablayout.getTabAt(1)) {
                    two.setIcon(getResources().getDrawable(R.mipmap.tab_buy_select));
                    mViewPager.setCurrentItem(1);
                } else if (tab == mTablayout.getTabAt(2)) {
                    tabSelect = false;
                    three.setIcon(getResources().getDrawable(R.mipmap.tab_service_select));
                    mViewPager.setCurrentItem(2);
                } else if (tab == mTablayout.getTabAt(3)) {
                    four.setIcon(getResources().getDrawable(R.mipmap.tab_mine_select));
                    mViewPager.setCurrentItem(3);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                if (tab == mTablayout.getTabAt(0)) {
                    one.setIcon(getResources().getDrawable(R.mipmap.tab_order_normal));
                } else if (tab == mTablayout.getTabAt(1)) {
                    two.setIcon(getResources().getDrawable(R.mipmap.tab_buy_normal));
                } else if (tab == mTablayout.getTabAt(2)) {
                    three.setIcon(getResources().getDrawable(R.mipmap.tab_service_normal));
                } else if (tab == mTablayout.getTabAt(3)) {
                    four.setIcon(getResources().getDrawable(R.mipmap.tab_mine_normal));
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showMessage(String message) {
        MyToast.makeText(this, message, 3 * 1000).show();
    }

    @Override
    public void launchActivity(Intent intent) {
        UiUtils.startActivity(intent);
    }

    @Override
    public void killMyself() {
        finish();
    }

    public RxPermissions getmRxPermissions() {
        return mRxPermissions;
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        this.mRxPermissions = new RxPermissions(this);
        DaggerHomeComponent
                .builder()
                .appComponent(appComponent)
                .homeModule(new HomeModule(this))
                .build()
                .inject(this);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.mRxPermissions = null;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(false);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
