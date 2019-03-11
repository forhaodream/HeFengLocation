package com.lh.ch.updateversion.listener;

import com.lh.ch.updateversion.bean.UpdateEntity;

/**
 * 数据解析
 * Created by LOVE on 2016/8/31 031.
 */
public interface ParseData {
    UpdateEntity parse(String httpResponse);
}
