package com.yyox.mvp.ui.holder;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.jakewharton.rxbinding.widget.RxTextView;
import com.jess.arms.base.BaseHolder;
import com.yyox.R;
import com.yyox.mvp.model.entity.OrderPackageGoods;

import butterknife.BindView;
import rx.Observable;

/**
 * Created by dadaniu on 2017-01-24.
 */

public class GoodsItemHolder extends BaseHolder<OrderPackageGoods> {

    @Nullable
    @BindView(R.id.tv_item_goods_list_name)
    TextView mName;

    @Nullable
    @BindView(R.id.tv_item_goods_list_count)
    TextView mCount;

    @Nullable
    @BindView(R.id.cb_item_goods_list_select)
    CheckBox mSelect;

    public GoodsItemHolder(View itemView) {
        super(itemView);
        mSelect.setOnClickListener(this);
    }

    @Override
    public void setData(OrderPackageGoods data) {
        Observable.just(data.getProductName()).subscribe(RxTextView.text(mName));
        Observable.just("*"+data.getStock()).subscribe(RxTextView.text(mCount));
        mSelect.setChecked(data.isChecked());
    }
}
