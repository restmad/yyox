package com.yyox.mvp.ui.adapter;

import android.view.View;

import com.jess.arms.base.BaseHolder;
import com.jess.arms.base.DefaultAdapter;
import com.yyox.R;
import com.yyox.mvp.model.entity.WarehouseList;
import com.yyox.mvp.ui.holder.WarehouseListHolder;

import java.util.List;

/**
 * Created by 95 on 2017/5/19.
 */
public class WarehouseAdapter extends DefaultAdapter<WarehouseList> {

    public WarehouseAdapter(List<WarehouseList> infos) {
        super(infos);
    }

    @Override
    public BaseHolder<WarehouseList> getHolder(View v) {
        return new WarehouseListHolder(v);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_user_warehouse_list;
    }
}
