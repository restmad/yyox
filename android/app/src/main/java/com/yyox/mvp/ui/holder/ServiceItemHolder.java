package com.yyox.mvp.ui.holder;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.jakewharton.rxbinding.widget.RxTextView;
import com.jess.arms.base.BaseHolder;
import com.yyox.R;
import com.yyox.mvp.model.entity.Question;

import butterknife.BindView;
import rx.Observable;

/**
 * Created by 95 on 2017/5/10.
 */
public class ServiceItemHolder extends BaseHolder<Question> {

    @Nullable
    @BindView(R.id.tv_item_question_list_question)
    TextView mQuestion;

    @Nullable
    @BindView(R.id.tv_item_question_list_answer)
    TextView mAnswer;

    public ServiceItemHolder(View view) {
        super(view);
    }

    @Override
    public void setData(Question data) {
        Observable.just(data.getQuestion()).subscribe(RxTextView.text(mQuestion));
        Observable.just(data.getAnswer()).subscribe(RxTextView.text(mAnswer));
    }
}
