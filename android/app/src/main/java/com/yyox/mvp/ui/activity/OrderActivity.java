package com.yyox.mvp.ui.activity;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.jess.arms.base.DefaultAdapter;
import com.jess.arms.utils.UiUtils;
import com.paginate.Paginate;
import com.tbruyelle.rxpermissions.RxPermissions;
import com.yyox.R;
import com.yyox.Utils.MyToast;
import com.yyox.consts.CodeDefine;
import com.yyox.consts.OrderStatus;
import com.yyox.di.component.DaggerOrderComponent;
import com.yyox.di.module.OrderModule;
import com.yyox.mvp.contract.OrderContract;
import com.yyox.mvp.model.entity.Fee;
import com.yyox.mvp.model.entity.OrderDetail;
import com.yyox.mvp.model.entity.OrderHistoryItem;
import com.yyox.mvp.model.entity.PriceQuery;
import com.yyox.mvp.presenter.OrderPresenter;
import com.yyox.mvp.ui.adapter.OrderPackageDetailAdapter;
import com.yyox.mvp.ui.adapter.PackageExpandableAdapter;
import com.yyox.mvp.ui.adapter.QuestionExpandableAdapter;

import java.util.List;

import butterknife.BindView;
import common.AppComponent;
import common.WEActivity;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import timber.log.Timber;

/**
 * 该Activity同时用于待处理、待出库、已出库、清关中、国内配送、已完成列表
 */
public class OrderActivity extends WEActivity<OrderPresenter> implements OrderContract.View, SwipeRefreshLayout.OnRefreshListener {

    @Nullable
    @BindView(R.id.tv_order_title)
    TextView mTextView_Title;

    @Nullable
    @BindView(R.id.ib_activity_order_ellipsis)
    Button mImageButton_Ellipsis;

    @Nullable
    @BindView(R.id.OrderSwipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @Nullable
    @BindView(R.id.OrderRecyclerView)
    RecyclerView mRecyclerView;

    private Paginate mPaginate;
    private boolean isLoadingMore;
    private RxPermissions mRxPermissions;
    private PopupMenu mPopupMenu;
    private Menu mMenu;
    private int mType;

    @Override
    protected View initView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_order, null, false);
    }

    private void boxPopupMenuClick() {
        Intent intent = new Intent(this, PackageActivity.class);
        intent.putExtra("type", OrderStatus.ORDER_STATUS_BOX_PACKAGE);
        startActivityForResult(intent, 10);
    }

    private void initPopupMenu() {
        mPopupMenu = new PopupMenu(this, mImageButton_Ellipsis);
        mMenu = mPopupMenu.getMenu();
        // 通过XML文件添加菜单项
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.popupmenu, mMenu);

        mPopupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                  /*  case R.id.onekey:
                        showMessage("功能研发中...");
                        break;*/
                    case R.id.addbox:
                        //合箱发货
                        boxPopupMenuClick();
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
    }

    private void initControl(int type) {
        switch (type) {
            case OrderStatus.ORDER_STATUS_WAITING_DEAL_WITH:
                mTextView_Title.setText("待处理");
                mImageButton_Ellipsis.setVisibility(View.VISIBLE);
                break;
            case OrderStatus.ORDER_STATUS_WAITING_OUT_WAREHOUSE:
                mTextView_Title.setText("待出库");
                break;
            case OrderStatus.ORDER_STATUS_OUT_WAREHOUSE:
                mTextView_Title.setText("已出库");
                break;
            case OrderStatus.ORDER_STATUS_CUSTOMS:
                mTextView_Title.setText("清关中");
                break;
            case OrderStatus.ORDER_STATUS_DISPATCHING:
                mTextView_Title.setText("国内配送");
                break;
            case OrderStatus.ORDER_STATUS_COMPLETED:
                mTextView_Title.setText("已完成");
                break;
            default:
                break;
        }
    }

    @Override
    protected void initData() {
//        initPopupMenu();
        mType = getIntent().getIntExtra("type", 0);

        initControl(mType);

        mPresenter.setAdapter(mType);
        if (OrderStatus.ORDER_STATUS_WAITING_DEAL_WITH == mType) {
            mPresenter.requestOrderPending(true);
            mImageButton_Ellipsis.setVisibility(View.VISIBLE);
        } else if (OrderStatus.ORDER_STATUS_WAITING_OUT_WAREHOUSE == mType) {
            mPresenter.requestOrders(true, "1", "待出库", "false");
        } else if (OrderStatus.ORDER_STATUS_OUT_WAREHOUSE == mType) {
            mPresenter.requestOrders(true, "1", "已出库", "false");
        } else if (OrderStatus.ORDER_STATUS_CUSTOMS == mType) {
            mPresenter.requestOrders(true, "1", "清关中", "false");
        } else if (OrderStatus.ORDER_STATUS_DISPATCHING == mType) {
            mPresenter.requestOrders(true, "1", "派送中", "false");
        } else if (OrderStatus.ORDER_STATUS_COMPLETED == mType) {
            mPresenter.requestOrders(true, "1", "已完成", "false");
        }
    }

    @Override
    public void onRefresh() {
        mRecyclerView.removeAllViews();
        if (OrderStatus.ORDER_STATUS_WAITING_DEAL_WITH == mType) {
            mPresenter.requestOrderPending(true);
        } else if (OrderStatus.ORDER_STATUS_WAITING_OUT_WAREHOUSE == mType) {
            mPresenter.requestOrders(true, "1", "待出库", "false");
        } else if (OrderStatus.ORDER_STATUS_OUT_WAREHOUSE == mType) {
            mPresenter.requestOrders(true, "1", "已出库", "false");
        } else if (OrderStatus.ORDER_STATUS_CUSTOMS == mType) {
            mPresenter.requestOrders(true, "1", "清关中", "false");
        } else if (OrderStatus.ORDER_STATUS_DISPATCHING == mType) {
            mPresenter.requestOrders(true, "1", "派送中", "false");
        } else if (OrderStatus.ORDER_STATUS_COMPLETED == mType) {
            mPresenter.requestOrders(true, "1", "已完成", "false");
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

    /**
     * 初始化Paginate,用于加载更多
     */
    private void initPaginate() {
        if (mPaginate == null) {
            Paginate.Callbacks callbacks = new Paginate.Callbacks() {
                @Override
                public void onLoadMore() {
                    if (OrderStatus.ORDER_STATUS_WAITING_DEAL_WITH == mType) {
                        mPresenter.requestOrderPending(false);
                    } else if (OrderStatus.ORDER_STATUS_WAITING_OUT_WAREHOUSE == mType) {
                        mPresenter.requestOrders(false, "1", "待出库", "false");
                    } else if (OrderStatus.ORDER_STATUS_OUT_WAREHOUSE == mType) {
                        mPresenter.requestOrders(false, "1", "已出库", "false");
                    } else if (OrderStatus.ORDER_STATUS_CUSTOMS == mType) {
                        mPresenter.requestOrders(false, "1", "清关中", "false");
                    } else if (OrderStatus.ORDER_STATUS_DISPATCHING == mType) {
                        mPresenter.requestOrders(false, "1", "派送中", "false");
                    } else if (OrderStatus.ORDER_STATUS_COMPLETED == mType) {
                        mPresenter.requestOrders(false, "1", "已完成", "false");
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
    public void setUIValue(OrderDetail orderDetailJson) {

    }

    @Override
    public void setFeeValue(Fee fee) {

    }

    @Override
    public void setPayMoney(int type, double totalAmount, double balanceCny, int payStatus) {

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
        mPaginate.setHasMoreDataToLoad(false);
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
//                        mSwipeRefreshLayout.setRefreshing(false);
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
        if (message.equals("无此状态订单信息")) {
            mPaginate.setHasMoreDataToLoad(false);
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
        DaggerOrderComponent
                .builder()
                .appComponent(appComponent)
                .orderModule(new OrderModule(this))
                .build()
                .inject(this);
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

    public void btn_back_click(View v) {
        setResult(CodeDefine.HOME_ORDER__RESULT);
        this.finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            setResult(CodeDefine.HOME_ORDER__RESULT);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void btn_ellipsis_click(View v) {
//        mPopupMenu.show();
        boxPopupMenuClick();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        isLoadingMore = false;
        if (mPaginate != null) {
            mPaginate.setHasMoreDataToLoad(false);
        }
        mRecyclerView.removeAllViews();
        if (OrderStatus.ORDER_STATUS_WAITING_DEAL_WITH == mType) {
            mPresenter.requestOrderPending(true);
        } else if (OrderStatus.ORDER_STATUS_WAITING_OUT_WAREHOUSE == mType) {
            mPresenter.requestOrders(true, "1", "待出库", "false");
        } else if (OrderStatus.ORDER_STATUS_OUT_WAREHOUSE == mType) {
            mPresenter.requestOrders(true, "1", "已出库", "false");
        } else if (OrderStatus.ORDER_STATUS_CUSTOMS == mType) {
            mPresenter.requestOrders(true, "1", "清关中", "false");
        } else if (OrderStatus.ORDER_STATUS_DISPATCHING == mType) {
            mPresenter.requestOrders(true, "1", "派送中", "false");
        } else if (OrderStatus.ORDER_STATUS_COMPLETED == mType) {
            mPresenter.requestOrders(true, "1", "已完成", "false");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRxPermissions = null;
    }
}
