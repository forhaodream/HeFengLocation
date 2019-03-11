package com.lh.ch.hefenglocation.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.lh.ch.hefenglocation.R;
import com.lh.ch.hefenglocation.activity.GuideActivity;
import com.lh.ch.hefenglocation.activity.HomeActivity;
import com.lh.ch.hefenglocation.activity.LoginActivity;
import com.lh.ch.hefenglocation.activity.ManagerActivity;
import com.lh.ch.hefenglocation.activity.MyMsgActivity;
import com.lh.ch.hefenglocation.model.VersionModel;
import com.lh.ch.hefenglocation.util.VersionTools;
import com.lh.ch.updateversion.UpdateHelper;
import com.lh.ch.updateversion.bean.UpdateEntity;
import com.lh.ch.updateversion.listener.UpdateListener;

import java.io.IOException;
import java.io.StringReader;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by CH on 2017/6/1.
 */

public class ExitFragment extends Fragment implements View.OnClickListener {
    private ImageView returnImg, exitImg;
    private SharedPreferences readInfo;
    private SharedPreferences.Editor editor;
    private LinearLayout myMsg, myManager, myUp, myExit;
    private Context mContext;
    private TextView userName;
    private String logName;
    private String quanxian;
    private VersionModel mVersionModel;
    private String virCode;
    private Handler mHandler;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        readInfo = getActivity().getSharedPreferences("user_npt", Context.MODE_PRIVATE);
        editor = readInfo.edit();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_exit, null);
        mHandler = new Handler();
        mContext = getActivity();
        myMsg = (LinearLayout) view.findViewById(R.id.mine_my_msg);
        myManager = (LinearLayout) view.findViewById(R.id.mine_my_manager);
        myExit = (LinearLayout) view.findViewById(R.id.mine_my_exit);
        myUp = (LinearLayout) view.findViewById(R.id.mine_my_up);
        userName = (TextView) view.findViewById(R.id.mine_login_name);
        myUp = (LinearLayout) view.findViewById(R.id.mine_my_up);
        myUp.setOnClickListener(this);
        myMsg.setOnClickListener(this);
        myManager.setOnClickListener(this);
        myExit.setOnClickListener(this);
        logName = readInfo.getString("xm", "");
        quanxian = readInfo.getString("qx", null);
        myExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent toLogin = new Intent(getActivity(), GuideActivity.class);
////                readInfo.edit().clear().commit();
//                editor.clear();
//                editor.commit();
//                startActivity(toLogin);
                editor.clear();
                editor.apply();
                Intent toLogin = new Intent(getActivity(), LoginActivity.class);

                startActivity(toLogin);

            }
        });
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                ((Activity) mContext).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            userName.setText("欢迎您登录, " + logName);


                        } catch (NullPointerException e) {
                            Log.e("null", "捕捉到异常");
                        }
                    }
                });
            }
        });
        thread.start();
        returnImg = (ImageView) view.findViewById(R.id.exit_title_fanhui);
        returnImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent show = new Intent(getActivity(), HomeActivity.class);
                show.putExtra("huan", 1);
                startActivity(show);
            }
        });
        return view;
    }

//    @Override
//    public void onResume() {
//        readInfo.edit().clear().commit();
//        super.onResume();
//    }
//
//    @Override
//    public void onDestroy() {
//        readInfo.edit().clear().commit();
//        super.onDestroy();
//    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mine_my_msg:
                Intent toMsg = new Intent(getActivity(), MyMsgActivity.class);
                startActivity(toMsg);
                break;
            case R.id.mine_my_manager:
                String qx = "1";
                if (quanxian != null) {
                    Log.d("-09", quanxian);
                } else {
                    Log.d("-09", "90--");
                }
                if (!quanxian.equals(qx)) {
                    Toast.makeText(mContext, "你没有权限登录这个界面", Toast.LENGTH_SHORT).show();
                } else {
                    Intent toManager = new Intent(getActivity(), ManagerActivity.class);
                    startActivity(toManager);
                }
                break;
            case R.id.mine_my_up:
                updateVir();
                break;
            case R.id.mine_my_exit:
                editor.clear();
                editor.apply();
                Intent toLogin = new Intent(getActivity(), LoginActivity.class);
                startActivity(toLogin);
                break;
        }
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
                Toast.makeText(getActivity(), "当前已是最新版本", Toast.LENGTH_SHORT).show();
//                Log.d("12", "we");
            }
        }
    };
}
