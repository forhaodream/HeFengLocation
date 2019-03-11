package com.lh.ch.hefenglocation.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.lh.ch.hefenglocation.R;
import com.lh.ch.hefenglocation.adapter.BuMenAdapter;
import com.lh.ch.hefenglocation.base.BaseActivity;
import com.lh.ch.hefenglocation.fragment.CheckFragment;
import com.lh.ch.hefenglocation.model.BuMenModel;
import com.lh.ch.hefenglocation.model.CheckModel;
import com.lh.ch.hefenglocation.net.HttpManager;
import com.lh.ch.hefenglocation.net.MyHttpCallback;
import com.lh.ch.hefenglocation.util.Url;
import com.lh.ch.pulltorefresh.PullToRefreshLayout;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * Created by CH on 2017/6/24.
 */

public class ManagerActivity extends BaseActivity {
    private EditText oldPsd, newOne, newTwo;
    private HttpManager mHttpManager;
    private String urls, userName, userXm;
    private int userId;
    private SharedPreferences readInfo;
    private ImageView retrunImg, xiuGaiImg;
    private ListView mListView;
    private CheckAdapter mAdapter;
    private CheckModel mCheckModel;
    private List<CheckModel.ListBean> data;
    private List<CheckModel.ListBean> pageData;
    private int index;
    private Handler mHandler;
    // 添加部门Spinner
    private Spinner mCheckSp;
    private BuMenAdapter mBuMenAdapter;
    private List<BuMenModel.ListBean> mBMData;
    private int buMenIndex = 0;
    private int checkItemPosition = 0;
    private String picUrl;
    private int posi;
    private EditText mEditText;
    private ImageView searchImg;
    private CheckModel checkModel;
    private int itemID = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_check);
        mHandler = new Handler();
        mHttpManager = HttpManager.getInstance();

        retrunImg = (ImageView) findViewById(R.id.xiugai_title_fanhui);
        retrunImg.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
//                Intent show = new Intent(ManagerActivity.this, HomeActivity.class);
//                show.putExtra("huan", 1);
//                startActivity(show);
            }
        });
        searchImg = (ImageView) findViewById(R.id.hw_search_btn);
        searchImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String aaa = Url.genzongUrl + "?xm=" + mEditText.getText();
                addCheck(aaa);
            }
        });
        PullToRefreshLayout ptr = (PullToRefreshLayout) findViewById(R.id.check_pullrefresh);
        mListView = (ListView) ptr.getPullableView();
        ptr.setOnPullListener(new PullToRefreshLayout.OnPullListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                addCheck(urls);
                pullToRefreshLayout.refreshFinish(0);
            }

            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                if (buMenIndex != 0) {
                    itemID += 1;
                    addPageCheck(Url.genzongUrl + "?bumen=" + buMenIndex + "&xm=" + mEditText.getText() + "&page=" + itemID);
                    Log.d("if", Url.genzongUrl + "?bumen=" + buMenIndex + "&page=" + itemID);
                    Toast.makeText(ManagerActivity.this, "加载完成,上拉查看", Toast.LENGTH_SHORT).show();
                    pullToRefreshLayout.loadmoreFinish(0);
                } else {
                    itemID += 1;
                    addPageCheck(Url.genzongUrl + "?xm=" + mEditText.getText() + "&bumen=" + buMenIndex + "&page=" + itemID);
                    Log.d("else", Url.genzongUrl + "?xm=" + mEditText.getText() + "&bumen=" + buMenIndex + "&page=" + itemID);
                    Toast.makeText(ManagerActivity.this, "加载完成,上拉查看", Toast.LENGTH_SHORT).show();
                    pullToRefreshLayout.loadmoreFinish(0);
                }
            }
        });
        mEditText = (EditText) findViewById(R.id.hw_search_edit);
        mCheckSp = (Spinner) findViewById(R.id.check_sp);
        mListView = (ListView) findViewById(R.id.check_list_view);
        readInfo = getSharedPreferences("user_npt", Context.MODE_PRIVATE);
        userName = readInfo.getString("userName", "");
        userXm = readInfo.getString("xm", "");
        userId = readInfo.getInt("userId", 0);
        Log.d("dsad", userName);
        Log.d("dsad", String.valueOf(userId));
        addSp();
    }

    private void addCheck(String url) {
        urls = Url.genzongUrl + "?xm=" + mEditText.getText();
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
                Log.d("body", body);
                try {
                    if (!TextUtils.isEmpty(body)) {
                        Gson gson = new Gson();
                        JsonReader reader = new JsonReader(new StringReader(body));
                        checkModel = gson.fromJson(reader, CheckModel.class);
                        mHandler.post(mRunnable);
                    } else {
                        Log.d("d", "d");
                    }
                } catch (IllegalStateException e) {
                    Log.d("没有", "数据");
                }
            }
        });

    }

    /*Runnable empty = new Runnable() {
        @Override
        public void run() {
            checkModel.getList().clear();
            mAdapter = new CheckAdapter(checkModel.getList());
            mAdapter.notifyDataSetChanged();
            data = checkModel.getList();
            mListView.setAdapter(mAdapter);

        }
    };*/

    private void addPageCheck(String url) {
        urls = Url.genzongUrl + "?xm=" + mEditText.getText();
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
                Log.d("body", body);
                try {
                    if (!TextUtils.isEmpty(body)) {
                        Gson gson = new Gson();
                        JsonReader reader = new JsonReader(new StringReader(body));
                        checkModel = gson.fromJson(reader, CheckModel.class);
                        pageData = checkModel.getList();
                        mHandler.post(pageRunn);
                    } else {
//                        Looper.prepare();
//                        Toast.makeText(ManagerActivity.this, "没有更多数据", Toast.LENGTH_SHORT).show();
//                        Looper.loop();
                        Log.d("没有", "数据");

                    }
                } catch (IllegalStateException e) {
                }
            }
        });

    }

    Runnable pageRunn = new Runnable() {
        @Override
        public void run() {
            if (pageData.size() > 0) {
                data.addAll(pageData);
                mAdapter.notifyDataSetChanged();
            } else {
//                Toast.makeText(ManagerActivity.this, "没有更多", Toast.LENGTH_SHORT).show();
                data.clear();
                mAdapter.notifyDataSetChanged();
                Log.d("没有", "数据");
            }


        }
    };

    Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            mAdapter = new CheckAdapter(checkModel.getList());
            data = checkModel.getList();
            mAdapter.notifyDataSetChanged();
            mListView.setAdapter(mAdapter);
            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    // picUrl = checkModel.getList().get(position).getPic();
                    index = position;
                    Intent toMap = new Intent(ManagerActivity.this, MapWebActivity.class);
                    toMap.putExtra("jing", checkModel.getList().get(position).getJingdu());
                    toMap.putExtra("wei", checkModel.getList().get(position).getWeidu());
                    toMap.putExtra("bm", checkModel.getList().get(position).getBumen());
                    toMap.putExtra("id", checkModel.getList().get(position).getId());
                    startActivity(toMap);
                }
            });
        }
    };


    private void addSp() {


        mHttpManager.doGet(Url.bmUrl, new MyHttpCallback<BuMenModel>() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onSuccess(Response response, BuMenModel buMenModel) throws IOException {
//                String str = response.body().string();
//                if (!TextUtils.isEmpty(str)) {
                mBMData = buMenModel.getList();
                mBuMenAdapter = new BuMenAdapter(mBMData);
                mCheckSp.setAdapter(mBuMenAdapter);
                mCheckSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        mBuMenAdapter.setCheckItem(position);
                        buMenIndex = mBMData.get(position).getId();
                        if (buMenIndex != 0) {
                            String ur = urls + "&bumen=" + buMenIndex;
                            addCheck(ur);
                            Log.d("lsls---", ur);
                            mBuMenAdapter.notifyDataSetChanged();
                            checkModel.getList().clear();
                            mAdapter = new CheckAdapter(checkModel.getList());
                            mAdapter.notifyDataSetChanged();
                        } else if (buMenIndex == 0) {
                            addCheck(Url.genzongUrl);
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

//                } else {
//                    Log.d("没有", "数据");
//                }

            }

            @Override
            public void onError(Response response, String errorMsg) {

            }
        });
    }

    public class CheckAdapter extends BaseAdapter {
        private List<CheckModel.ListBean> mData;

        public CheckAdapter(List<CheckModel.ListBean> data) {
            this.mData = data;
        }

        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public Object getItem(int position) {
            return mData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_check, null);
                holder.nameTv = (TextView) convertView.findViewById(R.id.item_check_name);
                holder.bumenTv = (TextView) convertView.findViewById(R.id.item_check_bumen);
                holder.phoneTv = (TextView) convertView.findViewById(R.id.item_check_phone);
                holder.timeTv = (TextView) convertView.findViewById(R.id.item_check_time);
                holder.img = (ImageView) convertView.findViewById(R.id.item_check_img);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.nameTv.setText(mData.get(position).getXm());
            holder.bumenTv.setText(String.valueOf(mData.get(position).getBumenname()));
            holder.phoneTv.setText(mData.get(position).getSj());
            holder.timeTv.setText(mData.get(position).getPaizhaoshijian2());
            Glide.with(holder.img.getContext()).load(mData.get(position).getPic()).into(holder.img);
            holder.img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    int poi = position;
//                    showPopupWindow(v, poi);
//                    Toast.makeText(getActivity(), "da", Toast.LENGTH_SHORT).show();
                    Intent toImg = new Intent(ManagerActivity.this, ShowImgActivity.class);
                    toImg.putExtra("imgUrl", mData.get(position).getPic());
                    startActivity(toImg);

                }
            });
            return convertView;
        }

        class ViewHolder {
            TextView nameTv;
            TextView bumenTv;
            TextView phoneTv;
            TextView timeTv;
            ImageView img;
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        itemID = 1;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        itemID = 1;
    }

}
