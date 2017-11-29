package com.yyox.mvp.ui.holder;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.jakewharton.rxbinding.widget.RxTextView;
import com.jess.arms.base.BaseHolder;
import com.yyox.R;
import com.yyox.Utils.DateUtils;
import com.yyox.mvp.model.entity.Order;

import butterknife.BindView;
import rx.Observable;

/**
 * Created by dadaniu on 2017-01-14.
 */

public class OrderCompletedItemHolder extends BaseHolder<Order>  {

    @Nullable
    @BindView(R.id.tv_order_completed_list_item_name)
    TextView mName;

    @Nullable
    @BindView(R.id.tv_order_completed_list_item_number)
    TextView mNumber;

    @Nullable
    @BindView(R.id.tv_order_completed_list_item_time)
    TextView mTime;

    @Nullable
    @BindView(R.id.tv_order_completed_list_item_status)
    TextView mStatus;

    public OrderCompletedItemHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setData(Order data) {
        String strTtime = "";
        if(!data.getOrStatusDate().isEmpty()){
            long timeStamp = DateUtils.strToTimeStamp(data.getOrStatusDate());
            strTtime = DateUtils.convertTimeToFormat(timeStamp);
        }
        if(data.getType() == 3){
            Observable.just("合箱发货").subscribe(RxTextView.text(mName));
        }else {
            Observable.just(data.getInventorybasic().get(0).getNickname()).subscribe(RxTextView.text(mName));
        }
        Observable.just("邮客单号："+data.getOrderNo()).subscribe(RxTextView.text(mNumber));
        Observable.just(strTtime).subscribe(RxTextView.text(mTime));
        Observable.just(data.getOrderStatus()).subscribe(RxTextView.text(mStatus));
    }
}
