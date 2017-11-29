package com.yyox.mvp.ui.adapter;

import android.view.View;

import com.jess.arms.base.BaseHolder;
import com.jess.arms.base.DefaultAdapter;
import com.yyox.R;
import com.yyox.mvp.model.entity.CouponItem;
import com.yyox.mvp.ui.holder.CouponItemHolder;

import java.util.List;

/**
 * Created by dadaniu on 2017-01-12.
 */

public class CouponAdapter extends DefaultAdapter<CouponItem>  {

    public CouponAdapter(List<CouponItem> infos) {
        super(infos);
    }

    @Override
    public BaseHolder<CouponItem> getHolder(View v) {
        return new CouponItemHolder(v);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_user_coupon_list;
    }
}
