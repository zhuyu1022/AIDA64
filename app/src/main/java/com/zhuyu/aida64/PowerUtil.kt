package com.zhuyu.aida64

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.util.Log

import android.os.PowerManager
import android.app.KeyguardManager
import android.app.admin.DevicePolicyManager
import android.content.Context.POWER_SERVICE
import androidx.core.content.ContextCompat.getSystemService
import android.content.ComponentName
import android.widget.Toast


object PowerUtil {


    var IS_CHARGE_DISABLE = true

    fun isChargingDisable(context: Context): Boolean {
        return IS_CHARGE_DISABLE && isCharging(context);
    }

    fun isCharging(context: Context): Boolean {
        val batteryBroadcast = context.registerReceiver(
            null,
            IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        )
        // 0 means we are discharging, anything else means charging
        val isCharging = batteryBroadcast?.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1) != 0;
        Log.d("PowerUtil", "isCharging = " + isCharging);
        return isCharging;
    }


    /**
     * @param view 亮屏
     */
    @SuppressLint("InvalidWakeLockTag")
     fun  checkScreenOn(context: Context) {
        val mPowerManager = context.getSystemService(POWER_SERVICE) as PowerManager
        val mWakeLock = mPowerManager.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK or PowerManager.ACQUIRE_CAUSES_WAKEUP, "tag")
        mWakeLock.acquire()
        mWakeLock.release()
    }
    /**
     * @param view 熄屏
     */
     fun  checkScreenOff(context: Context ) {
        val policyManager =context.getSystemService(Context.DEVICE_POLICY_SERVICE)as DevicePolicyManager
        val  admin = policyManager.isAdminActive(ComponentName(context, ScreenOffAdminReceiver::class.java))
        if (admin) {
            policyManager.lockNow()
        } else {
           // Toast.makeText(context, "没有设备管理权限", Toast.LENGTH_SHORT).show()
        }
       // checkScreenOn(context)

    }

    @SuppressLint("InvalidWakeLockTag")
    fun wakeUp(context: Context) {
        //屏锁管理器
        val km = context.getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
        val kl = km.newKeyguardLock("unLock")
        //解锁
        kl.disableKeyguard()
        //获取电源管理器对象
        val pm = context.getSystemService(Context.POWER_SERVICE) as PowerManager
        //获取PowerManager.WakeLock对象,后面的参数|表示同时传入两个值,最后的是LogCat里用的Tag
        val wl = pm.newWakeLock(
            PowerManager.ACQUIRE_CAUSES_WAKEUP or PowerManager.SCREEN_DIM_WAKE_LOCK,
            "bright"
        )
        //点亮屏幕
        wl.acquire()
        //释放
        wl.release()
    }

    @SuppressLint("InvalidWakeLockTag")
    fun unlock(context: Context) {
        //屏锁管理器
        val km = context.getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
        val kl = km.newKeyguardLock("unLock")
        //解锁
        kl.disableKeyguard()
        //获取电源管理器对象
        val pm = context.getSystemService(Context.POWER_SERVICE) as PowerManager
        //获取PowerManager.WakeLock对象,后面的参数|表示同时传入两个值,最后的是LogCat里用的Tag
        val wl = pm.newWakeLock(
            PowerManager.ACQUIRE_CAUSES_WAKEUP or PowerManager.SCREEN_DIM_WAKE_LOCK,
            "bright"
        )
        //锁屏
        kl.reenableKeyguard()
        //释放
        wl.setReferenceCounted(false)
        wl.release()
    }
}

