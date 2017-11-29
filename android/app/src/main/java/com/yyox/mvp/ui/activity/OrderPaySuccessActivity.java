package com.yyox.mvp.ui.activity;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jess.arms.base.DefaultAdapter;
import com.jess.arms.utils.UiUtils;
import com.tbruyelle.rxpermissions.RxPermissions;
import com.yyox.R;
import com.yyox.Utils.DateUtils;
import com.yyox.Utils.MyToast;
import com.yyox.consts.AddressType;
import com.yyox.consts.CodeDefine;
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

public class OrderPaySuccessActivity extends WEActivity<OrderPresenter> implements OrderContract.View {

    @Nullable
    @BindView(R.id.tv_activity_order_pay_success_orderno)
    TextView mTextView_OrderNo;

    @Nullable
    @BindView(R.id.ll_activity_order_pay_success_card)
    LinearLayout mLinearLayout_Card;

    @Nullable
    @BindView(R.id.ll_activity_order_pay_success_tracking)
    LinearLayout mLinearLayout_Tracking;

    @Nullable
    @BindView(R.id.tv_activity_order_pay_success_tracking_date)
    TextView mTextView_Date;

    @Nullable
    @BindView(R.id.tv_activity_order_pay_success_tracking_detail)
    TextView mTextView_Detail;

    @Nullable
    @BindView(R.id.textView)
    TextView mTextView;


    private int mAddressId = 0;
    private RxPermissions mRxPermissions;

    @Override
    protected View initView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_order_pay_success, null, false);
    }

    @Override
    protected void initData() {
        String orderno = getIntent().getStringExtra("orderNo");
        if (!orderno.isEmpty()) {
            mTextView_OrderNo.setText("邮客单号：" + orderno);
            mPresenter.requsetCardCheck(orderno);
        }
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
        String payTypeComments = getIntent().getStringExtra("payTypeComments") + "";
        if (!payTypeComments.isEmpty() && payTypeComments.equals("支付税金")) {
            mLinearLayout_Card.setVisibility(View.GONE);
            mLinearLayout_Tracking.setVisibility(View.GONE);
            mTextView.setText("您的税金已经支付成功，清关完成后我们将尽快完成配送，请关注订单状态。");
        } else {
            if (check) {
                //需要上传身份证
                mTextView.setText("您的订单已经完成支付，请尽快上传身份证照片，以便仓库安排出库。");
                mLinearLayout_Card.setVisibility(View.VISIBLE);
                mLinearLayout_Tracking.setVisibility(View.GONE);
                mAddressId = id;
            } else {
                //不需要上传身份证
                mTextView.setText("您的订单已经支付成功，我们将尽快安排出库，发往国内，请关注订单状态。");
                mLinearLayout_Card.setVisibility(View.GONE);
                mLinearLayout_Tracking.setVisibility(View.VISIBLE);
                mTextView_Date.setText(DateUtils.getTodayDate()+DateUtils.getCurrentTime());
                mTextView_Detail.setText("您的订单(" + getIntent().getStringExtra("orderNo") + ")已经创建");
            }
        }
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

    }

    @Override
    public void hideLoading() {

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
        DaggerOrderComponent.builder().appComponent(appComponent).orderModule(new OrderModule(this)).build().inject(this);
    }

    public void btn_back_click(View v) {
        setResult(CodeDefine.ORDER_PAY_RESULT);
        this.finish();
    }

    public void btn_upload_click(View v) {
        Intent intent = new Intent(this, AddressSaveActivity.class);
        intent.putExtra("type", AddressType.ADDRESS_TYPE_UPLOAD);
        intent.putExtra("addressid", mAddressId);
        startActivityForResult(intent, CodeDefine.ADDRESS_SAVE_REQUEST);
    }

    public void btn_no_upload_click(View v) {
        setResult(CodeDefine.ORDER_PAY_RESULT);
        this.finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            setResult(CodeDefine.ORDER_PAY_RESULT);
            this.finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CodeDefine.ADDRESS_SAVE_REQUEST && resultCode == CodeDefine.ADDRESS_SAVE_RESULT) {
            setResult(CodeDefine.ORDER_PAY_RESULT);
            this.finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.mRxPermissions = null;
    }
}
