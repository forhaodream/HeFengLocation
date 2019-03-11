package com.lh.ch.hefenglocation.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.ViewGroup.LayoutParams;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.lh.ch.hefenglocation.R;
import com.lh.ch.hefenglocation.activity.BaiDuMapActivity;
import com.lh.ch.hefenglocation.activity.HomeActivity;
import com.lh.ch.hefenglocation.activity.ShowImgActivity;
import com.lh.ch.hefenglocation.adapter.BuMenAdapter;
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
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.log.LoggerDefault;

/**
 * Created by CH on 2017/6/1.
 */

public class CheckFragment extends Fragment {
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
    private View emptyView;
    private CheckModel checkModel;
    private int itemId = 0;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_check, null);
        mHandler = new Handler();
        mHttpManager = HttpManager.getInstance();

        retrunImg = (ImageView) view.findViewById(R.id.xiugai_title_fanhui);
        retrunImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent show = new Intent(getActivity(), HomeActivity.class);
                show.putExtra("huan", 1);
                startActivity(show);
            }
        });
        searchImg = (ImageView) view.findViewById(R.id.hw_search_btn);
        searchImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCheck(urls);
                Log.d("usl", urls);
                Log.d("usl", mEditText.getText().toString());
            }
        });
        PullToRefreshLayout ptr = (PullToRefreshLayout) view.findViewById(R.id.check_pullrefresh);
        mListView = (ListView) ptr.getPullableView();
        ptr.setOnPullListener(new PullToRefreshLayout.OnPullListener() {
                                  @Override
                                  public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                                      addCheck(urls);
                                      pullToRefreshLayout.refreshFinish(0);
                                  }

                                  @Override
                                  public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                                      addCheck(Url.genzongUrl + "?xm=" + mEditText.getText() + "&bumen=" + buMenIndex);
                                      pullToRefreshLayout.refreshFinish(0);
                                  }
                              }

        );
        mEditText = (EditText) view.findViewById(R.id.hw_search_edit);
        mCheckSp = (Spinner) view.findViewById(R.id.check_sp);
        mListView = (ListView) view.findViewById(R.id.check_list_view);
        readInfo = getActivity().getSharedPreferences("user_npt", Context.MODE_PRIVATE);
        userName = readInfo.getString("userName", "");
        userXm = readInfo.getString("xm", "");
        userId = readInfo.getInt("userId", 0);
        Log.d("dsad", userName);
        Log.d("dsad", String.valueOf(userId));

        addCheck(Url.genzongUrl);

        addSp();

        return view;
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
                        mHandler.post(empty);
                    }
                } catch (IllegalStateException e) {
                    Toast.makeText(getActivity(), "没有数据", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    Runnable empty = new Runnable() {
        @Override
        public void run() {
            checkModel.getList().clear();
            mAdapter = new CheckAdapter(checkModel.getList());
            data = checkModel.getList();
            mAdapter.notifyDataSetChanged();
            mListView.setAdapter(mAdapter);
            mListView.setEmptyView(emptyView);

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
                    Intent toMap = new Intent(getActivity(), BaiDuMapActivity.class);
                    toMap.putExtra("jing", checkModel.getList().get(position).getJingdu());
                    toMap.putExtra("wei", checkModel.getList().get(position).getWeidu());
                    toMap.putExtra("bm", checkModel.getList().get(position).getBumen());
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
                    Intent toImg = new Intent(getActivity(), ShowImgActivity.class);
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

    private void showPopupWindow(View view, int poi) {
        View contentView = LayoutInflater.from(getActivity()).inflate(R.layout.pop_window, null);
        PhotoView imageView = (PhotoView) contentView.findViewById(R.id.popup_img);
        Glide.with(getActivity()).load(data.get(poi).getPic()).into(imageView);
        Log.d("INDEX", String.valueOf(index));
        final PopupWindow popupWindow = new PopupWindow(contentView,
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, true);
        popupWindow.setTouchable(true);
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                Log.i("mengdd", "onTouch : ");

                return false;
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
            }
        });

        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        // 我觉得这里是API的一个bug
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        int[] location = new int[2];
        view.getLocationOnScreen(location);

        popupWindow.showAtLocation(view, Gravity.NO_GRAVITY, location[0], location[1] - popupWindow.getHeight());

        // 设置好参数之后再show
        popupWindow.showAsDropDown(view);

    }


}
