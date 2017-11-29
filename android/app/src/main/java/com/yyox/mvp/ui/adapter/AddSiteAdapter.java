package com.yyox.mvp.ui.adapter;

import android.view.View;

import com.jess.arms.base.BaseHolder;
import com.jess.arms.base.DefaultAdapter;
import com.yyox.R;
import com.yyox.mvp.model.entity.Site;
import com.yyox.mvp.ui.holder.AddSiteItemHolder;
import com.yyox.mvp.ui.holder.SiteItemHolder;

import java.util.List;

/**
 * Created by dadaniu on 2017-01-12.
 */

public class AddSiteAdapter extends DefaultAdapter<Site>  {

    public AddSiteAdapter(List<Site> infos) {
        super(infos);
    }

    @Override
    public BaseHolder<Site> getHolder(View v) {
        return new AddSiteItemHolder(v);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_addsite_list;
    }
}
