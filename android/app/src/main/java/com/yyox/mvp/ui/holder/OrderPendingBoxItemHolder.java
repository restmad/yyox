package com.yyox.mvp.ui.holder;

import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.TextView;

import com.jakewharton.rxbinding.widget.RxTextView;
import com.jess.arms.base.BaseHolder;
import com.yyox.R;
import com.yyox.Utils.DateUtils;
import com.yyox.mvp.model.entity.OrderPending;

import butterknife.BindView;
import rx.Observable;

import static com.jess.arms.utils.UiUtils.getResources;

/**
 * Created by dadaniu on 2017-01-14.
 */

public class OrderPendingBoxItemHolder extends BaseHolder<OrderPending> {

    @Nullable
    @BindView(R.id.tv_item_order_pending_box_list_name)
    TextView mName;

    @Nullable
    @BindView(R.id.tv_item_order_pending_box_list_package)
    TextView mPackage;

    @Nullable
    @BindView(R.id.tv_item_order_pending_box_list_time)
    TextView mTime;

    @Nullable
    @BindView(R.id.tv_item_order_pending_box_list_status)
    TextView mStatus;

    @Nullable
    @BindView(R.id.tv_item_order_pending_box_list_operate_statement)
    TextView mStatement;

    @Nullable
    @BindView(R.id.tv_item_order_pending_box_list_operate_status)
    TextView mOperateStatus;

    public OrderPendingBoxItemHolder(View itemView) {
        super(itemView);
        mOperateStatus.setOnClickListener(this);
    }

    @Override
    public void setData(OrderPending data) {
        String strTtime = "";
        if (!data.getDate().isEmpty()) {
            long timeStamp = DateUtils.strToTimeStamp(data.getDate());
            strTtime = DateUtils.convertTimeToFormat(timeStamp);
        }
        Observable.just("邮客单号：" + data.getNo()).subscribe(RxTextView.text(mName));
        SpannableString styledText2 = new SpannableString("包裹数量：" + data.getPackageNum() + "件");
        int color2 = getResources().getColor(R.color.my_yellow);
        styledText2.setSpan(new ForegroundColorSpan(color2), 5, 6, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        Observable.just(styledText2).subscribe(RxTextView.text(mPackage));
        if (data.getOrderStatus() != null && data.getOrderStatus().equals("已入库")) {
            Observable.just(data.getWarehouseName()).subscribe(RxTextView.text(mTime));
            Observable.just(data.getOrderStatus() +" "+ strTtime).subscribe(RxTextView.text(mStatus));
        } else if (data.getStatus() == 3) {
            Observable.just(strTtime).subscribe(RxTextView.text(mTime));
            Observable.just(data.getStatement()).subscribe(RxTextView.text(mStatus));
        } else {
            Observable.just(strTtime).subscribe(RxTextView.text(mTime));
            Observable.just(data.getOrderStatus()).subscribe(RxTextView.text(mStatus));
        }

        if (data.getStatus() == 1) {
            Observable.just(data.getStatement()).subscribe(RxTextView.text(mStatement));
            Observable.just("创建订单").subscribe(RxTextView.text(mOperateStatus));
        } else if (data.getStatus() == 2) {
            Observable.just(data.getStatement()).subscribe(RxTextView.text(mStatement));
            Observable.just("去支付").subscribe(RxTextView.text(mOperateStatus));
        } else if (data.getStatus() == 3) {
            Observable.just("").subscribe(RxTextView.text(mStatement));
            Observable.just("上传身份证").subscribe(RxTextView.text(mOperateStatus));
        } else if (data.getStatus() == 4) {
            Observable.just(data.getStatement()).subscribe(RxTextView.text(mStatement));
            Observable.just("缴税金").subscribe(RxTextView.text(mOperateStatus));
        }
    }
}
