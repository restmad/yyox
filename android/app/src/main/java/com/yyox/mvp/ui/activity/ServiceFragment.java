package com.yyox.mvp.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.jess.arms.base.DefaultAdapter;
import com.jess.arms.utils.UiUtils;
import com.kf5.sdk.system.init.KF5SDKInitializer;
import com.paginate.Paginate;
import com.tbruyelle.rxpermissions.RxPermissions;
import com.yyox.R;
import com.yyox.Utils.MyToast;
import com.yyox.di.component.DaggerFragmentComponent;
import com.yyox.di.module.FragmentModule;
import com.yyox.mvp.contract.FragmentContract;
import com.yyox.mvp.model.entity.OrderCount;
import com.yyox.mvp.model.entity.UserDetail;
import com.yyox.mvp.presenter.FragmentPresenter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import butterknife.BindView;
import common.AppComponent;
import common.WEFragment;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import timber.log.Timber;

/**
 * A simple {@link Fragment} subclass.
 */
public class ServiceFragment extends WEFragment<FragmentPresenter> implements FragmentContract.View {

    @Nullable
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    private RxPermissions mRxPermissions;
    private boolean isLoadingMore;
    private Paginate mPaginate;


    @Override
    protected View initView() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_service, null, false);
        KF5SDKInitializer.init(this.getContext());
        return view;
    }

    @Override
    protected void initData() {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            BufferedReader bf = new BufferedReader(new InputStreamReader(this.getContext().getAssets().open("answerList.json")));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        mPresenter.serviceAdapter(getActivity());
        mPresenter.requestQuestions(true, stringBuilder.toString());

    }

    @Override
    public void setAdapter(DefaultAdapter adapter) {
        mRecyclerView.setAdapter(adapter);
        initRecycleView();
        initPaginate();
    }

    @Override
    public void setAdapterWeb(DefaultAdapter adapter) {

    }

    private void initPaginate() {
        if (mPaginate == null) {
            Paginate.Callbacks callbacks = new Paginate.Callbacks() {
                @Override
                public void onLoadMore() {
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

            mPaginate = Paginate.with(mRecyclerView, callbacks)
                    .setLoadingTriggerThreshold(0)
                    .build();
        }
    }

    private void initRecycleView() {
        configRecycleView(mRecyclerView, new GridLayoutManager(getContext(), 1));
    }

    private void configRecycleView(RecyclerView recyclerView, GridLayoutManager layoutManager) {
        recyclerView.setLayoutManager(layoutManager);
        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    public RxPermissions getRxPermissions() {
        return mRxPermissions;
    }

    @Override
    public void setUserDetail(UserDetail userDetail) {

    }

    @Override
    public void setOrderCount(OrderCount orderCount) {

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
    public void datadFew() {
        mPaginate.setHasMoreDataToLoad(false);
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
    }

    @Override
    public void showMessage(String message) {
        MyToast.makeText(getContext(), message, 3 * 1000).show();
    }

    @Override
    public void launchActivity(Intent intent) {
        UiUtils.startActivity(intent);
    }

    @Override
    public void killMyself() {

    }

    @Override
    protected void setupFragmentComponent(AppComponent appComponent) {
        this.mRxPermissions = new RxPermissions((Activity) getContext());
        DaggerFragmentComponent.builder().appComponent(appComponent).fragmentModule(new FragmentModule(this)).build().inject(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mRxPermissions = null;
    }
}
