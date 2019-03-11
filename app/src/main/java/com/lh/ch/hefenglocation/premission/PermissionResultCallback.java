package com.lh.ch.hefenglocation.premission;

import java.util.List;

/**
 * Created by CH on 2017/6/5.
 */
public interface PermissionResultCallback {
    void onPermissionGranted(List<String> grantedPermissions);

    void onPermissionDenied(List<String> deniedPermissions);
}
