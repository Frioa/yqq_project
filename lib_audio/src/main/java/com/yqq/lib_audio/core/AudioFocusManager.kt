package com.yqq.lib_audio.core

import android.content.Context
import android.media.AudioManager
import android.media.AudioManager.OnAudioFocusChangeListener

class AudioFocusManager(
    context: Context,
    listener: AudioFocusListener?
) : OnAudioFocusChangeListener {
    private val mAudioFocusListener: AudioFocusListener? = listener
    private val audioManager: AudioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    fun requestAudioFocus(): Boolean {
        return audioManager.requestAudioFocus(
            this, AudioManager.STREAM_MUSIC,
            AudioManager.AUDIOFOCUS_GAIN
        ) == AudioManager.AUDIOFOCUS_REQUEST_GRANTED
    }

    fun abandonAudioFocus() {
        audioManager.abandonAudioFocus(this)
    }

    override fun onAudioFocusChange(focusChange: Int) {
        when (focusChange) {
            AudioManager.AUDIOFOCUS_GAIN -> mAudioFocusListener?.audioFocusGrant()
            AudioManager.AUDIOFOCUS_LOSS -> mAudioFocusListener?.audioFocusLoss()
            AudioManager.AUDIOFOCUS_LOSS_TRANSIENT -> mAudioFocusListener?.audioFocusLossTransient()
            AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK -> mAudioFocusListener?.audioFocusLossDuck()
        }
    }

    /**
     * 音频焦点改变,接口回调，
     */
    interface AudioFocusListener {
        //获得焦点回调处理
        fun audioFocusGrant()

        //永久失去焦点回调处理
        fun audioFocusLoss()

        //短暂失去焦点回调处理
        fun audioFocusLossTransient()

        //瞬间失去焦点回调
        fun audioFocusLossDuck()
    }

    companion object {
        private val TAG = AudioFocusManager::class.java.simpleName
    }

}