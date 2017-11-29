package com.yyox.mvp.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jess.arms.base.BaseHolder;
import com.jess.arms.base.DefaultAdapter;
import com.yyox.R;
import com.yyox.mvp.model.entity.Question;
import com.yyox.mvp.ui.holder.ServiceFooterHolder;
import com.yyox.mvp.ui.holder.ServiceHeaderHolder;
import com.yyox.mvp.ui.holder.ServiceItemHolder;

import java.util.List;

/**
 * Created by 95 on 2017/5/10.
 */
public class ServiceFragmentAdpter extends DefaultAdapter<Question> {
    public static final int TYPE_HEADER = 0;  //说明是带有header的
    public static final int TYPE_NORMAL = 1;  //说明是不带有header和footer的
    public static  final int TYPE_FOOTER = 2; // 说明带footer的

    public ServiceFragmentAdpter(List<Question> infos) {
        super(infos);
    }


    @Override
    public BaseHolder<Question> getHolder(View v) {
        return null;
    }

    @Override
    public int getLayoutId() {
        return 0;
    }

    @Override
    public int getItemCount() {
        return super.getItemCount()+1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0){
            return TYPE_HEADER;
        }else if (position == getItemCount()-1){
            return TYPE_FOOTER;
        }
        return TYPE_NORMAL;
    }

    @Override
    public BaseHolder<Question> onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == TYPE_HEADER || mInfos.size() == 0){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_service, parent, false);
            BaseHolder<Question> mHolderHeader = new ServiceHeaderHolder(view);
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
        if(viewType == TYPE_FOOTER){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_service_footer, parent, false);
            BaseHolder<Question> mFooterHeader = new ServiceFooterHolder(view);
            mFooterHeader.setOnItemClickListener(new BaseHolder.OnViewClickListener() {//设置Item点击事件
                @Override
                public void onViewClick(View view, int position) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(view, null, position);
                    }
                }
            });
            return mFooterHeader;
        }
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_service_recycle_list, parent, false);
        BaseHolder<Question> mHolder = new ServiceItemHolder(view);
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
    public void onBindViewHolder(BaseHolder<Question> holder, int position) {
        if(getItemViewType(position) == TYPE_NORMAL && holder instanceof ServiceItemHolder ){
            holder.setData(mInfos.get(position));
        }else if (getItemViewType(position) == TYPE_HEADER && holder instanceof ServiceHeaderHolder && mInfos.size()>0){
            holder.setData(mInfos.get(0));
        }else if (getItemViewType(position) == TYPE_FOOTER && holder instanceof ServiceFooterHolder){
            holder.setData(mInfos.get(0));
        }
    }

}
