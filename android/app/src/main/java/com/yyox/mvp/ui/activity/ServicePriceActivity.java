package com.yyox.mvp.ui.activity;

import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.AbsoluteSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TextView.BufferType;

import com.jess.arms.base.DefaultAdapter;
import com.jess.arms.utils.UiUtils;
import com.tbruyelle.rxpermissions.RxPermissions;
import com.yyox.R;
import com.yyox.Utils.CommonUtils;
import com.yyox.Utils.MyToast;
import com.yyox.di.component.DaggerOrderComponent;
import com.yyox.di.module.OrderModule;
import com.yyox.mvp.contract.OrderContract;
import com.yyox.mvp.model.entity.Fee;
import com.yyox.mvp.model.entity.OrderDetail;
import com.yyox.mvp.model.entity.OrderHistoryItem;
import com.yyox.mvp.model.entity.OrderTypes;
import com.yyox.mvp.model.entity.PriceQuery;
import com.yyox.mvp.presenter.OrderPresenter;
import com.yyox.mvp.ui.adapter.OrderPackageDetailAdapter;
import com.yyox.mvp.ui.adapter.PackageExpandableAdapter;
import com.yyox.mvp.ui.adapter.QuestionExpandableAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.qqtheme.framework.picker.OptionPicker;
import common.AppComponent;
import common.WEActivity;

public class ServicePriceActivity extends WEActivity<OrderPresenter> implements OrderContract.View, View.OnClickListener {

    @Nullable
    @BindView(R.id.service_warehouse_text)
    TextView mService_warehouse_text;

    @Nullable
    @BindView(R.id.transport_service_text)
    TextView mTransport_service_text;

    @Nullable
    @BindView(R.id.weight_weight_edit)
    EditText mWeight_weight_edit;

    @Nullable
    @BindView(R.id.freight_money)
    TextView mFreight_money;

    @Nullable
    @BindView(R.id.iv_service_warehouse)
    ImageView mIv_service_warehouse;

    @Nullable
    @BindView(R.id.iv_service_channel)
    ImageView mIv_servic_channel;

    private List<PriceQuery> priceQiery = new ArrayList<>();
    private int warehouseId = 0;
    private int ordertypeId = 0;
    private int leadId = 0;
    private String orderType = null;
    private Drawable mErrorIcon;
    private RxPermissions mRxPermissions;

    @Override
    protected View initView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_service_price, null, false);
    }

    public void btn_back_click(View view) {
        finish();
    }

    @Override
    protected void initData() {
        SpannableString styledText = new SpannableString("RMB"+0.00);
        styledText.setSpan(new AbsoluteSizeSpan(15,true), 0, 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        styledText.setSpan(new AbsoluteSizeSpan(20,true), 3, ("RMB"+0.00).length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

//        mFreight_money.setText(styledText, TextView.BufferType.SPANNABLE);
        mFreight_money.setHint(styledText);
        mPresenter.requesPriceQuery();
        mErrorIcon = getResources().getDrawable(R.mipmap.error);
        mErrorIcon.setBounds(new Rect(0, 0, mErrorIcon.getIntrinsicWidth(), mErrorIcon.getIntrinsicHeight()));
        mWeight_weight_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String weight = mWeight_weight_edit.getText().toString();
                check("weight", weight);
            }
        });
        mWeight_weight_edit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b) {
                    String weight = mWeight_weight_edit.getText().toString();
                    check("weight", weight);
                } else {
                    String warehouse = mService_warehouse_text.getText().toString();
                    if (warehouse.isEmpty()) {
                        mIv_service_warehouse.setVisibility(View.VISIBLE);
                        showMessage("请选择仓库");
                    } else {
                        mIv_service_warehouse.setVisibility(View.GONE);
                    }
                    String channel = mTransport_service_text.getText().toString();
                    if (channel.isEmpty()) {
                        mIv_servic_channel.setVisibility(View.VISIBLE);
                        showMessage("请选择服务");
                    }/* else if (!channel.contains(warehouse)) {//说明需要重新选择服务
                        mTransport_service_text.setText(null);
                        mIv_servic_channel.setVisibility(View.VISIBLE);
                        showMessage("请重新选择服务");
                    }*/ else {
                        mIv_servic_channel.setVisibility(View.GONE);
                    }
                }
            }
        });
    }

    private void check(String type, String value) {
        switch (type) {
            case "weight":
                if (value.isEmpty()) {
                    mWeight_weight_edit.setError("请输入重量", mErrorIcon);
                }
                break;
        }

    }

    /**
     * 计算价格
     *
     * @param view
     */
    public void freight_compute(View view) {
        if (mService_warehouse_text.getText().toString().isEmpty()) {
            mIv_service_warehouse.setVisibility(View.VISIBLE);
            showMessage("请选择仓库");
        } else if (mTransport_service_text.getText().toString().isEmpty()) {
            mIv_servic_channel.setVisibility(View.VISIBLE);
            showMessage("请选择服务");
        } else if (mWeight_weight_edit.getText().toString().isEmpty()) {
            showMessage("请输入重量");
        } else {
            String warehouse = mService_warehouse_text.getText().toString();
            String estimatedWeight = mWeight_weight_edit.getText().toString();//重量
            double doubleWeight = Double.valueOf(estimatedWeight).doubleValue();
            String channel = mTransport_service_text.getText().toString();//
            for (PriceQuery priceQueryList : priceQiery) {
                if (priceQueryList.getName().equals(warehouse)) {
                    warehouseId = priceQueryList.getId();
                }
                for (OrderTypes orderTypes : priceQueryList.getOrderTypeList()) {
                    if (channel.equals(orderTypes.getName() /*+ " (" + priceQueryList.getName() + ")"*/)) {
                        orderType = orderTypes.getCode();
                        ordertypeId = orderTypes.getId();
                        leadId = orderTypes.getLeadId();
                    }
                }
            }
            mPresenter.requestCalculateFeeTool(orderType, doubleWeight, warehouseId, ordertypeId, leadId);
        }
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void setAdapter(DefaultAdapter adapter) {

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

    }

    @Override
    public void endLoadMore() {

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
        priceQiery = mPriceQuery;
    }

    @Override
    public void priceFee(String totalCostStr) {
//        mFreight_money.setText("RMB："+totalCostStr);

        SpannableString styledText = new SpannableString("RMB"+totalCostStr);
        styledText.setSpan(new AbsoluteSizeSpan(15,true), 0, 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        styledText.setSpan(new AbsoluteSizeSpan(20,true), 3, ("RMB"+totalCostStr).length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mFreight_money.setText(styledText, BufferType.SPANNABLE);
    }


    @Override
    public void getShare(OrderHistoryItem orderHistoryItem) {

    }

    @Override
    public void setOnClick() {

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showMessage(String message) {
        mFreight_money.setText(null);
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
        DaggerOrderComponent.builder().appComponent(appComponent).orderModule(new OrderModule(this)).build().inject(this);
    }

    public void btn_rule_click(View v) {
        Intent intent = new Intent(this, WebActivity.class);
//        intent.putExtra("showurl", "http://www.baidu.com");
        intent.putExtra("showurl",CommonUtils.getBaseUrl()+"/app/PriceRule/rule.html#/USA/yy");
        intent.putExtra("title", "计价规则");
        startActivity(intent);
    }

    /**
     * 仓库列表
     *
     * @param view
     */
    public void rl_order_report_warehouse_click(View view) {
        List<String> warehouseList = new ArrayList<>();
        for (PriceQuery priceQuery : priceQiery) {
            warehouseList.add(priceQuery.getName());
        }
        OptionPicker picker = new OptionPicker(this, warehouseList);
        picker.setCycleDisable(true);
        picker.setLineVisible(false);
        picker.setTextSize(14);
        picker.setOnOptionPickListener(new OptionPicker.OnOptionPickListener() {
            @Override
            public void onOptionPicked(int index, String item) {
                mService_warehouse_text.setText(item);
                mTransport_service_text.setText("");
                String channel = mTransport_service_text.getText().toString();
                String warehouse = mService_warehouse_text.getText().toString();
                mIv_service_warehouse.setVisibility(View.GONE);
                if (channel.isEmpty()) {
                    mIv_servic_channel.setVisibility(View.VISIBLE);
                    showMessage("请选择服务");
                } /*else if (!channel.contains(warehouse)) {
                    mIv_servic_channel.setVisibility(View.VISIBLE);
                    mTransport_service_text.setText(null);
                    showMessage("请重新选择服务");
                }*/ else {
                    mIv_servic_channel.setVisibility(View.GONE);
                }
            }
        });
        picker.show();
    }

    /**
     * 仓库对应的服务列表
     *
     * @param view
     */
    public void rl_order_report_channel_click(View view) {
        List<String> orderTypeList = new ArrayList<>();
        String warehouse = mService_warehouse_text.getText().toString();
        if (warehouse.equals("")) {//展示所有服务列表
            showMessage("请先选择仓库");
            mIv_service_warehouse.setVisibility(View.VISIBLE);
            return;
           /* for (PriceQuery priceQueryList : priceQiery) {
                for (OrderTypes orderTypes : priceQueryList.getOrderTypeList()) {
                    orderTypeList.add(orderTypes.getName() + " (" + priceQueryList.getName() + ")");
                }
            }*/
        } else if (!warehouse.equals("")) {//展示仓库对应的服务列表
            for (PriceQuery prceQueryList : priceQiery) {
                if (warehouse.equals(prceQueryList.getName())) {
                    for (OrderTypes orderYType : prceQueryList.getOrderTypeList()) {
                        orderTypeList.add(orderYType.getName() /*+ " (" + prceQueryList.getName() + ")"*/);
                    }
                }
            }
        }
        OptionPicker picker = new OptionPicker(this, orderTypeList);
        picker.setCycleDisable(true);
        picker.setLineVisible(false);
        picker.setTextSize(14);
        picker.setOnOptionPickListener(new OptionPicker.OnOptionPickListener() {
            @Override
            public void onOptionPicked(int index, String item) {
                mTransport_service_text.setText(item);
                mIv_servic_channel.setVisibility(View.GONE);
                String channel = mTransport_service_text.getText().toString();

                if (warehouse.isEmpty()) {
                    mIv_service_warehouse.setVisibility(View.VISIBLE);
                    showMessage("请选择仓库");
                } /*else if (!channel.contains(warehouse)) {
                    mIv_service_warehouse.setVisibility(View.VISIBLE);
                    mTransport_service_text.setText(null);
                    showMessage("请重新选择服务");
                }*/ else {
                    mIv_service_warehouse.setVisibility(View.GONE);
                }
            }
        });
        picker.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.mRxPermissions = null;
    }
}
