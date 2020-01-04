package com.zhuyu.aida64

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.util.AttributeSet
import android.webkit.WebView


class MyWebview : WebView {

    constructor(context: Context) : super(getFixedContext(context)) {}

    constructor(
        context: Context, attrs: AttributeSet
    ) : super(
        getFixedContext(context),
        attrs
    )

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        getFixedContext(context),
        attrs,
        defStyleAttr
    )
    companion object {

        fun getFixedContext(context: Context): Context {
            return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                context.createConfigurationContext(Configuration())
            } else {
                context
            }
        }
    }

}