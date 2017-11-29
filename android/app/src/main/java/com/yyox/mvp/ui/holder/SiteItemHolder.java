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

public class SiteItemHolder extends BaseHolder<Site> {

    @Nullable
    @BindView(R.id.tv_site_item_img)
    ImageView mImg;

    @Nullable
    @BindView(R.id.tv_site_item_name)
    TextView mName;

    @Nullable
    @BindView(R.id.ib_site_item_del)
    ImageButton mImageButton;

    public SiteItemHolder(View itemView) {
        super(itemView);
        mImageButton.setOnClickListener(this);
    }

    @Override
    public void setData(Site data) {
        switch (data.getId()) {
            case 1:
                mImg.setImageResource(R.mipmap.rebates);
                break;
            case 2:
                mImg.setImageResource(R.mipmap.wwhaitao);
                break;
            case 3:
                mImg.setImageResource(R.mipmap.lets);
                break;
            case 4:
                mImg.setImageResource(R.mipmap.smzdm);
                break;
            case 5:
                mImg.setImageResource(R.mipmap.dx);
                break;
            case 6:
                mImg.setImageResource(R.mipmap.nlzpy);
                break;
            case 7:
                mImg.setImageResource(R.mipmap.usamazon);
                break;
            case 8:
                mImg.setImageResource(R.mipmap.jpamazon);
                break;
            case 9:
                mImg.setImageResource(R.mipmap.deamazon);
                break;
            case 10:
                mImg.setImageResource(R.mipmap.uslpm);
                break;
            case 11:
                mImg.setImageResource(R.mipmap.usdrugstore);
                break;
            case 12:
                mImg.setImageResource(R.mipmap.jprakuten);
                break;
            default:
                break;
        }

        Observable.just(data.getName()).subscribe(RxTextView.text(mName));
        if (data.isEdit()) {
            mImageButton.setVisibility(View.VISIBLE);
        } else {
            mImageButton.setVisibility(View.GONE);
        }
    }
}
