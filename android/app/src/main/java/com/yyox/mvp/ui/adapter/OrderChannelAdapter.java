package com.yyox.mvp.ui.adapter;

import android.view.View;

import com.jess.arms.base.BaseHolder;
import com.jess.arms.base.DefaultAdapter;
import com.yyox.R;
import com.yyox.mvp.model.entity.Channel;
import com.yyox.mvp.ui.holder.OrderChannelItemHolder;

import java.util.List;

/**
 * Created by dadaniu on 2017-01-16.
 */

public class OrderChannelAdapter extends DefaultAdapter<Channel> {

    public OrderChannelAdapter(List<Channel> infos) {
        super(infos);
    }

    @Override
    public BaseHolder<Channel> getHolder(View v) {
        return new OrderChannelItemHolder(v);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_order_channel_list;
    }
}
