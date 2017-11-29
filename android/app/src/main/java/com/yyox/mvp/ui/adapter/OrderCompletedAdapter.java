package com.yyox.mvp.ui.adapter;

import android.view.View;

import com.jess.arms.base.BaseHolder;
import com.jess.arms.base.DefaultAdapter;
import com.yyox.R;
import com.yyox.mvp.model.entity.Order;
import com.yyox.mvp.ui.holder.OrderCompletedItemHolder;

import java.util.List;

/**
 * Created by dadaniu on 2017-01-14.
 */

public class OrderCompletedAdapter extends DefaultAdapter<Order>  {

    public OrderCompletedAdapter(List<Order> infos) {
        super(infos);
    }

    @Override
    public BaseHolder<Order> getHolder(View v) {
        return new OrderCompletedItemHolder(v);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_order_completed_list;
    }
}
