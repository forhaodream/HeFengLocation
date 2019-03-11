package com.lh.ch.hefenglocation.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.lh.ch.hefenglocation.R;
import com.lh.ch.hefenglocation.fragment.CJListFragment;
import com.lh.ch.hefenglocation.fragment.CaiJiFragment;
import com.lh.ch.hefenglocation.fragment.ExitFragment;
import com.lh.ch.hefenglocation.fragment.HomeFragment;
import com.lh.ch.hefenglocation.fragment.NoQXFragment;
import com.lh.ch.hefenglocation.fragment.TelFragment;
import com.lh.ch.hefenglocation.fragment.CheckFragment;
import com.lh.ch.hefenglocation.net.HttpManager;
import com.lh.ch.hefenglocation.util.VersionTools;
import com.lh.ch.updateversion.UpdateHelper;
import com.lh.ch.updateversion.bean.UpdateEntity;
import com.lh.ch.updateversion.listener.UpdateListener;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

/**
 * Created by CH on 2017/6/1.
 */

public class HomeActivity extends FragmentActivity implements View.OnClickListener {
    public static final String TAG = "HomeActivity";
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;//权限请求码
    private FragmentTabHost mTabHost;
    private LinearLayout button0, button1, button3, button4;
    private static Boolean isQuit = false;
    private Timer timer = new Timer();
    private boolean isExit;
    private SharedPreferences readInfo;
    private HttpManager mHttpManager;
    //初始化标签数组
    String tabs[] = {"Tab1", "Tab2", "Tab3", "Tab4"};

    //初始化界面数组
    Class cls[] = {HomeFragment.class, CJListFragment.class, TelFragment.class, ExitFragment.class};

    private ImageView image1, image2, image3, image4;
    private TextView text1, text2, text3, text4;
    private RelativeLayout button2; //显示数字标签布局
    private String quanxian;

    //清除sp
    private SharedPreferences npt;
    SharedPreferences.Editor editor;
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            isExit = false;
        }

    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //取消标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //取消状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_home);
        mHttpManager = HttpManager.getInstance();
        List<String> permissionList = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (ContextCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (!permissionList.isEmpty()) {
            String[] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(HomeActivity.this, permissions, 1);
        } else {
            Log.d("-a-a-", "已授权");
            //  Toast.makeText(getApplicationContext(), "已授权", Toast.LENGTH_SHORT).show();
        }
        mHandler = new Handler();
        npt = getSharedPreferences("user_npt", MODE_PRIVATE);
        editor = npt.edit();
        quanxian = npt.getString("qx", null);
        addView();
        Intent intent = getIntent();
        int index = intent.getIntExtra("huan", -1);
        Log.d("home_index", String.valueOf(index));
        if (String.valueOf(index).equals("1")) {
            setLayoutButton1();
            mTabHost.setCurrentTabByTag(tabs[0]);
        }
    }


    private void addView() {
        //实例化控件
        this.image1 = (ImageView) findViewById(R.id.image1);
        this.image2 = (ImageView) findViewById(R.id.image2);
        this.image3 = (ImageView) findViewById(R.id.image3);
        this.image4 = (ImageView) findViewById(R.id.image4);

        this.text1 = (TextView) findViewById(R.id.text1);
        this.text2 = (TextView) findViewById(R.id.text2);
        this.text3 = (TextView) findViewById(R.id.text3);
        this.text4 = (TextView) findViewById(R.id.text4);

        //实例化 FragmentTabHost (注：id 的获取必须为固定) 与 FrameLayout 布局
        mTabHost = (FragmentTabHost) this.findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
        mTabHost.getTabWidget().setVisibility(View.GONE);//隐藏顶部切换菜单
        for (int i = 0; i < tabs.length; i++) {
            //向 FragmentTabHost 添加标签以及 Fragment 界面
            mTabHost.addTab(mTabHost.newTabSpec(tabs[i]).setIndicator(tabs[i]),
                    cls[i], null);

        }

        //实例化布局按钮控件
        button0 = (LinearLayout) findViewById(R.id.Button0);
        button1 = (LinearLayout) findViewById(R.id.Button1);
        button2 = (RelativeLayout) findViewById(R.id.Button2);
        button3 = (LinearLayout) findViewById(R.id.Button3);

        //设置监听事件
        this.button0.setOnClickListener(this);
        this.button1.setOnClickListener(this);
        this.button2.setOnClickListener(this);
        this.button3.setOnClickListener(this);

        //设置默认选中标签
        mTabHost.setCurrentTabByTag(tabs[0]);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.Button0:
                setLayoutButton1();
                mTabHost.setCurrentTabByTag(tabs[0]);
                break;
            case R.id.Button1:
                setlayoutbutton2();
                mTabHost.setCurrentTabByTag(tabs[1]);
//                Intent toHome = new Intent(HomeActivity.this, MapTeAty.class);
//                startActivity(toHome);
                break;
            case R.id.Button2:
//                String qx = "1";
//                if (quanxian != null) {
//                    Log.d("-09", quanxian);
//                } else {
//                    Log.d("-09", "90--");
//                }
//                if (!quanxian.equals(qx)) {
//                    setLayoutNo();
//                    mTabHost.setCurrentTabByTag(tabs[3]);
//                } else {

                setlayoutbutton3();
                mTabHost.setCurrentTabByTag(tabs[2]);
//                }

                break;

            case R.id.Button3:

//                editor.clear();
//                editor.apply();
//                Intent toLogin = new Intent(HomeActivity.this, LoginActivity.class);
//
//                startActivity(toLogin);
                setlayoutbutton5();
                mTabHost.setCurrentTabByTag(tabs[3]);
                break;

        }
    }


    //设置点击切换标签字体颜色与背景图片的切换
    private void setLayoutButton1() {
        image1.setBackgroundResource(R.mipmap.home_lan);
        image2.setBackgroundResource(R.mipmap.sj_hui);
        image3.setBackgroundResource(R.mipmap.photo_hui);
        image4.setBackgroundResource(R.mipmap.wo_hui);
        text1.setTextColor(this.getResources().getColor(R.color.text_green));
        text2.setTextColor(this.getResources().getColor(R.color.text_gray));
        text3.setTextColor(this.getResources().getColor(R.color.text_gray));
        text4.setTextColor(this.getResources().getColor(R.color.text_gray));
    }

    private void setlayoutbutton2() {
        image1.setBackgroundResource(R.mipmap.home_hui);
        image2.setBackgroundResource(R.mipmap.sj_lan);
        image3.setBackgroundResource(R.mipmap.photo_hui);
        image4.setBackgroundResource(R.mipmap.wo_hui);
        text1.setTextColor(this.getResources().getColor(R.color.text_gray));
        text2.setTextColor(this.getResources().getColor(R.color.text_green));
        text3.setTextColor(this.getResources().getColor(R.color.text_gray));
        text4.setTextColor(this.getResources().getColor(R.color.text_gray));
    }

    private void setlayoutbutton3() {
        image1.setBackgroundResource(R.mipmap.home_hui);
        image2.setBackgroundResource(R.mipmap.sj_hui);
        image3.setBackgroundResource(R.mipmap.photo_lan);
        image4.setBackgroundResource(R.mipmap.wo_hui);
        text1.setTextColor(this.getResources().getColor(R.color.text_gray));
        text2.setTextColor(this.getResources().getColor(R.color.text_gray));
        text3.setTextColor(this.getResources().getColor(R.color.text_green));
        text4.setTextColor(this.getResources().getColor(R.color.text_gray));
    }

    private void setLayoutNo() {
        image1.setBackgroundResource(R.mipmap.home_hui);
        image2.setBackgroundResource(R.mipmap.photo_hui);
        image3.setBackgroundResource(R.mipmap.suo_lan);
        image4.setBackgroundResource(R.mipmap.exit_hui);
        text1.setTextColor(this.getResources().getColor(R.color.text_gray));
        text2.setTextColor(this.getResources().getColor(R.color.text_gray));
        text3.setTextColor(this.getResources().getColor(R.color.text_green));
        text4.setTextColor(this.getResources().getColor(R.color.text_gray));
    }

//    private void setlayoutbutton4() {
//        image1.setBackgroundResource(R.mipmap.home_hui);
//        image2.setBackgroundResource(R.mipmap.photo_hui);
//        image3.setBackgroundResource(R.mipmap.suo_hui);
//        image4.setBackgroundResource(R.mipmap.exit_lan);
//        text1.setTextColor(this.getResources().getColor(R.color.text_gray));
//        text2.setTextColor(this.getResources().getColor(R.color.text_gray));
//        text3.setTextColor(this.getResources().getColor(R.color.text_gray));
//        text4.setTextColor(this.getResources().getColor(R.color.text_green));
//    }

    private void setlayoutbutton5() {
        image1.setBackgroundResource(R.mipmap.home_hui);
        image2.setBackgroundResource(R.mipmap.sj_hui);
        image3.setBackgroundResource(R.mipmap.photo_hui);
        image4.setBackgroundResource(R.mipmap.wo_lan);
        text1.setTextColor(this.getResources().getColor(R.color.text_gray));
        text2.setTextColor(this.getResources().getColor(R.color.text_gray));
        text3.setTextColor(this.getResources().getColor(R.color.text_gray));
        text4.setTextColor(this.getResources().getColor(R.color.text_green));
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    public void exit() {
        if (!isExit) {
            isExit = true;
            Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
            mHandler.sendEmptyMessageDelayed(0, 2000);
        } else {
            Intent intent = new Intent(Intent.ACTION_MAIN);
//            readInfo = getSharedPreferences("user_npt", MODE_PRIVATE);
//            SharedPreferences.Editor editor = readInfo.edit();
//            editor.remove("userName");
//            editor.remove("pasd");
//            editor.apply();
            intent.addCategory(Intent.CATEGORY_HOME);
            startActivity(intent);
            System.exit(0);
        }
    }

    @Override
    protected void onResume() {

        super.onResume();
        Log.d("---", "ONR");
    }

    @Override
    protected void onStop() {
        super.onStop();

        Log.d("---", "ONP");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("---", "ONReS");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("---", "ONRR");
    }
}
