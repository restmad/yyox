package com.yyox.mvp.ui.holder;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.jakewharton.rxbinding.widget.RxTextView;
import com.jess.arms.base.BaseHolder;
import com.yyox.R;
import com.yyox.mvp.model.entity.Site;

import butterknife.BindView;
import rx.Observable;

/**
 * Created by dadaniu on 2017-01-12.
 */

public class AddSiteItemHolder extends BaseHolder<Site>  {

    @Nullable
    @BindView(R.id.iv_addsite_item_logo)
    ImageView mImageView_Logo;

    @Nullable
    @BindView(R.id.tv_addsite_item_name)
    TextView mTextView_Name;

    @Nullable
    @BindView(R.id.tv_addsite_item_detail)
    TextView mTextView_Detail;

    public AddSiteItemHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setData(Site data) {
        Observable.just(data.getName()).subscribe(RxTextView.text(mTextView_Name));
    }
}
