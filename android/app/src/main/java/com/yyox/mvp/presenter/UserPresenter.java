package com.yyox.mvp.presenter;

import android.app.Activity;
import android.app.Application;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.View;

import com.google.gson.JsonObject;
import com.jess.arms.base.AppManager;
import com.jess.arms.base.DefaultAdapter;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.utils.PermissionUtil;
import com.jess.arms.utils.RxUtils;
import com.yyox.R;
import com.yyox.Utils.DateUtils;
import com.yyox.consts.CodeDefine;
import com.yyox.mvp.contract.UserContract;
import com.yyox.mvp.model.entity.BaseJson;
import com.yyox.mvp.model.entity.CouponItem;
import com.yyox.mvp.model.entity.Record;
import com.yyox.mvp.model.entity.User;
import com.yyox.mvp.model.entity.WarehousJson;
import com.yyox.mvp.model.entity.WarehouseList;
import com.yyox.mvp.ui.adapter.CouponAdapter;
import com.yyox.mvp.ui.adapter.RecordAdapter;
import com.yyox.mvp.ui.adapter.WarehouseAdapter;

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
 * Created by dadaniu on 2017-02-08.
 */
@ActivityScope
public class UserPresenter extends BasePresenter<UserContract.Model, UserContract.View> {

    private RxErrorHandler mErrorHandler;
    private AppManager mAppManager;
    private Application mApplication;
    private List<CouponItem> mCouponsList = new ArrayList<>();
    private List<Record> mRecordList = new ArrayList<>();
    private DefaultAdapter mAdapter;
    private boolean mCode;
    private int pageNo;
    private List<WarehouseList> mWarehouseList = new ArrayList<>();

    @Inject
    public UserPresenter(UserContract.Model model, UserContract.View rootView, RxErrorHandler handler, AppManager appManager, Application application) {
        super(model, rootView);
        this.mApplication = application;
        this.mErrorHandler = handler;
        this.mAppManager = appManager;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mAdapter = null;
        this.mCouponsList = null;
        this.mErrorHandler = null;
        this.mAppManager = null;
        this.mApplication = null;
    }

    private void setAdapter_Coupon() {
        mAdapter = new CouponAdapter(mCouponsList);
        mRootView.setAdapter(mAdapter);//设置Adapter
        mAdapter.setOnItemClickListener(new DefaultAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, Object data, int position) {
                if (mCouponsList.get(position).getUsable() && mCouponsList.get(position).getStatus().equals("CAN_USE")) {
                    mRootView.getCouponItem(mCouponsList.get(position));
                }
            }
        });
    }

    public void setAdapter(int type) {
        switch (type) {
            case 0:
                setAdapter_Coupon();
                break;
            case 1:
                setAdapter_Record();
                break;
            case 2:
                setAdapter_warehouse();
        }
    }

    /**
     * 仓库列表设置数据
     */
    private void setAdapter_warehouse() {
        mAdapter = new WarehouseAdapter(mWarehouseList);
        mRootView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new DefaultAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, Object data, int position) {
                if (view.getId() == R.id.copy) {
                    //获取剪贴板管理器：
                    ClipboardManager cm = (ClipboardManager) mAppManager.getCurrentActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                    // 创建普通字符型ClipData
                    ClipData mClipData = ClipData.newPlainText("Label", mWarehouseList.get(position).getName() + "\n" + mWarehouseList.get(position).getValue());
                    // 将ClipData内容放到系统剪贴板里。
                    cm.setPrimaryClip(mClipData);
                    mRootView.showMessage("复制成功");
                }
            }
        });
    }

    private void setAdapter_Record() {
        mAdapter = new RecordAdapter(mRecordList);
        mRootView.setAdapter(mAdapter);
    }

    public void requestAuthCode(String mobile) {
        //请求外部存储权限用于适配android6.0的权限管理机制
        PermissionUtil.externalStorage(new PermissionUtil.RequestPermission() {
            @Override
            public void onRequestPermissionSuccess() {
                //request permission success, do something.
            }
        }, mRootView.getRxPermissions(), mRootView, mErrorHandler);

        mModel.authCode(mobile)
                .subscribeOn(Schedulers.io())
                .retryWhen(new RetryWithDelay(3, 2))
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxUtils.<BaseJson<String>>bindToLifecycle(mRootView))
                .subscribe(new ErrorHandleSubscriber<BaseJson<String>>(mErrorHandler) {
                               @Override
                               public void onNext(BaseJson<String> stringBaseJson) {
                                   if (stringBaseJson.getStatus() == 0) {
                                       mRootView.showMessage(stringBaseJson.getMsgs());
                                       if ( stringBaseJson.getData() != null) {
                                           mRootView.showMessage("您的短信验证码为：" + stringBaseJson.getData().toString());
                                       }
                                   } else {
                                       mRootView.showMessage(stringBaseJson.getMsgs());
                                   }
                               }

                               @Override
                               public void onError(Throwable e) {
                                   super.onError(e);
                               }
                           }
                );
    }

    public void requestRegister(String name, String email, String pwd, String code, String invite) {
        mModel.register(name, email, pwd, code, invite)
                .subscribeOn(Schedulers.io())
                .retryWhen(new RetryWithDelay(3, 2))
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxUtils.<BaseJson<JsonObject>>bindToLifecycle(mRootView))
                .subscribe(new ErrorHandleSubscriber<BaseJson<JsonObject>>(mErrorHandler) {
                               @Override
                               public void onNext(BaseJson<JsonObject> jsonObjectBaseJson) {
                                   if (jsonObjectBaseJson.getStatus() == 0) {

                                       mRootView.UserResult(jsonObjectBaseJson.getData());
                                   } else {
                                       mRootView.showMessage(jsonObjectBaseJson.getMsgs());
                                   }
                               }

                               @Override
                               public void onError(Throwable e) {
                                   super.onError(e);
                               }
                           }
                );
    }

    public void requestLogin(String name, String pwd) {
        mModel.login(name, pwd)
                .subscribeOn(Schedulers.io())
                .retryWhen(new RetryWithDelay(3, 2))
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxUtils.<BaseJson<JsonObject>>bindToLifecycle(mRootView))
                .subscribe(new ErrorHandleSubscriber<BaseJson<JsonObject>>(mErrorHandler) {
                               @Override
                               public void onNext(BaseJson<JsonObject> jsonObjectBaseJson) {
                                   if (jsonObjectBaseJson.getStatus() == 0) {
                                       mRootView.UserResult(jsonObjectBaseJson.getData());
                                   } else {
                                       mRootView.showMessage(jsonObjectBaseJson.getMsgs());
                                   }
                               }
                           }
                );
    }

    public void requestLogout(Activity activity) {
        mModel.logout()
                .subscribeOn(Schedulers.io())
                .retryWhen(new RetryWithDelay(3, 2))
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxUtils.<BaseJson<String>>bindToLifecycle(mRootView))
                .subscribe(new ErrorHandleSubscriber<BaseJson<String>>(mErrorHandler) {
                               @Override
                               public void onNext(BaseJson<String> jsonObjectBaseJson) {
                                   if (jsonObjectBaseJson.getStatus() == 0) {
//                                       mRootView.showMessage(jsonObjectBaseJson.getMsgs());
                                       activity.finish();
                                   } else {
                                       mRootView.showMessage(jsonObjectBaseJson.getMsgs());
                                   }
                               }
                           }
                );
    }

    public void requestForget(String phone, String pwd, String code) {
        mModel.forget(phone, pwd, code)
                .subscribeOn(Schedulers.io())
                .retryWhen(new RetryWithDelay(3, 2))
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxUtils.<BaseJson<String>>bindToLifecycle(mRootView))
                .subscribe(new ErrorHandleSubscriber<BaseJson<String>>(mErrorHandler) {
                               @Override
                               public void onNext(BaseJson<String> jsonObjectBaseJson) {
                                   if (jsonObjectBaseJson.getStatus() == 0) {
                                       mRootView.UserResult(null);
                                   } else {
                                       mRootView.showMessage(jsonObjectBaseJson.getMsgs());
                                   }
                               }
                           }
                );
    }

    public void requestCoupons(final boolean pullToRefresh, String money, int warehouseRange, int ordertypeRange) {

        mModel.getCoupons(money, warehouseRange, ordertypeRange)
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
                .compose(RxUtils.<BaseJson<List<CouponItem>>>bindToLifecycle(mRootView))//使用RXlifecycle,使subscription和activity一起销毁
                .subscribe(new ErrorHandleSubscriber<BaseJson<List<CouponItem>>>(mErrorHandler) {
                    @Override
                    public void onNext(BaseJson<List<CouponItem>> listBaseJson) {
                        if (pullToRefresh)
                            mCouponsList.clear();//如果是上拉刷新则晴空列表
                        List<CouponItem> couponItems = listBaseJson.getData();
                        if (0 == warehouseRange && 0 == ordertypeRange) {
                            //我的查看优惠券
                            for (CouponItem couponItem : couponItems) {
                                //根据时间判断是否过期
                                String formatValidTo = couponItem.getFormatValidTo();
                                long formatValidToL = DateUtils.strToTimeStamps(formatValidTo);
                                //当前时间毫秒值
                                long unixStamp = DateUtils.getUnixStamp();
                                if (!couponItem.getStatus().equals("EXPIRED")) {//表没有过期
                                    couponItem.setUsable(true);
                                } else {
                                    couponItem.setUsable(false);
                                }
                                mCouponsList.add(couponItem);
                            }
                        } else {
                            //订单使用优惠券
                            for (CouponItem couponItem : couponItems) {
                                if (couponItem.getStatus().equals("CAN_USE")) {//可用
                                    couponItem.setUsable(true);
                                } else {
                                    couponItem.setUsable(false);
                                }
                                mCouponsList.add(couponItem);
                            }
                        }

                        mCode = false;
                        mAdapter.notifyDataSetChanged();//
                    }
                });

    }

    public void requestUserVip() {
        mModel.getUserVip()
                .subscribeOn(Schedulers.io())
                .retryWhen(new RetryWithDelay(3, 2))
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxUtils.<BaseJson<JsonObject>>bindToLifecycle(mRootView))
                .subscribe(new ErrorHandleSubscriber<BaseJson<JsonObject>>(mErrorHandler) {
                               @Override
                               public void onNext(BaseJson<JsonObject> jsonObjectBaseJson) {
                                   if (jsonObjectBaseJson.getStatus() == 0) {
                                       String member = jsonObjectBaseJson.getData().get("member").getAsString();
                                       String expiryDate = jsonObjectBaseJson.getData().get("expiryDate").getAsString();
                                       String balanceCny = jsonObjectBaseJson.getData().get("balanceCny").getAsString();
                                       mRootView.setUserVip(member, expiryDate, balanceCny);
                                   } else {
                                       mRootView.showMessage(jsonObjectBaseJson.getMsgs());
                                   }
                               }
                           }
                );

    }

    public void requestPayRechare(String rechare, double paymoney) {
        mModel.getPayRecharge(rechare, paymoney)
                .subscribeOn(Schedulers.io())
                .retryWhen(new RetryWithDelay(3, 2))
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxUtils.<BaseJson<String>>bindToLifecycle(mRootView))
                .subscribe(new ErrorHandleSubscriber<BaseJson<String>>(mErrorHandler) {

                    @Override
                    public void onNext(BaseJson<String> stringBaseJson) {
                        if (stringBaseJson.getStatus() == 0) {
                            mRootView.aliPayReturn(stringBaseJson.getData());
                        } else {
                            mRootView.showMessage(stringBaseJson.getData());
                        }
                    }
                });
    }

    /**
     * 充值的支付宝信息
     */
    public void requsetVerifyResult(Map<String, Object> partMap) {
        mModel.verifyResult(partMap)
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
                                       int payStatus = Integer.valueOf(ojbData.get("payStatus").toString());
                                       if (payStatus == 0) {
                                           mRootView.showMessage("充值成功");
                                       }
                                   } else {
                                       mRootView.showMessage(listBaseJson.getMsgs());
                                   }
                               }

                               @Override
                               public void onError(Throwable e) {
                                   requsetVerifyResult(partMap);
                               }
                           }
                );

    }


    /**
     * 获取消费记录列表
     *
     * @param pullToRefresh
     */
    public void requestRecords(final boolean pullToRefresh) {
        if (pullToRefresh) {
            pageNo = 1;
            mRecordList.clear();
        }
        mModel.getRecords(pageNo, 10)
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
                .compose(RxUtils.<BaseJson<List<Record>>>bindToLifecycle(mRootView))//使用RXlifecycle,使subscription和activity一起销毁
                .subscribe(new ErrorHandleSubscriber<BaseJson<List<Record>>>(mErrorHandler) {
                    @Override
                    public void onNext(BaseJson<List<Record>> listBaseJson) {

                        if (pullToRefresh) mRecordList.clear();//如果是上拉刷新则晴空列表
                        List<Record> recordList = listBaseJson.getData();
                        if (mRecordList.size() < listBaseJson.getOtherJson().getTotalCount()) {
                            pageNo += 1;
                            for (Record record : recordList) {
                                mRecordList.add(record);
                            }
                            mAdapter.notifyDataSetChanged();
                        } else {
                            mRootView.datadFew();
                        }
                    }

                });

    }

    /**
     * 修改密码
     *
     * @param
     */
    public void requestAlterPassword(String originalPassword, String password, String confirmedPassword) {
        mModel.alterpassword(originalPassword, password, confirmedPassword)
                .subscribeOn(Schedulers.io())
                .retryWhen(new RetryWithDelay(3, 2))
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxUtils.<BaseJson<JsonObject>>bindToLifecycle(mRootView))
                .subscribe(new ErrorHandleSubscriber<BaseJson<JsonObject>>(mErrorHandler) {
                               @Override
                               public void onNext(BaseJson<JsonObject> jsonObjectBaseJson) {
                                   if (jsonObjectBaseJson.getStatus() == 0) {
                                       mRootView.showMessage(jsonObjectBaseJson.getMsgs());
                                       mRootView.alterPassword();
                                   } else {
                                       mRootView.showMessage(jsonObjectBaseJson.getMsgs());
                                   }
                               }
                           }
                );
    }

    /**
     * 获取个人资料
     */
    public void requestUserInfo() {

        mModel.getUserInfo()
                .subscribeOn(Schedulers.io())
                .retryWhen(new RetryWithDelay(3, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxUtils.<BaseJson<User>>bindToLifecycle(mRootView))//使用RXlifecycle,使subscription和activity一起销毁
                .subscribe(new ErrorHandleSubscriber<BaseJson<User>>(mErrorHandler) {
                    @Override
                    public void onNext(BaseJson<User> userBaseJson) {
                        if (userBaseJson.getStatus() == 0) {
                            mRootView.SetUserInfo(userBaseJson.getData());
                        } else {
                            mRootView.showMessage(userBaseJson.getMsgs());
                        }
                    }
                });

    }

    /**
     * 修改个人资料
     *
     * @param user
     */
    public void requestAlterCustomer(User user) {
        mModel.altercustomer(user)
                .subscribeOn(Schedulers.io())
                .retryWhen(new RetryWithDelay(3, 2))
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxUtils.<BaseJson<String>>bindToLifecycle(mRootView))
                .subscribe(new ErrorHandleSubscriber<BaseJson<String>>(mErrorHandler) {
                               @Override
                               public void onNext(BaseJson<String> jsonObjectBaseJson) {
                                   if (jsonObjectBaseJson.getStatus() == 0) {
                                       mRootView.showMessage("修改成功!");
                                       mAppManager.getCurrentActivity().setResult(CodeDefine.MINE_EDIT_RESULT);
                                       mAppManager.getCurrentActivity().finish();
                                   } else {
                                       mRootView.showMessage(jsonObjectBaseJson.getMsgs());
                                   }
                               }
                           }
                );
    }

    /**
     * 获取仓库列表
     */
    public void requestWarehouse() {
        mModel.getwarehouse()
                .subscribeOn(Schedulers.io())
                .retryWhen(new RetryWithDelay(3, 2))
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxUtils.<WarehousJson>bindToLifecycle(mRootView))
                .subscribe(new ErrorHandleSubscriber<WarehousJson>(mErrorHandler) {
                               @Override
                               public void onNext(WarehousJson warehousJson) {
                                   if (warehousJson.getStatus() == 0) {

                                       for (WarehouseList warehouses : warehousJson.getData()) {
                                           mWarehouseList.add(warehouses);
                                       }
                                       mAdapter.notifyDataSetChanged();
                                   }
                               }
                           }
                );
    }
}
