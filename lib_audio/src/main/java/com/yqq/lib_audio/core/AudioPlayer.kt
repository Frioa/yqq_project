package com.yqq.lib_audio.core

import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.wifi.WifiManager
import android.os.Handler
import android.os.Looper
import android.os.PowerManager
import android.util.Log
import android.widget.Toast
import com.yqq.lib_audio.app.AudioHelper
import com.yqq.lib_audio.event.*
import com.yqq.lib_audio.model.AudioBean
import org.greenrobot.eventbus.EventBus

/**
 * 1. 播放音频
 * 2. 对外发送各种事件
 */
class AudioPlayer() : MediaPlayer.OnCompletionListener, MediaPlayer.OnBufferingUpdateListener,
    MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener,
    AudioFocusManager.AudioFocusListener {

    // 负责音频的播放
    private lateinit var customMediaPlayer: CustomMediaPlayer
    // 保活能力
    private var mWifiLock: WifiManager.WifiLock? = null
    // 章频焦点监听器
    private var mAudioFocusManager: AudioFocusManager? = null
    private var mMediaPlayer: CustomMediaPlayer? = null
    private var isPauseByFocusLossTransient = false

    val mHandler = Handler(Looper.getMainLooper()) {
        when (it.what) {
//            TIME_MSG ->

        }
    }

    init {
        init()
    }

    private fun init() {
        mMediaPlayer = CustomMediaPlayer()
        mMediaPlayer!!.let {
            it.setWakeMode(AudioHelper.getContext(), PowerManager.PARTIAL_WAKE_LOCK)
            it.setAudioStreamType(AudioManager.STREAM_MUSIC)
            it.setOnCompletionListener(this)
            it.setOnBufferingUpdateListener(this)
            it.setOnPreparedListener(this)
            it.setOnErrorListener(this)
        }

        // 初始化 mWifiLock
        mWifiLock =
            (AudioHelper.getContext().applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager).createWifiLock(
                WifiManager.WIFI_MODE_FULL,
                TAG
            )
        mAudioFocusManager = AudioFocusManager(AudioHelper.getContext(), this)
    }

    /**
     * 对外提供的加载
     */
    fun load(audioBean: AudioBean) {
        try {
            mMediaPlayer?.let {
                it.reset() // 清空上一次加载数据
                it.setDataSource(audioBean.mUrl)
                it.prepareAsync() // 异步准备，完成回调 onPrepare()
            } ?: return
            // 对外发送 load 事件
            EventBus.getDefault().post(AudioLoadEvent(audioBean))
        } catch (e: Exception) {
            Log.e(TAG, "Exception", e)
            EventBus.getDefault().post(AudioErrorEvent())
        }
    }


    private fun start() {
        if (!mAudioFocusManager!!.requestAudioFocus()) {
            Log.e(TAG, "获取音频焦点失败")
            Toast.makeText(AudioHelper.getContext(), "获取音频焦点失败", Toast.LENGTH_LONG).show()
        }
        mMediaPlayer!!.start()
        mWifiLock!!.acquire()
        // 对外发送 start 事件
        EventBus.getDefault().post(AudioStartEvent())
    }

    fun pause() {
        if (getStatus() == CustomMediaPlayer.Status.STARTED) {
            mMediaPlayer?.pause() ?: return
            // 释放 wifilock
            if (mWifiLock!!.isHeld) {
                mWifiLock!!.release()
            }
            // 释放音频焦点
            mAudioFocusManager!!.abandonAudioFocus()
            // 发送暂停事件
            EventBus.getDefault().post(AudioPauseEvent())
        }
    }


    /**
     * 对外提供恢复
     */
    fun resume() {
        if (getStatus() == CustomMediaPlayer.Status.PAUSED) {
            start()
        }
    }

    /**
     * 清空播放器占用资源
     */
    fun release() {
        mMediaPlayer?.release() ?: return
        mMediaPlayer = null
        mAudioFocusManager?.abandonAudioFocus()
        mWifiLock?.release()
        mWifiLock = null
        mAudioFocusManager = null

        // 发送 release 销毁事件
        EventBus.getDefault().post(AudioReleaseEvent())

    }


    /**
     * 获取当前音乐总时长,更新进度用
     */
    fun getDuration(): Int {
        return if (getStatus() === CustomMediaPlayer.Status.STARTED
            || getStatus() === CustomMediaPlayer.Status.PAUSED
        ) mMediaPlayer?.duration ?: 0
        else 0
    }

    fun getCurrentPosition(): Int {
        return if (getStatus() === CustomMediaPlayer.Status.STARTED
            || getStatus() === CustomMediaPlayer.Status.PAUSED
        ) mMediaPlayer?.currentPosition ?: 0 else 0
    }

//    fun getStatus() = mMediaPlayer?.mStatus
    /**
     * 系统通知当前缓存了多少进度
     */
    override fun onBufferingUpdate(mp: MediaPlayer?, percent: Int) {
        // 缓存进度的回调
    }

    // 播放完毕回调
    override fun onCompletion(mp: MediaPlayer?) {
        EventBus.getDefault().post(AudioCompleteEvent())
    }

    // 准备完毕
    override fun onPrepared(mp: MediaPlayer?) = start()


    override fun onError(mp: MediaPlayer?, what: Int, extra: Int): Boolean {
        // 自行处理异常，不在回调 onCompletion
        EventBus.getDefault().post(AudioErrorEvent())
        return true
    }

    // 再次获得音频焦点
    override fun audioFocusGrant() {
        setVolumn(1.0f, 1.0f)
        if (isPauseByFocusLossTransient) {
            resume()
        }
        isPauseByFocusLossTransient = false
    }

    // 短暂性失去焦点（电话）
    override fun audioFocusLossTransient() {
        pause()
        isPauseByFocusLossTransient = true
    }

    // 瞬间失去焦点（短信到来）
    override fun audioFocusLossDuck() {
        setVolumn(0.41f, 0.41f)
    }

    // 永久失去焦点
    override fun audioFocusLoss() {
        pause()
    }


    fun getStatus(): CustomMediaPlayer.Status {
        return mMediaPlayer?.mStatus ?: CustomMediaPlayer.Status.STOPPTED
    }

    private fun setVolumn(leftVol: Float, rightVol: Float) {
        mMediaPlayer?.setVolume(leftVol, rightVol)
    }


    companion object {
        const val TAG = "AudioPlayer"
        const val TIME_MSG = 0X01
        const val TIME_INVAL = 100
    }
}