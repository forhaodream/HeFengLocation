package com.lh.ch.hefenglocation.base;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by CH on 2017/9/8.
 */

public abstract class MyBaseAdapter <T> extends BaseAdapter {
    private List<T> mDatas;

    public MyBaseAdapter(List<T> datas) {
        mDatas = datas;
//        this.mDatas.addAll(datas);
    }

    @Override
    public int getCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = ViewHolder.newsInstance(convertView, parent.getContext(), getLayoutId());

        setUI(holder, position, parent.getContext());

        return holder.getConverView();
    }

    protected abstract void setUI(ViewHolder holder, int position, Context context);

    protected abstract int getLayoutId();


}

