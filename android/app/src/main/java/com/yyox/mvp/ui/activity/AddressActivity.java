package com.yyox.mvp.ui.activity;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.yyox.di.component.DaggerAddressComponent;
import com.yyox.di.module.AddressModule;
import com.yyox.mvp.contract.AddressContract;
import com.yyox.mvp.model.entity.Address;
import com.yyox.mvp.model.entity.OrderDetail;
import com.yyox.mvp.model.entity.Region;
import com.yyox.mvp.presenter.AddressPresenter;

import java.io.Serializable;
import java.util.List;

import butterknife.BindView;
import common.AppComponent;
import common.WEActivity;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import timber.log.Timber;

public class AddressActivity extends WEActivity<AddressPresenter> implements AddressContract.View, SwipeRefreshLayout.OnRefreshListener {

    @Nullable
    @BindView(R.id.tv_activity_address_title)
    TextView mTextView_Title;

    @Nullable
    @BindView(R.id.AddressRecyclerView)
    RecyclerView mRecyclerView;

    @Nullable
    @BindView(R.id.AddressSwipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @Nullable
    @BindView(R.id.rl_activity_address_bottom)
    RelativeLayout mRelativeLayout_Bottom;

    @Nullable
    @BindView(R.id.btn_activity_address_bottom)
    Button mButton_Next;

    private boolean isLoadingMore;
    private RxPermissions mRxPermissions;

    public void btn_back_click(View v) {
        this.finish();
    }

    public void btn_address_add_click(View v) {
        Intent intent = new Intent(AddressActivity.this, AddressSaveActivity.class);
        intent.putExtra("type", AddressType.ADDRESS_TYPE_ADD);
        startActivityForResult(intent, CodeDefine.ADDRESS_SAVE_REQUEST);
    }

    public void btn_address_next_click(View v) {
        Address address = mPresenter.getSelectAddress();
        if (address == null) {
            showMessage("请选择地址");
        } else {
            if (AddressType.ADDRESS_TYPE_SELECT == getIntent().getIntExtra("type", 0)) {
                OrderDetail orderDetail = (OrderDetail) getIntent().getSerializableExtra("orderdetail");
                orderDetail.setAddress(address);
                Intent intent = new Intent(AddressActivity.this, OrderChannelActivity.class);
                intent.putExtra("orderdetail", (Serializable) orderDetail);
                intent.putExtra("channelcode", "");
                intent.putExtra("package", getIntent().getIntExtra("package", 0));
                if (getIntent().getIntExtra("box", 10) == OrderStatus.ORDER_STATUS_BOX_PACKAGE) {
                    intent.putExtra("box", OrderStatus.ORDER_STATUS_BOX_PACKAGE);
                }
                intent.putExtra("WarehouseId", orderDetail.getMerchandiseList().get(0).getWarehouseId());
                startActivityForResult(intent, CodeDefine.ADDRESS_REQUEST);
            } else if (AddressType.ADDRESS_TYPE_OREDER == getIntent().getIntExtra("type", 0)) {
                Intent intent = new Intent();
                intent.putExtra("addess", address);
                setResult(CodeDefine.ORDER_DETAIL_ADDRESS_RESULT, intent);
                this.finish();
            }
        }
    }

    @Override
    protected View initView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_address, null, false);
    }

    @Override
    protected void initData() {
        if (AddressType.ADDRESS_TYPE_LIST == getIntent().getIntExtra("type", 0)) {
            mTextView_Title.setText("地址管理");
            mRelativeLayout_Bottom.setVisibility(View.GONE);
            mPresenter.setAdapter(0);
            mPresenter.requestAddress(true);
        } else if (AddressType.ADDRESS_TYPE_SELECT == getIntent().getIntExtra("type", 0)) {
            mTextView_Title.setText("选择地址");
            mRelativeLayout_Bottom.setVisibility(View.VISIBLE);
            mPresenter.setAdapter(1);
            mPresenter.requestAddress(true);
        } else if (getIntent().getIntExtra("ID", 0) == AddressType.ADDRESS_TYPE_UPLOAD) {
            Intent intent = new Intent(this, AddressSaveActivity.class);
            intent.putExtra("type", AddressType.ADDRESS_TYPE_DEIT_PAYS);
            intent.putExtra("addressid", getIntent().getIntExtra("addressid", 0));
            startActivityForResult(intent, 850);
        } else if (AddressType.ADDRESS_TYPE_OREDER == getIntent().getIntExtra("type", 0)) {
            mTextView_Title.setText("选择地址");
            mRelativeLayout_Bottom.setVisibility(View.VISIBLE);
            mButton_Next.setText("确定");
            mPresenter.setAddressId(getIntent().getIntExtra("addressid", 0));
            mPresenter.setAdapter(1);
            mPresenter.requestSelectAddress(true);
        }
    }

    @Override
    public void onRefresh() {
        if (AddressType.ADDRESS_TYPE_OREDER == getIntent().getIntExtra("type", 0)) {
            mPresenter.requestSelectAddress(true);
        } else {
            mPresenter.requestAddress(true);
        }
    }

    /**
     * 初始化RecycleView
     */
    private void initRecycleView() {
        mSwipeRefreshLayout.setOnRefreshListener(this);
        configRecycleView(mRecyclerView, new GridLayoutManager(this, 1));
    }

    /**
     * 配置recycleview
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
    public void setRegions(int type, List<Region> regionList) {
    }

    @Override
    public void deleteTempPicture() {
    }

    @Override
    public void refreshAddress() {

    }

    @Override
    public void setAddressDetail(Address address) {

    }

    @Override
    public void setImage(int i, String data) {

    }

    @Override
    public void showLoading() {
        Timber.tag(TAG).w("showLoading");
        Observable.just(1)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        mSwipeRefreshLayout.setRefreshing(true);
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
        DaggerAddressComponent.builder().appComponent(appComponent).addressModule(new AddressModule(this)).build().inject(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.mRxPermissions = null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CodeDefine.ADDRESS_SAVE_REQUEST && resultCode == CodeDefine.ADDRESS_SAVE_RESULT) {
            mPresenter.requestAddress(true);
        } else if (requestCode == CodeDefine.ADDRESS_REQUEST && resultCode == CodeDefine.ADDRESS_RESULT) {
            setResult(CodeDefine.PACKAGES_DETAILS_RESULT);
            this.finish();
        } else if (requestCode == 850 && resultCode == CodeDefine.ADDRESS_SAVE_RESULT) {
            setResult(CodeDefine.ADDRESS_CARD_RESULT);
            finish();
        } else if (requestCode == 850 && resultCode == CodeDefine.ADDRESS_ORDER_RESULT) {
            finish();
        }
    }

}
