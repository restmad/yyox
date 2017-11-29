package com.yyox.mvp.ui.holder;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.jakewharton.rxbinding.widget.RxTextView;
import com.jess.arms.base.BaseHolder;
import com.yyox.R;
import com.yyox.mvp.model.entity.OrderHistrory;

import butterknife.BindView;
import rx.Observable;

/**
 * Created by dadaniu on 2017-01-24.
 */

public class OrderTrackingItemHolder extends BaseHolder<OrderHistrory> {

    @Nullable
    @BindView(R.id.tv_order_tracking_list_item_detail)
    TextView mDetail;

    @Nullable
    @BindView(R.id.tv_order_tracking_list_item_datetime)
    TextView mDatetime;

    public OrderTrackingItemHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setData(OrderHistrory data) {
        Observable.just(data.getHistory()).subscribe(RxTextView.text(mDetail));
        Observable.just(data.getActionDateWithFormat()).subscribe(RxTextView.text(mDatetime));
    }
}
