package com.lh.ch.hefenglocation.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.lh.ch.hefenglocation.R;
import com.lh.ch.hefenglocation.model.FengJiModel;
import com.lh.ch.hefenglocation.util.Url;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.IllegalFormatException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by CH on 2017/6/7.
 */

public class BaiDuMapActivity extends Activity {
    private MapView mMapView;
    private BaiduMap mBaiduMap;
    private FengJiModel mFengJiModel;
    private List<FengJiModel.ListBean> mData;
    private Handler mHandler;
    private LatLng poin;
    private OverlayOptions options;
    private String wei, jing;
    private ImageView returnImg;
    private int bumen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //取消标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //取消状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        SDKInitializer.initialize(getApplicationContext());
        SDKInitializer.setCoordType(CoordType.GCJ02);
        setContentView(R.layout.activity_baidu_map);
        mHandler = new Handler();
        mMapView = (MapView) findViewById(R.id.map_view);
        mBaiduMap = mMapView.getMap();
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        returnImg = (ImageView) findViewById(R.id.map_title_fanhui);
        returnImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Intent intent = getIntent();
        jing = intent.getStringExtra("jing");
        wei = intent.getStringExtra("wei");
        bumen = intent.getIntExtra("bm", 0);
        Log.d("wei", jing);
        Log.d("wei", wei);
        Log.d("wei", String.valueOf(bumen));
        addRen();
        addFJ();

    }

    private void addOverlay() {

        // 显示风机位置
        for (int i = 0; i < mData.size(); i++) {
            poin = new LatLng(Double.valueOf(mData.get(i).getWeidu()), Double.valueOf(mData.get(i).getJingdu()));
            BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.mipmap.fengji);
            OverlayOptions op = new MarkerOptions().position(poin).icon(bitmap);
            mBaiduMap.addOverlay(op);
        }


    }

    private void addRen() {
        //视野范围中心点
        MapStatus mapStatus = new MapStatus.Builder().target(new LatLng(Double.valueOf(wei), Double.valueOf(jing))).zoom(18).build();
        MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mapStatus);
        //改变地图状态
        mBaiduMap.setMapStatus(mapStatusUpdate);
//

        /**
         * // 将GPS设备采集的原始GPS坐标转换成百度坐标
         CoordinateConverter converter  = new CoordinateConverter();
         converter.from(CoordType.GPS);
         // sourceLatLng待转换坐标
         converter.coord(sourceLatLng);
         LatLng desLatLng = converter.convert();
         */
        // 显示检修人员位置
        LatLng ren = new LatLng(Double.valueOf(wei), Double.valueOf(jing));
        BitmapDescriptor renBp = BitmapDescriptorFactory.fromResource(R.mipmap.ren);
        OverlayOptions opin = new MarkerOptions().position(ren).icon(renBp);
        mBaiduMap.addOverlay(opin);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    private void addFJ() {
        String url = Url.fjUrl + "?bumen=" + bumen;
        Log.d("wei", url);
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {

                    String body = response.body().string();
                    Log.d("ad", body);
                    if (!TextUtils.isEmpty(body)) {
                        Gson gson = new Gson();
                        JsonReader reader = new JsonReader(new StringReader(body));
                        mFengJiModel = gson.fromJson(reader, FengJiModel.class);
                        mHandler.post(mRunnable);
                    } else {
                        Looper.prepare();
                        Toast.makeText(BaiDuMapActivity.this, "没有风机数据", Toast.LENGTH_SHORT).show();
                        Looper.loop();

                    }
                } catch (IllegalStateException e) {
                    Toast.makeText(BaiDuMapActivity.this, "没有数据", Toast.LENGTH_SHORT).show();
                }


            }
        });
    }

    Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            mData = mFengJiModel.getList();
//            Log.d("--da", mData.get(2).getJingdu());
            Log.d("--da", String.valueOf(mData.size()));
            Log.d("--jing", mData.get(0).getJingdu());
            Log.d("--wei", mData.get(0).getWeidu());
            addOverlay();
        }
    };

}
