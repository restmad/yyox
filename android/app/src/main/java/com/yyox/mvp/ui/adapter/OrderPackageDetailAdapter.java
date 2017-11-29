package com.yyox.mvp.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jess.arms.widget.imageloader.ImageLoader;
import com.yyox.R;
import com.yyox.Utils.CommonUtils;
import com.yyox.mvp.model.entity.OrderPackage;

import java.util.ArrayList;
import java.util.List;

import common.WEApplication;


/**
 * Created by 95 on 2017/3/25.
 */

public class OrderPackageDetailAdapter extends BaseExpandableListAdapter {

    private Context context;
    private LayoutInflater listContainer;
    private List<OrderPackage> listItems = new ArrayList<>();
    private ImageLoader mImageLoader;//用于加载图片的管理类,默认使用glide,使用策略模式,可替换框架
    private final WEApplication mApplication;

    //自定义控件集合
    public final class OrderPackgeDetailItemHolder {
        public TextView mName;
        public TextView mUps;
        public TextView mWeight;
        public RelativeLayout mImgs;
        public ImageView mImg1;
        public ImageView mImg2;
        public ImageView mImg3;
        public Integer position;
        public boolean enabled;
    }

    //自定义控件集合
    public final class OrderPackgeDetailChildItemHolder {
        public TextView mName;
        public TextView mPrice;
        public TextView mCount;
        public Integer position;
        public boolean enabled;
    }


    public OrderPackageDetailAdapter(Context context, List<OrderPackage> list) {
        listContainer = LayoutInflater.from(context);
        this.listItems = list;
        this.context = context;
        mApplication = (WEApplication) context.getApplicationContext();
        mImageLoader = mApplication.getAppComponent().imageLoader();
    }

    @Override
    public int getGroupCount() {
        return listItems.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return listItems.get(groupPosition).getGoodList().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return listItems.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return listItems.get(groupPosition).getGoodList().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View view, ViewGroup viewGroup) {
        OrderPackgeDetailItemHolder groupViewHolder;
        if (view == null) {
            view = listContainer.inflate(R.layout.item_order_packages_detail_list, viewGroup, false);
            groupViewHolder = new OrderPackgeDetailItemHolder();
            groupViewHolder.mName = (TextView) view.findViewById(R.id.tv_item_packages_detail_list_name);
            groupViewHolder.mUps = (TextView) view.findViewById(R.id.tv_item_packages_detail_list_ups);
            groupViewHolder.mWeight = (TextView) view.findViewById(R.id.tv_item_packages_detail_list_weight);
            groupViewHolder.mImgs = (RelativeLayout) view.findViewById(R.id.package_map);
            groupViewHolder.mImg1 = (ImageView) view.findViewById(R.id.iv_item_packages_detail_list_img1);
            groupViewHolder.mImg2 = (ImageView) view.findViewById(R.id.iv_item_packages_detail_list_img2);
            groupViewHolder.mImg3 = (ImageView) view.findViewById(R.id.iv_item_packages_detail_list_img3);
            view.findViewById(R.id.package_map).setOnClickListener((View.OnClickListener) context);
            view.setTag(groupViewHolder);
        } else {
            groupViewHolder = (OrderPackgeDetailItemHolder) view.getTag();
        }
        String nickname = listItems.get(groupPosition).getNickname();
        if (nickname != "null" && nickname != null && nickname != "") {
            groupViewHolder.mName.setText("包裹昵称: " + nickname);
            groupViewHolder.mUps.setText("运单号: " + listItems.get(groupPosition).getBikeUPS());
        } else {
            groupViewHolder.mName.setText("包裹昵称: 未命名");
            groupViewHolder.mUps.setText("运单号:" + listItems.get(groupPosition).getBikeUPS());
        }
        if (listItems.get(groupPosition).getWeight() != 0){
            groupViewHolder.mWeight.setText("包裹重量:"+listItems.get(groupPosition).getWeight()+"kg");
        }
        if (listItems.size() == 1) {
            view.findViewById(R.id.ib_item_order_submit_list_expand).setVisibility(View.GONE);
        } else {
            view.findViewById(R.id.ib_item_order_submit_list_expand).setVisibility(View.VISIBLE);
        }

        if (isExpanded) {
            view.findViewById(R.id.ib_item_order_submit_list_expand).setBackgroundResource(R.mipmap.arrow_up);
        } else {
            view.findViewById(R.id.ib_item_order_submit_list_expand).setBackgroundResource(R.mipmap.arrow_down);
        }

        if (listItems.get(groupPosition).getOrderSreenshot().size() > 0) {
            groupViewHolder.mImgs.setVisibility(View.VISIBLE);
            List<Bitmap> list = new ArrayList<>();
            if (listItems.get(groupPosition).getOrderSreenshot().size() != 0 ) {
                groupViewHolder.mImg1.setVisibility(View.VISIBLE);
                groupViewHolder.mImg1.setImageBitmap(listItems.get(groupPosition).getBitmap1());
                list.add(listItems.get(groupPosition).getBitmap1());
            }
            if (listItems.get(groupPosition).getOrderSreenshot().size() > 1 ) {
                groupViewHolder.mImg2.setVisibility(View.VISIBLE);
                groupViewHolder.mImg2.setImageBitmap(listItems.get(groupPosition).getBitmap2());
                list.add(listItems.get(groupPosition).getBitmap2());
            }
            if (listItems.get(groupPosition).getOrderSreenshot().size() > 2 ) {
                groupViewHolder.mImg3.setVisibility(View.VISIBLE);
                groupViewHolder.mImg3.setImageBitmap(listItems.get(groupPosition).getBitmap3());
                list.add(listItems.get(groupPosition).getBitmap3());
            }

            view.findViewById(R.id.package_map).setTag(list);
        }
        return view;

    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View view, ViewGroup viewGroup) {
        OrderPackgeDetailChildItemHolder childViewHolder;
        if (view == null) {
            view = listContainer.inflate(R.layout.item_order_package_submit_detail_list, viewGroup, false);
            childViewHolder = new OrderPackgeDetailChildItemHolder();
            childViewHolder.mName = (TextView) view.findViewById(R.id.tv_item_order_submit_detail_list_name);
            childViewHolder.mPrice = (TextView) view.findViewById(R.id.tv_item_order_submit_detail_list_price);
            childViewHolder.mCount = (TextView) view.findViewById(R.id.tv_item_order_submit_detail_list_count);
            view.setTag(childViewHolder);
        } else {
            childViewHolder = (OrderPackgeDetailChildItemHolder) view.getTag();
        }
        if (childPosition == 0){
            view.findViewById(R.id.goods).setVisibility(View.VISIBLE);
        }else {
            view.findViewById(R.id.goods).setVisibility(View.GONE);
        }
        childViewHolder.mName.setText(listItems.get(groupPosition).getGoodList().get(childPosition).getProductNameCNY());
        childViewHolder.mPrice.setText("总价：" + CommonUtils.doubleFormat(listItems.get(groupPosition).getGoodList().get(childPosition).getAmount()) + listItems.get(groupPosition).getGoodList().get(childPosition).getCurrencyName());
        childViewHolder.mCount.setText("数量：" + listItems.get(groupPosition).getGoodList().get(childPosition).getStock() + listItems.get(groupPosition).getGoodList().get(childPosition).getUnits());

        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }
}
