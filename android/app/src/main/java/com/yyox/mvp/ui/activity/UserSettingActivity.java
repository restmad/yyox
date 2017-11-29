package com.yyox.mvp.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;

import com.google.gson.JsonObject;
import com.jess.arms.base.DefaultAdapter;
import com.jess.arms.utils.UiUtils;
import com.tbruyelle.rxpermissions.RxPermissions;
import com.yyox.R;
import com.yyox.Utils.MyToast;
import com.yyox.consts.CodeDefine;
import com.yyox.di.component.DaggerUserComponent;
import com.yyox.di.module.UserModule;
import com.yyox.mvp.contract.UserContract;
import com.yyox.mvp.model.entity.CouponItem;
import com.yyox.mvp.model.entity.User;
import com.yyox.mvp.model.realm.RealmUser;
import com.yyox.mvp.presenter.UserPresenter;

import common.AppComponent;
import common.WEActivity;
import common.WEApplication;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class UserSettingActivity extends WEActivity<UserPresenter> implements UserContract.View {
    private RxPermissions mRxPermissions;


    public void btn_back_click(View v) {
        this.finish();
    }

    public void btn_logout_click(View v) {
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

        }
        mPresenter.requestLogout(this);
        WEApplication weApplication = (WEApplication) getApplication();
        weApplication.setRealmUser(null);

        Intent intent = new Intent();
        setResult(CodeDefine.SETTING_RESULT, intent);
    }

    public void rl_edit_click(View v) {
        Intent intent = new Intent(UserSettingActivity.this, UserEditActivity.class);
        startActivityForResult(intent, CodeDefine.STTINGS_EDIT_REQUEST);
    }

    public void rl_modify_click(View v) {
        Intent intent = new Intent(UserSettingActivity.this, UserModifyActivity.class);
        startActivityForResult(intent, CodeDefine.MINE_STTINFS_REQUEST);
    }

    @Override
    protected View initView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_user_setting, null, false);
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
    protected void onDestroy() {
        super.onDestroy();
        this.mRxPermissions = null;
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CodeDefine.MINE_STTINFS_REQUEST && resultCode == CodeDefine.MINE_STTINGS_RESULT) {
            finish();
        } else if (requestCode == CodeDefine.STTINGS_EDIT_REQUEST && resultCode == CodeDefine.MINE_EDIT_RESULT) {
            setResult(CodeDefine.STTINGS_EDIT_RESULT);
            finish();
        }
    }

    public void switch_on_off(View view) {
        Uri packageURI = Uri.parse("package:" + "com.yyox");
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI);
        startActivity(intent);
    }
}
