package com.yyox.mvp.ui.holder;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.jakewharton.rxbinding.widget.RxTextView;
import com.jess.arms.base.BaseHolder;
import com.yyox.R;
import com.yyox.Utils.DateUtils;
import com.yyox.mvp.model.entity.OrderPending;

import butterknife.BindView;
import rx.Observable;

/**
 * Created by dadaniu on 2017-01-14.
 */

public class OrderPendingPackageItemHolder extends BaseHolder<OrderPending>  {

    @Nullable
    @BindView(R.id.tv_item_order_pending_package_list_name)
    TextView mName;

    @Nullable
    @BindView(R.id.tv_item_order_pending_package_list_goods)
    TextView mGoods;

    @Nullable
    @BindView(R.id.tv_item_order_pending_package_list_time)
    TextView mTime;

    @Nullable
    @BindView(R.id.tv_item_order_pending_package_list_status)
    TextView mStatus;

    public OrderPendingPackageItemHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setData(OrderPending data) {
        String strTtime = "";
        if(!data.getDate().isEmpty()){
            long timeStamp = DateUtils.strToTimeStamp(data.getDate());
            strTtime = DateUtils.convertTimeToFormat(timeStamp);
        }
        if(!data.getNickname().isEmpty()){
            Observable.just("包裹昵称：" + data.getNickname()).subscribe(RxTextView.text(mName));
        }else{
            Observable.just("运单号："+data.getNo()).subscribe(RxTextView.text(mName));
        }
        Observable.just("").subscribe(RxTextView.text(mGoods));
        Observable.just(data.getWarehouseName()).subscribe(RxTextView.text(mTime));
        Observable.just(data.getOrderStatus()+" "+strTtime).subscribe(RxTextView.text(mStatus));
    }
}
