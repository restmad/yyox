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

import com.jess.arms.base.DefaultAdapter;
import com.jess.arms.utils.UiUtils;
import com.paginate.Paginate;
import com.tbruyelle.rxpermissions.RxPermissions;
import com.yyox.R;
import com.yyox.Utils.MyToast;
import com.yyox.Utils.NoDoubleClickUtils;
import com.yyox.consts.CodeDefine;
import com.yyox.consts.OrderStatus;
import com.yyox.di.component.DaggerOrderComponent;
import com.yyox.di.module.OrderModule;
import com.yyox.mvp.contract.OrderContract;
import com.yyox.mvp.model.entity.Channel;
import com.yyox.mvp.model.entity.Fee;
import com.yyox.mvp.model.entity.OrderDetail;
import com.yyox.mvp.model.entity.OrderHistoryItem;
import com.yyox.mvp.model.entity.OrderType;
import com.yyox.mvp.model.entity.PriceQuery;
import com.yyox.mvp.presenter.OrderPresenter;
import com.yyox.mvp.ui.adapter.OrderPackageDetailAdapter;
import com.yyox.mvp.ui.adapter.PackageExpandableAdapter;
import com.yyox.mvp.ui.adapter.QuestionExpandableAdapter;

import java.io.Serializable;
import java.util.List;

import butterknife.BindView;
import common.AppComponent;
import common.WEActivity;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import timber.log.Timber;

public class OrderChannelActivity extends WEActivity<OrderPresenter> implements OrderContract.View, SwipeRefreshLayout.OnRefreshListener {

    @Nullable
    @BindView(R.id.OrderChannelSwipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @Nullable
    @BindView(R.id.orderChannelView)
    RecyclerView mRecyclerView;

    @Nullable
    @BindView(R.id.next_or_confirm)
    Button nextOrConfim;

    private Paginate mPaginate;
    private boolean isLoadingMore;
    private RxPermissions mRxPermissions;

    @Override
    protected View initView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_order_channel, null, false);
    }

    @Override
    protected void initData() {
        OrderDetail orderDetail = (OrderDetail) getIntent().getSerializableExtra("orderdetail");
        int warehouseId = getIntent().getIntExtra("WarehouseId", 0);
        String channelcode = getIntent().getStringExtra("channelcode");
        mPresenter.setAdapter(OrderStatus.ORDER_STATUS_CHANNEL);
        mPresenter.requestChannels(true,warehouseId, orderDetail.getRealWeight(), channelcode);
        if ("order".equals(getIntent().getStringExtra("order"))) {
            nextOrConfim.setText("确定");
        }
    }

    @Override
    public void onRefresh() {
        mRecyclerView.removeAllViews();
        OrderDetail orderDetail = (OrderDetail) getIntent().getSerializableExtra("orderdetail");
        int warehouseId = getIntent().getIntExtra("WarehouseId", 0);
        String channelcode = getIntent().getStringExtra("channelcode");
        mPresenter.setAdapter(OrderStatus.ORDER_STATUS_CHANNEL);
        mPresenter.requestChannels(true,warehouseId, orderDetail.getRealWeight(), channelcode);
    }

    public void btn_back_click(View v) {
//        setResult(CodeDefine.ADDRESS_RESULT);
        this.finish();
    }

    public void btn_next_click(View v) {
        if (NoDoubleClickUtils.isDoubleClick()) {
            return;
        }
        Channel channel = mPresenter.getSelectChannel();
        if (channel == null) {
            showMessage("请选择运输服务");
        } else {
            if ("order".equals(getIntent().getStringExtra("order"))) {
                Intent intent = new Intent();
                intent.putExtra("Channel", channel);
                setResult(CodeDefine.ORDER_DETAIL_CHANNEL_RESULT, intent);
                this.finish();
            } else {
                OrderDetail orderDetail = (OrderDetail) getIntent().getSerializableExtra("orderdetail");
                orderDetail.setOrderchannel(channel);
                OrderType orderType = new OrderType();
                orderType.setCode(channel.getOrderTypeCode());
                orderDetail.setOrderType(orderType);
                Intent intent = new Intent(OrderChannelActivity.this, OrderDetailActivity.class);
                intent.putExtra("type", 1);
                intent.putExtra("orderdetail", (Serializable) orderDetail);
                intent.putExtra("package", getIntent().getIntExtra("package", 0));
                if (getIntent().getIntExtra("box", 10) == OrderStatus.ORDER_STATUS_BOX_PACKAGE) {
                    intent.putExtra("box", OrderStatus.ORDER_STATUS_BOX_PACKAGE);
                }
                startActivityForResult(intent, CodeDefine.ORDER_CHANNEL_REQUEST);
            }
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
        //initPaginate();
    }

    @Override
    public void setPackageAdapter(PackageExpandableAdapter adapter) {

    }

    @Override
    public void setOrderPackageAdapter(OrderPackageDetailAdapter adapter) {

    }

    @Override
    public void setQuestionAdapter(QuestionExpandableAdapter adapter) {

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
    public void setUIValue(OrderDetail orderDetailJson) {

    }

    @Override
    public void setFeeValue(Fee fee) {

    }

    @Override
    public void setPayMoney(int type, double totalAmount, double balanceCny, int setPayMoney) {

    }

    @Override
    public void setOrderInfo(String orderInfo) {

    }

    @Override
    public void setPayBalance(int status) {

    }

    @Override
    public void setCardCheck(boolean check, int id) {

    }

    @Override
    public void dataFew() {

    }

    @Override
    public void getData(List<PriceQuery> mPriceQuery) {

    }

    @Override
    public void priceFee(String totalCostStr) {

    }

    @Override
    public void getShare(OrderHistoryItem orderHistoryItem) {

    }

    @Override
    public void setOnClick() {

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
        DaggerOrderComponent
                .builder()
                .appComponent(appComponent)
                .orderModule(new OrderModule(this))
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
                    //mPresenter.requestChannels(false);
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
            mPaginate.setHasMoreDataToLoad(false);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.mRxPermissions = null;
        this.mPaginate = null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CodeDefine.ORDER_CHANNEL_REQUEST && resultCode == CodeDefine.ORDER_CHANNEL_RESULT) {
            setResult(CodeDefine.ADDRESS_RESULT);
            this.finish();
        }
    }

}
