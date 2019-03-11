package com.lh.ch.hefenglocation.adapter;

import android.content.Context;
import android.widget.TextView;

import com.lh.ch.hefenglocation.R;
import com.lh.ch.hefenglocation.base.MyBaseAdapter;
import com.lh.ch.hefenglocation.base.ViewHolder;
import com.lh.ch.hefenglocation.model.CjListModel;

import java.util.List;

/**
 * Created by CH on 2017/9/8.
 */

public class CJListAdapter extends MyBaseAdapter<CjListModel.ListBean> {
    public CJListAdapter(List<CjListModel.ListBean> datas) {
        super(datas);
    }

    @Override
    protected void setUI(ViewHolder holder, int position, Context context) {
        CjListModel.ListBean item = (CjListModel.ListBean) getItem(position);
        //标题
        TextView titleTv = holder.getItemView(R.id.cj_list_name);
        titleTv.setText(item.getXm());
        // 各种介绍
        TextView subjectName = holder.getItemView(R.id.cj_list_weidu);
        subjectName.setText(item.getWeidu());
        TextView gradeName = holder.getItemView(R.id.cj_list_jingdu);
        gradeName.setText(item.getJingdu());
        TextView banBenName = holder.getItemView(R.id.cj_list_bianhao);
        banBenName.setText(item.getFjbh());


    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_cj_list;
    }
}

