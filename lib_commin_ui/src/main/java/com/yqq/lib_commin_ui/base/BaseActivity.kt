package com.yqq.lib_commin_ui.base

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import com.yqq.lib_commin_ui.utils.StatusBarUtil

open class BaseActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 状态栏变为黑色
        StatusBarUtil.statusBarLightMode(this)
    }
}