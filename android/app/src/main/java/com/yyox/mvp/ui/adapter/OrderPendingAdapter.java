package com.yyox.mvp.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jess.arms.base.BaseHolder;
import com.jess.arms.base.DefaultAdapter;
import com.yyox.R;
import com.yyox.mvp.model.entity.OrderPending;
import com.yyox.mvp.ui.holder.OrderPendingBoxItemHolder;
import com.yyox.mvp.ui.holder.OrderPendingOrderItemHolder;
import com.yyox.mvp.ui.holder.OrderPendingPackageItemHolder;

import java.util.List;

/**
 * Created by dadaniu on 2017-01-14.
 */

public class OrderPendingAdapter extends DefaultAdapter<OrderPending> {

    public static final int TYPE_PACKAGE = 1;  //包裹
    public static final int TYPE_ORDER = 2;  //订单
    public static final int TYPE_BOX = 3;  //合箱

    public OrderPendingAdapter(List<OrderPending> infos) {
        super(infos);
    }

    @Override
    public BaseHolder<OrderPending> getHolder(View v) {
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
        if (1 == mInfos.get(position).getType()) {
            return TYPE_PACKAGE;
        } else if (2 == mInfos.get(position).getType()) {
            return TYPE_ORDER;
        } else if (3 == mInfos.get(position).getType()) {
            return TYPE_BOX;
        }
        return super.getItemViewType(position);

    }

    @Override
    public BaseHolder<OrderPending> onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_PACKAGE) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_pending_package_list, parent, false);
            BaseHolder<OrderPending> mHolderPackage = new OrderPendingPackageItemHolder(view);
            mHolderPackage.setOnItemClickListener(new BaseHolder.OnViewClickListener() {//设置Item点击事件
                @Override
                public void onViewClick(View view, int position) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(view, null, position);
                    }
                }
            });
            return mHolderPackage;
        } else if (viewType == TYPE_ORDER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_pending_order_list, parent, false);
            BaseHolder<OrderPending> mHolderOrder = new OrderPendingOrderItemHolder(view);
            mHolderOrder.setOnItemClickListener(new BaseHolder.OnViewClickListener() {//设置Item点击事件
                @Override
                public void onViewClick(View view, int position) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(view, null, position);
                    }
                }
            });
            return mHolderOrder;
        } else if (viewType == TYPE_BOX) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_pending_box_list, parent, false);
            BaseHolder<OrderPending> mHolderBox = new OrderPendingBoxItemHolder(view);
            mHolderBox.setOnItemClickListener(new BaseHolder.OnViewClickListener() {//设置Item点击事件
                @Override
                public void onViewClick(View view, int position) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(view, null, position);
                    }
                }
            });
            return mHolderBox;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(BaseHolder<OrderPending> holder, int position) {
        holder.setData(mInfos.get(position));
    }

}
