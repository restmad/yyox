package com.yyox.mvp.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.jess.arms.base.DefaultAdapter;
import com.jess.arms.utils.UiUtils;
import com.paginate.Paginate;
import com.tbruyelle.rxpermissions.RxPermissions;
import com.yyox.BuildConfig;
import com.yyox.R;
import com.yyox.Utils.MyToast;
import com.yyox.Utils.Net.NetEvevt;
import com.yyox.Utils.Net.NetUtil;
import com.yyox.consts.CodeDefine;
import com.yyox.consts.HomeMessage;
import com.yyox.consts.OrderStatus;
import com.yyox.di.component.DaggerFragmentComponent;
import com.yyox.di.module.FragmentModule;
import com.yyox.mvp.contract.FragmentContract;
import com.yyox.mvp.model.entity.Message;
import com.yyox.mvp.model.entity.OrderCount;
import com.yyox.mvp.model.entity.UserDetail;
import com.yyox.mvp.model.realm.RealmUser;
import com.yyox.mvp.presenter.FragmentPresenter;

import java.util.List;

import butterknife.BindView;
import common.AppComponent;
import common.WEApplication;
import common.WEFragment;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import timber.log.Timber;

/**
 * A simple {@link Fragment} subclass.
 */
public class OrderFragment extends WEFragment<FragmentPresenter> implements FragmentContract.View, View.OnClickListener, SwipeRefreshLayout.OnRefreshListener ,NetEvevt{


    @Nullable
    @BindView(R.id.OrderSwipeRefreshLayout1)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @Nullable
    @BindView(R.id.recyclerView1)
    RecyclerView mRecyclerView;

    @Nullable
    @BindView(R.id.net)
    LinearLayout mNet;
    private WEApplication weApplication;
    private RxPermissions mRxPermissions;
    private boolean isLoadingMore;
    private DefaultAdapter mAdapter;
    private Paginate mPaginate;
    private List<Message> mMessages;
    public static NetEvevt evevt;

    private void login() {
        new AlertDialog.Builder(getActivity()).setTitle("提示")
                .setMessage("您还未登录,请先登录")
                .setPositiveButton("下次",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //取消
                            }
                        })
                .setNegativeButton("登录",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //删除订单
                                Intent intentLogin = new Intent(getActivity(), UserLoginActivity.class);
                                startActivityForResult(intentLogin, CodeDefine.LOGIN_REQUEST);
                            }
                        }).show();
    }

    @Override
    protected View initView() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_order, null, false);
        return view;
    }

    /**
     * 初始化时判断有没有网络
     */

    public void inspectNet() {
        int netMobile = NetUtil.getNetWorkState(getContext());
        if (netMobile == 1) {
            mNet.setVisibility(View.GONE);
        } else if (netMobile == 0) {
            mNet.setVisibility(View.GONE);
        } else if (netMobile == -1) {
            mNet.setVisibility(View.VISIBLE);
        }
    }

    public void refreshOrderCount() {
        //刷新订单数量
        if (isLogin()) {
            if (mRecyclerView != null) {
                mRecyclerView.removeAllViews();
            }
            mPresenter.requestMessagess(true);
        }
    }

    @Override
    protected void initData() {
        evevt = this;
        inspectNet();
        if (!MyToast.checkPermission(getContext())) {
            MyToast.applyPermission(getContext());
        }
        mPresenter.messageSetAdapter();
        if (isLogin()) {
            mRecyclerView.removeAllViews();
            mPresenter.requestMessagess(true);
        }
    }

    private boolean isLogin() {
        try {
            weApplication = (WEApplication) getActivity().getApplication();
            RealmUser realmUser = weApplication.getRealmUser();
            if (realmUser != null && (realmUser.getName() != "" || realmUser.getEmail() != null)) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
//            showMessage("login error");
            return false;
        }
    }

    @Override
    public void setAdapter(DefaultAdapter adapter) {
        mAdapter = adapter;
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new DefaultAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, Object data, int position) {
                if (isLogin()) {
                    mMessages = mPresenter.getMesage();
                    if (position != 0) {
                        Message message = mMessages.get(position);
                        String state = message.getState();
                        if (state.equals(HomeMessage.HOME_LIST_ORDER)) {
                            //跳转运单相关界面
                            Intent intent = new Intent(WEApplication.getContext(), OrderTrackingActivity.class);
                            intent.putExtra("orderTitle", message.getOrderStatus());
                            intent.putExtra("orderNo", message.getOtherNo());
                            String messageOrderStatus = message.getOrderStatus();
                            if (!messageOrderStatus.isEmpty()) {
                                if (messageOrderStatus.equals("待出库")) {
                                    intent.putExtra("type", OrderStatus.ORDER_STATUS_WAITING_OUT_WAREHOUSE);
                                    startActivity(intent);
                                } else if (messageOrderStatus.equals("已出库")) {
                                    intent.putExtra("type", OrderStatus.ORDER_STATUS_OUT_WAREHOUSE);
                                    startActivity(intent);
                                } else if (messageOrderStatus.equals("清关中")) {
                                    intent.putExtra("type", OrderStatus.ORDER_STATUS_CUSTOMS);
                                    startActivity(intent);
                                } else if (messageOrderStatus.equals("派送中")) {
                                    intent.putExtra("type", OrderStatus.ORDER_STATUS_DISPATCHING);
                                    startActivity(intent);
                                } else if (messageOrderStatus.equals("已完成")) {
                                    intent.putExtra("type", OrderStatus.ORDER_STATUS_COMPLETED);
                                    startActivity(intent);
                                }
                            }
                        }
                        if (state.equals(HomeMessage.HOME_LIST_TRANSPORT) || state.equals(HomeMessage.HOME_LIST_COUPON)) {
                            //跳转消费界面
                            Intent intent = new Intent(WEApplication.getContext(), UserRecordActivity.class);
                            startActivity(intent);
                        }
                    }
                }

                switch (view.getId()) {
                    case R.id.btn_order_report:
                        if (isLogin()) {
                            Intent intent = new Intent(WEApplication.getContext(), PackageSaveActivity.class);
                            intent.putExtra("type", OrderStatus.ORDER_STATUS_REPORT);
                            startActivityForResult(intent, CodeDefine.PACKAGE_REQUEST);
                        } else {
                            login();
                        }
                        break;
                    case R.id.btn_order_pending:
                        if (isLogin()) {
                            Intent intent2 = new Intent(view.getContext(), OrderActivity.class);
                            intent2.putExtra("type", OrderStatus.ORDER_STATUS_WAITING_DEAL_WITH);
                            startActivityForResult(intent2, CodeDefine.HOME_ORDER_REQUEST);
                        } else {
                            login();
                        }
                        break;
                    case R.id.btn_order_completed:
                        if (isLogin()) {
                            /*
                            int finish = mMessages.get(0).getInitCustomer().getFinish();
                            if (finish>0){
                                SharedPreferences sharedPreferences =  WEApplication.getContext().getSharedPreferences("userEmail", Context.MODE_PRIVATE);
                                String email = sharedPreferences.getString("Email", "");
                                SharedPreferences sharedPreferencess =  WEApplication.getContext().getSharedPreferences(email, Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferencess.edit();
                                editor.putInt("number",Integer.valueOf(finish));
                                editor.commit();
                            }
                            */
                            Intent intent3 = new Intent(WEApplication.getContext(), OrderActivity.class);
                            intent3.putExtra("type", OrderStatus.ORDER_STATUS_COMPLETED);
                            startActivityForResult(intent3, CodeDefine.HOME_finish_REQUEST);
                        } else {
                            login();
                        }
                        break;
                    case R.id.btn_order_status1:
                        if (isLogin()) {
                            Intent intentOrderStatus1 = new Intent(WEApplication.getContext(), PackageActivity.class);
                            intentOrderStatus1.putExtra("type", OrderStatus.ORDER_STATUS_WAITING_IN_WAREHOUSE);
                            startActivityForResult(intentOrderStatus1, CodeDefine.HOME_PACKGE_REQUEST);
                        } else {
                            login();
                        }
                        break;
                    case R.id.btn_order_status2:
                        if (isLogin()) {
                            Intent intentOrderStatus2 = new Intent(WEApplication.getContext(), OrderActivity.class);
                            intentOrderStatus2.putExtra("type", OrderStatus.ORDER_STATUS_WAITING_OUT_WAREHOUSE);
                            startActivity(intentOrderStatus2);
                        } else {
                            login();
                        }
                        break;
                    case R.id.btn_order_status3:
                        if (isLogin()) {
                            Intent intentOrderStatus3 = new Intent(WEApplication.getContext(), OrderActivity.class);
                            intentOrderStatus3.putExtra("type", OrderStatus.ORDER_STATUS_OUT_WAREHOUSE);
                            startActivity(intentOrderStatus3);
                        } else {
                            login();
                        }
                        break;
                    case R.id.btn_order_status4:
                        if (isLogin()) {
                            Intent intentOrderStatus4 = new Intent(WEApplication.getContext(), OrderActivity.class);
                            intentOrderStatus4.putExtra("type", OrderStatus.ORDER_STATUS_CUSTOMS);
                            startActivity(intentOrderStatus4);
                        } else {
                            login();
                        }
                        break;
                    case R.id.btn_order_status5:
                        if (isLogin()) {
                            Intent intentOrderStatus5 = new Intent(WEApplication.getContext(), OrderActivity.class);
                            intentOrderStatus5.putExtra("type", OrderStatus.ORDER_STATUS_DISPATCHING);
                            startActivity(intentOrderStatus5);
                        } else {
                            login();
                        }
                        break;
                }
            }

        });
        initRecycleView();
        if (isLogin()) {
            mPaginate = null;
            initPaginate();
        }
    }

    @Override
    public void setAdapterWeb(DefaultAdapter adapter) {

    }

    private void initPaginate() {
        if (mPaginate == null) {
            Paginate.Callbacks callbacks = new Paginate.Callbacks() {
                @Override
                public void onLoadMore() {
                    mPresenter.requestMessagess(false);
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
        evevt = this;
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setProgressViewOffset(false, -200, -200);
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
        if(mPaginate != null) {
            mPaginate.setHasMoreDataToLoad(false);
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
//                        mSwipeRefreshLayout.setRefreshing(true);

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
        MyToast.makeText(WEApplication.getContext(), message, 3 * 1000).show();

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
        if (requestCode == CodeDefine.LOGIN_REQUEST && resultCode == CodeDefine.LOGIN_RESULT) {
            if (BuildConfig.LOG_DEBUG) {
                weApplication = (WEApplication) getActivity().getApplication();
                RealmUser realmUser = weApplication.getRealmUser();
                if (realmUser != null && (data.getStringExtra("username") != "" || data.getStringExtra("useremail") != null)) {
                    mRecyclerView.removeAllViews();
                    mPresenter.requestMessagess(true);
                }
            } else if (data.getStringExtra("username") != "") {
                mRecyclerView.removeAllViews();
                mPresenter.requestMessagess(true);
            } else if (data.getStringExtra("useremail") != "") {
                mRecyclerView.removeAllViews();
                mPresenter.requestMessagess(true);
            }

        } else if (requestCode == CodeDefine.PACKAGE_REQUEST && resultCode == CodeDefine.PACKAGE_RESULT) {
            mRecyclerView.removeAllViews();
            mPresenter.requestMessagess(true);
        } else if (requestCode == CodeDefine.HOME_ORDER_REQUEST && resultCode == CodeDefine.HOME_ORDER__RESULT) {
            mRecyclerView.removeAllViews();
            mPresenter.requestMessagess(true);
        } else if (requestCode == CodeDefine.HOME_PACKGE_REQUEST && resultCode == CodeDefine.HOME_PACKGE__RESULT) {
            mRecyclerView.removeAllViews();
            mPresenter.requestMessagess(true);
        } else if (requestCode == CodeDefine.HOME_finish_REQUEST && resultCode == CodeDefine.HOME_PACKGE__RESULT) {
            mRecyclerView.removeAllViews();
            mPresenter.requestMessagess(true);
        }
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mRxPermissions = null;
        this.mPaginate = null;
    }

    @Override
    public void onRefresh() {
        if (isLogin()) {
            mRecyclerView.removeAllViews();
            mPresenter.requestMessagess(true);
        }
    }
    @Override
    public void onNetChange(int netMobile) {
        if (mNet != null) {
            if (netMobile == 1) {
                mNet.setVisibility(View.GONE);
            } else if (netMobile == 0) {
                mNet.setVisibility(View.GONE);
            } else if (netMobile == -1) {
                mNet.setVisibility(View.VISIBLE);
            }
        }
    }
}
