package com.yyox.mvp.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.yyox.mvp.model.realm.RealmUser;
import com.yyox.mvp.presenter.UserPresenter;

import butterknife.BindView;
import common.AppComponent;
import common.WEActivity;
import common.WEApplication;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;


public class UserRegisterActivity extends WEActivity<UserPresenter> implements UserContract.View {

    private SmsCount smsCount;

    @Nullable
    @BindView(R.id.et_register_phone)
    EditText mEditText_Phone;

    @Nullable
    @BindView(R.id.et_register_email)
    EditText mEditText_Email;

    @Nullable
    @BindView(R.id.et_register_password)
    EditText mEditText_Password;

    @Nullable
    @BindView(R.id.et_register_code)
    EditText mEditText_Code;
    private User user;
    private RxPermissions mRxPermissions;

    public void btn_back_click(View v) {
        this.finish();
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
        this.mRxPermissions = null;
    }

    @Override
    protected View initView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_user_register, null, false);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    public void showMessage(String message) {
        if (!message.isEmpty()) {
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

    public void bt_authcode_click(View v) {

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
            ((Button) findViewById(R.id.authcode_btn)).setBackgroundColor(Color.parseColor("#c4c4c4"));
            ((Button) findViewById(R.id.authcode_btn)).setTextColor(Color.parseColor("#ff0000"));
            ((Button) findViewById(R.id.authcode_btn)).setEnabled(false);

            mPresenter.requestAuthCode(phone);

        }
    }

    public void bt_register_click(View v) {

        if (NoDoubleClickUtils.isDoubleClick()) {
            return;
        }

        String phone = mEditText_Phone.getText().toString();
        String email = mEditText_Email.getText().toString();
        String password = mEditText_Password.getText().toString();
        String code = mEditText_Code.getText().toString();
        user = new User();
        user.setPhone(phone);
        user.setEmail(email);
        String invite = "";
        if (phone.isEmpty()) {
            showMessage("请输入手机号码");
        } else if (!CommonUtils.isPhoneNumber(phone)) {
            showMessage("请输入正确的手机号码");
        } else if (email.isEmpty()) {
            showMessage("请输入邮箱地址");
        } else if (!CommonUtils.isEmail(email)) {
            showMessage("请输入正确的邮箱地址");
        } else if (password.isEmpty()) {
            showMessage("请输入6~12位密码");
        } else if (!CommonUtils.checkPassword(password)) {
            showMessage("请输入6-12位（字母，数字）组合密码");
        } else if (code.isEmpty()) {
            showMessage("请输入验证码");
        } else {
            mPresenter.requestRegister(phone, email, password, code, invite);
        }
    }

    public void btn_agreement_click(View v) {
        if (NoDoubleClickUtils.isDoubleClick()) {
            return;
        }
        Intent intent = new Intent(UserRegisterActivity.this,WebActivity.class);
        intent.putExtra("showurl", CommonUtils.getBaseUrl() + "/app/mesage/condition/usecondition.html");
        intent.putExtra("title", "使用条件及会员条款");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void initTimer() {
        smsCount = new SmsCount(60 * 1000, 100);
        smsCount.start();
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
        showMessage("注册成功");
        if (jsonObject != null) {
            String phone = user.getPhone();
            RealmConfiguration config = new RealmConfiguration.Builder().name("yyox.realm").build();
            Realm objRealm = Realm.getInstance(config);

            //查找
            RealmUser realmUser = objRealm.where(RealmUser.class).findFirst();
            if (realmUser != null) {
                //先删除
                objRealm.beginTransaction();
                RealmResults results = objRealm.where(RealmUser.class).findAll();
                results.deleteAllFromRealm();
                objRealm.commitTransaction();
                //再添加
                objRealm.beginTransaction();
                RealmUser realmUserNew = objRealm.createObject(RealmUser.class);
                realmUserNew.setName(phone);
                realmUserNew.setEmail(jsonObject.get("mail").getAsString());
                objRealm.commitTransaction();
            } else {
                //添加
                objRealm.beginTransaction();
                RealmUser realmUserNew = objRealm.createObject(RealmUser.class);
                realmUserNew.setName(phone);
                realmUserNew.setEmail(jsonObject.get("mail").getAsString());
                objRealm.commitTransaction();
            }

            WEApplication weApplication = (WEApplication) getApplication();
            weApplication.setRealmUser(objRealm.where(RealmUser.class).findFirst());

            SharedPreferences sharedPreferences = this.getSharedPreferences("userEmail", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("Email",jsonObject.get("mail").getAsString());
            editor.putString("phone",phone);
            SharedPreferences sharedPreferencess =  WEApplication.getContext().getSharedPreferences(jsonObject.get("mail").getAsString(), Context.MODE_PRIVATE);
            editor.commit();

            Intent intent = new Intent();
            intent.putExtra("username", phone);
            setResult(CodeDefine.REGISTER_RESULT, intent);
            finish();
        }
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

    /*
    验证码倒计时类
     */
    class SmsCount extends CountDownTimer {

        public SmsCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            ((Button) findViewById(R.id.authcode_btn)).setBackgroundColor(Color.parseColor("#1e81d1"));
            ((Button) findViewById(R.id.authcode_btn)).setTextColor(Color.parseColor("#ffffff"));
            ((Button) findViewById(R.id.authcode_btn)).setEnabled(true);
            ((Button) findViewById(R.id.authcode_btn)).setText("发送");
        }

        @Override
        public void onTick(long millisUntilFinished) {
            ((Button) findViewById(R.id.authcode_btn)).setText("" + millisUntilFinished / 1000 % 60 + "秒");
        }
    }
}
