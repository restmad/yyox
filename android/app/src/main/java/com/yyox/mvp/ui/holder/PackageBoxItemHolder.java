package com.yyox.mvp.ui.holder;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.jakewharton.rxbinding.widget.RxTextView;
import com.jess.arms.base.BaseHolder;
import com.yyox.R;
import com.yyox.Utils.DateUtils;
import com.yyox.mvp.model.entity.PackageItem;

import butterknife.BindView;
import rx.Observable;

/**
 * Created by dadaniu on 2017-01-14.
 */

public class PackageBoxItemHolder extends BaseHolder<PackageItem>  {

    @Nullable
    @BindView(R.id.tv_item_package_box_list_name)
    TextView mName;

    @Nullable
    @BindView(R.id.tv_item_package_box_list_warehouse)
    TextView mWarehouse;

    @Nullable
    @BindView(R.id.tv_item_package_box_list_status)
    TextView mStatus;

    @Nullable
    @BindView(R.id.cb_item_package_box_list_select)
    CheckBox mSelect;

    public PackageBoxItemHolder(View itemView) {
        super(itemView);
        mSelect.setOnClickListener(this);
    }

    @Override
    public void setData(PackageItem data) {
        String strTtime = "";
        if(data.getActionDate() != null && !data.getActionDate().isEmpty()){
            long timeStamp = DateUtils.strToTimeStamp(data.getActionDate());
            strTtime = DateUtils.convertTimeToFormat(timeStamp);
        }
        if(data.getNickname() != null && !data.getNickname().isEmpty()){
            Observable.just("包裹昵称：" + data.getNickname()).subscribe(RxTextView.text(mName));
        }else{
            Observable.just("运单号："+data.getCarrierNo()).subscribe(RxTextView.text(mName));
        }
        if(data.getInventoryStatus() != null){
            Observable.just(data.getWarehouseName()).subscribe(RxTextView.text(mWarehouse));
            Observable.just("已入库"+" "+strTtime).subscribe(RxTextView.text(mStatus));
        }else{
            Observable.just(strTtime).subscribe(RxTextView.text(mWarehouse));
            Observable.just(data.getInventoryStatus().getStatus()).subscribe(RxTextView.text(mStatus));
        }
       mSelect.setChecked(data.isChecked());
    }

}
