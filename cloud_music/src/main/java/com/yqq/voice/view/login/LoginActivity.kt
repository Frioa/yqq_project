package com.yqq.voice.view.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.yqq.lib_commin_ui.base.BaseActivity
import com.yqq.lib_network.okhttp.listener.DisposeDataListener
import com.yqq.voice.R
import com.yqq.voice.api.RequestCenter
import com.yqq.voice.enent.LoginEvent
import com.yqq.voice.model.user.User
import com.yqq.voice.view.login.manager.UserManager
import kotlinx.android.synthetic.main.activity_login_layout.*
import org.greenrobot.eventbus.EventBus

/**
 * 登录页面
 */
class LoginActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_layout)

        initView()
    }

    private fun initView() {
        login_view.setOnClickListener {
            RequestCenter.login(object :DisposeDataListener{
                override fun onSuccess(responseObj: Any?) {
                    val user = responseObj as User
                    UserManager.instance.saveUser(user)
                    EventBus.getDefault().post(LoginEvent())
                    finish()
                }

                override fun onFailure(reasonObj: Any?) {
                    Toast.makeText(applicationContext,"登录失败", Toast.LENGTH_LONG).show()
                }
            })
        }
    }

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, LoginActivity::class.java)
            context.startActivity(intent)
        }
    }
}