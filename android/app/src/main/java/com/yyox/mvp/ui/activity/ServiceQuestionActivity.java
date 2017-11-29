package com.yyox.mvp.ui.activity;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ExpandableListView;

import com.jess.arms.base.DefaultAdapter;
import com.jess.arms.utils.UiUtils;
import com.tbruyelle.rxpermissions.RxPermissions;
import com.yyox.R;
import com.yyox.Utils.ExpandedListUtils;
import com.yyox.Utils.MyToast;
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import butterknife.BindView;
import common.AppComponent;
import common.WEActivity;

public class ServiceQuestionActivity extends WEActivity<OrderPresenter> implements OrderContract.View {

    @Nullable
    @BindView(R.id.questionExpandableListView)
    ExpandableListView mExpandableListView;
    private RxPermissions mRxPermissions;

    @Override
    protected View initView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_service_question, null, false);
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
    }

    @Override
    protected void initData() {
        initControl();
        StringBuilder stringBuilder = new StringBuilder();
        try {
            BufferedReader bf = new BufferedReader(new InputStreamReader(this.getAssets().open("answerList.json")));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        mPresenter.setAdapter_Question(this);
        mPresenter.requestQuestion(true, stringBuilder.toString());
    }

    @Override
    public void setAdapter(DefaultAdapter adapter) {

    }

    @Override
    public void setPackageAdapter(PackageExpandableAdapter adapter) {

    }

    @Override
    public void setOrderPackageAdapter(OrderPackageDetailAdapter adapter) {

    }

    @Override
    public void setQuestionAdapter(QuestionExpandableAdapter adapter) {
        mExpandableListView.setAdapter(adapter);
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

    public void btn_back_click(View v) {
        this.finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.mRxPermissions = null;
    }
}
