package com.lh.ch.hefenglocation.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lh.ch.hefenglocation.R;
import com.lh.ch.hefenglocation.model.BuMenModel;
import com.lh.ch.hefenglocation.model.CheckModel;
import com.lh.ch.hefenglocation.photo.FullPhotoView;

import java.util.List;

import uk.co.senab.photoview.PhotoView;

/**
 * Created by CH on 2017/6/8.
 */

public class BuMenAdapter extends BaseAdapter {
    private int checkItemPosition = 0;
    private List<BuMenModel.ListBean> mData;

    public BuMenAdapter(List<BuMenModel.ListBean> data) {
        this.mData = data;
    }

    public void setCheckItem(int position) {
        checkItemPosition = position;
        notifyDataSetChanged();
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
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sp, null);
            holder.nameTv = (TextView) convertView.findViewById(R.id.item_spinner_text);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.nameTv.setText(mData.get(position).getTitle());
        return convertView;
    }

    class ViewHolder {
        TextView nameTv;

    }


}
