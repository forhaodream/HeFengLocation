package com.lh.ch.hefenglocation.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.lh.ch.hefenglocation.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;

/**
 * Created by CH on 2017/6/2.
 */

public class GuideActivity extends Activity {

    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;
    private SharedPreferences sp;
    private String mInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //取消标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //取消状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_guide);
        sp = getSharedPreferences("user_npt", Context.MODE_PRIVATE);
        mInfo = sp.getString("userName", "");
        Log.d("info", mInfo);
//        if (mInfo.length() > 0) {
//            Intent toHome = new Intent(GuideActivity.this, HomeActivity.class);
//            startActivity(toHome);
//        }
//        else {
//            Intent toHome = new Intent(GuideActivity.this, LoginActivity.class);
//            startActivity(toHome);
//        }
//        if (ContextCompat.checkSelfPermission(GuideActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(GuideActivity.this, new String[]{Manifest.permission.CAMERA}, 1);
//        }
//        if (ContextCompat.checkSelfPermission(GuideActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(GuideActivity.this, new String[]{Manifest.permission.CAMERA}, 1);
//        }
//        if (ContextCompat.checkSelfPermission(GuideActivity.this, Manifest.permission.LOCATION_HARDWARE) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(GuideActivity.this, new String[]{Manifest.permission.LOCATION_HARDWARE}, 1);
//        }
//        if (ContextCompat.checkSelfPermission(GuideActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(GuideActivity.this, new String[]{Manifest.permission.CALL_PHONE}, 1);
//        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
//                Intent intent = new Intent(GuideActivity.this, HomeActivity.class);
//                startActivity(intent);
                if (!TextUtils.isEmpty(mInfo)) {
                    Intent toHome = new Intent(GuideActivity.this, HomeActivity.class);
                    Log.d("info", mInfo);
                    startActivity(toHome);
                    finish();
                } else {
                    Intent toHome = new Intent(GuideActivity.this, HomeActivity.class);
                    startActivity(toHome);
                    finish();
                }
            }
        }, 1000);

    }

    @Override
    protected void onResume() {
        Log.d("info1", mInfo);
        super.onResume();
    }

    @Override
    protected void onStop() {
        Log.d("info2", mInfo);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.d("info3", mInfo);
        super.onDestroy();
    }
}

