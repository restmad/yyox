package com.yyox.mvp.ui.holder;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import com.jess.arms.base.BaseHolder;
import com.yyox.R;
import com.yyox.mvp.model.entity.Question;
import butterknife.BindView;

/**
 * Created by 95 on 2017/5/23.
 */
    public class ServiceFooterHolder extends BaseHolder<Question> {
        @Nullable
        @BindView(R.id.service_footer)
        Button mService_footer;
        public ServiceFooterHolder(View view) {
            super(view);
        }
        @Override
        public void setData(Question data) {
            mService_footer.setOnClickListener(this);
        }
    }
