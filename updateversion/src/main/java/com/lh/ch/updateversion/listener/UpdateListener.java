package com.lh.ch.updateversion.listener;

import com.lh.ch.updateversion.bean.UpdateEntity;

/**
 * 更新回调
 * Created by LOVE on 2016/9/12 012.
 */

public interface UpdateListener {
    void Update(boolean update, UpdateEntity updateEntity);

}
