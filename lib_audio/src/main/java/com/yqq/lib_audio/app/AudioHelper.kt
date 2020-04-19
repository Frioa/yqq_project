package com.yqq.lib_audio.app

import android.content.Context

/**
 * 与外界唯一通信的类
 */
object AudioHelper {
    private var mContext: Context? = null
    fun init(context: Context?) {
        mContext = context
    }

    fun getmContext(): Context {
        return mContext!!
    }
}