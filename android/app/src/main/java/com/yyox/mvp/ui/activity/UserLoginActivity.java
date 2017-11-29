package com.yyox.mvp.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.google.gson.JsonObject;
import com.jess.arms.base.DefaultAdapter;
import com.jess.arms.utils.UiUtils;
import com.tbruyelle.rxpermissions.RxPermissions;
import com.yyox.BuildConfig;
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

import org.json.JSONException;

import butterknife.BindView;
import common.AppComponent;
import common.WEActivity;
import common.WEApplication;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class UserLoginActivity extends WEActivity<UserPresenter> implements UserContract.View {

    @Nullable
    @BindView(R.id.phone)
    EditText mEditText_Phone;

    @Nullable
    @BindView(R.id.password)
    EditText mEditText_Password;

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
        this.mRxPermissions = null;
    }

    @Override
    protected android.view.View initView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_user_login, null, false);
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
        if (jsonObject != null) {
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
                realmUserNew.setName(jsonObject.get("name").getAsString());
                realmUserNew.setEmail(jsonObject.get("mail").getAsString());
                objRealm.commitTransaction();
            } else {
                //添加
                objRealm.beginTransaction();
                RealmUser realmUserNew = objRealm.createObject(RealmUser.class);
                realmUserNew.setName(jsonObject.get("name").getAsString());
                realmUserNew.setEmail(jsonObject.get("mail").getAsString());
                objRealm.commitTransaction();
            }

            WEApplication weApplication = (WEApplication) getApplication();
            weApplication.setRealmUser(objRealm.where(RealmUser.class).findFirst());

            SharedPreferences sharedPreferences = this.getSharedPreferences("userEmail", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("Email",jsonObject.get("mail").getAsString());
         //   editor.putString("phone",jsonObject.get("phone").getAsString());
            editor.putString("name",jsonObject.get("name").getAsString());
            SharedPreferences sharedPreferencess =  WEApplication.getContext().getSharedPreferences(jsonObject.get("mail").getAsString(), Context.MODE_PRIVATE);
            editor.commit();
            Intent intent = new Intent();
            intent.putExtra("username", jsonObject.get("name").getAsString());
            setResult(CodeDefine.LOGIN_RESULT, intent);
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

    public void btn_register_click(View v) {
        Intent intent = new Intent(this, UserRegisterActivity.class);
        startActivityForResult(intent, CodeDefine.REGISTER_REQUEST);
    }

    public void btn_login_click(View v) throws JSONException {

        if (NoDoubleClickUtils.isDoubleClick()) {
            return;
        }

        String phone = mEditText_Phone.getText().toString();
        String password = mEditText_Password.getText().toString();
        if (phone.isEmpty()) {
            showMessage("请输入手机号码或邮箱地址");
        } else if (!CommonUtils.isPhoneNumber(phone) && !CommonUtils.isEmail(phone)) {
            showMessage("请输入正确的手机号码或邮箱地址");
        } else if (password.isEmpty()) {
            showMessage("请输入密码");
        } else if (password.length() < 6 || password.length() > 12) {
            showMessage("请输入6~12位由数字和字母组成的密码");
        } else {
            //如果是Debug环境则验证本地数据
            if (BuildConfig.LOG_DEBUG) {
                RealmConfiguration config = new RealmConfiguration.Builder().name("yyox.realm").build();
                Realm objRealm = Realm.getInstance(config);

                long count = objRealm.where(RealmUser.class).count();
                //查找
                RealmUser realmUser = objRealm.where(RealmUser.class).findFirst();
                if (realmUser != null) {
                    if (realmUser.getName().toString().equals(phone) && realmUser.getPwd().toString().equals(password)) {
                        Intent intent = new Intent();
                        intent.putExtra("username", phone);
                        setResult(CodeDefine.LOGIN_RESULT, intent);
                        finish();
                    } else {
                        Intent intent = new Intent();
                        intent.putExtra("username", "");
                        setResult(CodeDefine.LOGIN_RESULT, intent);
                        showMessage("用户名或密码错误");
                    }
                } else {
                    Intent intent = new Intent();
                    intent.putExtra("username", "");
                    setResult(CodeDefine.LOGIN_RESULT, intent);
                    showMessage("登录失败");
                }
            } else {
                mPresenter.requestLogin(phone, password);
            }
        }
    }

    public void btn_forget_click(View v) {
        Intent intent = new Intent(UserLoginActivity.this, UserForgetActivity.class);
        startActivityForResult(intent, CodeDefine.FORGET_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CodeDefine.REGISTER_REQUEST && resultCode == CodeDefine.REGISTER_RESULT) {
            String username = data.getStringExtra("username");
            Intent intent = new Intent();
            intent.putExtra("username", username);
            setResult(CodeDefine.LOGIN_RESULT, intent);
            finish();
        }
    }
}
