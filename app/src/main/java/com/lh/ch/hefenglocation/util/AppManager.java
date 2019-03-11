package com.lh.ch.hefenglocation.util;

import android.content.Context;

import com.lh.ch.hefenglocation.base.BaseActivity;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by CH on 2017/6/6.
 */

public class AppManager {
    public static final String SPECIAL_IMEI = "000000000000000";
    private static Context mContext = null;
    private static final List<BaseActivity> ACTIVITY_STACK = new LinkedList<>();
    public static String deviceid;  // 设备ID
    public static String osVersion; // 操作系统版本
    public static String mobileType;// 手机型号
    public static String version;   // app的versionName
    public static int versionCode;  // app的versionCode
  //  public static String currentNetType = NetWorkUtils.NETWORK_TYPE_UNKNOWN;
    public static HashMap<String, Object> globalObjManager = new HashMap<>();

    public static Context getContext() {
        return mContext;
    }

    public static void setContext(Context mContext) {
        AppManager.mContext = mContext;
    }

    public static List<BaseActivity> getActivityList() {
        return ACTIVITY_STACK;
    }

    public static void pushAct(BaseActivity activity) {
        synchronized (ACTIVITY_STACK) {
            ACTIVITY_STACK.add(activity);
        }
    }

    public static void popAct(BaseActivity activity) {
        synchronized (ACTIVITY_STACK) {
            ACTIVITY_STACK.remove(activity);
        }
    }

    public static void popAllAct() {
        List<BaseActivity> copy;
        synchronized (ACTIVITY_STACK) {
            copy = new LinkedList<>(ACTIVITY_STACK);
            for (BaseActivity activity : copy) {
                activity.finish();
            }
        }
    }

    public static BaseActivity getStackTopAct() {
        int size = ACTIVITY_STACK.size();
        if (size > 0) {
            return ACTIVITY_STACK.get(size - 1);
        } else {
            return null;
        }

    }

    public static BaseActivity getStackBottomAct() {
        int size = ACTIVITY_STACK.size();
        if (size > 0) {
            return ACTIVITY_STACK.get(0);
        } else {
            return null;
        }
    }

}
