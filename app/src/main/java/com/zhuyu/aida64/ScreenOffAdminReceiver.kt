package com.zhuyu.aida64

import android.app.admin.DeviceAdminReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.blankj.utilcode.util.ToastUtils

class ScreenOffAdminReceiver: DeviceAdminReceiver() {
    override fun onEnabled(context: Context, intent: Intent) {
        super.onEnabled(context, intent)
        ToastUtils.showShort("设备管理器激活成功")
    }

    override fun onDisabled(context: Context, intent: Intent) {
        super.onDisabled(context, intent)
        ToastUtils.showShort("设备管理器激活失败")
    }
}