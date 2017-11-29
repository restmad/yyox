package com.yyox.mvp.ui.holder;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding.widget.RxTextView;
import com.jess.arms.base.BaseHolder;
import com.yyox.R;
import com.yyox.mvp.model.entity.CouponItem;

import butterknife.BindView;
import rx.Observable;

/**
 * Created by dadaniu on 2017-01-12.
 */

public class CouponItemHolder extends BaseHolder<CouponItem>  {

    @Nullable
    @BindView(R.id.coupon_lin)
    LinearLayout mCoupon_lin;
    @Nullable
    @BindView(R.id.tv_coupon_list_item_name)
    TextView mName;

    @Nullable
    @BindView(R.id.tv_coupon_list_item_condition)
    TextView mCondition;

    @Nullable
    @BindView(R.id.tv_coupon_list_item_date)
    TextView mDate;

    @Nullable
    @BindView(R.id.tv_coupon_list_item_money)
    TextView mMoney;

    @Nullable
    @BindView(R.id.lin)
    LinearLayout lin;

    public CouponItemHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setData(CouponItem data) {
       
        if (!data.getUsable()){//表示已过期或不可用
            lin.setBackgroundResource(R.mipmap.coupon_fales);
        }else {
            lin.setBackgroundResource(R.mipmap.coupon_normal_bg);
        }
        Observable.just(data.getMcoupon().getName()).subscribe(RxTextView.text(mName));
        Observable.just("使用限制:"+data.getLimitUse()).subscribe(RxTextView.text(mCondition));
        Observable.just("有效期: "+data.getValidTo()).subscribe(RxTextView.text(mDate));
        Observable.just((""+data.getMcoupon().getDiscountAmount()).substring(0,(""+data.getMcoupon().getDiscountAmount()).indexOf("."))).subscribe(RxTextView.text(mMoney));
    }
}
