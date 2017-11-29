package com.yyox.mvp.ui.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jess.arms.base.DefaultAdapter;
import com.jess.arms.utils.UiUtils;
import com.tbruyelle.rxpermissions.RxPermissions;
import com.yyox.BuildConfig;
import com.yyox.R;
import com.yyox.Utils.MyToast;
import com.yyox.consts.AddressType;
import com.yyox.consts.CodeDefine;
import com.yyox.di.component.DaggerFragmentComponent;
import com.yyox.di.module.FragmentModule;
import com.yyox.mvp.contract.FragmentContract;
import com.yyox.mvp.model.entity.OrderCount;
import com.yyox.mvp.model.entity.UserDetail;
import com.yyox.mvp.model.realm.RealmUser;
import com.yyox.mvp.presenter.FragmentPresenter;

import butterknife.BindView;
import common.AppComponent;
import common.WEApplication;
import common.WEFragment;

/**
 * Created by dadaniu on 2017-01-04.
 */

public class MineFragment extends WEFragment<FragmentPresenter> implements FragmentContract.View, View.OnClickListener {

    private WEApplication weApplication;

    @Nullable
    @BindView(R.id.btn_fragment_mine_login)
    Button mButton_Login;

    @Nullable
    @BindView(R.id.ll_mine_no_login)
    LinearLayout mLinearLayout_NoLogin;

    @Nullable
    @BindView(R.id.ll_mine_login)
    LinearLayout mLinearLayout_Login;

    @Nullable
    @BindView(R.id.tv_mine_name)
    TextView mTextView_Name;

    @Nullable
    @BindView(R.id.rl_mine_record)
    RelativeLayout mRelativeLayout_MineRecord;

//    @Nullable
//    @BindView(R.id.btn_signin)
//    Button mButton_Signin;

    @Nullable
    @BindView(R.id.btn_coupon)
    Button mButton_Coupon;

    @Nullable
    @BindView(R.id.btn_recharge)
    Button mButton_Recharge;

    @Nullable
    @BindView(R.id.rl_address)
    RelativeLayout mRelativeLayout_Address;

    //    @Nullable
//    @BindView(R.id.rl_invite)
//    RelativeLayout mRelativeLayout_Invite;
    @Nullable
    @BindView(R.id.rl_warehouse)
    RelativeLayout mRelativeLayout_Warehouse;

    @Nullable
    @BindView(R.id.rl_record)
    RelativeLayout mRelativeLayout_Record;

    @Nullable
    @BindView(R.id.rl_setting)
    RelativeLayout mRelativeLayout_Setting;

    @Nullable
    @BindView(R.id.remaining_sum)
    TextView mTextView_sum;

    @Nullable
    @BindView(R.id.button4)
    Button mButton4;
    //@Nullable
    //@BindView(R.id.post_money)
    //TextView mTextView_post_money;

    @Nullable
    @BindView(R.id.image_vip)
    ImageView mImage_vip;

    @Nullable
    @BindView(R.id.text_vip)
    TextView mText_vip;
    private RxPermissions mRxPermissions;

    @Override
    protected View initView() {
        return LayoutInflater.from(getContext()).inflate(R.layout.fragment_mine, null, false);
    }

    @Override
    protected void initData() {
        mRelativeLayout_MineRecord.setOnClickListener(this);
//        mButton_Signin.setOnClickListener(this);
        mButton_Coupon.setOnClickListener(this);
        mButton_Recharge.setOnClickListener(this);
        mRelativeLayout_Address.setOnClickListener(this);
//        mRelativeLayout_Invite.setOnClickListener(this);
        mRelativeLayout_Warehouse.setOnClickListener(this);
        mRelativeLayout_Record.setOnClickListener(this);
        mRelativeLayout_Setting.setOnClickListener(this);
        mButton_Login.setOnClickListener(this);
        mButton4.setOnClickListener(this);

        weApplication = (WEApplication) getActivity().getApplication();
        RealmUser realmUser = weApplication.getRealmUser();
        if (realmUser != null && (realmUser.getName() != "" || realmUser.getEmail() != null)) {
            //如果已登录则不显示“登录”按钮
            mButton_Login.setVisibility(View.GONE);
            mLinearLayout_NoLogin.setVisibility(View.GONE);
            mLinearLayout_Login.setVisibility(View.VISIBLE);
//            mTextView_Name.setText(realmUser.getName());
            //已登录,获取用户信息
            mPresenter.requestUserDetail();
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private boolean isLogin() {
        weApplication = (WEApplication) getActivity().getApplication();
        RealmUser realmUser = weApplication.getRealmUser();
        if (realmUser != null && (realmUser.getName() != "" || realmUser.getEmail() != null)) {
            return true;
        } else {
            return false;
        }
    }

    private void login() {
        new AlertDialog.Builder(getActivity()).setTitle("提示")
                .setMessage("您还未登录,请先登录")
                .setPositiveButton("下次",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //取消
                            }
                        })
                .setNegativeButton("登录",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //删除订单
                                Intent intentLogin = new Intent(getActivity(), UserLoginActivity.class);
                                startActivityForResult(intentLogin, CodeDefine.LOGIN_REQUEST);
                            }
                        }).show();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.button4) {
           /* Intent intent1 = new Intent(this.getContext(), UserPhoneActivity.class);
            startActivity(intent1);*/
        } else {
            if (!isLogin() && view.getId() != R.id.btn_fragment_mine_login) {
                login();
            } else {
                switch (view.getId()) {
                    case R.id.rl_mine_record:
                        Intent intent = new Intent(this.getContext(), UserEditActivity.class);
                        startActivityForResult(intent, CodeDefine.MINE_EDIT_REQUEST);
                        break;
                    case R.id.btn_coupon:
                        Intent intentCoupon = new Intent(this.getContext(), UserCouponActivity.class);
                        startActivity(intentCoupon);
                        break;
                    case R.id.btn_recharge:
                        Intent intentRecharge = new Intent(this.getContext(), UserRechargeActivity.class);
//                    startActivity(intentRecharge);
                        startActivityForResult(intentRecharge, CodeDefine.MINE_PEGISTER_REQUEST);
                        break;
                    case R.id.rl_address:
                        Intent intentAddress = new Intent(this.getContext(), AddressActivity.class);
                        intentAddress.putExtra("type", AddressType.ADDRESS_TYPE_LIST);
                        startActivity(intentAddress);
                        break;
//                case R.id.rl_invite:
//                    Intent intentInvite = new Intent (this.getContext(),UserInviteActivity.class);
//                    startActivity(intentInvite);
//                    break;
                    case R.id.rl_warehouse:
                        Intent intentInvite = new Intent(this.getContext(), UserWarehouseActivity.class);
                        startActivity(intentInvite);
                        break;
                    case R.id.rl_record:
                        Intent intentRecord = new Intent(this.getContext(), UserRecordActivity.class);
                        startActivity(intentRecord);
                        break;
                    case R.id.rl_setting:
                        Intent intentSetting = new Intent(this.getContext(), UserSettingActivity.class);
                        //startActivity(intentSetting);
                        startActivityForResult(intentSetting, CodeDefine.SETTING_REQUEST);
                        break;
                    case R.id.btn_fragment_mine_login:
                        Intent intentLogin = new Intent(this.getContext(), UserLoginActivity.class);
                        //startActivity(intentLogin);
                        startActivityForResult(intentLogin, CodeDefine.LOGIN_REQUEST);
                        break;
                }
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CodeDefine.LOGIN_REQUEST && resultCode == CodeDefine.LOGIN_RESULT) {
            if (BuildConfig.LOG_DEBUG) {
                weApplication = (WEApplication) getActivity().getApplication();
                RealmUser realmUser = weApplication.getRealmUser();
                if (realmUser != null && data.getStringExtra("username") != "") {
                    //如果已登录则不显示“登录”按钮
                    mButton_Login.setVisibility(View.GONE);
                    mLinearLayout_NoLogin.setVisibility(View.GONE);
                    mLinearLayout_Login.setVisibility(View.VISIBLE);
                }
            } else if (data.getStringExtra("username") != "" || data.getStringExtra("useremail") != "") {
                //如果已登录则不显示“登录”按钮
                mButton_Login.setVisibility(View.GONE);
                mLinearLayout_NoLogin.setVisibility(View.GONE);
                mLinearLayout_Login.setVisibility(View.VISIBLE);

            }
            mPresenter.requestUserDetail();
        } else if (requestCode == CodeDefine.SETTING_REQUEST && resultCode == CodeDefine.SETTING_RESULT) {
            mButton_Login.setVisibility(View.VISIBLE);
            mLinearLayout_NoLogin.setVisibility(View.VISIBLE);
            mLinearLayout_Login.setVisibility(View.GONE);
            mText_vip.setVisibility(View.GONE);
            mImage_vip.setVisibility(View.GONE);
            mButton_Coupon.setText("优惠券");
        } else if (requestCode == CodeDefine.MINE_PEGISTER_REQUEST && resultCode == CodeDefine.MINE_PEGISTER_RESULT) {
            mPresenter.requestUserDetail();
        } else if (requestCode == CodeDefine.MINE_EDIT_REQUEST && resultCode == CodeDefine.MINE_EDIT_RESULT) {
            mPresenter.requestUserDetail();
        } else if (requestCode == CodeDefine.SETTING_REQUEST && resultCode == CodeDefine.STTINGS_EDIT_RESULT) {
            mPresenter.requestUserDetail();
        }
    }

    @Override
    public void setAdapter(DefaultAdapter adapter) {

    }

    @Override
    public void setAdapterWeb(DefaultAdapter adapter) {

    }

    @Override
    public RxPermissions getRxPermissions() {
        return mRxPermissions;
    }


    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showMessage(String message) {
        MyToast.makeText(getContext(), message, 3 * 1000).show();
    }

    @Override
    public void launchActivity(Intent intent) {
        UiUtils.startActivity(intent);
    }

    @Override
    public void killMyself() {

    }

    @Override
    protected void setupFragmentComponent(AppComponent appComponent) {
        this.mRxPermissions = new RxPermissions((Activity) getContext());
        DaggerFragmentComponent.builder().appComponent(appComponent).fragmentModule(new FragmentModule(this)).build().inject(this);
    }

    @Override
    public void setUserDetail(UserDetail userDetail) {
      /*  if (!userDetail.getMember().isEmpty() && userDetail.getMember().indexOf("VIP") > -1){
            mImage_vip.setVisibility(View.VISIBLE);
            mText_vip.setVisibility(View.VISIBLE);
            mText_vip.setText("V"+userDetail.getMember().substring(userDetail.getMember().length()-1));
        }*/
        mTextView_Name.setText(userDetail.getName());
        mTextView_sum.setText("余额：¥" + userDetail.getBalanceCnyFormat2());
        //mTextView_post_money.setText("邮币:0");
        mButton_Coupon.setText("优惠券( " + userDetail.getCouponCount() + " )");
    }

    @Override
    public void setOrderCount(OrderCount orderCount) {

    }

    @Override
    public void startLoadMore() {

    }

    @Override
    public void endLoadMore() {

    }

    @Override
    public void datadFew() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mRxPermissions = null;
    }
}
