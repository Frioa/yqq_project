package com.yqq.voice.view.load

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import com.yqq.lib_commin_ui.base.BaseActivity
import com.yqq.lib_commin_ui.base.constant.Constant
import com.yqq.lib_pullalive.app.AliveJobService
import com.yqq.voice.R
import com.yqq.voice.view.home.HomeActivity


class LoadingActivity : BaseActivity() {
    private val mHandler: Handler = @SuppressLint("HandlerLeak")
    object : Handler() {
        override fun handleMessage(msg: Message?) {
            startActivity(Intent(this@LoadingActivity, HomeActivity::class.java))
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        avoidLauncherAgain()
        setContentView(R.layout.activity_loading_layout)
        pullAliveService()
        if (hasPermission(Constant.WRITE_READ_EXTERNAL_PERMISSION)) {
            doSDCardPermission()
        } else {
            doSDCardPermission()
        /*    requestPermission(
                Constant.WRITE_READ_EXTERNAL_CODE,
                Constant.WRITE_READ_EXTERNAL_PERMISSION
            )*/
        }
    }

    private fun avoidLauncherAgain() { // 避免从桌面启动程序后，会重新实例化入口类的activity
        if (!this.isTaskRoot) { // 判断当前activity是不是所在任务栈的根
            val intent = intent
            if (intent != null) {
                val action = intent.action
                if (intent.hasCategory(Intent.CATEGORY_LAUNCHER) && Intent.ACTION_MAIN == action) {
                    finish()
                }
            }
        }
    }

    private fun pullAliveService() {
        AliveJobService.start(this)
    }

    override fun doSDCardPermission() {
        Log.d(LoadingActivity::javaClass.name, "doSDCardPermission()")
        mHandler.sendEmptyMessageDelayed(0, 3000)
    }

    override fun onDestroy() {
        super.onDestroy()
        mHandler.removeCallbacksAndMessages(null)
    }
}
