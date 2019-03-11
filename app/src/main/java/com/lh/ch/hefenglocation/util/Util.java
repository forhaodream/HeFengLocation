package com.lh.ch.hefenglocation.util;

import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

/**
 * Created by CH on 2017/6/5.
 */

public class Util {

    static int level = 0;

    public static int getEmuiLeval() {
        // Finals 2016-6-14 如果获取过了就不用再获取了，因为读取配置文件很慢
        if (level > 0) {
            return level;
        }
        Properties properties = new Properties();
        File propFile = new File(Environment.getRootDirectory(), "build.prop");
        FileInputStream fis = null;
        if (propFile != null && propFile.exists()) {
            try {
                fis = new FileInputStream(propFile);
                properties.load(fis);
                fis.close();
                fis = null;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                }
            }
        }
        if (properties.containsKey("ro.build.hw_emui_api_level")) {
            String valueString = properties.getProperty("ro.build.hw_emui_api_level");
            try {
                level = Integer.parseInt(valueString);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return level;
    }
}

