package com.lh.ch.hefenglocation.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.lh.ch.hefenglocation.R;
import com.lh.ch.hefenglocation.base.BaseActivity;
import com.lh.ch.hefenglocation.model.WebViewModel;
import com.lh.ch.hefenglocation.util.Url;

import java.io.IOException;
import java.io.StringReader;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * Created by CH on 2017/6/26.
 */

public class MapWebActivity extends BaseActivity {
    private WebView mWebView;
    private WebViewModel mAboutModel;
    private Handler mHandler;
    private String url;
    private ImageView returnImg;
    private int bumen;
    private int id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_web);
        mHandler = new Handler();
        mWebView = new WebView(this);
        mWebView = (WebView) findViewById(R.id.map_webview);
        returnImg = (ImageView) findViewById(R.id.title_fanhui);
        returnImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Intent intent = getIntent();

        bumen = intent.getIntExtra("bm", 0);
        id = intent.getIntExtra("id", 0);
        Log.d("wei", String.valueOf(bumen));
        Log.d("wei", String.valueOf(id));
        getUrl();
    }

    private void getUrl() {
        String urls = Url.mapUrl + "?id=" + id + "&bumen=" + bumen;
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(urls).build();
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
                    mAboutModel = gson.fromJson(reader, WebViewModel.class);
                    mHandler.post(mRunnable);
                } else {
                    Log.d("没有", "数据");
                }
            }

        });

    }

    Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            url = mAboutModel.getUrl();
            mWebView.loadUrl(url);
            addWebView();
        }
    };

    private void addWebView() {

        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return false;

            }
        });
        WebSettings webSettings = mWebView.getSettings();
        //设置自适应屏幕，两者合用
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
        mWebView.getSettings().setSupportZoom(true);
        mWebView.getSettings().setBuiltInZoomControls(true);
        //如果访问的页面中要与Javascript交互，则webview必须设置支持Javascript
        webSettings.setJavaScriptEnabled(true);
        webSettings.setSupportZoom(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        //自适应屏幕
        mWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setDisplayZoomControls(false);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && mWebView.canGoBack()) {
            mWebView.goBack();// 返回前一个页面
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
