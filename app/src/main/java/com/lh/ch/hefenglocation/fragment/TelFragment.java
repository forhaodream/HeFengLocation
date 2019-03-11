package com.lh.ch.hefenglocation.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.lh.ch.hefenglocation.R;
import com.lh.ch.hefenglocation.activity.HomeActivity;
import com.lh.ch.hefenglocation.adapter.TelAdapter;
import com.lh.ch.hefenglocation.model.TelModel;
import com.lh.ch.hefenglocation.net.HttpManager;
import com.lh.ch.hefenglocation.net.MyHttpCallback;
import com.lh.ch.hefenglocation.util.Url;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by CH on 2017/6/1.
 */

public class TelFragment extends Fragment {
    private HttpManager mHttpManager;
    private String url;
    private List<TelModel.ListBean> mData;
    private TelAdapter mAdapter;
    private ListView mList;
    private String number;
    private ImageView returnImg;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tel, null);
        mHttpManager = HttpManager.getInstance();
        mList = (ListView) view.findViewById(R.id.tel_list);
        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 获取号码
                number = mData.get(position).getSj();
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
// 没有获得授权，申请授权
                    if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.CALL_PHONE)) {
// 返回值：
//如果app之前请求过该权限,被用户拒绝, 这个方法就会返回true.
//如果用户之前拒绝权限的时候勾选了对话框中”Don’t ask again”的选项,那么这个方法会返回false.
//如果设备策略禁止应用拥有这条权限, 这个方法也返回false.
// 弹窗需要解释为何需要该权限，再次请求授权
                        Toast.makeText(getActivity(), "请授权！", Toast.LENGTH_LONG).show();
// 帮跳转到该应用的设置界面，让用户手动授权
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", "com.lh.ch.hefenglocation", null);
                        intent.setData(uri);
                        startActivity(intent);
                    } else {
// 不需要解释为何需要该权限，直接请求授权
                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE}, 1);
                    }
                } else {
// 已经获得授权，可以打电话
                    callPhone();
                }
            }
        });
        returnImg = (ImageView) view.findViewById(R.id.tel_title_fanhui);
        returnImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent show = new Intent(getActivity(), HomeActivity.class);
                show.putExtra("huan", 1);
                startActivity(show);
            }
        });
        url = Url.telUrl;
        addTel(url);

        return view;
    }

    private void addTel(String url) {
        mHttpManager.doGet(url, new MyHttpCallback<TelModel>() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onSuccess(Response response, TelModel telModel) throws IOException {
                mData = telModel.getList();
                mAdapter = new TelAdapter(mData);
                Log.d("da,size()", String.valueOf(mData.size()));
                mList.setAdapter(mAdapter);

            }


            @Override
            public void onError(Response response, String errorMsg) {

            }
        });


    }

    private void callPhone() {
        if (TextUtils.isEmpty(number)) {
            // 提醒用户
            // 注意：在这个匿名内部类中如果用this则表示是View.OnClickListener类的对象，
            // 所以必须用MainActivity.this来指定上下文环境。
            Toast.makeText(getActivity(), "号码不能为空！", Toast.LENGTH_SHORT).show();
        } else {
            // 拨号：激活系统的拨号组件
            Intent intent = new Intent(); // 意图对象：动作 + 数据
            intent.setAction(Intent.ACTION_CALL); // 设置动作
            Uri data = Uri.parse("tel:" + number); // 设置数据
            intent.setData(data);
            startActivity(intent); // 激活Activity组件
        }
    }

}
