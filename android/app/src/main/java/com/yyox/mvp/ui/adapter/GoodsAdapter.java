package com.yyox.mvp.ui.adapter;

import android.view.View;

import com.jess.arms.base.BaseHolder;
import com.jess.arms.base.DefaultAdapter;
import com.yyox.R;
import com.yyox.mvp.model.entity.OrderPackageGoods;
import com.yyox.mvp.ui.holder.GoodsItemHolder;

import java.util.List;

/**
 * Created by dadaniu on 2017-01-24.
 */

public class GoodsAdapter extends DefaultAdapter<OrderPackageGoods> {

    public GoodsAdapter(List<OrderPackageGoods> infos) {
        super(infos);
    }

    @Override
    public BaseHolder<OrderPackageGoods> getHolder(View v) {
        return new GoodsItemHolder(v);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_goods_list;
    }
}
