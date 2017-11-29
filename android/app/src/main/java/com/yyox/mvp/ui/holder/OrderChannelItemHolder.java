package com.yyox.mvp.ui.holder;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.jakewharton.rxbinding.widget.RxTextView;
import com.jess.arms.base.BaseHolder;
import com.yyox.R;
import com.yyox.mvp.model.entity.Channel;

import butterknife.BindView;
import rx.Observable;

/**
 * Created by dadaniu on 2017-01-16.
 */

public class OrderChannelItemHolder extends BaseHolder<Channel> {

    @Nullable
    @BindView(R.id.tv_item_order_channel_list_name)
    TextView mName;

    @Nullable
    @BindView(R.id.tv_item_order_channel_list_price)
    TextView mPrice;

    @Nullable
    @BindView(R.id.tv_item_order_channel_list_detail)
    TextView mDetail;

    @Nullable
    @BindView(R.id.cb_item_order_channel_list_select)
    CheckBox mDefault;

    public OrderChannelItemHolder(View itemView) {
        super(itemView);
        mDefault.setOnClickListener(this);
    }

    @Override
    public void setData(Channel data) {
        Observable.just(data.getCode()).subscribe(RxTextView.text(mName));
        Observable.just(data.getPriceWeight()).subscribe(RxTextView.text(mPrice));
        Observable.just(data.getExplain()).subscribe(RxTextView.text(mDetail));
        mDefault.setChecked(data.isChecked());
    }
}
