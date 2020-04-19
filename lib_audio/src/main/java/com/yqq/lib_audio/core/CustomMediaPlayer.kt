package com.yqq.lib_audio.core

import android.media.MediaPlayer


class CustomMediaPlayer : MediaPlayer(), MediaPlayer.OnCompletionListener {

    var mStatus: Status = Status.IDEL
    lateinit var mCompletionListener: OnCompletionListener

    init {
        super.setOnCompletionListener(this)
    }

    override fun reset() {
        super.reset()
        mStatus = Status.IDEL
    }

    override fun setDataSource(path: String?) {
        super.setDataSource(path)
        mStatus = Status.INITALIZED
    }

    override fun start() {
        super.start()
        mStatus = Status.STARTED

    }

    override fun pause() {
        super.pause()
        mStatus = Status.PAUSED

    }

    override fun stop() {
        super.stop()
        mStatus = Status.STOPPTED

    }

    override fun onCompletion(mp: MediaPlayer?) {
        mStatus = Status.COMPLETED
    }

    fun isComplete() = mStatus == Status.COMPLETED

    fun setCompleteListener(listener: OnCompletionListener) {
        mCompletionListener = listener
    }

    enum class Status {
        IDEL, INITALIZED, STARTED, PAUSED, STOPPTED, COMPLETED
    }
}

