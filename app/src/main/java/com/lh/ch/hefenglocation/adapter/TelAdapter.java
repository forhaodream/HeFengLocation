package com.lh.ch.hefenglocation.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lh.ch.hefenglocation.R;
import com.lh.ch.hefenglocation.model.TelModel;

import java.util.List;


/**
 * Created by CH on 2017/6/2.
 */

public class TelAdapter extends BaseAdapter {
    private List<TelModel.ListBean> mData;

    public TelAdapter(List<TelModel.ListBean> data) {
        this.mData = data;
    }

    @Override
    public int getCount() {
        return mData.size();

    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tel, null);
            holder.nameTv = (TextView) convertView.findViewById(R.id.tel_item_name);
            holder.phoneTv = (TextView) convertView.findViewById(R.id.tel_item_phone);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (mData.get(position).getSj().equals("")) {
            holder.phoneTv.setText("暂无号码");
        } else {
            holder.phoneTv.setText(mData.get(position).getSj());
        }
        holder.nameTv.setText(mData.get(position).getXm());
        return convertView;
    }


    class ViewHolder {
        TextView nameTv, phoneTv;
    }
}
