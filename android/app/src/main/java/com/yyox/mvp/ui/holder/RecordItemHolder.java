package com.yyox.mvp.ui.holder;

import android.graphics.Color;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.jakewharton.rxbinding.widget.RxTextView;
import com.jess.arms.base.BaseHolder;
import com.yyox.R;
import com.yyox.mvp.model.entity.Record;

import butterknife.BindView;
import rx.Observable;

/**
 * Created by dadaniu on 2017-01-11.
 */

public class RecordItemHolder extends BaseHolder<Record> {

    @Nullable
    @BindView(R.id.tv_record_list_item_name)
    TextView mName;

    @Nullable
    @BindView(R.id.tv_record_list_item_value)
    TextView mValue;

    @Nullable
    @BindView(R.id.tv_record_list_item_datetime)
    TextView mDateTime;

    public RecordItemHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setData(Record data) {
        Observable.just(data.getSTATEMENT_TYPE_NAME()).subscribe(RxTextView.text(mName));
        if (data.getAMOUNT().contains("-")){
            mValue.setTextColor(Color.parseColor("#B1CB2A"));
        }else if (data.getAMOUNT().contains("+")){
            mValue.setTextColor(Color.parseColor("#FC9B31"));
        }
        Observable.just(data.getAMOUNT()).subscribe(RxTextView.text(mValue));
        Observable.just(data.getFREEZE_MONEY_DATE()).subscribe(RxTextView.text(mDateTime));
    }
}
