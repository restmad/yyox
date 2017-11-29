package com.yyox.mvp.ui.activity;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
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
import com.paginate.Paginate;
import com.tbruyelle.rxpermissions.RxPermissions;
import com.yyox.R;
import com.yyox.Utils.MyToast;
import com.yyox.consts.CodeDefine;
import com.yyox.consts.OrderStatus;
import com.yyox.di.component.DaggerPackageComponent;
import com.yyox.di.module.PackageModule;
import com.yyox.mvp.contract.PackageContract;
import com.yyox.mvp.model.entity.OrderPackage;
import com.yyox.mvp.model.entity.PackageItem;
import com.yyox.mvp.model.entity.Warehouse;
import com.yyox.mvp.presenter.PackagePresenter;
import com.yyox.mvp.ui.adapter.PackageExpandableAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import common.AppComponent;
import common.WEActivity;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import timber.log.Timber;

/**
 * 该Activity同时用于待入库列表及待处理中的合箱列表
 */
public class PackageActivity extends WEActivity<PackagePresenter> implements PackageContract.View, SwipeRefreshLayout.OnRefreshListener {

    @Nullable
    @BindView(R.id.tv_package_title)
    TextView mTextView_Title;

    @Nullable
    @BindView(R.id.PackageSwipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @Nullable
    @BindView(R.id.PackageRecyclerView)
    RecyclerView mRecyclerView;

    @Nullable
    @BindView(R.id.rl_activity_package_bottom)
    RelativeLayout mRelativeLayout_Bottom;

    @Nullable
    @BindView(R.id.package_next)
    Button mNext;

    private Paginate mPaginate;
    private boolean isLoadingMore;
    private RxPermissions mRxPermissions;

    @Override
    protected View initView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_package, null, false);
    }

    @Override
    protected void initData() {
        if (OrderStatus.ORDER_STATUS_WAITING_IN_WAREHOUSE == getIntent().getIntExtra("type", 1)) {
            mTextView_Title.setText("已预报待入库");
            mRelativeLayout_Bottom.setVisibility(View.GONE);
            mPresenter.setAdapter(getIntent().getIntExtra("type", 1), this);
            mPresenter.requestOrderWaitings(true);
            mNext.setText("下一步");
        } else if (OrderStatus.ORDER_STATUS_BOX_PACKAGE == getIntent().getIntExtra("type", 1)) {
            mTextView_Title.setText("待处理");
            mNext.setText("创建订单");
            mRelativeLayout_Bottom.setVisibility(View.VISIBLE);
            mPresenter.setAdapter(getIntent().getIntExtra("type", 1), this);
            mPresenter.requestBoxPackage(true);
        }
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
    public void CheckGoodsDeclare(List<OrderPackage> orderPackages) {

    }

    @Override
    public void ListWarehouses(List<Warehouse> warehouses) {

    }

    @Override
    public void setUIResult(int type) {
    }

    @Override
    public void dataFew() {
        mPaginate.setHasMoreDataToLoad(false);
    }

    @Override
    public void packageImage(int status) {

    }

    @Override
    public void setImage(int i,String img,int size,String url,int status) {

    }

    /**
     * 初始化Paginate,用于加载更多
     */
    private void initPaginate() {
        if (mPaginate == null) {

            Paginate.Callbacks callbacks = new Paginate.Callbacks() {
                @Override
                public void onLoadMore() {
                    if (OrderStatus.ORDER_STATUS_BOX_PACKAGE == getIntent().getIntExtra("type", 1)) {
                        mPresenter.requestBoxPackage(false);
                    } else if (OrderStatus.ORDER_STATUS_WAITING_IN_WAREHOUSE == getIntent().getIntExtra("type", 1)) {
                        mPresenter.requestOrderWaitings(false);
                    }
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

    @Override
    public void showLoading() {
        Timber.tag(TAG).w("showLoading");
        Observable.just(1)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        initPaginate();
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
        if (message.equals("包裹删除成功")) {
            mRecyclerView.removeAllViews();
        }
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

    /**
     * 初始化RecycleView
     */
    private void initRecycleView() {
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setProgressViewOffset(false, -200, -200);
        mSwipeRefreshLayout.setRefreshing(false);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CodeDefine.PACKAGE_REQUEST && resultCode == CodeDefine.PACKAGE_RESULT) {
            if (OrderStatus.ORDER_STATUS_WAITING_IN_WAREHOUSE == getIntent().getIntExtra("type", 1)) {
                mRecyclerView.removeAllViews();
                mPresenter.requestOrderWaitings(true);
            } else if (OrderStatus.ORDER_STATUS_BOX_PACKAGE == getIntent().getIntExtra("type", 1)) {
                mRecyclerView.removeAllViews();
                mPresenter.requestBoxPackage(true);
            }
        } else if (requestCode == CodeDefine.BOX_BACKAGE_REQUEST && resultCode == CodeDefine.BOX_BACKAGE_RESULT) {
            finish();
        }
    }

    public void btn_back_click(View v) {
        setResult(CodeDefine.HOME_PACKGE__RESULT);
        this.finish();
    }

    @Override
    public void onRefresh() {
        if (OrderStatus.ORDER_STATUS_BOX_PACKAGE == getIntent().getIntExtra("type", 1)) {
            mPresenter.requestBoxPackage(true);
        } else {
            mPresenter.requestOrderWaitings(true);
        }
    }

    /**
     * 判断仓库是否一致
     *
     * @param
     */
    public boolean warehouseIdEqual(List<PackageItem> packageItemList) {
        if (packageItemList.size() > 0) {
            int warehouseId1 = packageItemList.get(0).getWarehouseId();
            for (int i = 1; i < packageItemList.size(); i++) {
                int warehouseId2 = packageItemList.get(i).getWarehouseId();
                if (warehouseId1 == warehouseId2) {
                    return true;
                }
            }
        }
        return false;
    }

    public void btn_package_next_click(View v) {
        List<PackageItem> packageItemList = mPresenter.getSelectPackage();

        if (packageItemList.size() == 0) {
            showMessage("请选择包裹");
        } else if (packageItemList.size() < 2) {
            showMessage("合箱至少要选择两个包裹");
        } else if (!warehouseIdEqual(packageItemList)) {
            showMessage("选择的包裹必须在同一个仓库！");
        } else {
            ArrayList<Integer> list = new ArrayList<Integer>();
            for (int i = 0; i < packageItemList.size(); i++) {
                list.add(packageItemList.get(i).getId());
            }

            Intent intent = new Intent(this, PackagesDetailActivity.class);
            intent.putIntegerArrayListExtra("list", list);
            intent.putExtra("type", OrderStatus.ORDER_STATUS_PACKAGES);
            if (getIntent().getIntExtra("box", 10) == OrderStatus.ORDER_STATUS_BOX_PACKAGE) {
                intent.putExtra("box", OrderStatus.ORDER_STATUS_BOX_PACKAGE);
            }
            startActivityForResult(intent, CodeDefine.BOX_BACKAGE_REQUEST);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            setResult(CodeDefine.HOME_PACKGE__RESULT);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.mRxPermissions = null;
    }
}
