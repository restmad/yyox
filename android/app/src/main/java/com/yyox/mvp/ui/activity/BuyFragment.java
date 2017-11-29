package com.yyox.mvp.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.jess.arms.base.DefaultAdapter;
import com.jess.arms.utils.UiUtils;
import com.tbruyelle.rxpermissions.RxPermissions;
import com.yyox.R;
import com.yyox.Utils.MyToast;
import com.yyox.di.component.DaggerFragmentComponent;
import com.yyox.di.module.FragmentModule;
import com.yyox.mvp.contract.FragmentContract;
import com.yyox.mvp.model.entity.OrderCount;
import com.yyox.mvp.model.entity.UserDetail;
import com.yyox.mvp.presenter.FragmentPresenter;

import butterknife.BindView;
import common.AppComponent;
import common.WEFragment;

;

/**
 * Created by dadaniu on 2017-01-04.
 */

public class BuyFragment extends WEFragment<FragmentPresenter> implements FragmentContract.View, View.OnClickListener{

    /*@Nullable
    @BindView(R.id.btn_buy_edit)
    Button mButton_EditSite;*/

    @Nullable
    @BindView(R.id.siteRecyclerView)
    RecyclerView mSiteRecyclerView;

    @Nullable
    @BindView(R.id.webRecyclerView)
    RecyclerView mWebRecyclerView;
    private RxPermissions mRxPermissions;

    /*@Nullable
    @BindView(R.id.btn_buy_addsite)
    Button mButton_AddSite;*/

   /* @Nullable
    @BindView(R.id.btn_buy_addweb)
    Button mButton_AddWeb;*/

    @Override
    protected View initView() {
        return LayoutInflater.from(getContext()).inflate(R.layout.fragment_buy,null,false);
    }

    @Override
    protected void initData() {

        mPresenter.siteSetAdapter();
        mPresenter.requestSites(true,false);

        mPresenter.webSetAdapter();
        mPresenter.requestWebs(true,false);

//        mButton_EditSite.setOnClickListener(this);
//        mButton_AddSite.setOnClickListener(this);
//        mButton_AddWeb.setOnClickListener(this);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onClick(View view) {
       /* switch (view.getId()) {
            case R.id.btn_buy_edit:
                String editName = mButton_EditSite.getText().toString();
                if(editName.equals("编辑")){
                    mPresenter.requestSites(true,true);
                    mPresenter.requestWebs(true,true);
                    mButton_EditSite.setText("保存");
                }else{
                    mPresenter.requestSites(true,false);
                    mPresenter.requestWebs(true,false);
                    mButton_EditSite.setText("编辑");
                }
                break;
            case R.id.btn_buy_addsite:
                Intent intent = new Intent(this.getContext(),BuyAddSiteActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_buy_addweb:
                Intent intent1 = new Intent(this.getContext(),BuyAddSiteActivity.class);
                startActivity(intent1);
                break;
        }*/
    }

    /**
     * 初始化RecycleView
     */
    private void initRecycleView() {
        configRecycleView(mSiteRecyclerView, new GridLayoutManager(getContext(), 3));
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
    }

    /**
     * 初始化RecycleView
     */
    private void initRecycleViewWeb() {
        configRecycleView(mWebRecyclerView, new GridLayoutManager(getContext(), 3));
    }

    @Override
    public void setAdapter(DefaultAdapter adapter) {
        mSiteRecyclerView.setAdapter(adapter);
        initRecycleView();
    }

    @Override
    public void setAdapterWeb(DefaultAdapter adapter) {
        mWebRecyclerView.setAdapter(adapter);
        initRecycleViewWeb();
    }

    @Override
    public RxPermissions getRxPermissions() {
        return mRxPermissions;
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showMessage(String message) {
        MyToast.makeText(getContext(), message, 3*1000).show();
    }

    @Override
    public void launchActivity(Intent intent) {
        UiUtils.startActivity(intent);
    }

    @Override
    public void killMyself() {

    }

    @Override
    public void setUserDetail(UserDetail userDetail) {

    }

    @Override
    public void setOrderCount(OrderCount orderCount) {

    }

    @Override
    public void startLoadMore() {

    }

    @Override
    public void endLoadMore() {

    }

    @Override
    public void datadFew() {

    }

    @Override
    protected void setupFragmentComponent(AppComponent appComponent) {
        this.mRxPermissions = new RxPermissions((Activity) getContext());
        DaggerFragmentComponent.builder().appComponent(appComponent).fragmentModule(new FragmentModule(this)).build().inject(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mRxPermissions = null;
    }
}
