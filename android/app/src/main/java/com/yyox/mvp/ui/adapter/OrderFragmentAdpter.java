package com.yyox.mvp.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jess.arms.base.BaseHolder;
import com.jess.arms.base.DefaultAdapter;
import com.yyox.R;
import com.yyox.mvp.model.entity.Message;
import com.yyox.mvp.ui.holder.MessageHeaderHolder;
import com.yyox.mvp.ui.holder.MessageItemHolder;

import java.util.List;

public class OrderFragmentAdpter extends DefaultAdapter<Message> {
    public static final int TYPE_HEADER = 0;  //说明是带有header的
    public static final int TYPE_NORMAL = 1;  //说明是不带有header和footer的

    public OrderFragmentAdpter(List<Message> infos) {
        super(infos);
    }


    @Override
    public BaseHolder<Message> getHolder(View v) {
        return null;
    }

    @Override
    public int getLayoutId() {
        return 0;
    }

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEADER;
        }
        return TYPE_NORMAL;
    }

    @Override
    public BaseHolder<Message> onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER || mInfos.size() == 0) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_viewheader, parent, false);
            BaseHolder<Message> mHolderHeader = new MessageHeaderHolder(view);
            mHolderHeader.setOnItemClickListener(new BaseHolder.OnViewClickListener() {//设置Item点击事件
                @Override
                public void onViewClick(View view, int position) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(view, null, position);
                    }
                }
            });
            return mHolderHeader;
        }
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_list, parent, false);
        BaseHolder<Message> mHolder = new MessageItemHolder(view);
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
    public void onBindViewHolder(BaseHolder<Message> holder, int position) {
        if (getItemViewType(position) == TYPE_NORMAL && holder instanceof MessageItemHolder) {
            holder.setData(mInfos.get(position));
        } else if (getItemViewType(position) == TYPE_HEADER && holder instanceof MessageHeaderHolder && mInfos.size() > 0) {
            holder.setData(mInfos.get(0));
        }
    }
}