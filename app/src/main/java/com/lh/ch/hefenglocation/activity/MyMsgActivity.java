package com.lh.ch.hefenglocation.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.lh.ch.hefenglocation.R;
import com.lh.ch.hefenglocation.base.BaseActivity;
import com.lh.ch.hefenglocation.model.MyMsgModel;
import com.lh.ch.hefenglocation.util.Url;

import java.io.IOException;
import java.io.StringReader;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by CH on 2017/6/24.
 */

public class MyMsgActivity extends Activity {
    private ImageView retrunImg;
    private Handler mHandler;
    private MyMsgModel msgModel;
    private TextView userNameTv, xmTv, nianjiTv, bmTv, sexTv, telTv, shoujiTv, emailTv, qqTv, addressTv;
    private int uid;
    private SharedPreferences userId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //取消标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //取消状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_my_msg);
        mHandler = new Handler();
//        userId = this.getSharedPreferences("user_npt", MODE_PRIVATE);
//        uid = userId.getInt("userId", 0);
//        Log.d("---uid---", String.valueOf(uid));
        SharedPreferences readInfo = getSharedPreferences("user_npt", MODE_PRIVATE);

        uid = readInfo.getInt("userId", 1);
        Log.d("-1-1-", String.valueOf(uid));
        retrunImg = (ImageView) findViewById(R.id.mym_title_fanhui);
        retrunImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //   userNameTv = (TextView) findViewById(R.id.msg_username);
        xmTv = (TextView) findViewById(R.id.msg_xingming);
//        nianjiTv = (TextView) findViewById(R.id.msg_nianji);
//        banjiTv = (TextView) findViewById(R.id.msg_banji);
        sexTv = (TextView) findViewById(R.id.msg_sex);
        telTv = (TextView) findViewById(R.id.msg_tel);
        shoujiTv = (TextView) findViewById(R.id.msg_shouji);
        emailTv = (TextView) findViewById(R.id.msg_email);
        qqTv = (TextView) findViewById(R.id.msg_qq);
        addressTv = (TextView) findViewById(R.id.msg_address);
        bmTv = (TextView) findViewById(R.id.msg_bm);

        getInfo();

    }

    private void getInfo() {


        String url = Url.msgUrl + "?id=" + uid;
        Log.d("---uid---", url);
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        Call call = okHttpClient.newCall(request);
//        call.enqueue(new Callback() {
//            @Override
//            public void onFailure(Request request, IOException e) {
//
//            }
//
//            @Override
//            public void onResponse(Response response) throws IOException {
//                if (response.isSuccessful()) {
//                    String msg = response.body().string();
//                    Gson gson = new Gson();
//                    JsonReader reader = new JsonReader(new StringReader(msg));
//                    msgModel = gson.fromJson(reader, MyMsgModel.class);
//
//                    mHandler.post(mRunnable);
//                }
//
//            }
//        });

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String msg = response.body().string();
                Log.d("masg", msg);
                if (!TextUtils.isEmpty(msg)) {
                    Gson gson = new Gson();
                    JsonReader reader = new JsonReader(new StringReader(msg));
                    msgModel = gson.fromJson(reader, MyMsgModel.class);

                    mHandler.post(mRunnable);
                } else {
                    Looper.prepare();
                    Toast.makeText(MyMsgActivity.this, "内部错误", Toast.LENGTH_SHORT).show();
                    Looper.loop();
                }

            }
        });
    }

    Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            xmTv.setText(msgModel.getXm());
            sexTv.setText(msgModel.getXb());
            telTv.setText(msgModel.getGudingdianhua());
            shoujiTv.setText(msgModel.getSj());
            emailTv.setText(msgModel.getEmail());
            qqTv.setText(msgModel.getQq());
            addressTv.setText(msgModel.getFgld());
            bmTv.setText(msgModel.getBm());
        }
    };
}
