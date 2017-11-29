package com.yyox.mvp.ui.adapter;

import android.view.View;

import com.jess.arms.base.BaseHolder;
import com.jess.arms.base.DefaultAdapter;
import com.yyox.R;
import com.yyox.mvp.model.entity.PackageItem;
import com.yyox.mvp.ui.holder.PackageItemHolder;

import java.util.List;

/**
 * Created by dadaniu on 2017-01-14.
 */

public class PackageAdapter extends DefaultAdapter<PackageItem> {

    public PackageAdapter(List<PackageItem> infos) {
        super(infos);
    }

    @Override
    public BaseHolder<PackageItem> getHolder(View v) {
        return new PackageItemHolder(v);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_order_waiting_list;
    }

}
