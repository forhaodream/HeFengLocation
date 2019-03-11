package com.lh.ch.hefenglocation.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;

import com.lh.ch.hefenglocation.R;

/**
 * Created by CH on 2017/6/1.
 */

public class BaseActivity extends Activity {
    protected Toolbar mToolBar;

    protected Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();


    }

    protected void initView() {

        mToolBar = (Toolbar) findViewById(R.id.toolbar);


    }

}
