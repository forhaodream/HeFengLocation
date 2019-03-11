package com.lh.ch.hefenglocation.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.lh.ch.hefenglocation.R;
import com.lh.ch.hefenglocation.base.BaseActivity;
import com.lh.ch.hefenglocation.fragment.CaiJiFragment;
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
 * Created by CH on 2017/6/27.
 */

public class MapTeAty extends Activity implements View.OnClickListener {
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
    private SharedPreferences readInfo;
    private ImageView returnImg;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLocationClient = new LocationClient(getApplicationContext());
        mLocationClient.registerLocationListener(myListener);
        //取消标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //取消状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.fragment_cj);
        // 获取用户名
        readInfo = getSharedPreferences("user_npt", Context.MODE_PRIVATE);
        mHandler = new Handler();
        initLocation();
        mLocationClient.start();
        returnImg = (ImageView) findViewById(R.id.cj_fanhui);
        returnImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
//                Intent show = new Intent(MapTeAty.this, HomeActivity.class);
//                show.putExtra("huan", 1);
//                startActivity(show);
            }
        });
        cjImg = (ImageView) findViewById(R.id.cj_img);
        cjImg.setOnClickListener(this);
        cjEdit = (EditText) findViewById(R.id.cj_edit);
//        cj = (ImageView)  findViewById(R.id.cj_cj);
//        cj.setOnClickListener(new  OnClickListener() {
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
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cj_img:
                if (!TextUtils.isEmpty(cjEdit.getText())) {
                    initLocation();
                    mLocationClient.start();
                    addCj();
                    cjEdit.setText("");
                    cjEdit.setTextColor(R.color.hint_hui);
                } else {
                    Toast.makeText(this, "请输入风机编号", Toast.LENGTH_SHORT).show();
                }
                break;

        }
    }

    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setIsNeedAddress(true);
        option.setOpenGps(true);
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        option.setCoorType("bd09ll");
        mLocationClient.setLocOption(option);
    }

    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            lati = bdLocation.getLatitude();
            longa = bdLocation.getLongitude();
            Log.d("lati", String.valueOf(lati));
            Log.d("longa", String.valueOf(longa));
            int i = bdLocation.getLocType();
            Log.d("longa", String.valueOf(i));
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
                    Toast.makeText(MapTeAty.this, "ttt", Toast.LENGTH_SHORT).show();
                    Looper.loop();
                }
            }
        });
    }

    Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            if (mCjModel.getA().length() == 1) {
                showToast(MapTeAty.this, "上传成功");
                finish();
//                Toast.makeText(MapTeAty.this, "上传成功", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MapTeAty.this, "上传失败", Toast.LENGTH_SHORT).show();
            }
        }
    };
    private Toast mToast;
    private TextView tv;

    public void showToast(Context context, String str) {
        if (mToast == null) {
            mToast = Toast.makeText(context, null, Toast.LENGTH_LONG);
            mToast.setGravity(Gravity.CENTER, 0, 0);
            LinearLayout toastView = (LinearLayout) mToast.getView();
            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            DisplayMetrics outMetrics = new DisplayMetrics();
            wm.getDefaultDisplay().getMetrics(outMetrics);
            tv = new TextView(this);
            toastView.setBackgroundResource(R.mipmap.tiank);
            tv.setTextColor(Color.WHITE);
            tv.setTextSize(30);
            toastView.setGravity(Gravity.CENTER);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(800, 180);
            params.setMargins(0, 0, 0, 0);
            tv.setLayoutParams(params);
            tv.setGravity(Gravity.CENTER);
            mToast.setView(toastView);
            toastView.addView(tv);
        }
        tv.setText(str);
        mToast.show();
    }
}
