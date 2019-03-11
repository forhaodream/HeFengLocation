package com.lh.ch.hefenglocation.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.lh.ch.hefenglocation.R;
import com.lh.ch.hefenglocation.base.BaseActivity;
import com.lh.ch.hefenglocation.model.LoginModel;
import com.lh.ch.hefenglocation.net.HttpManager;
import com.lh.ch.hefenglocation.net.MyHttpCallback;
import com.lh.ch.hefenglocation.util.Url;

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

public class LoginActivity extends Activity {
    private EditText userEd, psdEd;
    private ImageView dengluImg;
    private HttpManager mHttpManager;
    private String logUrl;
    private boolean isExit;
    private CheckBox mCheckBox;
    private String account;
    private String password;
    private SharedPreferences.Editor userInfo;
    private SharedPreferences sp;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private LoginModel loginModel;
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
        setContentView(R.layout.activity_login);
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        mCheckBox = (CheckBox) findViewById(R.id.login_remember);

        mHttpManager = HttpManager.getInstance();
        userEd = (EditText) findViewById(R.id.login_name);
        psdEd = (EditText) findViewById(R.id.login_psd);
        dengluImg = (ImageView) findViewById(R.id.login_denglu);
        dengluImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(userEd.getText()) || !TextUtils.isEmpty(psdEd.getText())) {
                    toLogin();
                    editor = pref.edit();
                    if (mCheckBox.isChecked()) { // 检查复选框是否被选中
                        editor.putBoolean("remember_psd", true);
                        editor.putString("account", account);
                        editor.putString("password", password);

                    } else {
                        editor.clear();
                    }
                    editor.apply();
                } else {
                    Toast.makeText(LoginActivity.this, "请正确输入登录信息", Toast.LENGTH_SHORT).show();
                }

            }
        });
        boolean isRemember = pref.getBoolean("remember_psd", false);
        if (isRemember) {
            String account = pref.getString("account", "");
            String password = pref.getString("password", "");
            userEd.setText(account);
            psdEd.setText(password);
            mCheckBox.setChecked(true);
        }

        // sp = this.getSharedPreferences("userinfo", Context.MODE_PRIVATE);
//        boolean checkBoxLogin = mCheckBox.isChecked();
//        if (checkBoxLogin) {
//            SharedPreferences.Editor editor = sp.edit();
//            editor.putString("uname", userEd.getText().toString());
//            editor.putString("upswd", psdEd.getText().toString());
//            editor.putBoolean("auto", true);
//            editor.commit();
//        } else {
//            SharedPreferences.Editor editor = sp.edit();
//            editor.putString("uname", null);
//            editor.putString("upswd", null);
//            editor.putBoolean("auto", false);
//            editor.commit();
//        }
//        if (sp.getBoolean("checkBoxLogin", false)) {
//            userEd.setText(sp.getString("uname", null));
//            psdEd.setText(sp.getString("upswd", null));
//            mCheckBox.setChecked(true);
//        }


    }

    private void toLogin() {

        logUrl = Url.loginUrl + userEd.getText() + "&password=" + psdEd.getText();
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(logUrl).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
                Log.d("body1", body);
                if (!TextUtils.isEmpty(body)) {
                    Gson gson = new Gson();
                    JsonReader reader = new JsonReader(new StringReader(body));
                    loginModel = gson.fromJson(reader, LoginModel.class);
                    mHandler.post(mRunnable);
                } else {
                    Looper.prepare();
                    Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
                    Looper.loop();
                }


            }
        });


    }

    Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            Log.d("ad", logUrl);
            Log.d("ad", loginModel.getErrorcode());
            Log.d("ad", loginModel.getXm());
            Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();


            userInfo = getSharedPreferences("user_npt", Context.MODE_PRIVATE).edit();
            userInfo.putString("userName", loginModel.getUsername());
            userInfo.putString("pasd", String.valueOf(psdEd.getText()));
            userInfo.putString("xm", loginModel.getXm());
            userInfo.putInt("userId", loginModel.getId());
            userInfo.putString("qx", loginModel.getQuanxian());
            userInfo.putInt("bm", loginModel.getBumen());
            userInfo.apply();

            Intent toHome = new Intent(LoginActivity.this, HomeActivity.class);
            toHome.putExtra("quanxian", loginModel.getQuanxian());
            startActivity(toHome);
        }
    };

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
            intent.addCategory(Intent.CATEGORY_HOME);
            startActivity(intent);
            System.exit(0);
        }
    }
}
