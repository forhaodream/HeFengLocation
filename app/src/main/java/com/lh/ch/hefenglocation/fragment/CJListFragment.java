package com.lh.ch.hefenglocation.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.lh.ch.hefenglocation.R;
import com.lh.ch.hefenglocation.activity.CjAty;
import com.lh.ch.hefenglocation.activity.HomeActivity;
import com.lh.ch.hefenglocation.activity.MapTeAty;
import com.lh.ch.hefenglocation.adapter.CJListAdapter;
import com.lh.ch.hefenglocation.model.CjListModel;
import com.lh.ch.hefenglocation.model.UrlModel;
import com.lh.ch.hefenglocation.util.Url;
import com.lh.ch.pulltorefresh.PullToRefreshLayout;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;

/**
 * Created by CH on 2017/9/8.
 */

public class CJListFragment extends Fragment {
    private View mView;
    private WebView mWebView;
    private Handler mHandler;
    private ImageView returnImg;
    private WebSettings webSettings;
    private UrlModel mUrlModel;
    private String url;
    private ImageView toCj;
    private CJListAdapter mCJListAdapter;
    private List<CjListModel.ListBean> mData;
    private List<CjListModel.ListBean> mPageData;
    private CjListModel mCjListModel;
    private ListView mListView;
    private int itemId = 1, bumen;

    private SharedPreferences readInfo;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // 获取用户名
        readInfo = getActivity().getSharedPreferences("user_npt", Context.MODE_PRIVATE);
        bumen = readInfo.getInt("bm", 0);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_cj_list, null);
        mHandler = new Handler();
        toCj = (ImageView) mView.findViewById(R.id.cj_list_img);
        toCj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MapTeAty.class);
                startActivity(intent);
            }
        });
        returnImg = (ImageView) mView.findViewById(R.id.cj_fanhui);
        returnImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent show = new Intent(getActivity(), HomeActivity.class);
                show.putExtra("huan", 1);
                startActivity(show);
            }
        });
        mListView = (ListView) mView.findViewById(R.id.cj_list_list);
        addCj(Url.cjListUrl + bumen);
        PullToRefreshLayout ptr = (PullToRefreshLayout) mView.findViewById(R.id.cj_list_ptr);
        ptr.setOnPullListener(new PullToRefreshLayout.OnPullListener() {
                                  @Override
                                  public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                                      addCj(Url.cjListUrl + bumen);
                                      itemId = 1;
                                      pullToRefreshLayout.refreshFinish(0);
                                  }

                                  @Override
                                  public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                                      itemId += 1;
                                      addPage(Url.cjListUrl + bumen + "&page=" + itemId);
                                      Toast.makeText(getActivity(), "加载完成,上拉查看", Toast.LENGTH_SHORT).show();
                                      pullToRefreshLayout.loadmoreFinish(0);
                                  }
                              }

        );


        return mView;
    }

    private void addCj(String urls) {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(urls).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
            }

            @Override
            public void onResponse(Response response) throws IOException {
                String str = response.body().string();
                Log.d("str", str);
                if (!TextUtils.isEmpty(str)) {
                    Gson gson = new Gson();
                    JsonReader reader = new JsonReader(new StringReader(str));
                    mCjListModel = gson.fromJson(reader, CjListModel.class);
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mData = mCjListModel.getList();
                            mCJListAdapter = new CJListAdapter(mData);
                            mCJListAdapter.notifyDataSetChanged();
                            mListView.setAdapter(mCJListAdapter);
                        }
                    });
                } else {
                    Log.d("没有", "数据");
                }
            }
        });
    }

    /////////////////////////////分页加载//////////////////////////////
    private void addPage(String url) {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                String body = response.body().string();
                Log.d("body111", body);
                if (!TextUtils.isEmpty(body)) {
                    Gson gson = new Gson();
                    JsonReader reader = new JsonReader(new StringReader(body));
                    mCjListModel = gson.fromJson(reader, CjListModel.class);
                    mPageData = mCjListModel.getList();
                    mHandler.post(pageRunn);
                } else {
                    Log.d("没有", "数据");

                }

            }
        });


    }

    Runnable pageRunn = new Runnable() {
        @Override
        public void run() {
            if (mPageData.size() > 0) {
                mData.addAll(mPageData);
                mCJListAdapter.notifyDataSetChanged();
            } else {
                mData.clear();
                mCJListAdapter.notifyDataSetChanged();
                Log.d("没有", "数据");
            }

        }
    };

    @Override
    public void onStop() {
        super.onStop();
        itemId = 1;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        itemId = 1;
    }

    @Override
    public void onResume() {
        super.onResume();
        addCj(Url.cjListUrl + bumen);
    }
}
