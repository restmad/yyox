package com.yyox.mvp.presenter;

import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.dou361.dialogui.DialogUIUtils;
import com.dou361.dialogui.listener.DialogUIListener;
import com.google.gson.JsonObject;
import com.jess.arms.base.AppManager;
import com.jess.arms.base.DefaultAdapter;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.utils.RxUtils;
import com.yyox.R;
import com.yyox.Utils.CommonUtils;
import com.yyox.Utils.NoDoubleClickUtils;
import com.yyox.consts.CodeDefine;
import com.yyox.consts.OrderStatus;
import com.yyox.mvp.contract.PackageContract;
import com.yyox.mvp.model.entity.BaseJson;
import com.yyox.mvp.model.entity.Images;
import com.yyox.mvp.model.entity.OrderPackage;
import com.yyox.mvp.model.entity.OrderPackageGoods;
import com.yyox.mvp.model.entity.PackageItem;
import com.yyox.mvp.model.entity.Warehouse;
import com.yyox.mvp.ui.activity.GlobalData;
import com.yyox.mvp.ui.activity.ImageShowActivity;
import com.yyox.mvp.ui.activity.PackageSaveActivity;
import com.yyox.mvp.ui.adapter.GoodsAdapter;
import com.yyox.mvp.ui.adapter.PackageAdapter;
import com.yyox.mvp.ui.adapter.PackageBoxAdapter;
import com.yyox.mvp.ui.adapter.PackageDetailAdapter;

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
 * Created by dadaniu on 2017-02-27.
 */
@ActivityScope
public class PackagePresenter extends BasePresenter<PackageContract.Model, PackageContract.View> {

    private RxErrorHandler mErrorHandler;
    private AppManager mAppManager;
    private Application mApplication;
    private DefaultAdapter mAdapter;
    private List<PackageItem> mPackageItems = new ArrayList<>();
    private List<OrderPackage> mOrderPackageList = new ArrayList<>();
    private List<OrderPackageGoods> mNoDeclareGoodsList = new ArrayList<>();
    private OrderPackageGoods orderPackageGoodsSelect = null;
    private OrderPackageGoods oldOrderPackageGoodsSelect = null;
    private List<Integer> mPackagesId = new ArrayList<Integer>();
    private int lastUserId = 1;
    private Context mContext;

    @Inject
    public PackagePresenter(PackageContract.Model model, PackageContract.View rootView, RxErrorHandler handler, AppManager appManager, Application application) {
        super(model, rootView);
        this.mApplication = application;
        this.mErrorHandler = handler;
        this.mAppManager = appManager;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
        this.mAppManager = null;
        this.mApplication = null;
    }

    /**
     * 设置待入库Adapter
     */
    private void setAdapter_Waiting(Context context) {
        mAdapter = new PackageAdapter(mPackageItems);
        mRootView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new DefaultAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, Object data, int position) {
                if (view.getId() == R.id.ll_order_waiting_list_item_edit) {
                    edit(position, context);
                } else if (view.getId() == R.id.ll_order_waiting_list_item_delete) {
                    delete(position);
                }
            }
        });
    }

    /**
     * 设置合箱Adapter
     */
    private void setAdapter_Box(Context context) {
        mAdapter = new PackageBoxAdapter(mPackageItems);
        mRootView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new DefaultAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, Object data, int position) {
                if (view.getId() == R.id.ll_order_waiting_list_item_edit) {
                    edit(position, context);
                } else if (view.getId() == R.id.ll_order_waiting_list_item_delete) {
                    delete(position);
                } else if (view.getId() == R.id.cb_item_package_box_list_select) {
                    CheckBox checkBox = (CheckBox) view;
                    if (checkBox.isChecked()) {
                        mPackageItems.get(position).setChecked(true);
                    } else {
                        mPackageItems.get(position).setChecked(false);
                    }
                    mAdapter.notifyDataSetChanged();//通知更新数据
                }
            }
        });
    }

    /**
     * 设置合箱包裹详情Adapter
     */
    private void setAdapter_Package(Context context) {
        mAdapter = new PackageDetailAdapter(mOrderPackageList);
        mRootView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new DefaultAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, Object data, int position) {
                if (NoDoubleClickUtils.isDoubleClick()) {
                    return;
                }
                OrderPackage orderPackage = mOrderPackageList.get(position);
                if (view.getId() == R.id.package_map) {
                    List<String> orderSreenshot = orderPackage.getOrderSreenshot();
                    List<String> paths = new ArrayList<String>();
                    if (orderSreenshot.size() > 0) {
                        List<Images> images = new ArrayList<>();
                        for (int i = 0; i < orderSreenshot.size(); i++) {
                            if (i == 0){
                                images.add(new Images(orderSreenshot.get(i),"net",orderPackage.getBitmap1()));
                            }else if (i == 1){
                                images.add(new Images(orderSreenshot.get(i),"net",orderPackage.getBitmap2()));
                            }else if (i == 2){
                                images.add(new Images(orderSreenshot.get(i),"net",orderPackage.getBitmap3()));
                            }
                        }
                        GlobalData.setImagesList(images);
                        Intent intent = new Intent(mContext, ImageShowActivity.class);
                        //intent.putExtra("map", (Serializable) bitmapList);
                        intent.putExtra("delete", "delete");
//                        context.startActivityForResult(intent, CodeDefine.IMAGE_SHOW_REQUEST);
                        mContext.startActivity(intent);
                    }

                } else if (view.getId() == R.id.iv_item_package_detail_list_edit) {
                    //mRootView.showMessage("修改包裹昵称");
                    DialogUIUtils.showAlert(mAppManager.getCurrentActivity(), "修改昵称", "", "修改包裹昵称", "", "确定", "取消", false, true, true, new DialogUIListener() {
                        @Override
                        public void onPositive() {
                        }

                        @Override
                        public void onCancle() {
                            super.onCancle();
                        }

                        @Override
                        public void onNegative() {
                        }

                        @Override
                        public void onGetInput(CharSequence input1, CharSequence input2) {
                            super.onGetInput(input1, input2);
                            requestUpdatePackageName(orderPackage.getId(), input1.toString());
                        }

                    }).show();
                } else if (view.getId() == R.id.ib_item_package_detail_goods_list_del) {
                    OrderPackageGoods orderPackageGoods = orderPackage.getOrderPackageGoods();
                    new AlertDialog.Builder(mAppManager.getCurrentActivity()).setTitle("确认删除")
                            .setMessage("确认删除该商品申报信息吗?")
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
                                            //删除
                                            requestDelPackageGoods(orderPackageGoods.getId(), orderPackage.getId());
                                        }
                                    }).show();
                } else if (view.getId() == R.id.btn_item_package_detail_goods_footer_list_add) {
                    //mRootView.showMessage("商品申报");
                    mNoDeclareGoodsList = orderPackage.getNoDeclareGoodsList();

                    orderPackageGoodsSelect = null;

                    Dialog dialog = new Dialog(mAppManager.getCurrentActivity(), R.style.dialog_style);

                    View rootViewB = View.inflate(mApplication.getApplicationContext(), R.layout.custom_dialog_goods, null);
                    EditText etBrand = (EditText) rootViewB.findViewById(R.id.et_custom_dialog_goods_brand);
                    EditText etName = (EditText) rootViewB.findViewById(R.id.et_custom_dialog_goods_name);
                    EditText etPrice = (EditText) rootViewB.findViewById(R.id.et_custom_dialog_goods_price);
                    TextView tvSymbol = (TextView) rootViewB.findViewById(R.id.tv_custom_dialog_goods_symbol);
                    tvSymbol.setText(mNoDeclareGoodsList.get(0).getCurrencyName());
                    RecyclerView recyclerView = (RecyclerView) rootViewB.findViewById(R.id.CustomDialogGoodsRecyclerView);
                    GoodsAdapter adapter = new GoodsAdapter(mNoDeclareGoodsList);
                    recyclerView.setAdapter(adapter);
                    recyclerView.setLayoutManager(new GridLayoutManager(rootViewB.getContext(), 1));
                    adapter.setOnItemClickListener(new DefaultAdapter.OnRecyclerViewItemClickListener() {
                        @Override
                        public void onItemClick(View view, Object data, int position) {
                            //点击单个商品
                            OrderPackageGoods orderPackageGoods = orderPackage.getNoDeclareGoodsList().get(position);
                            orderPackageGoodsSelect = orderPackageGoods;
                            for (OrderPackageGoods item : mNoDeclareGoodsList) {
                                if (item.getId() != orderPackageGoodsSelect.getId()) {
                                    item.setChecked(false);
                                } else {
                                    item.setChecked(true);
                                }
                            }
                            adapter.notifyDataSetChanged();
                            mRootView.showMessage(orderPackageGoods.getProductName());
                        }
                    });
                    Button button = (Button) rootViewB.findViewById(R.id.btn_custom_dialog_goods_confirm);
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //修改商品信息
                            String newBrand = etBrand.getText().toString();
                            if (newBrand.isEmpty()) {
                                mRootView.showMessage("请输入品牌");
                                return;
                            }
                            if (!CommonUtils.isChineseValue(newBrand)) {
                                mRootView.showMessage("请填写汉字组成的品牌名");
                                return;
                            }

                            String newName = etName.getText().toString();
                            if (newName.isEmpty()) {
                                mRootView.showMessage("请输入商品名称");
                                return;
                            }
                            if (!CommonUtils.isChineseValue(newName)) {
                                mRootView.showMessage("请填写汉字组成的商品名称");
                                return;
                            }

                            String newPrice = etPrice.getText().toString();
                            if (newPrice.isEmpty()) {
                                mRootView.showMessage("总价不能为空");
                                return;
                            } else if (newPrice.equals("0")) {
                                mRootView.showMessage("总价不能为0");
                                return;
                            } else if (!CommonUtils.isChineseNumber(newPrice)) {
                                mRootView.showMessage("商品总价超过限制");
                                return;
                            }

                            if (orderPackageGoodsSelect == null) {
                                mRootView.showMessage("请选择包裹内件");
                                return;
                            }
                            requestUpdateGoods(orderPackageGoodsSelect.getId(), newName, newBrand, Double.valueOf(newPrice), orderPackageGoodsSelect.getStock());
                            dialog.dismiss();
                        }
                    });


                    dialog.setContentView(rootViewB);
                    Window dialogWindow = dialog.getWindow();
                    WindowManager.LayoutParams lp = dialogWindow.getAttributes();
                    lp.windowAnimations = R.style.dialog_animation_style;
                    lp.gravity = Gravity.BOTTOM;
                    WindowManager windowManager = mAppManager.getCurrentActivity().getWindowManager();
                    Display display = windowManager.getDefaultDisplay();
                    lp.width = (int) (display.getWidth()); //设置宽度
                    dialogWindow.setAttributes(lp);
                    dialog.show();
                } else {
                    if (orderPackage.getShowtype() == 2) {
                        mNoDeclareGoodsList.clear();
                        mNoDeclareGoodsList = orderPackage.getNoDeclareGoodsList();

                        orderPackageGoodsSelect = null;
                        oldOrderPackageGoodsSelect = null;

                        OrderPackageGoods editOrderPackageGoods = orderPackage.getOrderPackageGoods();
                        oldOrderPackageGoodsSelect = editOrderPackageGoods;
                        mNoDeclareGoodsList.add(editOrderPackageGoods);
                        for (OrderPackageGoods item : mNoDeclareGoodsList) {
                            if (item.getId() != editOrderPackageGoods.getId()) {
                                item.setChecked(false);
                            } else {
                                item.setChecked(true);
                            }
                        }
                        Dialog dialog = new Dialog(mAppManager.getCurrentActivity(), R.style.dialog_style);

                        View rootViewB = View.inflate(mApplication.getApplicationContext(), R.layout.custom_dialog_goods, null);
                        EditText etBrand = (EditText) rootViewB.findViewById(R.id.et_custom_dialog_goods_brand);
                        orderPackageGoodsSelect = editOrderPackageGoods;
                        etBrand.setText(editOrderPackageGoods.getAppBrandCNY());
                        EditText etName = (EditText) rootViewB.findViewById(R.id.et_custom_dialog_goods_name);
                        etName.setText(editOrderPackageGoods.getAppProductNameCNY());
                        EditText etPrice = (EditText) rootViewB.findViewById(R.id.et_custom_dialog_goods_price);
                        etPrice.setText(CommonUtils.doubleFormat(String.valueOf(editOrderPackageGoods.getAmount())));
                        TextView tvSymbol = (TextView) rootViewB.findViewById(R.id.tv_custom_dialog_goods_symbol);
                        tvSymbol.setText(editOrderPackageGoods.getCurrencyName());
                        TextView viewById = (TextView) rootViewB.findViewById(R.id.goods_dialag);
                        viewById.setText("编辑商品");
                        RecyclerView recyclerView = (RecyclerView) rootViewB.findViewById(R.id.CustomDialogGoodsRecyclerView);
                        GoodsAdapter adapter = new GoodsAdapter(mNoDeclareGoodsList);
                        recyclerView.setAdapter(adapter);
                        recyclerView.setLayoutManager(new GridLayoutManager(rootViewB.getContext(), 1));
                        adapter.setOnItemClickListener(new DefaultAdapter.OnRecyclerViewItemClickListener() {
                            @Override
                            public void onItemClick(View view, Object data, int position) {
                                //点击单个商品
                                OrderPackageGoods orderPackageGoods = orderPackage.getNoDeclareGoodsList().get(position);
                                orderPackageGoodsSelect = orderPackageGoods;
                                for (OrderPackageGoods item : mNoDeclareGoodsList) {
                                    if (item.getId() != orderPackageGoodsSelect.getId()) {
                                        item.setChecked(false);
                                    } else {
                                        item.setChecked(true);
                                    }
                                }
                                adapter.notifyDataSetChanged();
                                if (!orderPackageGoods.getProductName().isEmpty()) {
                                    mRootView.showMessage(orderPackageGoods.getProductName());
                                }
                            }
                        });
                        Button button = (Button) rootViewB.findViewById(R.id.btn_custom_dialog_goods_confirm);
                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //修改商品信息
                                String newBrand = etBrand.getText().toString();
                                if (newBrand.isEmpty()) {
                                    mRootView.showMessage("请输入品牌");
                                    return;
                                }
                                if (!CommonUtils.isChineseValue(newBrand)) {
                                    mRootView.showMessage("请填写汉字组成的品牌名");
                                    return;
                                }

                                String newName = etName.getText().toString();
                                if (newName.isEmpty()) {
                                    mRootView.showMessage("请输入商品名称");
                                    return;
                                }
                                if (!CommonUtils.isChineseValue(newName)) {
                                    mRootView.showMessage("请填写汉字组成的商品名称");
                                    return;
                                }

                                String newPrice = etPrice.getText().toString();
                                if (newPrice.isEmpty()) {
                                    mRootView.showMessage("总价不能为空");
                                    return;
                                } else if (newPrice.equals("0")) {
                                    mRootView.showMessage("总价不能为0");
                                    return;
                                } else if (!CommonUtils.isChineseNumber(newPrice)) {
                                    mRootView.showMessage("商品总价超过限制");
                                    return;
                                }

                                if (orderPackageGoodsSelect == null) {
                                    mRootView.showMessage("请选择包裹内件");
                                    return;
                                }
                                if (oldOrderPackageGoodsSelect.getId() != orderPackageGoodsSelect.getId()) {
                                    requestDelPackageGoods(oldOrderPackageGoodsSelect.getId(), orderPackage.getId());
                                    requestUpdateGoods(orderPackageGoodsSelect.getId(), newName, newBrand, Double.valueOf(newPrice), orderPackageGoodsSelect.getStock());
                                } else {
                                    requestUpdateGoods(orderPackageGoodsSelect.getId(), newName, newBrand, Double.valueOf(newPrice), orderPackageGoodsSelect.getStock());
                                }
                                dialog.dismiss();
                            }
                        });
                        dialog.setContentView(rootViewB);
                        Window dialogWindow = dialog.getWindow();
                        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
                        lp.windowAnimations = R.style.dialog_animation_style;
                        lp.gravity = Gravity.BOTTOM;
                        WindowManager windowManager = mAppManager.getCurrentActivity().getWindowManager();
                        Display display = windowManager.getDefaultDisplay();
                        lp.width = (int) (display.getWidth()); //设置宽度
                        dialogWindow.setAttributes(lp);
                        dialog.show();
                    }
                }
            }
        });
    }

    public void setAdapter(int type, Context context) {
        mContext = context;
        switch (type) {
            case OrderStatus.ORDER_STATUS_WAITING_IN_WAREHOUSE:
                setAdapter_Waiting(context);
                break;
            case OrderStatus.ORDER_STATUS_BOX_PACKAGE:
                setAdapter_Box(context);
                break;
            case OrderStatus.ORDER_STATUS_PACKAGES:
                setAdapter_Package(context);
                break;
        }
    }

    public void setPackagesId(List<Integer> list) {
        this.mPackagesId = list;
    }

    public List<PackageItem> getSelectPackage() {
        List<PackageItem> packageItemList = new ArrayList<PackageItem>();
        for (PackageItem item : mPackageItems) {
            if (item.isChecked()) {
                packageItemList.add(item);
            }
        }
        return packageItemList;
    }

    /**
     * 请求待入库列表
     *
     * @param pullToRefresh
     */
    public void requestOrderWaitings(final boolean pullToRefresh) {

        if (pullToRefresh) {
            lastUserId = 1;
            mPackageItems.clear();
            mAdapter.notifyDataSetChanged();
        }
        mModel.getOrderWaitings("" + lastUserId, 0, 10, "未入库")
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
                .compose(RxUtils.<BaseJson<List<PackageItem>>>bindToLifecycle(mRootView))//使用RXlifecycle,使subscription和activity一起销毁
                .subscribe(new ErrorHandleSubscriber<BaseJson<List<PackageItem>>>(mErrorHandler) {
                    @Override
                    public void onNext(BaseJson<List<PackageItem>> listBaseJson) {
                        if (listBaseJson.getStatus() == 0) {
                            int iTotalCount = listBaseJson.getOtherJson().getTotalCount();
                            if (lastUserId == 1) {
                                mPackageItems.clear();
                            }
                            if (mPackageItems.size() < iTotalCount) {
                                if (pullToRefresh)
                                    mPackageItems.clear();//如果是下拉刷新则清空列表
                                List<PackageItem> orders = listBaseJson.getData();
                                for (PackageItem PackageItem : orders) {
                                    mPackageItems.add(PackageItem);
                                }
                                mAdapter.notifyDataSetChanged();//通知更新数据
                                lastUserId++;
                            } else {
                                mRootView.dataFew();
                            }
                        } else {
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
     * 请求合箱列表
     *
     * @param pullToRefresh
     */
    public void requestBoxPackage(final boolean pullToRefresh) {

        if (pullToRefresh) {
            lastUserId = 1;
            mPackageItems.clear();
            mAdapter.notifyDataSetChanged();//通知更新数据
        }
        mModel.getOrderWaitings(lastUserId + "", 0, 10, "已入库")
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
                .compose(RxUtils.<BaseJson<List<PackageItem>>>bindToLifecycle(mRootView))//使用RXlifecycle,使subscription和activity一起销毁
                .subscribe(new ErrorHandleSubscriber<BaseJson<List<PackageItem>>>(mErrorHandler) {
                    @Override
                    public void onNext(BaseJson<List<PackageItem>> listBaseJson) {
                        if (listBaseJson.getStatus() == 0) {
                            int iTotalCount = listBaseJson.getOtherJson().getTotalCount();
                            if (lastUserId == 1) {
                                mPackageItems.clear();
                            }
                            if (mPackageItems.size() < iTotalCount) {
                                if (pullToRefresh) mPackageItems.clear();//如果是上拉刷新则晴空列表
                                List<PackageItem> orders = listBaseJson.getData();
                                for (PackageItem order : orders) {
                                    order.setChecked(false);
                                    mPackageItems.add(order);
                                }
                                mAdapter.notifyDataSetChanged();//通知更新数据
                                lastUserId++;
                            } else {
                                mRootView.dataFew();
                            }
                        } else {
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

    public void requestPackageDetail(List<Integer> list) {

        mModel.getPackageDetail(list)
                .subscribeOn(Schedulers.io())
                .retryWhen(new RetryWithDelay(3, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxUtils.<BaseJson<List<OrderPackage>>>bindToLifecycle(mRootView))//使用RXlifecycle,使subscription和activity一起销毁
                .subscribe(new ErrorHandleSubscriber<BaseJson<List<OrderPackage>>>(mErrorHandler) {
                    @Override
                    public void onNext(BaseJson<List<OrderPackage>> listBaseJson) {
                        if (listBaseJson.getStatus() == 0) {
                            mOrderPackageList.clear();

                            List<OrderPackage> orderPackageList = listBaseJson.getData();
                            for (OrderPackage orderPackage : orderPackageList) {
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
                                                        mAdapter.notifyDataSetChanged();//通知更新数据
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
                                                        mAdapter.notifyDataSetChanged();//通知更新数据
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
                                                        mAdapter.notifyDataSetChanged();//通知更新数据
                                                    } else {
                                                        //mRootView.showMessage(jsonObjectBaseJson.getMsgs());
                                                    }
                                                }
                                            });
                                }
                                //0添加包裹详情中的包裹
                                orderPackage.setShowtype(0);
                                mOrderPackageList.add(orderPackage);

                                List<OrderPackageGoods> orderPackageGoodsList = orderPackage.getGoodList();
                                if (orderPackageGoodsList.size() > 0) {
                                    //1添加包裹详情中的商品信息头
                                    OrderPackage orderPackageGoodsHeader = new OrderPackage();
                                    orderPackageGoodsHeader.setShowtype(1);
                                    mOrderPackageList.add(orderPackageGoodsHeader);

                                    //未申报商品个数
                                    int iCount = 0;
                                    List<OrderPackageGoods> noDeclareGoodsList = new ArrayList<OrderPackageGoods>();
                                    for (OrderPackageGoods orderPackageGoods : orderPackageGoodsList) {
                                        if (!orderPackageGoods.isGoolsType()) {
                                            iCount++;
                                            noDeclareGoodsList.add(orderPackageGoods);
                                        }
                                    }
                                    for (OrderPackageGoods orderPackageGoods : orderPackageGoodsList) {
                                        if (orderPackageGoods.isGoolsType()) {
                                            //2添加包裹详情中的商品信息
                                            OrderPackage orderPackageGoodsItem = new OrderPackage();
                                            orderPackageGoodsItem.setId(orderPackage.getId());
                                            orderPackageGoodsItem.setShowtype(2);
                                            orderPackageGoodsItem.setOrderPackageGoods(orderPackageGoods);
                                            orderPackageGoodsItem.setNoDeclareGoodsList(noDeclareGoodsList);
                                            mOrderPackageList.add(orderPackageGoodsItem);
                                        }
                                    }

                                    if (iCount > 0) {
                                        //3添加包裹详情中的商品信息脚
                                        OrderPackage orderPackageGoodsFooter = new OrderPackage();
                                        orderPackageGoodsFooter.setShowtype(3);
                                        orderPackageGoodsFooter.setNoDeclareGoodsList(noDeclareGoodsList);
                                        OrderPackageGoods orderPackageGoodsFooterItem = new OrderPackageGoods();
                                        orderPackageGoodsFooterItem.setCount(iCount);
                                        orderPackageGoodsFooter.setOrderPackageGoods(orderPackageGoodsFooterItem);
                                        mOrderPackageList.add(orderPackageGoodsFooter);
                                    }
                                }
                            }
                            mAdapter.notifyDataSetChanged();//通知更新数据
                        } else {
                            mRootView.showMessage(listBaseJson.getMsgs());
                        }
                    }
                });

    }

    public void requestCheckDeclare() {

        mModel.getPackageDetail(mPackagesId)
                .subscribeOn(Schedulers.io())
                .retryWhen(new RetryWithDelay(3, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxUtils.<BaseJson<List<OrderPackage>>>bindToLifecycle(mRootView))//使用RXlifecycle,使subscription和activity一起销毁
                .subscribe(new ErrorHandleSubscriber<BaseJson<List<OrderPackage>>>(mErrorHandler) {
                    @Override
                    public void onNext(BaseJson<List<OrderPackage>> listBaseJson) {
                        if (listBaseJson.getStatus() == 0) {
                            List<OrderPackage> orderPackageList = listBaseJson.getData();
                            boolean bNoGoods = false;
                            //未申报商品个数
                            int iCount = 0;
                            for (OrderPackage orderPackage : orderPackageList) {
                                List<OrderPackageGoods> orderPackageGoodsList = orderPackage.getGoodList();
                                if (orderPackageGoodsList.size() > 0) {
                                    for (OrderPackageGoods orderPackageGoods : orderPackageGoodsList) {
                                        if (orderPackageGoods.isGoolsType()) {
                                        } else {
                                            iCount++;
                                        }
                                    }
                                } else {
                                    bNoGoods = true;
                                    break;
                                }
                            }
                            if (bNoGoods) {
                                mRootView.showMessage("却少商品信息");
                            } else {
                                if (iCount > 0) {
                                    mRootView.showMessage("请完善所有商品申报信息");
                                } else {
                                    mRootView.CheckGoodsDeclare(orderPackageList);
                                }
                            }
                        } else {
                            mRootView.showMessage(listBaseJson.getMsgs());
                        }
                    }
                });

    }

    /**
     * 修改包裹昵称
     *
     * @param id
     * @param nickname
     */
    public void requestUpdatePackageName(int id, String nickname) {

        mModel.updatePackageName(id, nickname)
                .subscribeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ErrorHandleSubscriber<BaseJson<String>>(mErrorHandler) {
                    @Override
                    public void onNext(BaseJson<String> listBaseJson) {
                        if (listBaseJson.getStatus() == 0) {
                            mRootView.showMessage("修改成功");
                            for (OrderPackage orderPackage : mOrderPackageList) {
                                if (orderPackage.getId() == id) {
                                    orderPackage.setNickname(nickname);
                                }
                            }
                            requestPackageDetail(mPackagesId);
                            mAdapter.notifyDataSetChanged();
                        } else {
                            mRootView.showMessage(listBaseJson.getMsgs());
                        }
                    }
                });

    }

    /**
     * 删除包裹中商品申报信息
     *
     * @param goodsId
     * @param inventoryId
     */
    public void requestDelPackageGoods(int goodsId, int inventoryId) {

        mModel.deleteGoods(goodsId, inventoryId)
                .subscribeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ErrorHandleSubscriber<BaseJson<String>>(mErrorHandler) {
                    @Override
                    public void onNext(BaseJson<String> listBaseJson) {
                        if (listBaseJson.getStatus() == 0) {
                            mRootView.showMessage("操作成功");
                            requestPackageDetail(mPackagesId);
                        } else {
                            mRootView.showMessage(listBaseJson.getMsgs());
                        }
                    }
                });

    }

    /**
     * 修改商品信息
     *
     * @param id
     * @param nickname
     * @param brandcny
     * @param amount
     * @param unitsNumber
     */
    public void requestUpdateGoods(int id, String nickname, String brandcny, double amount, int unitsNumber) {
        mModel.updateGoods(id, nickname, brandcny, amount, unitsNumber)
                .subscribeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ErrorHandleSubscriber<BaseJson<String>>(mErrorHandler) {
                    @Override
                    public void onNext(BaseJson<String> listBaseJson) {
                        if (listBaseJson.getStatus() == 0) {
                            mRootView.showMessage("操作成功");
                            requestPackageDetail(mPackagesId);
                            mAdapter.notifyDataSetChanged();
                        } else {
                            mRootView.showMessage(listBaseJson.getMsgs());
                        }
                    }
                });
    }

    private void edit(int position, Context context) {
        PackageItem packageItem = mPackageItems.get(position);
        if (packageItem.getId() <= 0) {
            mRootView.showMessage("请选择设置项");
            return;
        }
        Intent intent = new Intent(mAppManager.getCurrentActivity().getApplicationContext(), PackageSaveActivity.class);
        intent.putExtra("type", OrderStatus.ORDER_STATUS_PACKAGE_EDIT);
        intent.putExtra("PackageItem", packageItem);
        mAppManager.getCurrentActivity().startActivityForResult(intent, CodeDefine.PACKAGE_REQUEST);
    }

    private void delete(int position) {
        PackageItem packageItem = mPackageItems.get(position);
        if (packageItem.getId() <= 0) {
            mRootView.showMessage("请选择删除项");
            return;
        }
        new AlertDialog.Builder(mAppManager.getCurrentActivity()).setTitle("确认删除")
                .setMessage("确认删除该订单吗?")
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
                                mModel.deleteOrder(packageItem.getId())
                                        .subscribeOn(Schedulers.io())
                                        .retryWhen(new RetryWithDelay(3, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                                        .subscribeOn(AndroidSchedulers.mainThread())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(new ErrorHandleSubscriber<BaseJson<String>>(mErrorHandler) {
                                            @Override
                                            public void onNext(BaseJson<String> listBaseJson) {
                                                mRootView.showMessage(listBaseJson.getMsgs());
                                                requestOrderWaitings(true);
                                            }
                                        });
                            }
                        }).show();
    }

    public void getWarehouse() {

        mModel.getWarehouses()
                .subscribeOn(Schedulers.io())
                .retryWhen(new RetryWithDelay(3, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxUtils.<BaseJson<List<Warehouse>>>bindToLifecycle(mRootView))//使用RXlifecycle,使subscription和activity一起销毁
                .subscribe(new ErrorHandleSubscriber<BaseJson<List<Warehouse>>>(mErrorHandler) {
                    @Override
                    public void onNext(BaseJson<List<Warehouse>> listBaseJson) {
                        if (listBaseJson.getStatus() == 0) {
                            List<Warehouse> data = listBaseJson.getData();
                            mRootView.ListWarehouses(data);
                        } else {
                            mRootView.showMessage(listBaseJson.getMsgs());
                        }
                    }
                });

    }

    /**
     * 提交预报(待入库编辑包裹)
     *
     * @param carrierNo
     * @param warehouseId
     * @param nickname
     * @param inventoryBasicId
     */
    public void commitForecast(String carrierNo, int warehouseId, String nickname, int inventoryBasicId) {

        mModel.commitForecast(carrierNo, warehouseId, nickname, inventoryBasicId)
                .subscribeOn(Schedulers.io())
              //  .retryWhen(new RetryWithDelay(1, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxUtils.<BaseJson<JsonObject>>bindToLifecycle(mRootView))//使用RXlifecycle,使subscription和activity一起销毁
                .subscribe(new ErrorHandleSubscriber<BaseJson<JsonObject>>(mErrorHandler) {

                    @Override
                    public void onNext(BaseJson<JsonObject> listBaseJson) {
                        if (listBaseJson.getStatus() == 0) {
                            if (0 == inventoryBasicId) {
                                mRootView.setUIResult(0);
                            } else {
                                mRootView.setUIResult(1);
                            }
                        } else {
                            mRootView.showMessage(listBaseJson.getMsgs());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        mRootView.showMessage("网络超时!请重试");
                    }
                });
    }

    /**
     * 上传包裹截图
     *
     * @param mImage1
     * @param mImage2
     * @param mImage3
     */
    public void putPackageImage(String mImage1, String mImage2, String mImage3) {

        mModel.putPackageImage(mImage1, mImage2, mImage3)
                .subscribeOn(Schedulers.io())
                .retryWhen(new RetryWithDelay(3, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxUtils.<BaseJson<JsonObject>>bindToLifecycle(mRootView))//使用RXlifecycle,使subscription和activity一起销毁
                .subscribe(new ErrorHandleSubscriber<BaseJson<JsonObject>>(mErrorHandler) {

                    @Override
                    public void onNext(BaseJson<JsonObject> jsonObjectBaseJson) {
                        if (jsonObjectBaseJson.getStatus() == 0) {
                            mRootView.packageImage(jsonObjectBaseJson.getStatus());
                        } else {
                            mRootView.showMessage(jsonObjectBaseJson.getMsgs());
                            mRootView.packageImage(jsonObjectBaseJson.getStatus());
                        }
                    }
                });
    }

    public void uploadPackageImage(Bitmap bitmap1, Bitmap bitmap2, Bitmap bitmap3) {

        mModel.uploadPackageImage(bitmap1, bitmap2, bitmap3)
                .subscribeOn(Schedulers.io())
                .retryWhen(new RetryWithDelay(3, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxUtils.<BaseJson<JsonObject>>bindToLifecycle(mRootView))//使用RXlifecycle,使subscription和activity一起销毁
                .subscribe(new ErrorHandleSubscriber<BaseJson<JsonObject>>(mErrorHandler) {

                    @Override
                    public void onNext(BaseJson<JsonObject> jsonObjectBaseJson) {
                        if (jsonObjectBaseJson.getStatus() == 0) {
                            mRootView.packageImage(jsonObjectBaseJson.getStatus());
                        } else {
                            mRootView.showMessage(jsonObjectBaseJson.getMsgs());
                            mRootView.packageImage(jsonObjectBaseJson.getStatus());
                        }
                    }
                });
    }

    public void extractPicture(int i,String url,int size) {

        mModel.extractPicture(url)
                .subscribeOn(Schedulers.io())
//                .retryWhen(new RetryWithDelay(3, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxUtils.<BaseJson<String>>bindToLifecycle(mRootView))//使用RXlifecycle,使subscription和activity一起销毁
                .subscribe(new ErrorHandleSubscriber<BaseJson<String>>(mErrorHandler) {
                    @Override
                    public void onNext(BaseJson<String> jsonObjectBaseJson) {
                        if (jsonObjectBaseJson.getStatus() == 0) {
                            mRootView.setImage(i,jsonObjectBaseJson.getData(),size,url,0);
                        } else {
                            mRootView.showMessage(jsonObjectBaseJson.getMsgs());
                            mRootView.setImage(i,"",size,url,1);
                        }
                    }
                });

    }

}
