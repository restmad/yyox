package com.yyox.mvp.presenter;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.View;

import com.google.gson.JsonObject;
import com.jess.arms.base.AppManager;
import com.jess.arms.base.DefaultAdapter;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.utils.RxUtils;
import com.yyox.R;
import com.yyox.consts.AddressType;
import com.yyox.consts.CodeDefine;
import com.yyox.consts.OrderStatus;
import com.yyox.mvp.contract.OrderContract;
import com.yyox.mvp.model.entity.BaseJson;
import com.yyox.mvp.model.entity.Channel;
import com.yyox.mvp.model.entity.Fee;
import com.yyox.mvp.model.entity.Order;
import com.yyox.mvp.model.entity.OrderDetail;
import com.yyox.mvp.model.entity.OrderHistoryItem;
import com.yyox.mvp.model.entity.OrderHistrory;
import com.yyox.mvp.model.entity.OrderPackage;
import com.yyox.mvp.model.entity.OrderPending;
import com.yyox.mvp.model.entity.PriceQuery;
import com.yyox.mvp.model.entity.QuestionItem;
import com.yyox.mvp.model.entity.Site;
import com.yyox.mvp.ui.activity.AddressSaveActivity;
import com.yyox.mvp.ui.activity.OrderDetailActivity;
import com.yyox.mvp.ui.activity.OrderPayActivity;
import com.yyox.mvp.ui.activity.OrderTrackingActivity;
import com.yyox.mvp.ui.activity.PackagesDetailActivity;
import com.yyox.mvp.ui.adapter.AddSiteAdapter;
import com.yyox.mvp.ui.adapter.OrderChannelAdapter;
import com.yyox.mvp.ui.adapter.OrderCompletedAdapter;
import com.yyox.mvp.ui.adapter.OrderPackageDetailAdapter;
import com.yyox.mvp.ui.adapter.OrderPendingAdapter;
import com.yyox.mvp.ui.adapter.OrderTrackingAdapter;
import com.yyox.mvp.ui.adapter.PackageExpandableAdapter;
import com.yyox.mvp.ui.adapter.QuestionExpandableAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;
import me.jessyan.rxerrorhandler.handler.RetryWithDelay;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

/**
 * Created by dadaniu on 2017-02-03.
 */
@ActivityScope
public class OrderPresenter extends BasePresenter<OrderContract.Model, OrderContract.View> {

    private RxErrorHandler mErrorHandler;
    private AppManager mAppManager;
    private Application mApplication;
    private List<OrderPending> mOrderPendings = new ArrayList<>();  //待处理数据源
    private List<Order> mOrders = new ArrayList<>();//待出库、已出库、清关中、国内配送、已完成数据源
    private List<OrderHistrory> mOrderHistorys = new ArrayList<>();
    private List<OrderPackage> mOrderPackageList = new ArrayList<>();
    private List<QuestionItem> mQuestionList = new ArrayList<>();
    private List<Channel> mChannels = new ArrayList<>();
    private List<Site> mSites = new ArrayList<>();
    private DefaultAdapter mAdapter;
    private PackageExpandableAdapter mPackageAdapter;
    private QuestionExpandableAdapter mQuestionAdapter;
    private OrderPackageDetailAdapter mOrderPackageAdapter;
    private int lastUserId = 1;
    public static final int PER_PAGE = 10;
    private int pageNo = 1;
    private List<PriceQuery> mPriceQuery = new ArrayList<>();

    @Inject
    public OrderPresenter(OrderContract.Model model, OrderContract.View rootView, RxErrorHandler handler, AppManager appManager, Application application) {
        super(model, rootView);
        this.mApplication = application;
        this.mErrorHandler = handler;
        this.mAppManager = appManager;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mAdapter = null;
        this.mChannels = null;
        this.mOrderHistorys = null;
        this.mOrders = null;
        this.mErrorHandler = null;
        this.mAppManager = null;
        this.mApplication = null;
    }

    /**
     * 设置待处理Adapter
     */
    private void setAdapter_DealWith() {
        mAdapter = new OrderPendingAdapter(mOrderPendings);
        mRootView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new DefaultAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, Object data, int position) {
                OrderPending orderPending = mOrderPendings.get(position);
                if (view.getId() == R.id.tv_item_order_pending_order_list_operate_status || view.getId() == R.id.tv_item_order_pending_box_list_operate_status) {
                    if (orderPending.getStatus() == 2) {
                        //待支付
                        Intent intent = new Intent(mAppManager.getCurrentActivity(), OrderDetailActivity.class);
                        intent.putExtra("type", orderPending.getType());
                        intent.putExtra("status", orderPending.getStatus());
                        intent.putExtra("orderNo", orderPending.getNo());
                        mAppManager.startActivity(intent);
                    } else if (orderPending.getStatus() == 3) {
                        //待上传身份证
                        if (orderPending.getAddressId() > 0) {
                            Intent intent = new Intent(mAppManager.getCurrentActivity(), AddressSaveActivity.class);
                            intent.putExtra("type", AddressType.ADDRESS_TYPE_UPLOAD);
                            intent.putExtra("addressid", orderPending.getAddressId());
                            mAppManager.getCurrentActivity().startActivityForResult(intent, CodeDefine.ADDRESS_SAVE_REQUEST);
                        } else {
                            mRootView.showMessage("数据异常");
                        }
                    } else if (orderPending.getStatus() == 4) {
                        //待缴税金
                        Intent intent = new Intent(mAppManager.getCurrentActivity(), OrderPayActivity.class);
                        intent.putExtra("orderNo", orderPending.getNo());
                        intent.putExtra("payType", "TAXREPAY");
                        intent.putExtra("payTypeComments", "支付税金");
                        mAppManager.getCurrentActivity().startActivityForResult(intent, 3);
                    }
                } else {
                    if (orderPending.getType() == 1) {
                        //包裹
                        ArrayList<Integer> list = new ArrayList<Integer>();
                        list.add(orderPending.getInventoryId());
                        Intent intent = new Intent(mAppManager.getCurrentActivity(), PackagesDetailActivity.class);
                        intent.putIntegerArrayListExtra("list", list);
                        mAppManager.getCurrentActivity().startActivityForResult(intent, 2);
                    } else {
                        //订单/合箱
                        Intent intent = new Intent(mAppManager.getCurrentActivity(), OrderDetailActivity.class);
                        intent.putExtra("type", orderPending.getType());
                        intent.putExtra("status", orderPending.getStatus());
                        intent.putExtra("orderNo", orderPending.getNo());
                        mAppManager.getCurrentActivity().startActivityForResult(intent, 1);
                    }
                }
            }
        });
    }

    /**
     * 设置运输服务Adapter
     */
    private void setAdapter_Channel() {
        mAdapter = new OrderChannelAdapter(mChannels);
        mRootView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new DefaultAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, Object data, int position) {
                if (view.getId() == R.id.cb_item_order_channel_list_select) {
                    for (Channel channel : mChannels) {
                        channel.setChecked(false);
                    }
                    mChannels.get(position).setChecked(true);
                    mAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    /**
     * 设置待出库、已出库、清关中、国内配送、已完成Adapter
     */
    private void setAdapter_Order() {
        mAdapter = new OrderCompletedAdapter(mOrders);
        mRootView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new DefaultAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, Object data, int position) {
                Order order = mOrders.get(position);
                Intent objIntent = mAppManager.getCurrentActivity().getIntent();
                Intent intent = new Intent(mAppManager.getCurrentActivity(), OrderTrackingActivity.class);
                if (order.getType() == 3) {
                    intent.putExtra("orderTitle", "合箱发货");
                } else {
                    intent.putExtra("orderTitle", order.getInventorybasic().get(0).getNickname());
                }
                intent.putExtra("orderNo", order.getOrderNo());
                intent.putExtra("type", objIntent.getIntExtra("type", 0));
                mAppManager.getCurrentActivity().startActivityForResult(intent, 1);
            }
        });
    }

    /**
     * 设置订单追踪Adapter
     */
    private void setAdapter_Tracking() {
        mAdapter = new OrderTrackingAdapter(mOrderHistorys);
        mRootView.setAdapter(mAdapter);
    }

    /**
     * 设置订单详情Adapter
     */
    private void setAdapter_Detail() {
        mPackageAdapter = new PackageExpandableAdapter(mApplication.getApplicationContext(), mOrderPackageList);
        mRootView.setPackageAdapter(mPackageAdapter);
    }

    public void setAdapter_Submit(Context context, int type) {
        mPackageAdapter = new PackageExpandableAdapter(context, mOrderPackageList, type);
        mRootView.setPackageAdapter(mPackageAdapter);
    }

    public void setAdapter_package_editor(Context context, List<OrderPackage> orderPackages, int type) {
        mPackageAdapter = new PackageExpandableAdapter(context, orderPackages, type);
        mRootView.setPackageAdapter(mPackageAdapter);
    }

    public void setAdapter_AddSite() {
        mAdapter = new AddSiteAdapter(mSites);
        mRootView.setAdapter(mAdapter);
    }

    public void setAdapter_Question(Context context) {
        mQuestionAdapter = new QuestionExpandableAdapter(context, mQuestionList);
        mRootView.setQuestionAdapter(mQuestionAdapter);
    }

    public void setAdapter(int type) {
        switch (type) {
            case OrderStatus.ORDER_STATUS_WAITING_DEAL_WITH:
                setAdapter_DealWith();
                break;
            case OrderStatus.ORDER_STATUS_CHANNEL:
                setAdapter_Channel();
                break;
            case OrderStatus.ORDER_STATUS_WAITING_OUT_WAREHOUSE:
            case OrderStatus.ORDER_STATUS_OUT_WAREHOUSE:
            case OrderStatus.ORDER_STATUS_CUSTOMS:
            case OrderStatus.ORDER_STATUS_DISPATCHING:
            case OrderStatus.ORDER_STATUS_COMPLETED:
                setAdapter_Order();
                break;
            case OrderStatus.ORDER_STATUS_TRACKING:
                setAdapter_Tracking();
                break;
            case OrderStatus.ORDER_STATUS_DETAIL:
                setAdapter_Detail();
                break;
            case OrderStatus.ORDER_STATUS_PACKAGE_SUBMIT:
                //setAdapter_Submit();
                break;
            case OrderStatus.ORDER_STATUS_PACKAGES:
                break;
        }
    }

    /**
     * 设置包裹详情的Adapter
     */
    public void setAdapter_Packages_Detail(Context context) {

        mOrderPackageAdapter = new OrderPackageDetailAdapter(context, mOrderPackageList);
        mRootView.setOrderPackageAdapter(mOrderPackageAdapter);
    }

    /**
     * 请求待处理列表
     *
     * @param pullToRefresh
     */
    public void requestOrderPending(final boolean pullToRefresh) {
        if (pullToRefresh) {
            pageNo = 1;
            mOrderPendings.clear();
            mAdapter.notifyDataSetChanged();
        }
        mModel.getOrderPendings(pageNo, PER_PAGE)
                .subscribeOn(Schedulers.io())
                .retryWhen(new RetryWithDelay(3, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        if (pullToRefresh)
                            mRootView.showLoading();//显示上拉刷新的进度条
                        else
                            mRootView.startLoadMore();
                    }
                }).subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doAfterTerminate(new Action0() {
                    @Override
                    public void call() {
                        if (pullToRefresh)
                            mRootView.hideLoading();//隐藏上拉刷新的进度条
                        else
                            mRootView.endLoadMore();
                    }
                })
                .compose(RxUtils.<BaseJson<List<OrderPending>>>bindToLifecycle(mRootView))//使用RXlifecycle,使subscription和activity一起销毁
                .subscribe(new ErrorHandleSubscriber<BaseJson<List<OrderPending>>>(mErrorHandler) {
                    @Override
                    public void onNext(BaseJson<List<OrderPending>> listBaseJson) {
                        if (listBaseJson.getStatus() == 0) {
                            int iTotalCount = listBaseJson.getOtherJson().getTotalCount();
                            if (pageNo == 1) {
                                mOrderPendings.clear();
                            }
                            if (mOrderPendings.size() < iTotalCount) {
                                if (pullToRefresh)
                                    mOrderPendings.clear();//如果是下拉刷新则清空列表
                                List<OrderPending> orders = listBaseJson.getData();
                                for (OrderPending order : orders) {
                                    mOrderPendings.add(order);
                                }
                                mAdapter.notifyDataSetChanged();//通知更新数据
                                pageNo++;
                            } else {
//                                mRootView.showMessage("没有此状态信息");
                                mRootView.dataFew();
                            }
                        } else {
                            mRootView.dataFew();
                            mRootView.showMessage(listBaseJson.getMsgs());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        mRootView.dataFew();
                    }
                });
    }

    /**
     * 请求待待出库、已出库、清关中、国内配送、已完成列表
     *
     * @param pullToRefresh
     * @param pageNo
     * @param statusCodeC
     * @param showCancleOrder
     */
    public void requestOrders(final boolean pullToRefresh, String pageNo, String statusCodeC, String showCancleOrder) {

        if (pullToRefresh) {
            lastUserId = 1;
            mOrders.clear();
            mAdapter.notifyDataSetChanged();
        }
        mModel.getOrders(String.valueOf(lastUserId), statusCodeC, showCancleOrder, PER_PAGE)
                .subscribeOn(Schedulers.io())
                .retryWhen(new RetryWithDelay(1, 500))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        if (pullToRefresh)
                            mRootView.showLoading();//显示上拉刷新的进度条
                        else
                            mRootView.startLoadMore();
                    }
                }).subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doAfterTerminate(new Action0() {
                    @Override
                    public void call() {
                        if (pullToRefresh)
                            mRootView.hideLoading();//隐藏上拉刷新的进度条
                        else
                            mRootView.endLoadMore();
                    }
                })
                .compose(RxUtils.<BaseJson<List<Order>>>bindToLifecycle(mRootView))//使用RXlifecycle,使subscription和activity一起销毁
                .subscribe(new ErrorHandleSubscriber<BaseJson<List<Order>>>(mErrorHandler) {
                    @Override
                    public void onNext(BaseJson<List<Order>> listBaseJson) {
                        //lastUserId = addresses.get(addresses.size() - 1).getId();//记录最后一个id,用于下一次请求
                        if (listBaseJson.getStatus() == 0) {
                            int iTotalCount = listBaseJson.getOtherJson().getTotalCount();
                            if (mOrders.size() < iTotalCount) {
                                if (pullToRefresh)
                                    mOrders.clear();//如果是下拉刷新则清空列表
                                List<Order> orders = listBaseJson.getData();
                                for (Order order : orders) {
                                    mOrders.add(order);
                                }
                                mAdapter.notifyDataSetChanged();//通知更新数据
                                lastUserId++;
                            } else {
                                if (iTotalCount == 0){
//                                    mRootView.showMessage("没有此状态信息");
                                }
                                mRootView.dataFew();
                            }
                        } else {
                            mRootView.dataFew();
                            mRootView.showMessage(listBaseJson.getMsgs());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        mRootView.dataFew();
                    }
                });
    }

    public void requestOrderTracking(final boolean pullToRefresh, String orderNo) {

        if (pullToRefresh) lastUserId = 1;

        mModel.orderTracking(orderNo)
                .subscribeOn(Schedulers.io())
                .retryWhen(new RetryWithDelay(3, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxUtils.<BaseJson<OrderHistoryItem>>bindToLifecycle(mRootView))//使用RXlifecycle,使subscription和activity一起销毁
                .subscribe(new ErrorHandleSubscriber<BaseJson<OrderHistoryItem>>(mErrorHandler) {
                    @Override
                    public void onNext(BaseJson<OrderHistoryItem> listBaseJson) {
                        //lastUserId = addresses.get(addresses.size() - 1).getId();//记录最后一个id,用于下一次请求
                        if (pullToRefresh) mOrderHistorys.clear();//如果是上拉刷新则晴空列表
                        OrderHistoryItem orderHistoryItem = listBaseJson.getData();
                        List<OrderHistrory> orderHistrories = orderHistoryItem.getList();
                        for (OrderHistrory user : orderHistrories) {
                            mOrderHistorys.add(user);
                        }
                        String start = orderHistoryItem.getSearchNo();
                        String end = orderHistoryItem.getSearchNo();
                        String status = orderHistoryItem.getSearchNo();
                        mRootView.getShare(orderHistoryItem);
                        mAdapter.notifyDataSetChanged();//通知更新数据
                    }
                });

    }

    public void requestOrderDetail(final boolean pullToRefresh, String orderNo, int type, OrderDetail orderDetail) {

        if (type == 1) {
            for (OrderPackage item : orderDetail.getMerchandiseList()) {
                mOrderPackageList.add(item);
            }
            mRootView.setUIValue(orderDetail);
            mPackageAdapter.notifyDataSetChanged();
        } else {

            if (pullToRefresh) lastUserId = 1;
            mModel.getOrderDetail(orderNo)
                    .subscribeOn(Schedulers.io())
                    .retryWhen(new RetryWithDelay(3, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .compose(RxUtils.<BaseJson<OrderDetail>>bindToLifecycle(mRootView))//使用RXlifecycle,使subscription和activity一起销毁
                    .subscribe(new ErrorHandleSubscriber<BaseJson<OrderDetail>>(mErrorHandler) {
                        @Override
                        public void onNext(BaseJson<OrderDetail> listBaseJson) {
                            if (pullToRefresh) mOrderPackageList.clear();
                            OrderDetail orderHistoryJson = listBaseJson.getData();
                            List<OrderPackage> orderHistroryListJsons = orderHistoryJson.getMerchandiseList();
                            for (OrderPackage user : orderHistroryListJsons) {

                                String nickname = user.getNickname();
                                user.setNickname("" + nickname);
                                mOrderPackageList.add(user);
                            }
                            mRootView.setUIValue(orderHistoryJson);
                            mPackageAdapter.notifyDataSetChanged();
                        }
                    });
        }
    }

    public Channel getSelectChannel() {
        Channel channel = null;
        for (Channel item : mChannels) {
            if (item.isChecked()) {
                channel = item;
                break;
            }
        }
        return channel;
    }

    public void requestChannels(boolean pullToRefresh,int warehouseId, double weight, String channelcode) {
        if (pullToRefresh) {
            mChannels.clear();
        }
        //if (pullToRefresh) lastUserId = 1;
        mModel.getChannels(warehouseId, weight)
                .subscribeOn(Schedulers.io())
                .retryWhen(new RetryWithDelay(3, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        if (pullToRefresh)
                            mRootView.showLoading();//显示上拉刷新的进度条
                        else
                            mRootView.startLoadMore();
                    }
                }).subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doAfterTerminate(new Action0() {
                    @Override
                    public void call() {
                        if (pullToRefresh)
                            mRootView.hideLoading();//隐藏上拉刷新的进度条
                        else
                            mRootView.endLoadMore();
                    }

                })
                .compose(RxUtils.<BaseJson<List<Channel>>>bindToLifecycle(mRootView))//使用RXlifecycle,使subscription和activity一起销毁
                .subscribe(new ErrorHandleSubscriber<BaseJson<List<Channel>>>(mErrorHandler) {
                    @Override
                    public void onNext(BaseJson<List<Channel>> listBaseJson) {
                        if (listBaseJson.getStatus() == 0) {
                            int iIndex = 0;
                            for (Channel channel : listBaseJson.getData()) {
                                if (!channelcode.isEmpty()) {
                                    if (channel.getCode().equals(channelcode)) {
                                        channel.setChecked(true);
                                    } else {
                                        channel.setChecked(false);
                                    }
                                } else {
                                    if (iIndex == 0) {
                                        channel.setChecked(true);
                                    } else {
                                        channel.setChecked(false);
                                    }
                                }
                                mChannels.add(channel);
                                iIndex++;
                            }
                            mAdapter.notifyDataSetChanged();//通知更新数据
                        } else {
                            mRootView.showMessage(listBaseJson.getMsgs());
                        }
                    }
                });
    }

    public void requestCalcuteFee(List<Integer> inventorys, int warehouseId, int numberInventory, double estimatedWeight, String ordertype, String couponCode, int ordertypeId, int leadId, String orderNo) {

        mModel.calculateFee(inventorys, warehouseId, numberInventory, estimatedWeight, ordertype, couponCode, ordertypeId, leadId, orderNo)
                .subscribeOn(Schedulers.io()) //指定 subscribe() 发生在 IO 线程
                .retryWhen(new RetryWithDelay(3, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread()) //指定 Subscriber 的回调发生在主线程
                .compose(RxUtils.<BaseJson<Fee>>bindToLifecycle(mRootView))//使用RXlifecycle,使subscription和activity一起销毁
                .subscribe(new ErrorHandleSubscriber<BaseJson<Fee>>(mErrorHandler) {
                    @Override
                    public void onNext(BaseJson<Fee> listBaseJson) {
                        if (listBaseJson.getStatus() == 0) {
                            mRootView.setFeeValue(listBaseJson.getData());
                        } else {
                            mRootView.showMessage(listBaseJson.getMsgs());
                        }
                    }
                });

    }

    public void requestCommitOrder(List<Integer> inventoryIds, String orderType, int customerAddressId, double money, double weight, List<String> list, String couponcode, String orderNo) {

        mModel.commitOrder(inventoryIds, orderType, customerAddressId, money, weight, list, couponcode, orderNo)
                .subscribeOn(Schedulers.io())
//                .retryWhen(new RetryWithDelay(3, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxUtils.<BaseJson<JsonObject>>bindToLifecycle(mRootView))//使用RXlifecycle,使subscription和activity一起销毁
                .subscribe(new ErrorHandleSubscriber<BaseJson<JsonObject>>(mErrorHandler) {
                    @Override
                    public void onNext(BaseJson<JsonObject> listBaseJson) {
                        if (listBaseJson.getStatus() == 0) {
                            mRootView.showMessage("提交成功");
                            JsonObject ojbData = listBaseJson.getData();
                            Intent intent = new Intent(mAppManager.getCurrentActivity(), OrderPayActivity.class);
                            intent.putExtra("orderNo", ojbData.get("orderNo").getAsString());
                            intent.putExtra("payType", ojbData.get("payType").getAsString());
                            if (mAppManager.getCurrentActivity().getIntent().getIntExtra("type", 10) == OrderStatus.ORDER_STATUS_BOX_PACKAGE) {
                                intent.putExtra("type", OrderStatus.ORDER_STATUS_BOX_PACKAGE);
                            }
                            intent.putExtra("payTypeComments", ojbData.get("payTypeComments").getAsString());
                            mAppManager.getCurrentActivity().startActivityForResult(intent, CodeDefine.ORDER_DETAIL_REQUEST);
                        } else {
                            mRootView.showMessage(listBaseJson.getMsgs());
                            mRootView.setOnClick();
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                    }
                });
    }

    public void requsetpayOrder(String orderld, String comments, double price, String payTypeBackSide) {

        mModel.getPayOder(orderld, comments, price, payTypeBackSide)
                .subscribeOn(Schedulers.io())
                .retryWhen(new RetryWithDelay(3, 2))
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxUtils.<BaseJson<String>>bindToLifecycle(mRootView))
                .subscribe(new ErrorHandleSubscriber<BaseJson<String>>(mErrorHandler) {
                               @Override
                               public void onNext(BaseJson<String> listBaseJson) {
                                   if (listBaseJson.getStatus() == 0) {
                                       mRootView.setOrderInfo(listBaseJson.getData());
                                   } else {
                                       mRootView.showMessage(listBaseJson.getMsgs());
                                   }
                               }
                           }
                );
    }

    public void requsetPayBalance(String orderNo, String payType) {

        mModel.payBalance(orderNo, payType)
                .subscribeOn(Schedulers.io())
                .retryWhen(new RetryWithDelay(3, 2))
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxUtils.<BaseJson<String>>bindToLifecycle(mRootView))
                .subscribe(new ErrorHandleSubscriber<BaseJson<String>>(mErrorHandler) {
                               @Override
                               public void onNext(BaseJson<String> listBaseJson) {
                                   if (listBaseJson.getStatus() == 0) {
                                       mRootView.setPayBalance(listBaseJson.getStatus());
                                   } else {
                                       mRootView.showMessage(listBaseJson.getMsgs());
                                   }
                               }
                           }
                );

    }

    public void requestCancelBox(String orderNo) {

        mModel.cancelBox(orderNo)
                .subscribeOn(Schedulers.io())
                .retryWhen(new RetryWithDelay(3, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxUtils.<BaseJson<String>>bindToLifecycle(mRootView))//使用RXlifecycle,使subscription和activity一起销毁
                .subscribe(new ErrorHandleSubscriber<BaseJson<String>>(mErrorHandler) {
                    @Override
                    public void onNext(BaseJson<String> listBaseJson) {
                        if (listBaseJson.getStatus() == 0) {
                            mRootView.showMessage("取消成功");
                            mAppManager.getCurrentActivity().finish();
                        } else {
                            mRootView.showMessage(listBaseJson.getMsgs());
                        }
                    }
                });

    }


    public void payVerifyResult(int type, String orderNo, Map<String, Object> partMap) {
        mModel.payVerifyResult(orderNo, partMap)
                .subscribeOn(Schedulers.io())
                .retryWhen(new RetryWithDelay(3, 2))
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxUtils.<BaseJson<JsonObject>>bindToLifecycle(mRootView))
                .subscribe(new ErrorHandleSubscriber<BaseJson<JsonObject>>(mErrorHandler) {
                               @Override
                               public void onNext(BaseJson<JsonObject> listBaseJson) {
                                   if (listBaseJson.getStatus() == 0) {
                                       JsonObject ojbData = listBaseJson.getData();
                                       double totalAmount = Double.valueOf(ojbData.get("totalAmount").toString());
                                       double balanceCny = Double.valueOf(ojbData.get("balanceCny").toString());
                                       int payStatus = ojbData.get("payStatus").getAsInt();
                                       mRootView.setPayMoney(type, totalAmount, balanceCny, payStatus);
                                   } else {
                                       mRootView.showMessage(listBaseJson.getMsgs());
                                   }
                               }
                           }
                );
    }

    public void requsetCardCheck(String orderNo) {

        mModel.cardCheck(orderNo)
                .subscribeOn(Schedulers.io())
                .retryWhen(new RetryWithDelay(3, 2))
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxUtils.<BaseJson<JsonObject>>bindToLifecycle(mRootView))
                .subscribe(new ErrorHandleSubscriber<BaseJson<JsonObject>>(mErrorHandler) {
                               @Override
                               public void onNext(BaseJson<JsonObject> listBaseJson) {
                                   if (listBaseJson.getStatus() == 0) {
                                       JsonObject ojbData = listBaseJson.getData();
                                       boolean checkCard = ojbData.get("checkCard").getAsBoolean();
                                       if (checkCard) {
                                           int addressId = ojbData.get("addressId").getAsInt();
                                           mRootView.setCardCheck(checkCard, addressId);
                                       } else {
                                           mRootView.setCardCheck(checkCard, 0);
                                       }
                                   } else {
                                       mRootView.showMessage(listBaseJson.getMsgs());
                                   }
                               }
                           }
                );

    }

    /**
     * 获取订单详情的包裹详情
     *
     * @param orderNo
     */

    public void requestOrderPackageDetail(String orderNo) {
        mModel.getOrderDetail(orderNo)
                .subscribeOn(Schedulers.io())
                .retryWhen(new RetryWithDelay(3, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxUtils.<BaseJson<OrderDetail>>bindToLifecycle(mRootView))//使用RXlifecycle,使subscription和activity一起销毁
                .subscribe(new ErrorHandleSubscriber<BaseJson<OrderDetail>>(mErrorHandler) {
                    @Override
                    public void onNext(BaseJson<OrderDetail> listBaseJson) {
                        OrderDetail orderHistoryJson = listBaseJson.getData();
                        String orderNo1 = orderHistoryJson.getOrderNo();
                        List<OrderPackage> orderHistroryListJsons = orderHistoryJson.getMerchandiseList();
                        for (OrderPackage orderPackage : orderHistroryListJsons) {
                                    if(orderPackage.getOrderSreenshot().size() > 0){
                                        mModel.extractPicture(orderPackage.getOrderSreenshot().get(0))
                                                .subscribeOn(Schedulers.io())
                                                .retryWhen(new RetryWithDelay(1, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                                                .subscribeOn(AndroidSchedulers.mainThread())
                                                .observeOn(AndroidSchedulers.mainThread())
                                                .compose(RxUtils.<BaseJson<String>>bindToLifecycle(mRootView))//使用RXlifecycle,使subscription和activity一起销毁
                                                .subscribe(new ErrorHandleSubscriber<BaseJson<String>>(mErrorHandler) {
                                                    @Override
                                                    public void onNext(BaseJson<String> jsonObjectBaseJson) {
                                                        if (jsonObjectBaseJson.getStatus() == 0) {
                                                            byte[] decodedString = Base64.decode(jsonObjectBaseJson.getData(), Base64.DEFAULT);
                                                            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                                                            //Bitmap smallBitmap = Bitmap.createScaledBitmap(decodedByte,120,120,true);
                                                            orderPackage.setBitmap1(decodedByte);
                                                            mOrderPackageAdapter.notifyDataSetChanged();//通知更新数据
                                                        } else {
                                                            //mRootView.showMessage(jsonObjectBaseJson.getMsgs());
                                                        }
                                                    }
                                                });
                                    }
                                    if(orderPackage.getOrderSreenshot().size() > 1){
                                        mModel.extractPicture(orderPackage.getOrderSreenshot().get(1))
                                                .subscribeOn(Schedulers.io())
                                                .retryWhen(new RetryWithDelay(1, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                                                .subscribeOn(AndroidSchedulers.mainThread())
                                                .observeOn(AndroidSchedulers.mainThread())
                                                .compose(RxUtils.<BaseJson<String>>bindToLifecycle(mRootView))//使用RXlifecycle,使subscription和activity一起销毁
                                                .subscribe(new ErrorHandleSubscriber<BaseJson<String>>(mErrorHandler) {
                                                    @Override
                                                    public void onNext(BaseJson<String> jsonObjectBaseJson) {
                                                        if (jsonObjectBaseJson.getStatus() == 0) {
                                                            byte[] decodedString = Base64.decode(jsonObjectBaseJson.getData(), Base64.DEFAULT);
                                                            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                                                            //Bitmap smallBitmap = Bitmap.createScaledBitmap(decodedByte,120,120,true);
                                                            orderPackage.setBitmap2(decodedByte);
                                                            mOrderPackageAdapter.notifyDataSetChanged();//通知更新数据
                                                        } else {
                                                            //mRootView.showMessage(jsonObjectBaseJson.getMsgs());
                                                        }
                                                    }
                                                });
                                    }
                                    if(orderPackage.getOrderSreenshot().size() > 2){
                                        mModel.extractPicture(orderPackage.getOrderSreenshot().get(2))
                                                .subscribeOn(Schedulers.io())
                                                .retryWhen(new RetryWithDelay(1, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                                                .subscribeOn(AndroidSchedulers.mainThread())
                                                .observeOn(AndroidSchedulers.mainThread())
                                                .compose(RxUtils.<BaseJson<String>>bindToLifecycle(mRootView))//使用RXlifecycle,使subscription和activity一起销毁
                                                .subscribe(new ErrorHandleSubscriber<BaseJson<String>>(mErrorHandler) {
                                                    @Override
                                                    public void onNext(BaseJson<String> jsonObjectBaseJson) {
                                                        if (jsonObjectBaseJson.getStatus() == 0) {
                                                            byte[] decodedString = Base64.decode(jsonObjectBaseJson.getData(), Base64.DEFAULT);
                                                            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                                                            //Bitmap smallBitmap = Bitmap.createScaledBitmap(decodedByte,120,120,true);
                                                            orderPackage.setBitmap3(decodedByte);
                                                            mOrderPackageAdapter.notifyDataSetChanged();//通知更新数据
                                                        } else {
                                                            //mRootView.showMessage(jsonObjectBaseJson.getMsgs());
                                                        }
                                                    }
                                                });
                                    }

                                orderPackage.setOrderNo(orderNo1);
                            mOrderPackageList.add(orderPackage);
                        }
                        mRootView.setUIValue(orderHistoryJson);

                        mOrderPackageAdapter.notifyDataSetChanged();
                    }

                });
    }


    public void requestAddSite(final boolean pullToRefresh) {

        mModel.getSites()
                .subscribeOn(Schedulers.io())
                .retryWhen(new RetryWithDelay(3, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                    }
                }).subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doAfterTerminate(new Action0() {
                    @Override
                    public void call() {
                    }
                })
                .compose(RxUtils.<BaseJson<List<Site>>>bindToLifecycle(mRootView))//使用RXlifecycle,使subscription和activity一起销毁
                .subscribe(new ErrorHandleSubscriber<BaseJson<List<Site>>>(mErrorHandler) {
                    @Override
                    public void onNext(BaseJson<List<Site>> listBaseJson) {
                        if (listBaseJson.getStatus() == 0) {
                            List<Site> sites = listBaseJson.getData();
                            for (Site site : sites) {
                                mSites.add(site);
                            }
                            mAdapter.notifyDataSetChanged();//通知更新数据
                        } else {
                            mRootView.showMessage(listBaseJson.getMsgs());
                        }
                    }
                });
    }

    public void requestQuestion(final boolean pullToRefresh, String value) {

        if (pullToRefresh) lastUserId = 1;
        mModel.getQuestionItems(value)
                .subscribeOn(Schedulers.io())
                .retryWhen(new RetryWithDelay(3, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxUtils.<List<QuestionItem>>bindToLifecycle(mRootView))//使用RXlifecycle,使subscription和activity一起销毁
                .subscribe(new ErrorHandleSubscriber<List<QuestionItem>>(mErrorHandler) {
                    @Override
                    public void onNext(List<QuestionItem> questionItemList) {
                        if (pullToRefresh) mOrderPackageList.clear();
                        for (QuestionItem questionItem : questionItemList) {
                            mQuestionList.add(questionItem);
                        }
                        mQuestionAdapter.notifyDataSetChanged();
                    }
                });
    }

    /**
     * 计算价格界面获取厂库和对应地址
     */
    public void requesPriceQuery() {
        mModel.PriceQuery()
                .subscribeOn(Schedulers.io())
                .retryWhen(new RetryWithDelay(3, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxUtils.<BaseJson<List<PriceQuery>>>bindToLifecycle(mRootView))//使用RXlifecycle,使subscription和activity一起销毁
                .subscribe(new ErrorHandleSubscriber<BaseJson<List<PriceQuery>>>(mErrorHandler) {
                    @Override
                    public void onNext(BaseJson<List<PriceQuery>> listBaseJson) {
                        for (PriceQuery priceQuery : listBaseJson.getData()) {
                            mPriceQuery.add(priceQuery);
                        }
                        mRootView.getData(mPriceQuery);
                    }
                });
    }

    public void requestCalculateFeeTool(String orderType, double estimatedWeight, int warehouseId, int ordertypeId, int leadId) {
        mModel.calculateFeeTool(orderType, estimatedWeight, warehouseId, ordertypeId, leadId)
                .subscribeOn(Schedulers.io())
                .retryWhen(new RetryWithDelay(3, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxUtils.<BaseJson<JsonObject>>bindToLifecycle(mRootView))//使用RXlifecycle,使subscription和activity一起销毁
                .subscribe(new ErrorHandleSubscriber<BaseJson<JsonObject>>(mErrorHandler) {
                    @Override
                    public void onNext(BaseJson<JsonObject> jsonObjectBaseJson) {
                        if (jsonObjectBaseJson.getStatus() == 0) {
                            JsonObject data = jsonObjectBaseJson.getData();
                            String totalCostStr = data.get("shippingCostStr").getAsString();
                            mRootView.priceFee(totalCostStr);
                        } else {
                            mRootView.showMessage(jsonObjectBaseJson.getMsgs());
                        }
                    }
                });
    }

}
