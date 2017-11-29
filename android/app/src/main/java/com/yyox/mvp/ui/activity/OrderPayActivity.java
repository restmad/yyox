package com.yyox.mvp.ui.activity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.util.ArrayMap;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;;

import com.alipay.sdk.app.PayTask;
import com.jess.arms.base.DefaultAdapter;
import com.jess.arms.utils.UiUtils;
import com.tbruyelle.rxpermissions.RxPermissions;
import com.yyox.R;
import com.yyox.Utils.CommonUtils;
import com.yyox.Utils.MyToast;
import com.yyox.Utils.PayResult;
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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import common.AppComponent;
import common.WEActivity;

public class OrderPayActivity extends WEActivity<OrderPresenter> implements OrderContract.View {

    @Nullable
    @BindView(R.id.tv_activity_order_pay_number)
    TextView mTextView_Number;

    @Nullable
    @BindView(R.id.tv_activity_order_pay_money)
    TextView mTextView_Money;

    @Nullable
    @BindView(R.id.rg_activity_order_pay_type)
    RadioGroup mRadioGroup;

    @Nullable
    @BindView(R.id.rb_activity_order_pay_alipay)
    RadioButton mRadioButton_Alipay;

    @Nullable
    @BindView(R.id.rb_activity_order_pay_balance)
    RadioButton mRadioButton_Balance;

    @Nullable
    @BindView(R.id.tv_activity_remaining_money)
    TextView mRemaining_money;

    @Nullable
    @BindView(R.id.order_pay_btn)
    Button mOrderPayBtn;

    private static final int SDK_PAY_FLAG = 1;

    private String mOrderNo;
    private String mPayType;
    private String mPayTypeComments;
    private int PAY_TYPE = 0;
    private double dTotalAmount;
    private RxPermissions mRxPermissions;

    public void btn_back_click(View v) {

        new AlertDialog.Builder(this).setTitle("放弃支付")
                .setMessage("确认放弃支付该订单吗?")
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
                                //确认
                                back();
                            }
                        }).show();
    }

    private void back() {
        Intent intent = new Intent();
        intent.putExtra("pay", 12);
        intent.putExtra("orderNo", mOrderNo);
        setResult(CodeDefine.ORDER_DETAIL_PAY_RESULT, intent);
        this.finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            new AlertDialog.Builder(this).setTitle("放弃支付")
                    .setMessage("确认放弃支付该订单吗?")
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
                                    //确认
                                    backTwo();
                                }
                            }).show();
        }
        return super.onKeyDown(keyCode, event);
    }

    private void backTwo() {
        Intent intent = new Intent();
        intent.putExtra("pay", 12);
        if (getIntent().getIntExtra("type", 10) == OrderStatus.ORDER_STATUS_BOX_PACKAGE) {
            intent.putExtra("type", OrderStatus.ORDER_STATUS_BOX_PACKAGE);
        }
        intent.putExtra("orderNo", mOrderNo);
        setResult(CodeDefine.ORDER_DETAIL_PAY_RESULT, intent);
        this.finish();
    }

    private Map<String, Object> stringToMap(String string) {
        Map<String, Object> stringObjectMap = new ArrayMap<>();
        String[] strings = string.split("&");
        for (int i = 0; i < strings.length; i++) {
            String temp = strings[i];
            int iIndex = temp.indexOf("=");
            String key = temp.substring(0, iIndex);
            String value = temp.substring(iIndex + 1, temp.length());
            stringObjectMap.put(key, value);
        }
        return stringObjectMap;
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    PayResult payResult = new PayResult((String) msg.obj);
                    Log.i("返回", (String) msg.obj);
                    /**
                     * 同步返回的结果必须放置到服务端进行验证（验证的规则请看https://doc.open.alipay.com/doc2/
                     * detail.htm?spm=0.0.0.0.xdvAU6&treeId=59&articleId=103665&
                     * docType=1) 建议商户依赖异步通知
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息

                    if (resultInfo != null && !resultInfo.isEmpty()) {
                        Map<String, Object> stringObjectMap = stringToMap(resultInfo);
                        String input_charset = (String) stringObjectMap.get("_input_charset");
                        String body = (String) stringObjectMap.get("body");
                        String currency = (String) stringObjectMap.get("currency");
                        String forex_biz = (String) stringObjectMap.get("forex_biz");
                        String notify_id = (String) stringObjectMap.get("notify_id");
                        String notify_time = (String) stringObjectMap.get("notify_time");
                        String notify_type = (String) stringObjectMap.get("notify_type");
                        String notify_url = (String) stringObjectMap.get("notify_url");
                        String out_trade_no = (String) stringObjectMap.get("out_trade_no");
                        String partner = (String) stringObjectMap.get("partner");
                        String payment_type = (String) stringObjectMap.get("payment_type");
                        String return_url = (String) stringObjectMap.get("return_url");
                        String rmb_fee = (String) stringObjectMap.get("rmb_fee");
                        String seller_id = (String) stringObjectMap.get("seller_id");
                        String service = (String) stringObjectMap.get("service");
                        String sign = (String) stringObjectMap.get("sign");
                        String sign_type = (String) stringObjectMap.get("sign_type");
                        String subject = (String) stringObjectMap.get("subject");
                        String success = (String) stringObjectMap.get("success");
                        String trade_no = (String) stringObjectMap.get("trade_no");
                        String trade_status = (String) stringObjectMap.get("trade_status");

                        mPresenter.payVerifyResult(1, mOrderNo, stringObjectMap);
                    }
                    break;
                }
                default:
                    break;
            }
        }
    };

    /**
     * call alipay sdk pay. 调用SDK支付
     */
    public void affirm_pay_click(View v) throws UnsupportedEncodingException {
        if (0 == PAY_TYPE) {
            //支付宝支付
            mPresenter.requsetpayOrder(mOrderNo, URLEncoder.encode(mPayTypeComments, "utf8"), dTotalAmount, mPayType);
        } else {
            //余额支付
            if (getIntent().getStringExtra("payTypeComments").equals("支付税金")) {
                mPresenter.requsetPayBalance(mOrderNo, "1");
            } else {
                mPresenter.requsetPayBalance(mOrderNo, "0");
            }
        }
    }

    public void order_detail_click(View v) {
        //跳转包裹详情界面
        Intent intent = new Intent(this, OrderPackageDetailActivity.class);
        intent.putExtra("orderNo", mOrderNo);//订单号
        intent.putExtra("type", OrderStatus.ORDER_STATUS_PACKAGES);
        startActivity(intent);

    }

    /**
     * get the sdk version. 获取SDK版本号
     */
    public void getSDKVersion() {
        PayTask payTask = new PayTask(this);
        String version = payTask.getVersion();
        Toast.makeText(this, version, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        this.mRxPermissions = new RxPermissions(this);
        DaggerOrderComponent.builder().appComponent(appComponent).orderModule(new OrderModule(this)).build().inject(this);
    }

    @Override
    protected void initData() {
        mOrderNo = getIntent().getStringExtra("orderNo");
        mPayType = getIntent().getStringExtra("payType");
        mPayTypeComments = getIntent().getStringExtra("payTypeComments");
        mTextView_Number.setText(mOrderNo);
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {

                if (mRadioButton_Alipay.getId() == checkedId) {
                    PAY_TYPE = 0;
                }
                if (mRadioButton_Balance.getId() == checkedId) {
                    PAY_TYPE = 1;
                }
            }
        });

        mPresenter.payVerifyResult(1, mOrderNo,new ArrayMap<>());
    }

    @Override
    protected View initView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_order_pay, null, false);
    }

    @Override
    public void setUIValue(OrderDetail orderDetailJson) {

    }

    @Override
    public void setFeeValue(Fee fee) {

    }

    @Override
    public void setPayMoney(int type, double totalAmount, double balanceCny, int payStatus) {
        if (payStatus == 0) {
            //showMessage("支付成功");
            paySuccess();
        } else if (2 == payStatus) {
            dTotalAmount = totalAmount;
            mTextView_Money.setText(CommonUtils.doubleFormat(String.valueOf(totalAmount)) + "元");
            mRemaining_money.setText(CommonUtils.doubleFormat(String.valueOf(balanceCny)) + "元");
            mOrderPayBtn.setText("确认支付"+CommonUtils.doubleFormat(String.valueOf(totalAmount)) + "元");
            if (balanceCny >= totalAmount) {
                mRadioButton_Balance.setEnabled(true);
            } else {
                mRadioButton_Balance.setEnabled(false);
                PAY_TYPE = 0;
            }
        }else if(3 == payStatus){
            showMessage("支付失败,请重新支付");
        }
    }

    @Override
    public void setOrderInfo(String orderInfo) {
        if (!orderInfo.isEmpty()) {
            Runnable payRunnable = new Runnable() {
                @Override
                public void run() {
                    // 构造PayTask 对象
                    PayTask alipay = new PayTask(OrderPayActivity.this);
                    // 调用支付接口，获取支付结果
                    String result = alipay.pay(orderInfo, true);

                    Message msg = new Message();
                    msg.what = SDK_PAY_FLAG;
                    msg.obj = result;
                    mHandler.sendMessage(msg);
                }
            };

            // 必须异步调用
            Thread payThread = new Thread(payRunnable);
            payThread.start();
        }
    }

    @Override
    public void setPayBalance(int status) {
        if (0 == status) {
            paySuccess();
        } else {
            showMessage("支付失败");
        }
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
    public void setAdapter(DefaultAdapter adapter) {

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

    private void paySuccess() {
        Intent intent = new Intent(this, OrderPaySuccessActivity.class);
        intent.putExtra("orderNo", mOrderNo);
        if (getIntent().getStringExtra("payTypeComments").equals("支付税金")) {
            intent.putExtra("payTypeComments", "支付税金");
        }
        startActivityForResult(intent, CodeDefine.ORDER_PAY_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CodeDefine.ORDER_PAY_REQUEST && resultCode == CodeDefine.ORDER_PAY_RESULT) {
            Intent intent = new Intent();
            if (getIntent().getIntExtra("type", 10) == OrderStatus.ORDER_STATUS_BOX_PACKAGE) {
                intent.putExtra("type", OrderStatus.ORDER_STATUS_BOX_PACKAGE);
            }
            setResult(CodeDefine.ORDER_DETAIL_RESULT);
            this.finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.mRxPermissions = null;
    }
}
