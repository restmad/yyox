package com.yyox.mvp.ui.holder;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.jakewharton.rxbinding.widget.RxTextView;
import com.jess.arms.base.BaseHolder;
import com.yyox.R;
import com.yyox.Utils.CommonUtils;
import com.yyox.mvp.model.entity.OrderPackage;
import com.yyox.mvp.model.entity.OrderPackageGoods;

import butterknife.BindView;
import rx.Observable;

/**
 * Created by dadaniu on 2017-01-12.
 */

public class PackageDetailGoodsItemHolder extends BaseHolder<OrderPackage> {

    @Nullable
    @BindView(R.id.tv_item_package_detail_goods_list_name)
    TextView mName;

    @Nullable
    @BindView(R.id.tv_item_package_detail_goods_list_price)
    TextView mPrice;

    @Nullable
    @BindView(R.id.tv_item_package_detail_goods_list_count)
    TextView mCount;

    @Nullable
    @BindView(R.id.ib_item_package_detail_goods_list_del)
    ImageButton mImageButton;

    public PackageDetailGoodsItemHolder(View itemView) {
        super(itemView);
        mImageButton.setOnClickListener(this);
    }

    @Override
    public void setData(OrderPackage data) {
        OrderPackageGoods orderPackageGoods = data.getOrderPackageGoods();
        if(orderPackageGoods != null){
            Observable.just(orderPackageGoods.getAppProductNameCNY()).subscribe(RxTextView.text(mName));
            Observable.just("总价：" +CommonUtils.doubleFormat(orderPackageGoods.getAmount()) + orderPackageGoods.getCurrencyName() ).subscribe(RxTextView.text(mPrice));
            Observable.just("数量："+orderPackageGoods.getStock() + orderPackageGoods.getUnits()).subscribe(RxTextView.text(mCount));
        }
    }
}
