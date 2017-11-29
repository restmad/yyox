package com.yyox.mvp.ui.adapter;

import android.view.View;

import com.jess.arms.base.BaseHolder;
import com.jess.arms.base.DefaultAdapter;
import com.yyox.R;
import com.yyox.mvp.model.entity.OrderHistrory;
import com.yyox.mvp.ui.holder.OrderTrackingItemHolder;

import java.util.List;

/**
 * Created by dadaniu on 2017-01-24.
 */

public class OrderTrackingAdapter extends DefaultAdapter<OrderHistrory> {

    public OrderTrackingAdapter(List<OrderHistrory> infos) {
        super(infos);
    }

    @Override
    public BaseHolder<OrderHistrory> getHolder(View v) {
        return new OrderTrackingItemHolder(v);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_order_tracking_list;
    }
}
