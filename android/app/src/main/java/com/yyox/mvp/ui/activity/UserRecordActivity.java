package com.yyox.mvp.ui.activity;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.google.gson.JsonObject;
import com.jess.arms.base.DefaultAdapter;
import com.jess.arms.utils.UiUtils;
import com.paginate.Paginate;
import com.tbruyelle.rxpermissions.RxPermissions;
import com.yyox.R;
import com.yyox.Utils.MyToast;
import com.yyox.di.component.DaggerUserComponent;
import com.yyox.di.module.UserModule;
import com.yyox.mvp.contract.UserContract;
import com.yyox.mvp.model.entity.CouponItem;
import com.yyox.mvp.model.entity.User;
import com.yyox.mvp.presenter.UserPresenter;

import butterknife.BindView;
import common.AppComponent;
import common.WEActivity;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import timber.log.Timber;

public class UserRecordActivity extends WEActivity<UserPresenter> implements UserContract.View, SwipeRefreshLayout.OnRefreshListener {

    @Nullable
    @BindView(R.id.recordView)
    RecyclerView mRecordRecyclerView;
    @Nullable
    @BindView(R.id.RecordSwipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    private Paginate mPaginate;
    private boolean isLoadingMore;
    private RxPermissions mRxPermissions;

    public void btn_back_click(View v) {
        this.finish();
    }

    @Override
    protected View initView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_user_record, null, false);
    }

    @Override
    protected void initData() {
        mPresenter.setAdapter(1);
        mPresenter.requestRecords(true);
    }

    @Override
    public void onRefresh() {
        mRecordRecyclerView.removeAllViews();
        mPresenter.requestRecords(true);
    }

    @Override
    public void setAdapter(DefaultAdapter adapter) {
        mRecordRecyclerView.setAdapter(adapter);
        initRecycleView();
        initPaginate();
    }

    @Override
    public void startLoadMore() {
        isLoadingMore = true;
    }

    @Override
    public void endLoadMore() {
        isLoadingMore = false;
    }

    @Override
    public RxPermissions getRxPermissions() {
        return mRxPermissions;
    }

    @Override
    public void UserResult(JsonObject jsonObject) {

    }

    @Override
    public void setUserVip(String member, String expiryDate, String balanceCny) {

    }

    @Override
    public void aliPayReturn(String data) {

    }

    @Override
    public void getCouponItem(CouponItem couponItem) {

    }

    @Override
    public void datadFew() {
        mPaginate.setHasMoreDataToLoad(false);
    }

    @Override
    public void SetUserInfo(User user) {

    }

    @Override
    public void alterPassword() {

    }

    @Override
    public void showLoading() {
        Timber.tag(TAG).w("showLoading");
        Observable.just(1)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                    }
                });
    }

    @Override
    public void hideLoading() {
        Timber.tag(TAG).w("hideLoading");
        mSwipeRefreshLayout.setRefreshing(false);
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

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        this.mRxPermissions = new RxPermissions(this);
        DaggerUserComponent
                .builder()
                .appComponent(appComponent)
                .userModule(new UserModule(this))
                .build()
                .inject(this);
    }

    /**
     * 初始化Paginate,用于加载更多
     */
    private void initPaginate() {
        if (mPaginate == null) {
            Paginate.Callbacks callbacks = new Paginate.Callbacks() {
                @Override
                public void onLoadMore() {
                    mPresenter.requestRecords(false);
                }

                @Override
                public boolean isLoading() {
                    return isLoadingMore;
                }

                @Override
                public boolean hasLoadedAllItems() {
                    return false;
                }
            };

            mPaginate = Paginate.with(mRecordRecyclerView, callbacks)
                    .setLoadingTriggerThreshold(0)
                    .build();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.mRxPermissions = null;
        this.mPaginate = null;
    }

    /**
     * 初始化RecycleView
     */
    private void initRecycleView() {
        mSwipeRefreshLayout.setOnRefreshListener(this);
        configRecycleView(mRecordRecyclerView, new GridLayoutManager(this, 1));
    }

    /**
     * 配置recycleview
     *
     * @param recyclerView
     * @param layoutManager
     */
    private void configRecycleView(RecyclerView recyclerView, RecyclerView.LayoutManager layoutManager) {
        recyclerView.setLayoutManager(layoutManager);
        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        mSwipeRefreshLayout.setProgressViewOffset(false, -200, -200);
        mSwipeRefreshLayout.setRefreshing(false);
    }

}
