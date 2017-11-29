package com.yyox.mvp.ui.holder;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import com.jess.arms.base.BaseHolder;
import com.yyox.R;
import com.yyox.mvp.model.entity.OrderPackage;
import com.yyox.mvp.model.entity.OrderPackageGoods;

import butterknife.BindView;

/**
 * Created by dadaniu on 2017-01-12.
 */

public class PackageDetailGoodsFooterItemHolder extends BaseHolder<OrderPackage> {

    @Nullable
    @BindView(R.id.btn_item_package_detail_goods_footer_list_add)
    Button mButton_Add;

    public PackageDetailGoodsFooterItemHolder(View itemView) {
        super(itemView);
        mButton_Add.setOnClickListener(this);
    }

    @Override
    public void setData(OrderPackage data) {
        OrderPackageGoods orderPackageGoods = data.getOrderPackageGoods();
        if(orderPackageGoods != null) {
            mButton_Add.setText("完善商品申报信息("+orderPackageGoods.getCount()+")");
        }
    }
}
