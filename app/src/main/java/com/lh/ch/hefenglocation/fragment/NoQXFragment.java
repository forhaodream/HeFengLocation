package com.lh.ch.hefenglocation.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.lh.ch.hefenglocation.R;
import com.lh.ch.hefenglocation.activity.HomeActivity;

/**
 * Created by CH on 2017/6/9.
 */

public class NoQXFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_no_qx, null);
        ImageView img = (ImageView) view.findViewById(R.id.tel_title_fanhui);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent show = new Intent(getActivity(), HomeActivity.class);
                show.putExtra("huan", 1);
                startActivity(show);
            }
        });
        return view;
    }
}
