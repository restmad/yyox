package com.yyox.mvp.ui.holder;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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

public class AddressFotterItemHolder extends BaseHolder<Address> {

    @Nullable
    @BindView(R.id.rl_item_address_list_footer)
    RelativeLayout mRelativeLayout_Footer;

    public AddressFotterItemHolder(View itemView) {
        super(itemView);
        mRelativeLayout_Footer.setOnClickListener(this);
    }

    @Override
    public void setData(Address data) {
    }
}
