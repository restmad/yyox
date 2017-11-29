package com.yyox.mvp.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.util.ArrayMap;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.alipay.sdk.app.PayTask;
import com.google.gson.JsonObject;
import com.jess.arms.base.DefaultAdapter;
import com.jess.arms.utils.UiUtils;
import com.tbruyelle.rxpermissions.RxPermissions;
import com.yyox.R;
import com.yyox.Utils.CommonUtils;
import com.yyox.Utils.MyToast;
import com.yyox.Utils.PayResult;
import com.yyox.consts.CodeDefine;
import com.yyox.di.component.DaggerUserComponent;
import com.yyox.di.module.UserModule;
import com.yyox.mvp.contract.UserContract;
import com.yyox.mvp.model.entity.CouponItem;
import com.yyox.mvp.model.entity.User;
import com.yyox.mvp.presenter.UserPresenter;

import java.util.Map;

import butterknife.BindView;
import common.AppComponent;
import common.WEActivity;

public class UserRechargeActivity extends WEActivity<UserPresenter> implements UserContract.View {

    @Nullable
    @BindView(R.id.code)
    public EditText mCode;

    @Nullable
    @BindView(R.id.level)
    public EditText mLevel;

    @Nullable
    @BindView(R.id.phone)
    public EditText mPhone;

    @Nullable
    @BindView(R.id.money)
    public EditText mMoney;

    @Nullable
    @BindView(R.id.vipDate)
    public LinearLayout mVipDate;

    @Nullable
    @BindView(R.id.user_recharge)
    Button mRecharge;

    private static final int SDK_PAY_FLAG = 1;
    private Drawable mErrorIcon;
    public double doubleMoney;
    private RxPermissions mRxPermissions;

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
                        mPresenter.requsetVerifyResult(stringObjectMap);
                    }
                    break;
                }
                default:
                    break;
            }
        }
    };

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

    @Override
    protected View initView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_user_recharge, null, false);
    }

    @Override
    protected void initData() {
        mPresenter.requestUserVip();

        mErrorIcon = getResources().getDrawable(R.mipmap.error);
        mErrorIcon.setBounds(new Rect(0, 0, mErrorIcon.getIntrinsicWidth(), mErrorIcon.getIntrinsicHeight()));
        mMoney.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String number = mMoney.getText().toString();
                if (number == null || number.isEmpty()){
                    mRecharge.setText("确认支付0.00元");
                }else {
                    mRecharge.setText("确认支付"+CommonUtils.doubleFormat(number)+"元");
                }

                check("money", number);
            }
        });
    }

    private void check(String type, String value) {
        switch (type) {
            case "money": {
                if (value.isEmpty()) {
                } else if (value.equals("0")) {
                    mMoney.setError("金额不能为0", mErrorIcon);
                } else if (!CommonUtils.isChineseNumber(value)) {
                    mMoney.setError("充值金额超过限制", mErrorIcon);
                }
            }
        }
    }

    public void btn_back_click(View v) {
        setResult(CodeDefine.MINE_PEGISTER_RESULT);
        this.finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {

            setResult(CodeDefine.MINE_PEGISTER_RESULT);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void setAdapter(DefaultAdapter adapter) {

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
    public void UserResult(JsonObject jsonObject) {

    }

    @Override
    public void setUserVip(String member, String expiryDate, String balanceCny) {
        if (!expiryDate.isEmpty() && !member.equals("普通会员")) {
            mVipDate.setVisibility(View.VISIBLE);
        }
        mCode.setText(member);
        mLevel.setText(expiryDate);
        mPhone.setText(balanceCny + "元");
        mMoney.setText("");
    }

    @Override
    public void aliPayReturn(String data) {
        if (!data.isEmpty()) {
            Runnable payRunnable = new Runnable() {
                @Override
                public void run() {
                    // 构造PayTask 对象
                    PayTask alipay = new PayTask(UserRechargeActivity.this);
                    // 调用支付接口，获取支付结果
                    String result = alipay.pay(data, true);

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
    public void getCouponItem(CouponItem couponItem) {

    }

    @Override
    public void datadFew() {

    }

    @Override
    public void SetUserInfo(User user) {

    }

    @Override
    public void alterPassword() {

    }

    public void recharge_click(View v) {
        String rechare = "充值";
        String money = mMoney.getText().toString();
        if (!money.isEmpty()) {
            doubleMoney = Double.valueOf(money).doubleValue();
        }
        if (money.isEmpty() || money.equals(" ")) {
            mMoney.setError("金额不能为空", mErrorIcon);
        } else if (money.equals("0")) {
            mMoney.setError("金额不能为0", mErrorIcon);
        } else if (!CommonUtils.isChineseNumber(money)) {
            mMoney.setError("充值金额超过限制", mErrorIcon);
        } else if (doubleMoney < 0.1) {
            showMessage("充值金额不能低于0.1元");
        } else {
            mPresenter.requestPayRechare(rechare, doubleMoney);
        }
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showMessage(String message) {
//        mPresenter.requestUserVip();
        if (!message.isEmpty() && message.equals("充值成功")) {
            setResult(CodeDefine.MINE_PEGISTER_RESULT);
            finish();
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
        DaggerUserComponent
                .builder()
                .appComponent(appComponent)
                .userModule(new UserModule(this))
                .build()
                .inject(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRxPermissions = null;
    }
}
