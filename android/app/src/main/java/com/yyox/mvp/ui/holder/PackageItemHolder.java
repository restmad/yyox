package com.yyox.mvp.ui.holder;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding.widget.RxTextView;
import com.jess.arms.base.BaseHolder;
import com.yyox.R;
import com.yyox.mvp.model.entity.PackageItem;

import butterknife.BindView;
import rx.Observable;

/**
 * Created by dadaniu on 2017-01-14.
 */

public class PackageItemHolder extends BaseHolder<PackageItem>  {

    @Nullable
    @BindView(R.id.tv_order_waiting_list_item_name)
    TextView mName;

    @Nullable
    @BindView(R.id.tv_order_waiting_list_item_number)
    TextView mNumber;

    @Nullable
    @BindView(R.id.tv_order_waiting_list_item_detail)
    TextView mDetail;

    @Nullable
    @BindView(R.id.tv_order_waiting_list_item_warehouse)
    TextView mWarehouse;

    @Nullable
    @BindView(R.id.ll_order_waiting_list_item_edit)
    LinearLayout mEdit;

    @Nullable
    @BindView(R.id.ll_order_waiting_list_item_delete)
    LinearLayout mDelete;

    public PackageItemHolder(View itemView) {
        super(itemView);
        mEdit.setOnClickListener(this);
        mDelete.setOnClickListener(this);
    }

    @Override
    public void setData(PackageItem data) {

        Observable.just(data.getNickname()).subscribe(RxTextView.text(mName));
        Observable.just("运单号："+data.getCarrierNo()).subscribe(RxTextView.text(mNumber));
        Observable.just("").subscribe(RxTextView.text(mDetail));
        Observable.just(data.getWarehouseName()).subscribe(RxTextView.text(mWarehouse));

    }

}
