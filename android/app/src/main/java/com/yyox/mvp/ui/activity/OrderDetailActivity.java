package com.yyox.mvp.ui.activity;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jess.arms.base.DefaultAdapter;
import com.jess.arms.utils.UiUtils;
import com.tbruyelle.rxpermissions.RxPermissions;
import com.yyox.R;
import com.yyox.Utils.CommonUtils;
import com.yyox.Utils.ExpandedListUtils;
import com.yyox.Utils.MyToast;
import com.yyox.Utils.NoDoubleClickUtils;
import com.yyox.consts.AddressType;
import com.yyox.consts.CodeDefine;
import com.yyox.consts.OrderStatus;
import com.yyox.di.component.DaggerOrderComponent;
import com.yyox.di.module.OrderModule;
import com.yyox.mvp.contract.OrderContract;
import com.yyox.mvp.model.entity.Address;
import com.yyox.mvp.model.entity.Channel;
import com.yyox.mvp.model.entity.CouponItem;
import com.yyox.mvp.model.entity.Fee;
import com.yyox.mvp.model.entity.OrderDetail;
import com.yyox.mvp.model.entity.OrderHistoryItem;
import com.yyox.mvp.model.entity.OrderPackage;
import com.yyox.mvp.model.entity.OrderPackageGoods;
import com.yyox.mvp.model.entity.PriceQuery;
import com.yyox.mvp.presenter.OrderPresenter;
import com.yyox.mvp.ui.adapter.OrderPackageDetailAdapter;
import com.yyox.mvp.ui.adapter.PackageExpandableAdapter;
import com.yyox.mvp.ui.adapter.QuestionExpandableAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import common.AppComponent;
import common.WEActivity;

/**
 * 该Activity同时用于包裹提交、订单提交、合箱提交及各订单状态详情
 */
public class OrderDetailActivity extends WEActivity<OrderPresenter> implements OrderContract.View, View.OnClickListener {

    @Nullable
    @BindView(R.id.tv_activity_order_detail_title)
    TextView mTextView_Title;

    @Nullable
    @BindView(R.id.btn_activity_order_detail_cancel)
    Button mButton_Cancel;

    @Nullable
    @BindView(R.id.ll_activity_order_detail_warning)
    LinearLayout mLinearLayout_Warning;

    @Nullable
    @BindView(R.id.tv_order_detail_address_name)
    TextView mTextView_Address_Name;

    @Nullable
    @BindView(R.id.tv_order_detail_address_phone)
    TextView mTextView_Address_Phone;

    @Nullable
    @BindView(R.id.tv_order_detail_address_default)
    TextView mTextView_Address_Default;

    @Nullable
    @BindView(R.id.tv_order_detail_address_detail)
    TextView mTextView_Address_Detail;

    @Nullable
    @BindView(R.id.orderDetailExpandableListView)
    ExpandableListView mExpandableListView;

    @Nullable
    @BindView(R.id.tv_order_detail_channel_name)
    TextView mTextView_Channel_Name;

    @Nullable
    @BindView(R.id.tv_order_detail_channel_price)
    TextView mTextView_Channel_Price;

    @Nullable
    @BindView(R.id.tv_order_detail_coupon_value)
    TextView mTextView_Coupon_Value;

    @Nullable
    @BindView(R.id.rl_activity_order_detail_bottom)
    RelativeLayout mRelativeLayout_Bottom;

    @Nullable
    @BindView(R.id.freight_itme)
    RelativeLayout mRelativeLayout;

    @Nullable
    @BindView(R.id.tv_order_detail_freight_name)
    TextView mTextView_freight_name;

    @Nullable
    @BindView(R.id.tv_order_detail_freight_price)
    TextView mTextView_freight_price;

    @Nullable
    @BindView(R.id.cb_address_save_default)
    CheckBox mCheckBox;

    @Nullable
    @BindView(R.id.btn_activity_order_detail_submit)
    Button mButton_Submit;

    @Nullable
    @BindView(R.id.tv_activity_order_detail_fee)
    TextView mTextView_Fee;

    @Nullable
    @BindView(R.id.addrss_choice)
    ImageButton mAddrss_choice;

    @Nullable
    @BindView(R.id.channel_choice)
    ImageButton mChannel_choice;

    @Nullable
    @BindView(R.id.coupon_choice)
    ImageButton mCoupon_choice;

    @Nullable
    @BindView(R.id.rl_mine_record)
    RelativeLayout rl_mine_record;

    @Nullable
    @BindView(R.id.have_adress_id)
    LinearLayout mHave_adress_id;

    @Nullable
    @BindView(R.id.no_adress_id)
    LinearLayout mNo_adress_id;

    @Nullable
    @BindView(R.id.coupon_order_text)
    TextView mCoupon_order_text;

    @Nullable
    @BindView(R.id.tv_order_detail_terms)
    TextView mTerms;

    @Nullable
    @BindView(R.id.enter_weight)
    RelativeLayout mREnterWeight;

    @Nullable
    @BindView(R.id.tv_order_detail_enter_weight)
    TextView mTvEnterWeight;

    @Nullable
    @BindView(R.id.out_weight)
    RelativeLayout mROutWeright;

    @Nullable
    @BindView(R.id.tv_order_detail_out_weight)
    TextView mTvOutWeright;

    @Nullable
    @BindView(R.id.tv_activity_order_detail_overall_weight)
    TextView mOverallWeight;

    private double fee;
    private OrderDetail mOrderDetail;
    private Address mSelectAddress = null;
    private Channel mSelectChannel = null;
    private int pay;
    private double totalAmount;
    private RxPermissions mRxPermissions;
    private boolean state = true;

    @Override
    protected View initView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_order_detail, null, false);
    }

    private void initControl() {

        mExpandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                ExpandedListUtils.setExpandedListViewHeightBasedOnChildren(mExpandableListView, groupPosition);
                // 更新group每一项的高度
                ExpandedListUtils.setListViewHeightBasedOnChildren(mExpandableListView);
            }
        });

        mExpandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {
                ExpandedListUtils.setCollapseListViewHeightBasedOnChildren(mExpandableListView, groupPosition);
                /*
                 * 重新评估group的高度
                 */
                ExpandedListUtils.setListViewHeightBasedOnChildren(mExpandableListView);
            }
        });

        if (1 == getIntent().getIntExtra("type", OrderStatus.ORDER_STATUS_DETAIL)) {
            //包裹
            mTextView_Title.setText("提交订单");
            mRelativeLayout_Bottom.setVisibility(View.VISIBLE);
            mButton_Submit.setText("提交订单");
            OrderDetail orderdetail = (OrderDetail) getIntent().getSerializableExtra("orderdetail");
            if (orderdetail.getMerchandiseList().size()>1){
                mButton_Cancel.setVisibility(View.VISIBLE);
                mLinearLayout_Warning.setVisibility(View.VISIBLE);
                mButton_Cancel.setText("取消合箱");
            }else {
                mLinearLayout_Warning.setVisibility(View.GONE);
                mButton_Cancel.setVisibility(View.GONE);
            }
            mREnterWeight.setVisibility(View.GONE);
            mROutWeright.setVisibility(View.GONE);
        } else if (2 == getIntent().getIntExtra("type", OrderStatus.ORDER_STATUS_DETAIL) || 3 == getIntent().getIntExtra("type", OrderStatus.ORDER_STATUS_DETAIL)) {
            //订单/合箱
            mTextView_Title.setText("提交订单");
            mREnterWeight.setVisibility(View.GONE);
            mROutWeright.setVisibility(View.GONE);
            if (3 == getIntent().getIntExtra("type", OrderStatus.ORDER_STATUS_DETAIL)) {
                mLinearLayout_Warning.setVisibility(View.VISIBLE);
            }
            mRelativeLayout_Bottom.setVisibility(View.VISIBLE);
            if (2 == getIntent().getIntExtra("status", 2)) {
                //待支付
                mButton_Submit.setText("去支付");
            } else if (4 == getIntent().getIntExtra("status", 2)) {
                //待缴税金
                mTextView_Title.setText("订单详情");
                mLinearLayout_Warning.setVisibility(View.GONE);
                mRelativeLayout_Bottom.setVisibility(View.VISIBLE);
                mRelativeLayout.setVisibility(View.VISIBLE);
                mREnterWeight.setVisibility(View.VISIBLE);
                mROutWeright.setVisibility(View.VISIBLE);
                mOverallWeight.setVisibility(View.GONE);
                mChannel_choice.setVisibility(View.GONE);
                mCoupon_choice.setVisibility(View.GONE);
                mAddrss_choice.setBackground(null);
                mButton_Submit.setText("去支付");
            } else {
                //订单详情
                mTextView_Title.setText("订单详情");
                mLinearLayout_Warning.setVisibility(View.GONE);
                mRelativeLayout_Bottom.setVisibility(View.GONE);
                mRelativeLayout.setVisibility(View.VISIBLE);
                mCheckBox.setVisibility(View.GONE);
                mTerms.setVisibility(View.GONE);
                mChannel_choice.setVisibility(View.GONE);
                mCoupon_choice.setVisibility(View.GONE);
                mREnterWeight.setVisibility(View.VISIBLE);
                mROutWeright.setVisibility(View.VISIBLE);
            }

            if (3 == getIntent().getIntExtra("type", OrderStatus.ORDER_STATUS_DETAIL) || 2 == getIntent().getIntExtra("type", OrderStatus.ORDER_STATUS_DETAIL)) {
                if (getIntent().getIntExtra("status", OrderStatus.ORDER_STATUS_DETAIL) != 3 && getIntent().getIntExtra("status", OrderStatus.ORDER_STATUS_DETAIL) != 4) {
                    mButton_Cancel.setVisibility(View.VISIBLE);
                    if (3 == getIntent().getIntExtra("type", OrderStatus.ORDER_STATUS_DETAIL)){
                        mButton_Cancel.setText("取消合箱");
                    }else if (2 == getIntent().getIntExtra("type", OrderStatus.ORDER_STATUS_DETAIL)){
                        mButton_Cancel.setText("取消订单");
                    }
                }
            } else {
                mButton_Cancel.setVisibility(View.GONE);
            }
        } else {
            //订单详情
            mAddrss_choice.setBackground(null);
            mChannel_choice.setBackground(null);
            mCoupon_choice.setBackground(null);

            mTextView_Title.setText("订单详情");
            mLinearLayout_Warning.setVisibility(View.GONE);
            mRelativeLayout_Bottom.setVisibility(View.GONE);
            mRelativeLayout.setVisibility(View.VISIBLE);
            mCheckBox.setVisibility(View.GONE);
            mTerms.setVisibility(View.GONE);
            mChannel_choice.setVisibility(View.GONE);
            mCoupon_choice.setVisibility(View.GONE);
            mREnterWeight.setVisibility(View.VISIBLE);
            mROutWeright.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void initData() {
        initControl();
        int visibility = mOverallWeight.getVisibility();
        if (visibility == 8){
           mTextView_Fee.setPadding(mTextView_Fee.getPaddingLeft(),mTextView_Fee.getPaddingTop()+20,mTextView_Title.getPaddingRight(),mTextView_Fee.getPaddingBottom());
        }
        int iType = getIntent().getIntExtra("type", OrderStatus.ORDER_STATUS_DETAIL);
        switch (iType) {
            case 1://包裹提交
                OrderDetail orderDetail = (OrderDetail) getIntent().getSerializableExtra("orderdetail");
                mPresenter.setAdapter_Submit(this, iType);
                mPresenter.requestOrderDetail(true, "", iType, orderDetail);
                break;
            case 2://合箱订单提交
            case 3: //订单提交
            case 17://订单详情
                String strOrderNo = getIntent().getStringExtra("orderNo");
                if (!strOrderNo.isEmpty()) {
                    int type = getIntent().getIntExtra("status", OrderStatus.ORDER_STATUS_DETAIL);
                    mPresenter.setAdapter_Submit(this, type);
                    mPresenter.requestOrderDetail(true, strOrderNo, type, null);
                }
                break;
        }
        mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    mCheckBox.setChecked(true);
                    state = b;
                }else {
                    state = b;
                    mCheckBox.setChecked(false);
                }
            }
        });
    }

    @Override
    public void setAdapter(DefaultAdapter adapter) {
    }

    @Override
    public void setPackageAdapter(PackageExpandableAdapter adapter) {
        mExpandableListView.setAdapter(adapter);
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
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            if (pay == 12) {
                setResult(CodeDefine.ORDER_CHANNEL_RESULT);
                this.finish();
            } else {
                if (getIntent().getIntExtra("package", 0) != OrderStatus.ORDER_STATUS_PACKAGES) {
                    setResult(CodeDefine.ORDER_CHANNEL_RESULT);
                    this.finish();
                } else {
                    finish();
                }
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void setUIValue(OrderDetail orderDetailJson) {
        if (orderDetailJson != null) {
            mOrderDetail = orderDetailJson;
            if (mSelectAddress != null) {
                mOrderDetail.setAddress(mSelectAddress);
            }
            if (mSelectChannel != null) {
                mOrderDetail.setOrderchannel(mSelectChannel);
            }
            if (orderDetailJson.getAddress() != null) {
                mHave_adress_id.setVisibility(View.VISIBLE);
                mNo_adress_id.setVisibility(View.GONE);
                mTextView_Address_Name.setText(orderDetailJson.getAddress().getName());
                mTextView_Address_Phone.setText(orderDetailJson.getAddress().getMobile());
                mTextView_Address_Detail.setText(orderDetailJson.getAddress().getProvince() + orderDetailJson.getAddress().getCity() + orderDetailJson.getAddress().getDistrict() + orderDetailJson.getAddress().getDetailaddress());
            } else {
                mHave_adress_id.setVisibility(View.GONE);
                mNo_adress_id.setVisibility(View.VISIBLE);
            }
            mTextView_Channel_Name.setText(orderDetailJson.getOrderchannel().getCode());
            mTextView_Channel_Price.setText(" " + orderDetailJson.getOrderchannel().getPriceWeight());

            SpannableString styledText = new SpannableString("运费：¥"+CommonUtils.doubleFormat(orderDetailJson.getRealcost()));
            int color = getResources().getColor(R.color.my_yellow);
            styledText.setSpan(new ForegroundColorSpan(color), 0, 3, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
            mTextView_Fee.setText(styledText);

            mOverallWeight.setText("总重量："+ CommonUtils.doubleFormat(orderDetailJson.getRealWeight())+"kg");
            mTvEnterWeight.setText(orderDetailJson.getRealWeight()+"kg");
            mTvOutWeright.setText(orderDetailJson.getInWeight());
            if (orderDetailJson.getCouponList() == null || orderDetailJson.getCouponList().getCoupon().getName().equals("")) {
                if (getIntent().getIntExtra("type", OrderStatus.ORDER_STATUS_DETAIL) == 17 ||
                        2 != getIntent().getIntExtra("status", 2)
                        || 4 == getIntent().getIntExtra("status", 2)){
                mTextView_Coupon_Value.setText("未使用");
                }else {
                mTextView_Coupon_Value.setText("有0张可用");
                }
            } else {
                mTextView_Coupon_Value.setText(orderDetailJson.getCouponList().getCoupon().getName());
            }
            if (OrderStatus.ORDER_STATUS_DETAIL == getIntent().getIntExtra("type", OrderStatus.ORDER_STATUS_DETAIL)) {
                mTextView_freight_price.setText("总运费" +  CommonUtils.doubleFormat(orderDetailJson.getOriginalCost()) + "元,实际支付" +  CommonUtils.doubleFormat(orderDetailJson.getRealcost()) + "元");
            }
            if (getIntent().getIntExtra("type", 0) == 2 || getIntent().getIntExtra("type", 0) == 3) {
                mTextView_freight_price.setText("总运费" +  CommonUtils.doubleFormat(orderDetailJson.getOriginalCost()) + "元,实际支付" +  CommonUtils.doubleFormat(orderDetailJson.getRealcost()) + "元");
            }
            if (orderDetailJson.getAddress() != null && orderDetailJson.getAddress().getIsdefault()) {
                mTextView_Address_Default.setVisibility(View.VISIBLE);
            } else {
                mTextView_Address_Default.setVisibility(View.GONE);
            }

            for (int i = 0; i < orderDetailJson.getMerchandiseList().size(); i++) {
                if (orderDetailJson.getMerchandiseList().size() != 1) {
                    mExpandableListView.collapseGroup(i);
                } else {
                    List<OrderPackage> orderPackage = orderDetailJson.getMerchandiseList();
                    if (orderPackage.get(0).getGoodList().size() > 0) {
                        mExpandableListView.expandGroup(i);
                    }
                }

                mExpandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
                    @Override
                    public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                        if (orderDetailJson.getMerchandiseList().size() != 1) {
                            return false;
                        } else {
                            return true;
                        }
                    }
                });
            }
            String couponCode = "";
            if (orderDetailJson.getCouponList() != null && !orderDetailJson.getCouponList().getCode().isEmpty()) {
                couponCode = orderDetailJson.getCouponList().getCode();
            } else {
                couponCode = "removeCoupon";
            }

            if (4 == getIntent().getIntExtra("status", 2) || 3 == getIntent().getIntExtra("status", 2)) {
//                mTextView_Fee.setText("税金：" + CommonUtils.doubleFormat(orderDetailJson.getTaxRepay()));

                SpannableString styledText2 = new SpannableString("税金：¥" + CommonUtils.doubleFormat(orderDetailJson.getTaxRepay()));
                int color2 = getResources().getColor(R.color.my_yellow);
                styledText2.setSpan(new ForegroundColorSpan(color), 0, 3, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                mTextView_Fee.setText(styledText2);
            } else {
                if (OrderStatus.ORDER_STATUS_DETAIL != getIntent().getIntExtra("type", 8) || 1 == getIntent().getIntExtra("type", OrderStatus.ORDER_STATUS_DETAIL)) {
                    List<OrderPackage> orderPackage = orderDetailJson.getMerchandiseList();
                    List<Integer> list = new ArrayList<Integer>();
                    for (OrderPackage item : orderPackage) {
                        list.add(item.getId());
                    }

                    mPresenter.requestCalcuteFee(list, orderDetailJson.getOrderchannel().getWarehouseId(), orderPackage.size(), orderDetailJson.getRealWeight(), orderDetailJson.getOrderType().getCode(), couponCode, orderDetailJson.getOrderchannel().getOrderTypeId(), orderDetailJson.getOrderchannel().getLeadId(), orderDetailJson.getOrderNo());
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        mButton_Submit.setClickable(true);

        if (requestCode == CodeDefine.ORDER_DETAIL_ADDRESS_REQUEST && resultCode == CodeDefine.ORDER_DETAIL_ADDRESS_RESULT) {
            Address addess = (Address) data.getSerializableExtra("addess");
            mSelectAddress = addess;
            mOrderDetail.setAddress(addess);
            boolean isdefault = addess.getIsdefault();
            if (isdefault) {
                mTextView_Address_Default.setVisibility(View.VISIBLE);
            } else {
                mTextView_Address_Default.setVisibility(View.GONE);
            }
            mHave_adress_id.setVisibility(View.VISIBLE);
            mNo_adress_id.setVisibility(View.GONE);
            mTextView_Address_Name.setText(addess.getName());
            mTextView_Address_Phone.setText(addess.getMobile());
            mTextView_Address_Detail.setText(addess.getProvince() + addess.getCity() + addess.getDistrict() + addess.getDetailaddress());

        } else if (requestCode == CodeDefine.ORDER_DETAIL_CHANNEL_REQUEST && resultCode == CodeDefine.ORDER_DETAIL_CHANNEL_RESULT) {
            Channel channel = (Channel) data.getSerializableExtra("Channel");
            mSelectChannel = channel;
            mOrderDetail.setOrderchannel(channel);
            mOrderDetail.getOrderType().setCode(channel.getOrderTypeCode());
            mTextView_Channel_Name.setText(channel.getCode());
            mTextView_Channel_Price.setText(" " + channel.getPriceWeight());
            String orderNo = mOrderDetail.getOrderNo();
            //重新计算运费
            setUIValue(mOrderDetail);

        } else if (requestCode == CodeDefine.ORDER_DETAIL_REQUEST && resultCode == CodeDefine.ORDER_DETAIL_RESULT) {
            setResult(CodeDefine.ORDER_CHANNEL_RESULT);
            this.finish();
        } else if (requestCode == CodeDefine.ORDER_DETAIL_PACKAGE_REQUEST && resultCode == CodeDefine.ORDER_DETAIL_PACKAGE_RESULT) {
            //包裹修改返回
            String strOrderNo = getIntent().getStringExtra("orderNo");
            if (strOrderNo != null) {
                mPresenter.setAdapter_Submit(this, getIntent().getIntExtra("type", OrderStatus.ORDER_STATUS_DETAIL));
                if (strOrderNo.equals("1")) {
                    mPresenter.requestOrderDetail(true, strOrderNo, getIntent().getIntExtra("type", OrderStatus.ORDER_STATUS_DETAIL), null);
                } else {
                    mPresenter.requestOrderDetail(true, strOrderNo, 0, null);
                }
            } else {//包裹进入提交订单界面的包裹修改
                List<OrderPackage> orderPackages = (List<OrderPackage>) data.getSerializableExtra("orderPackages");
                if (orderPackages != null) {

                    mPresenter.setAdapter_package_editor(this, orderPackages, 1);
                    for (int i = 0; i < orderPackages.size(); i++) {
                        if (orderPackages.size() != 1) {
                            mExpandableListView.expandGroup(i);
                            mExpandableListView.collapseGroup(i);
                        } else {
                            mExpandableListView.expandGroup(i);
                        }
                    }
                }
            }
        } else if (resultCode == CodeDefine.ORDER_DETAIL_PAY_RESULT) {
            pay = data.getIntExtra("pay", 12);
            mPresenter.requestOrderDetail(true, data.getStringExtra("orderNo"), 17, null);
        } else if (requestCode == CodeDefine.ORDER_DETAIL_ADDRESS_REQUEST && resultCode == CodeDefine.ADDRESS_CARD_RESULT) {
            finish();
        } else if (requestCode == CodeDefine.ORDER_COUPON_REQUEST && resultCode == CodeDefine.ORDER_COUPON_RESULT) {
            CouponItem couponItem = (CouponItem) data.getSerializableExtra("CouponItem");
            if (!couponItem.getCode().equals("")) {
                mOrderDetail.setCouponList(couponItem);
            } else {
                mOrderDetail.setCouponList(null);
            }
            setUIValue(mOrderDetail);
        }

    }

    @Override
    public void setFeeValue(Fee fee) {
        if (fee != null || 1 == getIntent().getIntExtra("type", OrderStatus.ORDER_STATUS_DETAIL)) {
            totalAmount = fee.getMoney().getTotalAmount();
            if (mOrderDetail.getCouponList() == null || mOrderDetail.getCouponList().getCoupon() == null || mOrderDetail.getCouponList().getCoupon().getName().equals("")) {
                mTextView_Coupon_Value.setText("有" + fee.getCouponNumber() + "张可用");
            }
//            mTextView_Fee.setText("运费：" + CommonUtils.doubleFormat(fee.getMoney().getTotalAmount()));

            SpannableString styledText = new SpannableString("运费：¥"+CommonUtils.doubleFormat(fee.getMoney().getTotalAmount()));
            int color = getResources().getColor(R.color.my_yellow);
            styledText.setSpan(new ForegroundColorSpan(color), 0, 3, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
            mTextView_Fee.setText(styledText);

            mOrderDetail.setValueAddedlist(fee.getValueAddedlist());
            this.fee = fee.getMoney().getTotalAmount();
        }
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
        mButton_Submit.setClickable(true);
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
        if (pay == 12) {
            setResult(CodeDefine.ORDER_CHANNEL_RESULT);
            this.finish();
        } else {
            if (getIntent().getIntExtra("package", 0) != OrderStatus.ORDER_STATUS_PACKAGES) {
                setResult(CodeDefine.ORDER_CHANNEL_RESULT);
                this.finish();
            } else {
                finish();
            }
        }
    }

    public void btn_cancalbox_click(View v) {
        //showMessage("取消合箱");
        if (1 == getIntent().getIntExtra("type", OrderStatus.ORDER_STATUS_DETAIL)){//合箱包裹提交
            setResult(CodeDefine.ORDER_CHANNEL_RESULT);
            this.finish();
        }
        if (mOrderDetail != null) {
            mPresenter.requestCancelBox(mOrderDetail.getOrderNo());
        }
    }

    public void btn_address_click(View v) {
        if (OrderStatus.ORDER_STATUS_DETAIL != getIntent().getIntExtra("type", 8) && OrderStatus.ORDER_STATUS_TAX_SUBMIT != getIntent().getIntExtra("type", 8) && getIntent().getIntExtra("status", OrderStatus.ORDER_STATUS_DETAIL) != 4) {
            v.setVisibility(View.VISIBLE);
            mSelectAddress = null;
            Intent intent = new Intent(this, AddressActivity.class);
            if (getIntent().getIntExtra("status", OrderStatus.ORDER_STATUS_DETAIL) == 3) {
                intent.putExtra("ID", AddressType.ADDRESS_TYPE_UPLOAD);
            }
            intent.putExtra("type", AddressType.ADDRESS_TYPE_OREDER);
            if (mOrderDetail.getAddress() != null) {
                intent.putExtra("addressid", mOrderDetail.getAddress().getId());
            } else {
                intent.putExtra("addressid", 0);
            }
            startActivityForResult(intent, CodeDefine.ORDER_DETAIL_ADDRESS_REQUEST);
        } else {
            rl_mine_record.isEnabled();
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.ll_item_order_submit_list_edit) {
            ArrayList<Integer> list = new ArrayList<Integer>();
            for (int i = 0; i < mOrderDetail.getMerchandiseList().size(); i++) {
                list.add(mOrderDetail.getMerchandiseList().get(i).getId());
            }
            Intent intent = new Intent(this, PackagesDetailActivity.class);
            intent.putIntegerArrayListExtra("list", list);
            intent.putExtra("type", OrderStatus.ORDER_STATUS_PACKAGES_EDIT);
            startActivityForResult(intent, CodeDefine.ORDER_DETAIL_PACKAGE_REQUEST);
        }
    }

    public void channel_select(View v) {
        if (OrderStatus.ORDER_STATUS_DETAIL != getIntent().getIntExtra("type", 8) && OrderStatus.ORDER_STATUS_TAX_SUBMIT != getIntent().getIntExtra("type", 8) && getIntent().getIntExtra("status", OrderStatus.ORDER_STATUS_DETAIL) != 3 && getIntent().getIntExtra("status", OrderStatus.ORDER_STATUS_DETAIL) != 4) {
            mSelectChannel = null;
            Intent intent = new Intent(this, OrderChannelActivity.class);
            intent.putExtra("type", AddressType.ADDRESS_TYPE_SELECT);
            intent.putExtra("orderdetail", mOrderDetail);
            intent.putExtra("WarehouseId", mOrderDetail.getOrderchannel().getWarehouseId());
            intent.putExtra("order", "order");
            intent.putExtra("channelcode", mOrderDetail.getOrderchannel().getCode());
            startActivityForResult(intent, CodeDefine.ORDER_DETAIL_CHANNEL_REQUEST);
        } else {
            v.setEnabled(false);
        }
    }

    public void btn_next_click(View v) {
        if (!state){
            showMessage("您还没有同意邮客服务条例");
            return;
        }
        if (mOrderDetail.getMerchandiseList() != null){
            List<OrderPackageGoods> goodList = new ArrayList<>();
            for (int i= 0 ;i<mOrderDetail.getMerchandiseList().size();i++){
                if (mOrderDetail.getMerchandiseList().get(i).getGoodList() != null){
                   goodList = mOrderDetail.getMerchandiseList().get(i).getGoodList();
                }
                for (int z = 0;z <goodList.size();z ++){
                    if (goodList.get(z).isGoolsType() == false){
                        showMessage("缺少包裹信息");
                        return;
                    }
                }
            }
        }
        if (NoDoubleClickUtils.isDoubleClick()) {
            return;
        }
        mButton_Submit.setClickable(false);

        if (4 == getIntent().getIntExtra("status", 2)) {
            Intent intent = new Intent(this, OrderPayActivity.class);
            intent.putExtra("orderNo", mOrderDetail.getOrderNo());
            intent.putExtra("payType", "TAXREPAY");
            intent.putExtra("payTypeComments", "支付税金");
            startActivityForResult(intent, CodeDefine.ORDER_DETAIL_REQUEST);
        } else {
            if (mOrderDetail != null) {
                List<OrderPackage> orderPackageList = mOrderDetail.getMerchandiseList();
                List<Integer> list = new ArrayList<Integer>();
                for (int i = 0; i < orderPackageList.size(); i++) {
                    list.add(orderPackageList.get(i).getId());
                }

                double dWeight = 0;
                int iWarehouseId = 0;
                for (OrderPackage item : orderPackageList) {
                    iWarehouseId = item.getWarehouseId();
                    dWeight += item.getWeight();
                }

                Address address = mOrderDetail.getAddress(); //(Address) getIntent().getSerializableExtra("address");
                String couponCode = "";
                if (mOrderDetail.getCouponList() != null && !mOrderDetail.getCouponList().getCode().isEmpty()) {
                    couponCode = mOrderDetail.getCouponList().getCode();
                } else {
                    couponCode = "removeCoupon";
                }
                if (address == null || address.getId() == 0) {
                    showMessage("缺少地址信息");
                    mButton_Submit.setClickable(true);
                } else {
                    mPresenter.requestCommitOrder(list, mOrderDetail.getOrderType().getCode(), address.getId(), this.fee, dWeight, mOrderDetail.getValueAddedlist(), couponCode, mOrderDetail.getOrderNo());
                }
            }else {
                mButton_Submit.setClickable(true);
            }
        }
    }

    public void coupon_order(View view) {
        if (OrderStatus.ORDER_STATUS_DETAIL != getIntent().getIntExtra("type", 8) && OrderStatus.ORDER_STATUS_TAX_SUBMIT != getIntent().getIntExtra("type", 8) && getIntent().getIntExtra("status", OrderStatus.ORDER_STATUS_DETAIL) != 3 && getIntent().getIntExtra("status", OrderStatus.ORDER_STATUS_DETAIL) != 4) {
            Intent intent = new Intent(this, UserCouponActivity.class);
            intent.putExtra("type", 1);
            if (mOrderDetail.getCouponList() == null || mOrderDetail.getCouponList().getCode() == null){
                intent.putExtra("monye", totalAmount);
            }else {//discountAmount
                intent.putExtra("monye", totalAmount+mOrderDetail.getCouponList().getCoupon().getDiscountAmount());
            }
            int id = mOrderDetail.getOrderchannel().getOrderTypeId();
            intent.putExtra("ordertypeRange", id);
            int warehouseId = mOrderDetail.getOrderchannel().getWarehouseId();
            intent.putExtra("warehouseRange", warehouseId);
            startActivityForResult(intent, CodeDefine.ORDER_COUPON_REQUEST);
        } else {
            view.setEnabled(false);
        }
    }

    public void orderDetailTerms(View view){
        if (NoDoubleClickUtils.isDoubleClick()) {
            return;
        }
        Intent intent = new Intent(this,WebActivity.class);
        intent.putExtra("showurl", CommonUtils.getBaseUrl() + "/app/mesage/member/clause.html");
        intent.putExtra("title", "邮客服务条例");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.mRxPermissions = null;
    }
}
