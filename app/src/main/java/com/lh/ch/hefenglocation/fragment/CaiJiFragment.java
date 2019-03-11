package com.lh.ch.hefenglocation.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.lh.ch.hefenglocation.R;
import com.lh.ch.hefenglocation.activity.HomeActivity;
import com.lh.ch.hefenglocation.activity.MapTeAty;
import com.lh.ch.hefenglocation.model.CjModel;
import com.lh.ch.hefenglocation.util.Url;

import java.io.IOException;
import java.io.StringReader;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by CH on 2017/6/24.
 */

public class CaiJiFragment extends Fragment implements View.OnClickListener {
    private ImageView cjImg, cj;
    private CjModel mCjModel;
    private EditText cjEdit;
    private Handler mHandler;
    // 定位相关
    private double lati;
    private double longa;
    public LocationClient mLocationClient = null;
    public BDLocationListener myListener = new MyLocationListener();
    private String userName, xm;
    private int userId, bm;
    private ImageView returnImg;
    private SharedPreferences readInfo;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // 获取用户名
        readInfo = getActivity().getSharedPreferences("user_npt", Context.MODE_PRIVATE);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLocationClient = new LocationClient(getContext().getApplicationContext());
        mLocationClient.registerLocationListener(myListener);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_cj, null);
//        mLocationClient = new LocationClient(getActivity().getApplicationContext());
//        mLocationClient.registerLocationListener(myListener);
        mHandler = new Handler();
        initLocation();
        mLocationClient.start();
        returnImg = (ImageView) view.findViewById(R.id.cj_fanhui);
        returnImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent show = new Intent(getActivity(), HomeActivity.class);
                show.putExtra("huan", 1);
                startActivity(show);
            }
        });
        cjImg = (ImageView) view.findViewById(R.id.cj_img);
        cjImg.setOnClickListener(this);
        cjEdit = (EditText) view.findViewById(R.id.cj_edit);
//        cj = (ImageView) view.findViewById(R.id.cj_cj);
//        cj.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
        userName = readInfo.getString("userName", "");
        bm = readInfo.getInt("bm", 0);
        xm = readInfo.getString("xm", "");
        userId = readInfo.getInt("userId", 0);
        Log.d("12a", userName);
        Log.d("12a", String.valueOf(bm));
        Log.d("12a", xm);
        Log.d("12a", String.valueOf(userId));
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
////                Intent intent = new Intent(GuideActivity.this, HomeActivity.class);
////                startActivity(intent);
//                Intent toHome = new Intent(getActivity(), MapTeAty.class);
//                startActivity(toHome);
//                ;
//            }
//
//        }, 100);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cj_img:
//                if (!TextUtils.isEmpty(cjEdit.getText())) {
//                    addCj();
//                    cjEdit.setText("例如: 1A01");
                Intent to = new Intent(getActivity(), MapTeAty.class);
                startActivity(to);
//                } else {
//                    Toast.makeText(getActivity(), "请输入风机编号", Toast.LENGTH_SHORT).show();
//                }
                break;

        }
    }

    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setScanSpan(5000);
//        option.setIsNeedAddress(true);
//        option.setOpenGps(true);
//        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
//        option.setCoorType("bd09ll");
        mLocationClient.setLocOption(option);
    }

    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
//            lati = bdLocation.getLatitude();
//            longa = bdLocation.getLongitude();
//            Log.d("lati", String.valueOf(lati));
//            Log.d("longa", String.valueOf(longa));
//            int i = bdLocation.getLocType();
//            Log.d("longa", String.valueOf(i));
            StringBuilder sb = new StringBuilder();
            sb.append("纬度: ").append(bdLocation.getLatitude()).append("\n");
            sb.append("经度: ").append(bdLocation.getLongitude()).append("\n");
            sb.append("定位方式: ");
            if (bdLocation.getLocType() == BDLocation.TypeGpsLocation) {
                sb.append("GPS");
            } else if (bdLocation.getLocType() == BDLocation.TypeNetWorkException) {
                sb.append("网络");
            }
            Log.d("s111", String.valueOf(sb));

        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {

        }
    }

    private void addCj() {
        FormBody.Builder formBuilder = new FormBody.Builder();
        formBuilder.add("userid", String.valueOf(userId));
        formBuilder.add("xm", xm);
        formBuilder.add("fjbh", cjEdit.getText().toString());
        formBuilder.add("bumen", String.valueOf(bm));
        formBuilder.add("jingdu", String.valueOf(longa));
        formBuilder.add("weidu", String.valueOf(lati));
        Request request = new Request.Builder().url(Url.cjUrl).post(formBuilder.build()).build();
        OkHttpClient okHttpClient = new OkHttpClient();
//        Request request = new Request.Builder().url(Url.cjUrl).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
                if (!TextUtils.isEmpty(body)) {
                    Gson gson = new Gson();
                    JsonReader reader = new JsonReader(new StringReader(body));
                    mCjModel = gson.fromJson(reader, CjModel.class);
                    mHandler.post(mRunnable);
                } else {
                    Looper.prepare();
                    Toast.makeText(getActivity(), "ttt", Toast.LENGTH_SHORT).show();
                    Looper.loop();
                }
            }
        });
    }

    Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            if (mCjModel.getA().length() == 1) {
                Toast.makeText(getActivity(), "上传成功", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), "上传失败", Toast.LENGTH_SHORT).show();
            }
        }
    };
}
