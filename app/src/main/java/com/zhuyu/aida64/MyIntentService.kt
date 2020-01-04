package com.zhuyu.aida64

import android.app.IntentService
import android.content.Intent
import com.blankj.utilcode.util.ScreenUtils
import org.jetbrains.anko.runOnUiThread
import org.jetbrains.anko.toast


class MyIntentService : IntentService("MyIntentService") {

    /**线程执行一次休眠的时间  10秒     */
    val threadTime = 10000L

    /**手机进入休眠状态的时间   10分钟**/
    val sleepTime = 0.5 * 60 * 1000

    override fun onHandleIntent(intent: Intent?) {
        var noChargeTime = 0L
        Thread(Runnable {
            while (true) {
                if (PowerUtil.isCharging(this)) {
                    if (noChargeTime > sleepTime) {
                        noChargeTime = 0L
                        PowerUtil.checkScreenOn(this)
                    }

                } else {

                    if (noChargeTime > sleepTime) {
                        PowerUtil.checkScreenOff(this)
                        runOnUiThread { toast("进入休眠状态")}
                    }else{
                        noChargeTime += threadTime
                    }

                }
                Thread.sleep(threadTime)

            }
        }).start()


    }


}
