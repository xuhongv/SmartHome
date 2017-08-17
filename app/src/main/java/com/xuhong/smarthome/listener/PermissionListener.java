package com.xuhong.smarthome.listener;

import java.util.List;

/**
 * @author JayChan
 * @description: 权限申请回调的接口
 * @date 2017/3/25  10:41
 * Copyright 2017 xuhong
 */
public interface PermissionListener {

    void onGranted();

    void onDenied(List<String> deniedPermissions);
}
