package com.yqq.lib_audio.app

import android.app.Activity
import android.content.Context
import android.util.Log
import com.yqq.lib_audio.core.AudioController
import com.yqq.lib_audio.core.MusicService
import com.yqq.lib_audio.db.GreenDaoHelper
import com.yqq.lib_audio.model.AudioBean
import com.yqq.lib_audio.view.MusicPlayerActivity


/**
 * 与外界唯一通信的类
 */
object AudioHelper {
    private var mContext: Context? = null
    fun init(context: Context?) {
        mContext = context
        GreenDaoHelper.initDatabase()
    }

    //外部启动MusicService方法
    fun startMusicService(audios: ArrayList<AudioBean>) {
        Log.d("AudioHelper", "startMusicService()")
        MusicService.startMusicService(audios)
    }

    @JvmStatic
    fun getContext(): Context {
        return mContext!!
    }

    @JvmStatic
    fun addAudio(activity: Activity, bean: AudioBean) {
        AudioController.instance.addAudio(bean)
        MusicPlayerActivity.start(activity)
    }
}