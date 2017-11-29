package com.yyox.mvp.ui.holder;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding.widget.RxTextView;
import com.jess.arms.base.BaseHolder;
import com.yyox.R;
import com.yyox.mvp.model.entity.Address;

import butterknife.BindView;
import rx.Observable;

/**
 * Created by dadaniu on 2017-01-12.
 */

public class AddressOrderItemHolder extends BaseHolder<Address> {

    @Nullable
    @BindView(R.id.tv_item_address_list_order_name)
    TextView mName;

    @Nullable
    @BindView(R.id.tv_item_address_list_order_phone)
    TextView mPhone;

    @Nullable
    @BindView(R.id.tv_item_address_list_order_address)
    TextView mAddress;

    @Nullable
    @BindView(R.id.cb_item_address_list_order_default)
    CheckBox mDefault;

    @Nullable
    @BindView(R.id.ll_item_address_list_order_edit)
    LinearLayout mLinearLayout_Edit;

    @Nullable
    @BindView(R.id.ll_item_address_list_order_delete)
    LinearLayout mLinearLayout_Delete;

    @Nullable
    @BindView(R.id.address_select_checkBox)
    CheckBox mAddress_select_checkBox;

    public AddressOrderItemHolder(View itemView) {
        super(itemView);
        mDefault.setOnClickListener(this);
        mLinearLayout_Edit.setOnClickListener(this);
        mLinearLayout_Delete.setOnClickListener(this);
        mAddress_select_checkBox.setOnClickListener(this);
    }

    @Override
    public void setData(Address data) {
        Observable.just(data.getName()).subscribe(RxTextView.text(mName));
        Observable.just(data.getMobile()).subscribe(RxTextView.text(mPhone));
        Observable.just(data.getRegion()+data.getDetailaddress()).subscribe(RxTextView.text(mAddress));
        mDefault.setChecked(data.getIschecked());
        mAddress_select_checkBox.setChecked(data.getIsdefault());
    }
}
