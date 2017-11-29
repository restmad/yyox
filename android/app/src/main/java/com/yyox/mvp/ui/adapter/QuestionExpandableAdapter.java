package com.yyox.mvp.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.yyox.R;
import com.yyox.mvp.model.entity.QuestionItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dadaniu on 2017-01-12.
 */

public class QuestionExpandableAdapter extends BaseExpandableListAdapter {

    private Context context;
    private LayoutInflater listContainer;
    private List<QuestionItem> listItems = new ArrayList<>();
    private int mtype;

    //自定义控件集合
    public final class QuestionGroupItemHolder {
        public TextView mClass;
        public Integer position;
        public boolean enabled;
    }

    //自定义控件集合
    public final class QuestionChildItemHolder {
        public TextView mQuestion;
        public TextView mAnswer;
        public Integer position;
        public boolean enabled;
    }

    public QuestionExpandableAdapter(Context context, List<QuestionItem> list) {
        this.context = context;
        listContainer = LayoutInflater.from(context);
        this.listItems = list;
    }

    public QuestionExpandableAdapter(Context context, List<QuestionItem> list, int type) {
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
        return listItems.get(groupPosition).getQuestion().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return listItems.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return listItems.get(groupPosition).getQuestion().get(childPosition);
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
        QuestionGroupItemHolder groupViewHolder;
        if (view == null) {
            view = listContainer.inflate(R.layout.item_question_group_list, viewGroup, false);
            groupViewHolder = new QuestionGroupItemHolder();
            groupViewHolder.mClass = (TextView) view.findViewById(R.id.tv_item_question_group_list_class);
            view.setTag(groupViewHolder);
        } else {
            groupViewHolder = (QuestionGroupItemHolder) view.getTag();
        }
        String sClass = listItems.get(groupPosition).getName();
        groupViewHolder.mClass.setText(sClass);

        if (isExpanded) {
            view.findViewById(R.id.iv_item_question_group_list_expand).setBackgroundResource(R.mipmap.arrow_up);
        } else {
            view.findViewById(R.id.iv_item_question_group_list_expand).setBackgroundResource(R.mipmap.arrow_down);
        }

        return view;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View view, ViewGroup viewGroup) {
        QuestionChildItemHolder childViewHolder;
        if (view == null) {
            view = listContainer.inflate(R.layout.item_question_child_list, viewGroup, false);
            childViewHolder = new QuestionChildItemHolder();
            childViewHolder.mQuestion = (TextView) view.findViewById(R.id.tv_item_question_child_list_question);
            childViewHolder.mAnswer = (TextView) view.findViewById(R.id.tv_item_question_child_list_answer);
            view.setTag(childViewHolder);
        } else {
            childViewHolder = (QuestionChildItemHolder) view.getTag();
        }
        childViewHolder.mQuestion.setText(listItems.get(groupPosition).getQuestion().get(childPosition).getQuestion());
        childViewHolder.mAnswer.setText(listItems.get(groupPosition).getQuestion().get(childPosition).getAnswer());
        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }

}
