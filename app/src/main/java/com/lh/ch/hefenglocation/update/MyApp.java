package com.lh.ch.hefenglocation.update;

import android.app.ActivityManager;
import android.app.Application;
import android.content.pm.PackageManager;

import com.lh.ch.updateversion.UpdateHelper;
import com.lh.ch.updateversion.bean.UpdateEntity;
import com.lh.ch.updateversion.listener.ParseData;

import java.util.Iterator;
import java.util.List;

/**
 * Created by CH on 2017/6/10.
 */

public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
//        EMOptions options = new EMOptions();
//        options.setAcceptInvitationAlways(false);
//
//        // 初始化
//        EMClient.getInstance().init(getApplicationContext(), options);
//
//        EMClient.getInstance().setDebugMode(true);

        UpdateHelper.init(this);

        UpdateHelper
                .getInstance()

                .get("http://122.114.57.74:806/wuzi/genzong/shengji.aspx")

                .setDownType(UpdateHelper.DownType.down_click_Install)
                .showProgressDialog(true)
                .setJsonParser(new ParseData() {
                    @Override
                    public UpdateEntity parse(String httpResponse) {

                        UpdateEntity updateEntity = new UpdateEntity();
                        updateEntity.setAppName("检修定位");
                        updateEntity.setContent("本次升级新增、优化了部分功能。");
                        //https://qd.myapp.com/myapp/qqteam/AndroidQQ/mobileqq_android.apk
                        //http://www.kuxuel.com/shengji/kuxuele.apk
                        // http://cs.whqcjr.com:801/shengji/jianxiudingwei.apk
                        updateEntity.setUpdateUrl("http://122.114.57.74:806/wuzi/shengji/jianxiudingwei.apk");

                        updateEntity.setVersionCode(2);
                        updateEntity.setVersionName("1.1");

                        return updateEntity;
                    }
                });

    }

    private String getAppName(int pID) {
        String processName = null;
        ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        List l = am.getRunningAppProcesses();
        Iterator i = l.iterator();
        PackageManager pm = this.getPackageManager();
        while (i.hasNext()) {
            ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) i.next();
            try {
                if (info.pid == pID) {
                    processName = info.processName;
                    return processName;
                }
            } catch (Exception e) {


            }

        }

        return processName;
    }


}