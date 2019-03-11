package com.lh.ch.updateversion.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.content.FileProvider;

import java.io.File;
import java.io.IOException;
import java.util.EventListener;

/**
 * 类描述：FileUtil
 * <p>
 * Created by LOVE on 2016/8/31 031.
 */
public class FileUtil {

    public static File updateDir = null;
    public static File updateFile = null;
    /***********
     * 保存升级APK的目录
     ***********/
    public static final String KonkaApplication = "UpdateApp";

    public static boolean isCreateFileSucess;

    /**
     * 方法描述：createFile方法
     *
     * @param app_name
     * @return
     * @see FileUtil
     */
    public static void createFile(String app_name) {

        if (android.os.Environment.MEDIA_MOUNTED.equals(android.os.Environment.getExternalStorageState())) {
            isCreateFileSucess = true;
            updateDir = new File(Environment.getExternalStorageDirectory() + "/" + KonkaApplication + "/");
            updateFile = new File(updateDir + "/" + app_name + ".apk");
            if (!updateDir.exists()) {
                updateDir.mkdirs();
            }
            if (!updateFile.exists()) {
                try {
                    updateFile.createNewFile();
                } catch (IOException e) {
                    isCreateFileSucess = false;
                    e.printStackTrace();
                }
            }
        } else {
            isCreateFileSucess = false;
        }
    }

    public static void install(Context context, String app_name) {
        if (Environment.MEDIA_MOUNTED.equals(android.os.Environment.getExternalStorageState())) {
            isCreateFileSucess = true;

//            File file = new File(
//                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
//                    , "myApp.apk");
            updateDir = new File(Environment.getExternalStorageDirectory() + "/" + KonkaApplication + "/");
            updateFile = new File(updateDir + "/" + app_name + ".apk");
            //参数1 上下文, 参数2 Provider主机地址 和配置文件中保持一致   参数3  共享的文件
            Uri apkUri =
                    FileProvider.getUriForFile(context, "com.lh.ch.gkgedu.fileprovider", updateFile);

            Intent intent = new Intent(Intent.ACTION_VIEW);
            // 由于没有在Activity环境下启动Activity,设置下面的标签
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //添加这一句表示对目标应用临时授权该Uri所代表的文件
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
            context.startActivity(intent);
            if (!updateDir.exists()) {
                updateDir.mkdirs();
            }
            if (!updateFile.exists()) {
                try {
                    updateFile.createNewFile();
                } catch (IOException e) {
                    isCreateFileSucess = false;
                    e.printStackTrace();
                }
            }
        } else {
            isCreateFileSucess = false;
        }
    }
}