package com.yyox.mvp.ui.activity;

import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.google.gson.JsonObject;
import com.jess.arms.base.DefaultAdapter;
import com.jess.arms.utils.UiUtils;
import com.tbruyelle.rxpermissions.RxPermissions;
import com.yyox.R;
import com.yyox.Utils.CommonUtils;
import com.yyox.Utils.MyToast;
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

public class UserModifyActivity extends WEActivity<UserPresenter> implements UserContract.View {

    @Nullable
    @BindView(R.id.before_password)
    EditText mBefore_password;

    @Nullable
    @BindView(R.id.new_password)
    EditText mNew_password;

    @Nullable
    @BindView(R.id.confirm_new_password)
    EditText mConfirm_new_password;
    private Drawable mErrorIcon;
    private RxPermissions mRxPermissions;

    @Override
    protected View initView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_user_modify, null, false);
    }

    @Override
    protected void initData() {

    }

    public void btn_back_click(View v) {
        this.finish();
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
        Intent intrnt = new Intent();
        setResult(CodeDefine.MINE_STTINGS_RESULT, intrnt);
        finish();
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
        DaggerUserComponent.builder().appComponent(appComponent).userModule(new UserModule(this)).build().inject(this);
    }

    public void password_confirm(View view) {
        mErrorIcon = getResources().getDrawable(R.mipmap.error);
        mErrorIcon.setBounds(new Rect(0, 0, mErrorIcon.getIntrinsicWidth(), mErrorIcon.getIntrinsicHeight()));
        String originalPassword = mBefore_password.getText().toString();
        String password = mNew_password.getText().toString();
        String confirmedPassword = mConfirm_new_password.getText().toString();
        if (originalPassword.isEmpty()) {
            mBefore_password.setError("请输入原始密码", mErrorIcon);
            showMessage("请输入原始密码");
        } else if (!CommonUtils.checkPassword(password)) {
            mNew_password.setError("请输入6-12位(字母,数字)组合密码", mErrorIcon);
            showMessage("请输入6-12位(字母,数字)组合密码");
        } else if (!password.equals(confirmedPassword)) {
            mConfirm_new_password.setError("两次输入的密码不相符", mErrorIcon);
            showMessage("两次输入的密码不相符");
        } else {
            mPresenter.requestAlterPassword(originalPassword, password, confirmedPassword);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.mRxPermissions = null;
    }
}
