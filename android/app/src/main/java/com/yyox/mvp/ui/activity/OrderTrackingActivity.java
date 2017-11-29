package com.yyox.mvp.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.jess.arms.base.DefaultAdapter;
import com.jess.arms.utils.UiUtils;
import com.tbruyelle.rxpermissions.RxPermissions;
import com.yyox.R;
import com.yyox.Utils.CommonUtils;
import com.yyox.Utils.MyToast;
import com.yyox.Utils.PictureUtil;
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

import java.io.ByteArrayOutputStream;
import java.util.List;

import butterknife.BindView;
import cn.sharesdk.facebook.Facebook;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.ShareContentCustomizeCallback;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.system.text.ShortMessage;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.tencent.weibo.TencentWeibo;
import cn.sharesdk.twitter.Twitter;
import cn.sharesdk.wechat.favorite.WechatFavorite;
import cn.sharesdk.wechat.moments.WechatMoments;
import common.AppComponent;
import common.WEActivity;
import common.WEApplication;

public class OrderTrackingActivity extends WEActivity<OrderPresenter> implements OrderContract.View {

    @Nullable
    @BindView(R.id.tv_order_tracking_title)
    TextView mTextView_Title;

    @Nullable
    @BindView(R.id.tv_order_tracking_orderno)
    TextView mTextView_OrderNo;

    @Nullable
    @BindView(R.id.orderTrackingRecyclerView)
    RecyclerView mRecyclerView;

    @Nullable
    @BindView(R.id.v_order_tracking_circle1)
    View mCircle1;

    @Nullable
    @BindView(R.id.v_order_tracking_circle2)
    View mCircle2;

    @Nullable
    @BindView(R.id.v_order_tracking_circle3)
    View mCircle3;

    @Nullable
    @BindView(R.id.v_order_tracking_circle4)
    View mCircle4;

    @Nullable
    @BindView(R.id.v_order_tracking_circle5)
    View mCircle5;

    @Nullable
    @BindView(R.id.v_order_tracking_line1)
    View mLine1;

    @Nullable
    @BindView(R.id.v_order_tracking_line2)
    View mLine2;

    @Nullable
    @BindView(R.id.v_order_tracking_line3)
    View mLine3;

    @Nullable
    @BindView(R.id.v_order_tracking_line4)
    View mLine4;

    @Nullable
    @BindView(R.id.tv_order_tracking_title_share)
    Button mBtn_share;

    @Nullable
    @BindView(R.id.text1)
    TextView mText1;

    @Nullable
    @BindView(R.id.text2)
    TextView mText2;

    @Nullable
    @BindView(R.id.text3)
    TextView mText3;

    @Nullable
    @BindView(R.id.text4)
    TextView mText4;
    @Nullable
    @BindView(R.id.text5)
    TextView mText5;

    private String mOrderNo;
    private String summary;
    private String url;
    private String searchNo;
    private int orderStatus;
    private String companyCode;
    private String mTitle;
    private RxPermissions mRxPermissions;
    private Bitmap temp;
    private String mImageTemp;
    private int mCustomerId;

    @Override
    protected View initView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_order_tracking, null, false);
    }

    @Override
    protected void initData() {
        Bitmap logoBitmap = BitmapFactory.decodeResource(WEApplication.getContext().getResources(), R.mipmap.logo);
        ByteArrayOutputStream logoStream = new ByteArrayOutputStream();
        boolean res = logoBitmap.compress(Bitmap.CompressFormat.PNG, 100, logoStream);
        byte[] logoBuf = logoStream.toByteArray();
         temp = BitmapFactory.decodeByteArray(logoBuf, 0, logoBuf.length);
        String path = PictureUtil.getAlbumDir().getPath();
        mImageTemp = path + "/yyox.png";
        PictureUtil.saveImageToGallery(mImageTemp, temp);

        mOrderNo = getIntent().getStringExtra("orderNo");
//        if(!mOrderNo.isEmpty()){
        mTextView_Title.setText(getIntent().getStringExtra("orderTitle"));
        mTextView_OrderNo.setText("邮客单号  " + mOrderNo);
        mPresenter.setAdapter(OrderStatus.ORDER_STATUS_TRACKING);
        mPresenter.requestOrderTracking(true, mOrderNo);

        if (OrderStatus.ORDER_STATUS_WAITING_OUT_WAREHOUSE == getIntent().getIntExtra("type", 0)) {
            //待出库
            orderStatus = 1;
            mText1.setTextColor(Color.parseColor("#FC9B31"));
            mCircle1.setBackground(getResources().getDrawable(R.drawable.ic_circle_bk_wait));
            mLine1.setBackgroundColor(Color.parseColor("#FC9B31"));
        } else if (OrderStatus.ORDER_STATUS_OUT_WAREHOUSE == getIntent().getIntExtra("type", 0)) {
            //已出库
            orderStatus = 2;
            mText1.setTextColor(Color.parseColor("#1B82D1"));
            mText2.setTextColor(Color.parseColor("#FC9B31"));
            mCircle1.setBackground(getResources().getDrawable(R.drawable.ic_circle_bk));
            mLine1.setBackgroundColor(Color.parseColor("#1B82D1"));
            mCircle2.setBackground(getResources().getDrawable(R.drawable.ic_circle_bk_wait));
            mLine2.setBackgroundColor(Color.parseColor("#FC9B31"));
        } else if (OrderStatus.ORDER_STATUS_CUSTOMS == getIntent().getIntExtra("type", 0)) {
            //清关中
            orderStatus = 3;
            mText1.setTextColor(Color.parseColor("#1B82D1"));
            mText2.setTextColor(Color.parseColor("#1B82D1"));
            mText3.setTextColor(Color.parseColor("#FC9B31"));
            mCircle1.setBackground(getResources().getDrawable(R.drawable.ic_circle_bk));
            mLine1.setBackgroundColor(Color.parseColor("#1B82D1"));
            mCircle2.setBackground(getResources().getDrawable(R.drawable.ic_circle_bk));
            mLine2.setBackgroundColor(Color.parseColor("#1B82D1"));
            mCircle3.setBackground(getResources().getDrawable(R.drawable.ic_circle_bk_wait));
            mLine3.setBackgroundColor(Color.parseColor("#FC9B31"));
        } else if (OrderStatus.ORDER_STATUS_DISPATCHING == getIntent().getIntExtra("type", 0)) {
            //国内配送
            orderStatus = 4;
            mText1.setTextColor(Color.parseColor("#1B82D1"));
            mText2.setTextColor(Color.parseColor("#1B82D1"));
            mText3.setTextColor(Color.parseColor("#1B82D1"));
            mText4.setTextColor(Color.parseColor("#FC9B31"));
            mCircle1.setBackground(getResources().getDrawable(R.drawable.ic_circle_bk));
            mLine1.setBackgroundColor(Color.parseColor("#1B82D1"));
            mCircle2.setBackground(getResources().getDrawable(R.drawable.ic_circle_bk));
            mLine2.setBackgroundColor(Color.parseColor("#1B82D1"));
            mCircle3.setBackground(getResources().getDrawable(R.drawable.ic_circle_bk));
            mLine3.setBackgroundColor(Color.parseColor("#1B82D1"));
            mCircle4.setBackground(getResources().getDrawable(R.drawable.ic_circle_bk_wait));
            mLine4.setBackgroundColor(Color.parseColor("#FC9B31"));
        } else if (OrderStatus.ORDER_STATUS_COMPLETED == getIntent().getIntExtra("type", 0)) {
            //已完成
            orderStatus = 5;
            mText1.setTextColor(Color.parseColor("#1B82D1"));
            mText2.setTextColor(Color.parseColor("#1B82D1"));
            mText3.setTextColor(Color.parseColor("#1B82D1"));
            mText4.setTextColor(Color.parseColor("#1B82D1"));
            mText5.setTextColor(Color.parseColor("#FC9B31"));
            mCircle1.setBackground(getResources().getDrawable(R.drawable.ic_circle_bk));
            mLine1.setBackgroundColor(Color.parseColor("#1B82D1"));
            mCircle2.setBackground(getResources().getDrawable(R.drawable.ic_circle_bk));
            mLine2.setBackgroundColor(Color.parseColor("#1B82D1"));
            mCircle3.setBackground(getResources().getDrawable(R.drawable.ic_circle_bk));
            mLine3.setBackgroundColor(Color.parseColor("#1B82D1"));
            mCircle4.setBackground(getResources().getDrawable(R.drawable.ic_circle_bk));
            mLine4.setBackgroundColor(Color.parseColor("#1B82D1"));
            mCircle5.setBackground(getResources().getDrawable(R.drawable.ic_circle_bk_wait));
        }
    }

    public void btn_back_click(View v) {
        this.finish();
    }

    /**
     * 初始化RecycleView
     */
    private void initRecycleView() {
        configRecycleView(mRecyclerView, new GridLayoutManager(this, 1));
    }

    /**
     * 配置recycleview
     *
     * @param recyclerView
     * @param layoutManager
     */
    private void configRecycleView(RecyclerView recyclerView, RecyclerView.LayoutManager layoutManager) {
        recyclerView.setLayoutManager(layoutManager);
        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    public void setAdapter(DefaultAdapter adapter) {
        mRecyclerView.setAdapter(adapter);
        initRecycleView();
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
    public void setUIValue(OrderDetail orderDetailJson) {

    }

    @Override
    public void setFeeValue(Fee fee) {

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
        summary = orderHistoryItem.getsummary();
        url = orderHistoryItem.getUrl();
        searchNo = orderHistoryItem.getSearchNo();
        companyCode = orderHistoryItem.getCompanyCode();
        mTitle = orderHistoryItem.getTitle();
        mCustomerId = orderHistoryItem.getCustomerId();
    }

    @Override
    public void setOnClick() {

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

    public void rl_order_tracking_orderno_click(View v) {
        Intent intent = new Intent(OrderTrackingActivity.this, OrderDetailActivity.class);
        intent.putExtra("type", OrderStatus.ORDER_STATUS_DETAIL);
        intent.putExtra("orderNo", mOrderNo);
        startActivity(intent);
    }

    public void logistics_share(View view) {
        showShare();
    }

    private void showShare() {
        ShareSDK.initSDK(this);
        OnekeyShare oks = new OnekeyShare();
        //隐藏
        oks.addHiddenPlatform(QQ.NAME);
        oks.addHiddenPlatform(SinaWeibo.NAME);
        oks.addHiddenPlatform(TencentWeibo.NAME);
        oks.addHiddenPlatform(QZone.NAME);
        oks.addHiddenPlatform(WechatFavorite.NAME);
        oks.addHiddenPlatform(Facebook.NAME);
        oks.addHiddenPlatform(Twitter.NAME);
        oks.addHiddenPlatform(ShortMessage.NAME);

        oks.disableSSOWhenAuthorize();
        String packgeName = mTextView_Title.getText().toString();
        if (!packgeName.isEmpty() && packgeName != "") {
            oks.setTitle(packgeName);
            mTitle = packgeName;
        } else {
            oks.setTitle(mTitle);
        }
        oks.setShareContentCustomizeCallback(new ShareContentCustomizeCallback() {
            @Override
            public void onShare(Platform platform, Platform.ShareParams paramsToShare) {
                if (WechatMoments.NAME.equals(platform.getName())) {
                    paramsToShare.setTitle(summary);
                }
            }
        });
        oks.setText(summary);
//        oks.setImagePath(mImageTemp+"/yyox.png");  //分享sdcard目录下的图片
        oks.setImageUrl(CommonUtils.getBaseUrl()+"/app/share/images/share.jpg");
        oks.setUrl(url + "?orderNo=" + searchNo + "&orderStatus=" + orderStatus + "&companyCode=" + companyCode + "&title=" + mTitle+"&customerId="+mCustomerId);
        oks.show(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRxPermissions = null;
    }
}
