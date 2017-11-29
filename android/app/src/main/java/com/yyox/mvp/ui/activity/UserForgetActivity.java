package com.yyox.mvp.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.JsonObject;
import com.jess.arms.base.DefaultAdapter;
import com.jess.arms.utils.UiUtils;
import com.tbruyelle.rxpermissions.RxPermissions;
import com.yyox.R;
import com.yyox.Utils.CommonUtils;
import com.yyox.Utils.MyToast;
import com.yyox.Utils.NoDoubleClickUtils;
import com.yyox.consts.CodeDefine;
import com.yyox.di.component.DaggerUserComponent;
import com.yyox.di.module.UserModule;
import com.yyox.mvp.contract.UserContract;
import com.yyox.mvp.model.entity.CouponItem;
import com.yyox.mvp.model.entity.User;
import com.yyox.mvp.presenter.UserPresenter;

import butterknife.BindView;
import common.AppComponent;
import common.WEActivity;

public class UserForgetActivity extends WEActivity<UserPresenter> implements UserContract.View {

    private SmsCount smsCount;

    @Nullable
    @BindView(R.id.et_forget_phone)
    EditText mEditText_Phone;

    @Nullable
    @BindView(R.id.et_forget_password)
    EditText mEditText_Password;

    @Nullable
    @BindView(R.id.et_forget_code)
    EditText mEditText_Code;
    private RxPermissions mRxPermissions;

    public void btn_back_click(View v) {
        this.finish();
    }

    public void btn_forget_authcode_click(View v) {

        if (NoDoubleClickUtils.isDoubleClick()) {
            return;
        }

        String phone = mEditText_Phone.getText().toString();
        if (phone.isEmpty()) {
            showMessage("请输入手机号码");
        } else if (!CommonUtils.isPhoneNumber(phone)) {
            showMessage("请输入正确的手机号码");
        } else {
            initTimer();
            ((Button) findViewById(R.id.btn_forget_authcode)).setBackgroundColor(Color.parseColor("#c4c4c4"));
            ((Button) findViewById(R.id.btn_forget_authcode)).setTextColor(Color.parseColor("#ff0000"));
            ((Button) findViewById(R.id.btn_forget_authcode)).setEnabled(false);

            mPresenter.requestAuthCode(phone);

        }
    }

    public void btn_forget_click(View v) {

        if (NoDoubleClickUtils.isDoubleClick()) {
            return;
        }

        String phone = mEditText_Phone.getText().toString();
        String password = mEditText_Password.getText().toString();
        String code = mEditText_Code.getText().toString();
        if (phone.isEmpty()) {
            showMessage("请输入手机号码");
        } else if (!CommonUtils.isPhoneNumber(phone)) {
            showMessage("请输入正确的手机号码");
        } else if (password.isEmpty()) {
            showMessage("请输入6~12位新密码");
        } else if (!CommonUtils.checkPassword(password)) {
            showMessage("密码格式不正确");
        } else if (code.isEmpty()) {
            showMessage("请输入验证码");
        } else if (code.length() != 5) {
            showMessage("验证码位数不正确");
        } else {
            mPresenter.requestForget(phone, password, code);
        }
    }

    @Override
    protected View initView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_user_forget, null, false);
    }

    @Override
    protected void initData() {

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
        Intent intent = new Intent();
        intent.putExtra("username", "");
        setResult(CodeDefine.FORGET_RESULT, intent);
        finish();
    }

    @Override
    public void setUserVip(String member, String expiryDate, String balanceCny) {

    }

    @Override
    public void aliPayReturn(String data) {

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

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showMessage(String message) {
        if (!message.isEmpty()){
        MyToast.makeText(this, message, 3 * 1000).show();
        }
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
        DaggerUserComponent.builder().appComponent(appComponent).userModule(new UserModule(this)).build().inject(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (smsCount != null) {
            smsCount.cancel();
        }
        mRxPermissions = null;
    }

    private void initTimer() {
        smsCount = new SmsCount(60 * 1000, 100);
        smsCount.start();
    }

    /*
    验证码倒计时类
     */
    class SmsCount extends CountDownTimer {

        public SmsCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            ((Button) findViewById(R.id.btn_forget_authcode)).setBackgroundColor(Color.parseColor("#1e81d1"));
            ((Button) findViewById(R.id.btn_forget_authcode)).setTextColor(Color.parseColor("#ffffff"));
            ((Button) findViewById(R.id.btn_forget_authcode)).setEnabled(true);
            ((Button) findViewById(R.id.btn_forget_authcode)).setText("发送");
        }

        @Override
        public void onTick(long millisUntilFinished) {
            ((Button) findViewById(R.id.btn_forget_authcode)).setText("" + millisUntilFinished / 1000 % 60 + "秒");
        }
    }

}
