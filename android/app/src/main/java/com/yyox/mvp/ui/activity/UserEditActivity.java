package com.yyox.mvp.ui.activity;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.jess.arms.base.DefaultAdapter;
import com.jess.arms.utils.UiUtils;
import com.tbruyelle.rxpermissions.RxPermissions;
import com.yyox.R;
import com.yyox.Utils.MyToast;
import com.yyox.di.component.DaggerUserComponent;
import com.yyox.di.module.UserModule;
import com.yyox.mvp.contract.UserContract;
import com.yyox.mvp.model.entity.CouponItem;
import com.yyox.mvp.model.entity.User;
import com.yyox.mvp.presenter.UserPresenter;

import butterknife.BindView;
import common.AppComponent;
import common.WEActivity;

public class UserEditActivity extends WEActivity<UserPresenter> implements UserContract.View {

    @Nullable
    @BindView(R.id.tv_activity_user_edit_code)
    TextView mTextView_Code;

    @Nullable
    @BindView(R.id.tv_activity_user_edit_level)
    TextView mTextView_Level;

    @Nullable
    @BindView(R.id.tv_activity_user_edit_phone)
    TextView mTextView_Phone;

    @Nullable
    @BindView(R.id.tv_activity_user_edit_email)
    TextView mTextView_Email;

    @Nullable
    @BindView(R.id.et_activity_user_edit_nickname)
    EditText mEditText_Nickname;

    @Nullable
    @BindView(R.id.et_activity_user_edit_name)
    EditText mEditText_Name;

    @Nullable
    @BindView(R.id.et_activity_user_edit_surname)
    EditText mEditText_Surname;

    @Nullable
    @BindView(R.id.et_activity_user_edit_qq)
    EditText mEditText_QQ;
    private RxPermissions mRxPermissions;

    @Override
    protected View initView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_user_edit, null, false);
    }

    @Override
    protected void initData() {
        mPresenter.requestUserInfo();
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
        if (user != null) {
            mTextView_Code.setText(user.getIdentifier());
            mTextView_Level.setText(user.getLevel());
            mTextView_Phone.setText(user.getMobile());
            mTextView_Email.setText(user.getMail());
            mEditText_Nickname.setText(user.getNickname());
            mEditText_Name.setText(user.getFirstName());
            mEditText_Surname.setText(user.getLastName());
            mEditText_QQ.setText(user.getQq());
        }
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

    public void btn_back_click(View v) {
        this.finish();
    }

    public void btn_confirm_click(View v) {
        User user = new User();
        user.setNickname(mEditText_Nickname.getText().toString());
        user.setQq(mEditText_QQ.getText().toString());
        user.setFirstName(mEditText_Name.getText().toString());
        user.setLastName(mEditText_Surname.getText().toString());
        mPresenter.requestAlterCustomer(user);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.mRxPermissions = null;
    }
}
