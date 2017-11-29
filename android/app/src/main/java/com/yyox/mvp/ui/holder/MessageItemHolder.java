package com.yyox.mvp.ui.holder;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jakewharton.rxbinding.widget.RxTextView;
import com.jess.arms.base.BaseHolder;
import com.yyox.R;
import com.yyox.Utils.DateUtils;
import com.yyox.consts.HomeMessage;
import com.yyox.mvp.model.entity.Message;

import butterknife.BindView;
import rx.Observable;

/**
 * Created by dadaniu on 2017-01-24.
 */

public class MessageItemHolder extends BaseHolder<Message> {

    @Nullable
    @BindView(R.id.tv_item_message_list_name)
    TextView mName;

    @Nullable
    @BindView(R.id.tv_item_message_list_datetime)
    TextView mDatetime;

    @Nullable
    @BindView(R.id.tv_item_message_list_detail)
    TextView mDetail;

    @Nullable
    @BindView(R.id.image)
    ImageView mImage;

    public MessageItemHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setData(Message data) {
        String strTtime = "";
        if (!data.getOperateTime().isEmpty()) {
            long timeStamp = DateUtils.strToTimeStamp(data.getOperateTime());
            strTtime = DateUtils.convertTimeToFormat(timeStamp);
        }
        Observable.just(strTtime).subscribe(RxTextView.text(mDatetime));
        Observable.just(data.getInformation()).subscribe(RxTextView.text(mDetail));
        String state = data.getState();
        switch (state) {
            case HomeMessage.HOME_LIST_USER:
                mImage.setImageResource(R.mipmap.kehu);
                Observable.just(data.getSource_name()).subscribe(RxTextView.text(mName));
                break;
            case HomeMessage.HOME_LIST_ORDER:
                String sourceType = data.getSourceType();
                if (sourceType.equals("1")) {
                    mImage.setImageResource(R.mipmap.yunzhuan);
                    Observable.just("邮客转运").subscribe(RxTextView.text(mName));
                } else if (sourceType.equals("2")) {
                    mImage.setImageResource(R.mipmap.kehu);
                    Observable.just(data.getCustomerName()).subscribe(RxTextView.text(mName));
                } else if (sourceType.equals("3")) {
                    mImage.setImageResource(R.mipmap.cangku);
                    Observable.just(data.getSource_name()).subscribe(RxTextView.text(mName));
                } else {
                    mImage.setImageResource(R.mipmap.cangku);
                    Observable.just(data.getSource_name()).subscribe(RxTextView.text(mName));
                }
                //设置运单信息
                String information = data.getInformation();
                if (information.contains(") 已创建")) {
                    data.setInformation("您的订单(" + data.getOtherNo() + ")已经创建");
                } else if (information.contains("仓库订单已分拣")) {
                    data.setInformation("您的订单已经打印完毕");
                } else if (information.contains("仓库订单已下架")) {
                    data.setInformation("您的订单已经完成拣货");
                } else if (information.contains("请及时上传身份证以便订单清关")) {
                    data.setInformation("您的订单(" + data.getOtherNo() + ")已经创建，等待提供身份证信息");
                } else if (information.contains("仓库订单已出库")) {
                    data.setInformation("您的订单(" + data.getOtherNo() + ")已经打包成功");
                } else if (information.contains("订单清关已完成，正在派送中")) {
                    String[] splits = information.split(" ");
                    data.setInformation("您的订单海关已放行，已交往" + splits[1] + "，单号" + splits[2]);
                } else if (information.contains("发往机场，预计起飞时间")) {
                    boolean empty = data.getFlightNo().isEmpty();
                    if (empty) {
                        data.setInformation("您的订单已经离开" + data.getSource_name() + "，发往" + data.getDestination());
                    } else {
                        data.setInformation("您的订单已经离开" + data.getSource_name() + "，发往" + data.getDestination() + "，航班号为" + data.getFlightNo());
                    }
                } else if (information.contains("机场，正在提货中")) {
                    data.setInformation("您的订单已抵达口岸机场");
                } else if (information.contains("海关清关中")) {
                    data.setInformation("您的订单已从机场提货，送交海关清关中");
                } else if (information.contains("海关查验中")) {
                    data.setInformation("您的订单正在海关查验中");
                } else if (information.contains("请尽快支付税金以便订单及时派送")) {
                    data.setInformation("您的订单等待补缴税款后放行");
                }else if (information.startsWith("订单")&&information.endsWith("已取消")){
                    data.setInformation("您的"+information);
                }
               if (information.startsWith("订单")&&information.endsWith("已完成")){
                   data.setInformation("您的订单("+ data.getOtherNo() +")已经送达,感谢您使用我们的服务");
               }
                Observable.just(data.getInformation()).subscribe(RxTextView.text(mDetail));
                break;
            case HomeMessage.HOME_LIST_TRANSPORT:
                mImage.setImageResource(R.mipmap.yunzhuan);
                Observable.just("邮客转运").subscribe(RxTextView.text(mName));
                break;
            case HomeMessage.HOME_LIST_SERVICE:
                mImage.setImageResource(R.mipmap.kehu);
                Observable.just(data.getSource_name()).subscribe(RxTextView.text(mName));
                break;
            case HomeMessage.HOME_LIST_COUPON:
                mImage.setImageResource(R.mipmap.yunzhuan);
                Observable.just("邮客转运").subscribe(RxTextView.text(mName));
                break;
        }
    }
}
