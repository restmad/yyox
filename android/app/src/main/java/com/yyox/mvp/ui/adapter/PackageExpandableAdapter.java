package com.yyox.mvp.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yyox.R;
import com.yyox.Utils.CommonUtils;
import com.yyox.mvp.model.entity.OrderPackage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dadaniu on 2017-01-12.
 */

public class PackageExpandableAdapter extends BaseExpandableListAdapter {

    private Context context;
    private LayoutInflater listContainer;
    private List<OrderPackage> listItems = new ArrayList<>();
    private int mtype;

    //自定义控件集合
    public final class OrderSubmitItemHolder {
        public TextView mName;
        public TextView mUps;
        public TextView mWeight;
        public Integer position;
        public boolean enabled;
    }

    //自定义控件集合
    public final class OrderSubmitChildItemHolder {
        public TextView mName;
        public TextView mPrice;
        public TextView mCount;
        public Integer position;
        public boolean enabled;
    }

    public PackageExpandableAdapter(Context context, List<OrderPackage> list) {
        this.context = context;
        listContainer = LayoutInflater.from(context);
        this.listItems = list;
    }

    public PackageExpandableAdapter(Context context, List<OrderPackage> list, int type) {
        this.context = context;
        listContainer = LayoutInflater.from(context);
        this.listItems = list;
        this.mtype = type;

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
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View view, ViewGroup viewGroup) {
        OrderSubmitItemHolder groupViewHolder;
        if (view == null) {
            view = listContainer.inflate(R.layout.item_order_submit_list, viewGroup, false);
            groupViewHolder = new OrderSubmitItemHolder();
            groupViewHolder.mName = (TextView) view.findViewById(R.id.tv_item_order_submit_list_name);
            groupViewHolder.mUps = (TextView) view.findViewById(R.id.tv_item_order_submit_list_number);
            groupViewHolder.mWeight = (TextView) view.findViewById(R.id.tv_item_order_submit_list_weight);
            view.setTag(groupViewHolder);
        } else {
            groupViewHolder = (OrderSubmitItemHolder) view.getTag();
        }
        String nickname = listItems.get(groupPosition).getNickname();
        if (nickname != null && !nickname.equals("null") && !nickname.equals("")) {
            groupViewHolder.mName.setText("包裹昵称：" + nickname);
        } else {
            groupViewHolder.mName.setText("包裹昵称：未命名");
        }
        if (!listItems.get(groupPosition).getBikeUPS().isEmpty()) {
            groupViewHolder.mUps.setText("运单号：" + listItems.get(groupPosition).getBikeUPS());
        } else {
            groupViewHolder.mUps.setText("运单号：" + listItems.get(groupPosition).getCarrierNo());
        }
        if (listItems.get(groupPosition).getWeight() != 0){
            groupViewHolder.mWeight.setText("包裹重量："+listItems.get(groupPosition).getWeight()+"kg");
        }
        if (listItems.size() == 1) {
            ImageView imageView = (ImageView) view.findViewById(R.id.ib_item_order_submit_list_expand);
            imageView.setVisibility(View.GONE);
        }

        if (isExpanded) {
            view.findViewById(R.id.ib_item_order_submit_list_expand).setBackgroundResource(R.mipmap.arrow_up);
        } else {
            view.findViewById(R.id.ib_item_order_submit_list_expand).setBackgroundResource(R.mipmap.arrow_down);
        }

        if (mtype == 3 || mtype == 4 || mtype == 17) {

            view.findViewById(R.id.ll_item_order_submit_list_edit).setVisibility(View.GONE);
        }
        view.findViewById(R.id.ll_item_order_submit_list_edit).setOnClickListener((View.OnClickListener) context);

        return view;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View view, ViewGroup viewGroup) {
        OrderSubmitChildItemHolder childViewHolder;
        if (view == null) {
            view = listContainer.inflate(R.layout.item_order_submit_detail_list, viewGroup, false);
            childViewHolder = new OrderSubmitChildItemHolder();

            childViewHolder.mName = (TextView) view.findViewById(R.id.tv_item_order_submit_detail_list_name);
            childViewHolder.mPrice = (TextView) view.findViewById(R.id.tv_item_order_submit_detail_list_price);
            childViewHolder.mCount = (TextView) view.findViewById(R.id.tv_item_order_submit_detail_list_count);
            view.setTag(childViewHolder);
        } else {
            childViewHolder = (OrderSubmitChildItemHolder) view.getTag();
        }
        if (listItems.get(groupPosition).getGoodList().get(childPosition).isGoolsType()){
            childViewHolder.mName.setText(listItems.get(groupPosition).getGoodList().get(childPosition).getProductNameCNY());
            childViewHolder.mPrice.setText("总价：" + CommonUtils.doubleFormat(listItems.get(groupPosition).getGoodList().get(childPosition).getAmount()) + listItems.get(groupPosition).getGoodList().get(childPosition).getCurrencyName());
            childViewHolder.mCount.setText("数量：" + listItems.get(groupPosition).getGoodList().get(childPosition).getStock() + listItems.get(groupPosition).getGoodList().get(childPosition).getUnits());
            return view;
        }else {
            view.findViewById(R.id.lin).setVisibility(View.GONE);
            return view;
        }
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }

}
