package com.lh.ch.hefenglocation.fragment;

import android.content.Context;
import android.content.Intent;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.lh.ch.hefenglocation.R;
import com.lh.ch.hefenglocation.activity.TakePhotoActivity;
import com.lh.ch.hefenglocation.model.VersionModel;
import com.lh.ch.hefenglocation.premission.EasyPermission;
import com.lh.ch.hefenglocation.util.VersionTools;
import com.lh.ch.updateversion.UpdateHelper;
import com.lh.ch.updateversion.bean.UpdateEntity;
import com.lh.ch.updateversion.listener.UpdateListener;

import java.io.IOException;
import java.io.StringReader;
import okhttp3.Response;


/**
 * Created by CH on 2017/6/1.
 */

public class HomeFragment extends Fragment {
    private View view;
    private ImageView toChooseImg;
    private EasyPermission easyPermission = null;
    private VersionModel mVersionModel;
    private String virCode;
    private Handler mHandler;
    SharedPreferences readInfo;
    SharedPreferences.Editor mEditor;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, null);
        mHandler = new Handler();
        readInfo = getActivity().getSharedPreferences("user_npt", Context.MODE_PRIVATE);
        mEditor = readInfo.edit();

        String aaaaa = readInfo.getString("userName", "");
        Log.d("aaaa", aaaaa);
        toChooseImg = (ImageView) view.findViewById(R.id.home_to_choose);
        toChooseImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toChoose = new Intent(getActivity(), TakePhotoActivity.class);
                startActivity(toChoose);
            }
        });
        updateVir();
        return view;
    }
    private void updateVir() {
        String url = "http://122.114.57.74:806/wuzi/genzong/shengji.aspx";
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
                String body = response.body().string();
                Gson gson = new Gson();
                JsonReader reader = new JsonReader(new StringReader(body));
                mVersionModel = gson.fromJson(reader, VersionModel.class);
                mHandler.post(virRunn);
            }


        });

    }

    Runnable virRunn = new Runnable() {
        @Override
        public void run() {
            virCode = mVersionModel.getBanben();
            Log.d("12-", virCode);
            Log.d("12now-", VersionTools.getVersion(getActivity()));
            if (!VersionTools.getVersion(getActivity()).equals(virCode)) {
                UpdateHelper
                        .getInstance()
                        .setCheckType(UpdateHelper.CheckType.check_with_Dialog)
                        .setUpdateListener(false, new UpdateListener() {
                            @Override
                            public void Update(boolean update, UpdateEntity updateEntity) {
                                if (update) {
                                    Toast.makeText(getActivity(), "发现新版本", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getActivity(), "未发现新版本", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .check(getActivity());
            } else {
                Log.d("12", "we");
            }
        }
    };


}
