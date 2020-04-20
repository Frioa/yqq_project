package com.yqq.lib_audio.app

import android.app.Application

class LibAudioApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        mApplication = this
        AudioHelper.init(this)
    }

    companion object {
        private var mApplication: LibAudioApplication? = null

        @JvmStatic
        fun getInstance() = mApplication
    }
}