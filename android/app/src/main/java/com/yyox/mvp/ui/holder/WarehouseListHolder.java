package com.yyox.mvp.ui.holder;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.jakewharton.rxbinding.widget.RxTextView;
import com.jess.arms.base.BaseHolder;
import com.yyox.R;
import com.yyox.mvp.model.entity.WarehouseList;

import butterknife.BindView;
import rx.Observable;

/**
 * Created by 95 on 2017/5/22.
 */
public class WarehouseListHolder extends BaseHolder<WarehouseList> {

   @Nullable
   @BindView(R.id.warehouseList_img)
   ImageView mImge;

    @Nullable
    @BindView(R.id.warehouseList_name)
    TextView mName;

    @Nullable
    @BindView(R.id.warehouseList_value)
    TextView mVaslue;

    @Nullable
    @BindView(R.id.copy)
    Button mButton;

    public WarehouseListHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setData(WarehouseList data) {
        mButton.setOnClickListener(this);
        if (!data.getName().isEmpty() && data.getType() == 1){
            mImge.setImageResource(R.mipmap.meiguo);
        }else if (!data.getName().isEmpty() && data.getType() == 2){
            mImge.setImageResource(R.mipmap.deguo);
        }else if (!data.getName().isEmpty() && data.getType() == 3){
            mImge.setImageResource(R.mipmap.riben);
        }else if (!data.getName().isEmpty() && data.getType() == 4){
            mImge.setImageResource(R.mipmap.aodaliya);
        }
        Observable.just(data.getName()).subscribe(RxTextView.text(mName));
        Observable.just(data.getValue()).subscribe(RxTextView.text(mVaslue));

    }
}
