package com.yyox.mvp.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jess.arms.base.BaseHolder;
import com.jess.arms.base.DefaultAdapter;
import com.yyox.R;
import com.yyox.mvp.model.entity.Address;
import com.yyox.mvp.ui.holder.AddressFotterItemHolder;
import com.yyox.mvp.ui.holder.AddressItemHolder;

import java.util.List;

/**
 * Created by dadaniu on 2017-01-12.
 */

public class AddressAdapter extends DefaultAdapter<Address> {

    public static final int TYPE_FOOTER = 1;  //说明是带有Footer的
    public static final int TYPE_NORMAL = 2;  //说明是不带有header和footer的

    public AddressAdapter(List<Address> infos) {
        super(infos);
    }

    @Override
    public BaseHolder<Address> getHolder(View v) {
        return null;
    }

    @Override
    public int getLayoutId() {
        return 0;
    }

    @Override
    public int getItemCount() {
        return super.getItemCount() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1) {
            return TYPE_FOOTER;
        }
        return TYPE_NORMAL;
    }

    @Override
    public BaseHolder<Address> onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_FOOTER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_address_list_footer, parent, false);
            BaseHolder<Address> mHolderFooter = new AddressFotterItemHolder(view);
            mHolderFooter.setOnItemClickListener(new BaseHolder.OnViewClickListener() {//设置Item点击事件
                @Override
                public void onViewClick(View view, int position) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(view, null, position);
                    }
                }
            });
            return mHolderFooter;
        }
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_address_list, parent, false);
        BaseHolder<Address> mHolder = new AddressItemHolder(view);
        mHolder.setOnItemClickListener(new BaseHolder.OnViewClickListener() {//设置Item点击事件
            @Override
            public void onViewClick(View view, int position) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(view, mInfos.get(position), position);
                }
            }
        });
        return mHolder;
    }

    @Override
    public void onBindViewHolder(BaseHolder<Address> holder, int position) {
        if (getItemViewType(position) == TYPE_NORMAL && holder instanceof AddressItemHolder) {
            holder.setData(mInfos.get(position));
        }
    }
}
