package com.yyox.mvp.presenter;

import android.app.Application;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.google.gson.JsonObject;
import com.jess.arms.base.AppManager;
import com.jess.arms.base.DefaultAdapter;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.utils.RxUtils;
import com.yyox.R;
import com.yyox.Utils.Utils;
import com.yyox.consts.AddressType;
import com.yyox.consts.CodeDefine;
import com.yyox.mvp.contract.AddressContract;
import com.yyox.mvp.model.entity.Address;
import com.yyox.mvp.model.entity.BaseJson;
import com.yyox.mvp.model.entity.Region;
import com.yyox.mvp.ui.activity.AddressSaveActivity;
import com.yyox.mvp.ui.adapter.AddressAdapter;
import com.yyox.mvp.ui.adapter.AddressOrderAdapter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;
import me.jessyan.rxerrorhandler.handler.RetryWithDelay;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

/**
 * Created by dadaniu on 2017-01-12.
 */
@ActivityScope
public class AddressPresenter extends BasePresenter<AddressContract.Model, AddressContract.View> {

    private RxErrorHandler mErrorHandler;
    private AppManager mAppManager;
    private Application mApplication;
    private List<Address> mAddresses = new ArrayList<>();
    private List<Region> mRegions = new ArrayList<>();
    private DefaultAdapter mAdapter;
    public int mAddressId = 0;

    @Inject
    public AddressPresenter(AddressContract.Model model, AddressContract.View rootView, RxErrorHandler handler, AppManager appManager, Application application) {
        super(model, rootView);
        this.mApplication = application;
        this.mErrorHandler = handler;
        this.mAppManager = appManager;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mAdapter = null;
        this.mRegions = null;
        this.mAddresses = null;
        this.mErrorHandler = null;
        this.mAppManager = null;
        this.mApplication = null;
    }

    public void setAddressId(int id) {
        mAddressId = id;
    }

    public Address getSelectAddress() {
        Address address = null;
        for (Address item : mAddresses) {
            if (item.getIschecked()) {
                address = item;
                break;
            }
        }
        return address;
    }

    public void setAdapter(int type) {
        //设置Adapter
        if (0 == type) {
            mAdapter = new AddressAdapter(mAddresses);
            mRootView.setAdapter(mAdapter);
        } else {
            mAdapter = new AddressOrderAdapter(mAddresses);
            mRootView.setAdapter(mAdapter);
        }
        //事件处理
        mAdapter.setOnItemClickListener(new DefaultAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, Object data, int position) {
                if (view.getId() == R.id.cb_item_address_list_default || view.getId() == R.id.address_select_checkBox) {
                    setdefault(position);
                } else if (view.getId() == R.id.cb_item_address_list_order_default) {
                    for (Address address : mAddresses) {
                        address.setIschecked(false);
                    }
                    mAddresses.get(position).setIschecked(true);
                    mAdapter.notifyDataSetChanged();//通知更新数据
                } else if (view.getId() == R.id.ll_item_address_list_edit || view.getId() == R.id.ll_item_address_list_order_edit) {
                    Intent intent = new Intent(Utils.getCurrentActivity(), AddressSaveActivity.class);
                    intent.putExtra("type", AddressType.ADDRESS_TYPE_EDIT);
                    intent.putExtra("address", mAddresses.get(position));
                    Utils.getCurrentActivity().startActivityForResult(intent, CodeDefine.ADDRESS_SAVE_REQUEST);
                } else if (view.getId() == R.id.ll_item_address_list_delete || view.getId() == R.id.ll_item_address_list_order_delete) {
                    delete(position);
                } else if (view.getId() == R.id.rl_item_address_list_footer) {
                    Intent intent = new Intent(mAppManager.getCurrentActivity(), AddressSaveActivity.class);
                    intent.putExtra("type", AddressType.ADDRESS_TYPE_ADD);
                    mAppManager.getCurrentActivity().startActivityForResult(intent, CodeDefine.ADDRESS_SAVE_REQUEST);
                } else {
                    Intent intent = new Intent(Utils.getCurrentActivity(), AddressSaveActivity.class);
                    intent.putExtra("type", AddressType.ADDRESS_TYPE_DETAIL);
                    intent.putExtra("address", mAddresses.get(position));
                    Utils.getCurrentActivity().startActivityForResult(intent, CodeDefine.ADDRESS_SAVE_REQUEST);
                }
            }
        });
    }

    public void requestAddress(final boolean pullToRefresh) {

        mModel.getAddress()
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
                .compose(RxUtils.<BaseJson<List<Address>>>bindToLifecycle(mRootView))//使用RXlifecycle,使subscription和activity一起销毁
                .subscribe(new ErrorHandleSubscriber<BaseJson<List<Address>>>(mErrorHandler) {
                    @Override
                    public void onNext(BaseJson<List<Address>> listBaseJson) {
                        if (listBaseJson.getStatus() == 0) {
                            if (pullToRefresh) mAddresses.clear();//如果是上拉刷新则晴空列表
                            List<Address> addresses = listBaseJson.getData();
                            for (Address address : addresses) {
                                if (address.getId() == mAddressId) {
                                    address.setIschecked(true);
                                } else {
                                    if (address.getIsdefault() && mAddressId == 0) {
                                        address.setIschecked(true);
                                    } else {
                                        address.setIschecked(false);
                                    }
                                }
                                mAddresses.add(address);
                            }
                            mAdapter.notifyDataSetChanged();//通知更新数据
                        } else {
                            mRootView.showMessage(listBaseJson.getMsgs());
                        }
                    }
                });
    }

    public void requestSelectAddress(final boolean pullToRefresh) {

        mModel.getAddress()
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
                .compose(RxUtils.<BaseJson<List<Address>>>bindToLifecycle(mRootView))//使用RXlifecycle,使subscription和activity一起销毁
                .subscribe(new ErrorHandleSubscriber<BaseJson<List<Address>>>(mErrorHandler) {
                    @Override
                    public void onNext(BaseJson<List<Address>> listBaseJson) {
                        if (listBaseJson.getStatus() == 0) {
                            if (pullToRefresh) mAddresses.clear();//如果是上拉刷新则晴空列表
                            List<Address> addresses = listBaseJson.getData();
                            for (Address address : addresses) {
                                if (address.getId() == mAddressId) {
                                    address.setIschecked(true);
                                } else {
                                    if (address.getIsdefault() && mAddressId == 0) {
                                        address.setIschecked(true);
                                    } else {
                                        address.setIschecked(false);
                                    }
                                }
                                mAddresses.add(address);
                            }
                            mAdapter.notifyDataSetChanged();//通知更新数据
                        } else {
                            mRootView.showMessage(listBaseJson.getMsgs());
                        }
                    }
                });
    }

    public void requestAddressSave(Address address) {

        mModel.saveAddress(address)
                .subscribeOn(Schedulers.io())
                .retryWhen(new RetryWithDelay(3, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ErrorHandleSubscriber<BaseJson<String>>(mErrorHandler) {
                    @Override
                    public void onNext(BaseJson<String> listBaseJson) {
                        if (listBaseJson.getStatus() == 0) {
                            mRootView.refreshAddress();
                        } else {
                            String str = listBaseJson.getMsgs();
                            mRootView.showMessage(str);
                        }
                    }
                });
    }

    public void requestRegions(int type, int id) {

        mModel.getRegions(id)
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
                .subscribe(new ErrorHandleSubscriber<BaseJson<List<Region>>>(mErrorHandler) {
                    @Override
                    public void onNext(BaseJson<List<Region>> orders) {
                        mRegions.clear();
                        for (Region region : orders.getData()) {
                            mRegions.add(region);
                        }
                        mRootView.setRegions(type, mRegions);
                    }
                });
    }

    public void requestUploadIdcard(Bitmap idcarda, Bitmap idcardb) {

        mModel.uploadIdCard(idcarda, idcardb)
                .subscribeOn(Schedulers.io())
//                .retryWhen(new RetryWithDelay(3, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ErrorHandleSubscriber<BaseJson<JsonObject>>(mErrorHandler) {
                    @Override
                    public void onNext(BaseJson<JsonObject> listBaseJson) {
                        if (listBaseJson.getStatus() == 0) {
                            mRootView.deleteTempPicture();
                        } else {
                            mRootView.showMessage(listBaseJson.getMsgs());
                            mRootView.deleteTempPicture();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        mRootView.showMessage("网络超时!请重试");
                    }
                });
    }

    public void setdefault(int position) {
        Address objAddress = mAddresses.get(position);
        if (objAddress.getId() <= 0) {
            mRootView.showMessage("请选择设置项");
            return;
        }
        mModel.setDefaultAddress(objAddress.getId())
                .subscribeOn(Schedulers.io())
                .retryWhen(new RetryWithDelay(3, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ErrorHandleSubscriber<BaseJson<String>>(mErrorHandler) {
                    @Override
                    public void onNext(BaseJson<String> listBaseJson) {
                        mRootView.showMessage(listBaseJson.getMsgs());
                        requestAddress(true);
                    }
                });
    }

    public void requestAddressDetail(int id) {

        mModel.getAddressDetail(id)
                .subscribeOn(Schedulers.io())
                .retryWhen(new RetryWithDelay(3, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ErrorHandleSubscriber<BaseJson<Address>>(mErrorHandler) {
                    @Override
                    public void onNext(BaseJson<Address> listBaseJson) {
                        if (listBaseJson.getStatus() == 0) {
                            mRootView.setAddressDetail(listBaseJson.getData());
                        } else {
                            mRootView.showMessage(listBaseJson.getMsgs());
                        }
                    }
                });
    }

    private void delete(int position) {
        Address objAddress = mAddresses.get(position);
        if (objAddress.getId() <= 0) {
            mRootView.showMessage("请选择删除项");
            return;
        }
        new AlertDialog.Builder(mAppManager.getCurrentActivity()).setTitle("确认删除")
                .setMessage("确认删除该地址吗?")
                .setPositiveButton("取消",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //取消
                            }
                        })
                .setNegativeButton("确认",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //删除订单
                                mModel.deleteAddress(objAddress.getId())
                                        .subscribeOn(Schedulers.io())
                                        .retryWhen(new RetryWithDelay(3, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                                        .subscribeOn(AndroidSchedulers.mainThread())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(new ErrorHandleSubscriber<BaseJson<String>>(mErrorHandler) {
                                            @Override
                                            public void onNext(BaseJson<String> listBaseJson) {
                                                mRootView.showMessage(listBaseJson.getMsgs());
                                                requestAddress(true);
                                            }
                                        });
                            }
                        }).show();
    }

    /**
     * 请求身份证图片
     * @param i
     * @param url
     */
    public void requestAddressImage(int i, String url) {
        mModel.getAddressImage(url)
                .subscribeOn(Schedulers.io())
                .retryWhen(new RetryWithDelay(3, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxUtils.<BaseJson<String>>bindToLifecycle(mRootView))//使用RXlifecycle,使subscription和activity一起销毁
                .subscribe(new ErrorHandleSubscriber<BaseJson<String>>(mErrorHandler) {
                    @Override
                    public void onNext(BaseJson<String> jsonObjectBaseJson) {
                        if (jsonObjectBaseJson.getStatus() == 0) {
                            mRootView.setImage(i,jsonObjectBaseJson.getData());
                        } else {
                            mRootView.showMessage(jsonObjectBaseJson.getMsgs());
                        }
                    }
                });
    }
}
