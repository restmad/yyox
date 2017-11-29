package com.yyox.mvp.ui.activity;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jess.arms.base.DefaultAdapter;
import com.jess.arms.utils.UiUtils;
import com.tbruyelle.rxpermissions.RxPermissions;
import com.yyox.R;
import com.yyox.Utils.MyToast;
import com.yyox.consts.AddressType;
import com.yyox.consts.CodeDefine;
import com.yyox.consts.OrderStatus;
import com.yyox.di.component.DaggerPackageComponent;
import com.yyox.di.module.PackageModule;
import com.yyox.mvp.contract.PackageContract;
import com.yyox.mvp.model.entity.OrderDetail;
import com.yyox.mvp.model.entity.OrderPackage;
import com.yyox.mvp.model.entity.Warehouse;
import com.yyox.mvp.presenter.PackagePresenter;
import com.yyox.mvp.ui.adapter.PackageExpandableAdapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import common.AppComponent;
import common.WEActivity;

public class PackagesDetailActivity extends WEActivity<PackagePresenter> implements PackageContract.View {

    @Nullable
    @BindView(R.id.tv_activity_packages_detail_title)
    TextView mTextView_Title;

    @Nullable
    @BindView(R.id.PackagesDetailRecyclerView)
    RecyclerView mRecyclerView;

    @Nullable
    @BindView(R.id.rl_activity_packages_detail_bottom)
    RelativeLayout mRelativeLayout_Bottom;

    @Nullable
    @BindView(R.id.btn_order_detail_cancel)
    Button mCancel;
    private RxPermissions mRxPermissions;

    @Override
    protected View initView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_packages_detail, null, false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.mRxPermissions = null;
    }

    @Override
    protected void initData() {
        if (OrderStatus.ORDER_STATUS_PACKAGES == getIntent().getIntExtra("type", 5)) {
            mTextView_Title.setText("待处理");
            ArrayList<Integer> list = getIntent().getIntegerArrayListExtra("list");
            if (list.size()>1){
                mCancel.setVisibility(View.VISIBLE);
            }
        } else if (OrderStatus.ORDER_STATUS_PACKAGES_EDIT == getIntent().getIntExtra("type", 5)) {
            mTextView_Title.setText("编辑包裹");
            mRelativeLayout_Bottom.setVisibility(View.GONE);
            mCancel.setVisibility(View.GONE);
        }

        List<Integer> list = getIntent().getIntegerArrayListExtra("list");
        mPresenter.setAdapter(OrderStatus.ORDER_STATUS_PACKAGES, this);
        mPresenter.setPackagesId(list);
        mPresenter.requestPackageDetail(list);
    }

    public void btn_back_click(View v) {
        if (OrderStatus.ORDER_STATUS_PACKAGES_EDIT == getIntent().getIntExtra("type", 5)) {
            mPresenter.requestCheckDeclare();
        } else {
            this.finish();
        }
    }

    public void btn_send_click(View v) {
        mPresenter.requestCheckDeclare();
    }

    /**
     * 初始化RecycleView
     */
    private void initRecycleView() {
        configRecycleView(mRecyclerView, new GridLayoutManager(this, 1));
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

    @Override
    public void setAdapter(DefaultAdapter adapter) {
        mRecyclerView.setAdapter(adapter);
        initRecycleView();
    }

    @Override
    public void setPackageAdapter(PackageExpandableAdapter adapter) {

    }

    @Override
    public void startLoadMore() {

    }

    @Override
    public void endLoadMore() {

    }

    @Override
    public RxPermissions getRxPermissions() {
        return mRxPermissions;
    }

    @Override
    public void CheckGoodsDeclare(List<OrderPackage> orderPackages) {
        if (orderPackages != null) {
            if (OrderStatus.ORDER_STATUS_PACKAGES_EDIT == getIntent().getIntExtra("type", 5)) {
                Intent intent = new Intent();
                intent.putExtra("orderPackages", (Serializable) orderPackages);
                setResult(CodeDefine.ORDER_DETAIL_PACKAGE_RESULT, intent);
                finish();
            } else {
                OrderDetail orderDetail = new OrderDetail();
                orderDetail.setMerchandiseList(orderPackages);
                double realWeight = 0;
                for (OrderPackage item : orderPackages) {
                    realWeight += item.getWeight();
                }
                orderDetail.setRealWeight(realWeight);

                Intent intent = new Intent(PackagesDetailActivity.this, AddressActivity.class);
                intent.putExtra("package", OrderStatus.ORDER_STATUS_PACKAGES);
                intent.putExtra("type", AddressType.ADDRESS_TYPE_SELECT);
                intent.putExtra("orderdetail", (Serializable) orderDetail);
                if (getIntent().getIntExtra("box", 10) == OrderStatus.ORDER_STATUS_BOX_PACKAGE) {
                    intent.putExtra("box", OrderStatus.ORDER_STATUS_BOX_PACKAGE);
                }
                startActivityForResult(intent, CodeDefine.PACKAGES_DETAILS_REQUEST);
            }
        }
    }

    @Override
    public void ListWarehouses(List<Warehouse> warehouses) {

    }

    @Override
    public void setUIResult(int type) {

    }

    @Override
    public void dataFew() {

    }

    @Override
    public void packageImage(int status) {

    }

    @Override
    public void setImage(int i,String img,int siez,String url,int status) {

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

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        this.mRxPermissions = new RxPermissions(this);
        DaggerPackageComponent.builder().appComponent(appComponent).packageModule(new PackageModule(this)).build().inject(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CodeDefine.PACKAGES_DETAILS_REQUEST && resultCode == CodeDefine.PACKAGES_DETAILS_RESULT) {
            setResult(CodeDefine.BOX_BACKAGE_RESULT);
            this.finish();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            if (OrderStatus.ORDER_STATUS_PACKAGES_EDIT == getIntent().getIntExtra("type", 5)) {
                mPresenter.requestCheckDeclare();
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    public void btn_cancalbox_click(View view){
        setResult(CodeDefine.BOX_BACKAGE_RESULT);
        finish();
    }
}
