package com.yqq.lib_commin_ui.base

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.yqq.lib_commin_ui.base.constant.Constant
import com.yqq.lib_commin_ui.utils.StatusBarUtil


open class BaseActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 状态栏变为黑色
        StatusBarUtil.statusBarLightMode(this)
    }

    /**
     * 申请指定的权限.
     */
    open fun requestPermission(code: Int, permissions: Array<String>) {
        Log.d(BaseActivity::javaClass.name, "permissions ${permissions.asList()}")
        ActivityCompat.requestPermissions(this, permissions, code)
    }

    /**
     * 判断是否有指定的权限
     */
    open fun hasPermission(permissions: Array<String>): Boolean {
        for (permisson in permissions) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    permisson
                ) != PackageManager.PERMISSION_GRANTED
            ) return false
        }
        return true
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            Constant.WRITE_READ_EXTERNAL_CODE -> if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                doSDCardPermission()
            }
            Constant.HARDWEAR_CAMERA_CODE -> if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                doCameraPermission()
            }
        }
    }

    /**
     * 处理整个应用用中的SDCard业务
     */
    open fun doSDCardPermission() {}

    open fun doCameraPermission() {}
}