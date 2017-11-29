package com.yyox.mvp.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jess.arms.base.BaseHolder;
import com.jess.arms.base.DefaultAdapter;
import com.yyox.R;
import com.yyox.mvp.model.entity.OrderPackage;
import com.yyox.mvp.ui.holder.PackageDetailGoodsFooterItemHolder;
import com.yyox.mvp.ui.holder.PackageDetailGoodsHeaderItemHolder;
import com.yyox.mvp.ui.holder.PackageDetailGoodsItemHolder;
import com.yyox.mvp.ui.holder.PackageDetailItemHolder;

import java.util.List;

/**
 * Created by dadaniu on 2017-01-12.
 */

public class PackageDetailAdapter extends DefaultAdapter<OrderPackage> {

    public static final int TYPE_PACKAGE = 0;                   //包裹信息
    public static final int TYPE_PACKAGE_GOODS_HEADER = 1;    //包裹中商品信息头
    public static final int TYPE_PACKAGE_GOODS = 2;            //包裹中商品信息
    public static final int TYPE_PACKAGE_GOODS_FOOTER = 3;    //包裹中商品信息脚

    public PackageDetailAdapter(List<OrderPackage> infos) {
        super(infos);
    }

    @Override
    public BaseHolder<OrderPackage> getHolder(View v) {
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
        return mInfos.get(position).getShowtype();
    }

    @Override
    public BaseHolder<OrderPackage> onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_PACKAGE) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_package_detail_list, parent, false);
            BaseHolder<OrderPackage> mHolderPackage = new PackageDetailItemHolder(view);
            mHolderPackage.setOnItemClickListener(new BaseHolder.OnViewClickListener() {//设置Item点击事件
                @Override
                public void onViewClick(View view, int position) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(view, null, position);
                    }
                }
            });
            return mHolderPackage;
        } else if (viewType == TYPE_PACKAGE_GOODS_HEADER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_package_detail_goods_header_list, parent, false);
            BaseHolder<OrderPackage> mHolderPackageGoodsHeader = new PackageDetailGoodsHeaderItemHolder(view);
            mHolderPackageGoodsHeader.setOnItemClickListener(new BaseHolder.OnViewClickListener() {//设置Item点击事件
                @Override
                public void onViewClick(View view, int position) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(view, null, position);
                    }
                }
            });
            return mHolderPackageGoodsHeader;
        } else if (viewType == TYPE_PACKAGE_GOODS) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_package_detail_goods_list, parent, false);
            BaseHolder<OrderPackage> mHolderPackageGoods = new PackageDetailGoodsItemHolder(view);
            mHolderPackageGoods.setOnItemClickListener(new BaseHolder.OnViewClickListener() {//设置Item点击事件
                @Override
                public void onViewClick(View view, int position) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(view, null, position);
                    }
                }
            });
            return mHolderPackageGoods;
        } else if (viewType == TYPE_PACKAGE_GOODS_FOOTER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_package_detail_goods_footer_list, parent, false);
            BaseHolder<OrderPackage> mHolderPackageGoodsFooter = new PackageDetailGoodsFooterItemHolder(view);
            mHolderPackageGoodsFooter.setOnItemClickListener(new BaseHolder.OnViewClickListener() {//设置Item点击事件
                @Override
                public void onViewClick(View view, int position) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(view, null, position);
                    }
                }
            });
            return mHolderPackageGoodsFooter;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(BaseHolder<OrderPackage> holder, int position) {
        holder.setData(mInfos.get(position));
    }
}
