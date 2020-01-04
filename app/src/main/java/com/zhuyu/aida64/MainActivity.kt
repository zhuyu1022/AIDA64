package com.zhuyu.aida64

import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.view.View
import android.view.WindowManager
import android.webkit.WebChromeClient
import android.webkit.WebView
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.PhoneUtils
import com.blankj.utilcode.util.SPStaticUtils
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.runOnUiThread
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.toast

class MainActivity : AppCompatActivity() {

    var mUrl = "http://192.168.0.106:8888"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
        initWebView()
        checkAndTurnOnDeviceManager()
    }

    fun initView() {
        // 设置界面常亮
        window.setFlags(
            WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
            WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
        )
        if (SPStaticUtils.getString("url").isNotEmpty()) {
            mUrl = SPStaticUtils.getString("url")
        }

        urlTv.setText(mUrl)
        goBtn.onClick {
            mUrl = urlTv.text.toString()
            SPStaticUtils.put("url", mUrl)
            if (!TextUtils.isEmpty(mUrl)) {
                webView.loadUrl(mUrl)
            }

        }


        startService(Intent(this,MyIntentService::class.java))



    }

    fun initWebView() {
        val settings = webView.settings
        settings.javaScriptEnabled = true
        settings.useWideViewPort = true
        settings.loadWithOverviewMode = true
        webView.webChromeClient = webChromeClient
        if (!TextUtils.isEmpty(mUrl)) {
            webView.loadUrl(mUrl)
        }

    }


    /**
     * 检测并去激活设备管理器权限
     */
    fun checkAndTurnOnDeviceManager() {
        val intent = Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN)
        intent.putExtra(
            DevicePolicyManager.EXTRA_DEVICE_ADMIN,
            ComponentName(this, ScreenOffAdminReceiver::class.java)
        )
        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "开启后就可以使用锁屏功能了...")
        startActivityForResult(intent, 0)
    }


    private val webChromeClient = object : WebChromeClient() {
        override fun onProgressChanged(view: WebView, newProgress: Int) {
            super.onProgressChanged(view, newProgress)
            if (newProgress == 100) {
                progressBar.visibility = View.GONE
                topLayout.visibility = View.GONE
            } else {
                progressBar.progress = newProgress
            }
        }
    }


    override fun onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack()
            return
        }
        if (topLayout.visibility == View.GONE) {
            topLayout.visibility = View.VISIBLE
            return
        }
        super.onBackPressed()
    }


}
