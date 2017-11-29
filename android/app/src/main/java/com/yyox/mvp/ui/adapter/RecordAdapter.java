package com.yyox.mvp.ui.adapter;

import android.view.View;

import com.jess.arms.base.BaseHolder;
import com.jess.arms.base.DefaultAdapter;
import com.yyox.R;
import com.yyox.mvp.model.entity.Record;
import com.yyox.mvp.ui.holder.RecordItemHolder;

import java.util.List;

/**
 * Created by dadaniu on 2017-01-11.
 */

public class RecordAdapter extends DefaultAdapter<Record> {

    public RecordAdapter(List<Record> infos) {
        super(infos);
    }

    @Override
    public BaseHolder<Record> getHolder(View v) {
        return new RecordItemHolder(v);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_user_record_list;
    }
}
