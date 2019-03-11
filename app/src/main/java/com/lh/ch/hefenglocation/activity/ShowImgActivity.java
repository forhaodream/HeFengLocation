package com.lh.ch.hefenglocation.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.lh.ch.hefenglocation.R;
import com.lh.ch.hefenglocation.photo.FullPhotoView;

/**
 * Created by CH on 2017/6/12.
 */

public class ShowImgActivity extends Activity {
    private FullPhotoView mPhotoView;
    private ImageView mImageView;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //取消标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //取消状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_show_img);
//        mPhotoView = (FullPhotoView) findViewById(R.id.activity_show_photo);
//        mPhotoView.setCustomView(R.layout.popup_default, R.id.popup_default_iv);
        mImageView = (ImageView) findViewById(R.id.activity_show_img);
        Intent intent = getIntent();
        url = intent.getStringExtra("imgUrl");
        Glide.with(this).load(url).into(mImageView);
    }
}
