package com.yyox.mvp.ui.adapter;

import android.view.View;

import com.jess.arms.base.BaseHolder;
import com.jess.arms.base.DefaultAdapter;
import com.yyox.R;
import com.yyox.mvp.model.entity.PackageItem;
import com.yyox.mvp.ui.holder.PackageBoxItemHolder;

import java.util.List;

/**
 * Created by dadaniu on 2017-01-14.
 */

public class PackageBoxAdapter extends DefaultAdapter<PackageItem> {

    public PackageBoxAdapter(List<PackageItem> infos) {
        super(infos);
    }

    @Override
    public BaseHolder<PackageItem> getHolder(View v) {
        return new PackageBoxItemHolder(v);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_package_box_list;
    }

}
