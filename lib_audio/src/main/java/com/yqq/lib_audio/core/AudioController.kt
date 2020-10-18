package com.yqq.lib_audio.core

import com.yqq.lib_audio.db.GreenDaoHelper
import com.yqq.lib_audio.event.AudioFavouriteEvent
import com.yqq.lib_audio.exception.AudioQueueEmptyException
import com.yqq.lib_audio.model.AudioBean
import org.greenrobot.eventbus.EventBus
import java.util.*
import kotlin.collections.ArrayList

class AudioController private constructor() {
    enum class PlayMode {
        // 列表循环
        LOOP,
        // 随机
        RANDOM,
        // 单曲循环
        REPEAT,
    }

    var mQueue: ArrayList<AudioBean> = ArrayList()
        set(value) = setQueue(value, 0)
    var mPlayMode: PlayMode = PlayMode.LOOP

    private var mQueueIndex: Int = 0
    private val mAudioPlayer: AudioPlayer = AudioPlayer()

    fun setQueue(list: ArrayList<AudioBean>) = setQueue(list, 0)

    fun setQueue(list: ArrayList<AudioBean>, index: Int) {
        mQueue.addAll(list)
        mQueueIndex = index
    }

    fun setPlayMode(mode: PlayMode) {
        mPlayMode = mode
    }


    fun addAudio(bean: AudioBean) = addAudio(0, bean)
    // 添加单一歌曲
    fun addAudio(index: Int, bean: AudioBean) {
        if (mQueue.isEmpty()) throw AudioQueueEmptyException("当前播放队列为空，请先设置播放队列")
        val query = queryAudio(bean)
        if (query <= -1) {
            // 没有添加过
            addCustomAudio(index, bean)
            setPlayIndex(index)
        } else {
            val audioBean = getNowPlaying()
            if (audioBean.id != bean.id) {
                // 已经添加且播放中
                setPlayIndex(query)
            }
        }
    }

    private fun addCustomAudio(index: Int, bean: AudioBean) {

    }

    private fun queryAudio(bean: AudioBean): Int {
        return 0
    }

    fun setPlayIndex(index: Int) {
        mQueueIndex = index
        play()
    }

    fun play() {
        mAudioPlayer.load(getNowPlaying())
    }

    fun pause() {
        mAudioPlayer.pause()
    }

    fun resume() {
        mAudioPlayer.resume()
    }

    fun release() {
        mAudioPlayer.resume()
        EventBus.getDefault().unregister(this)
    }

    fun next() {
        mAudioPlayer.load(getNextPlaying())
    }


    fun previous() {
        mAudioPlayer.load(getPreviousPlaying())
    }

    fun playOrPause() {
        if (isStartState()) pause()
        else if (isPauseState()) resume()
    }


    fun isStartState() = CustomMediaPlayer.Status.STARTED == getStatus()

    fun isPauseState() = CustomMediaPlayer.Status.PAUSED == getStatus()

    fun changeFavourite() {
        if (GreenDaoHelper.selectFavourite(getNowPlaying()) != null) {
            // 取消收藏
            GreenDaoHelper.removeFavourite(getNowPlaying())
            EventBus.getDefault().post(AudioFavouriteEvent(false))
        } else {
            GreenDaoHelper.addFavourite(getNowPlaying())
            EventBus.getDefault().post(AudioFavouriteEvent(true))

        }
    }

    private fun getStatus() = mAudioPlayer.getStatus()


    fun getNowPlaying(): AudioBean {
        return getPlaying()
    }

    private fun getNextPlaying(): AudioBean {
        when (mPlayMode) {
            PlayMode.LOOP -> {
                mQueueIndex = (mQueueIndex + 1) % mQueue.size
            }
            PlayMode.RANDOM -> {
                mQueueIndex = Random().nextInt(mQueue.size) % mQueue.size
            }
            PlayMode.REPEAT -> {
            }
        }

        return getPlaying()
    }

    private fun getPreviousPlaying(): AudioBean {
        when (mPlayMode) {
            PlayMode.LOOP -> {
                mQueueIndex = (mQueueIndex - 1 + mQueue.size) % mQueue.size
            }
            PlayMode.RANDOM -> {
                mQueueIndex = Random().nextInt(mQueue.size) % mQueue.size
            }
            PlayMode.REPEAT -> {
            }
        }

        return getPlaying()
    }

    private fun getPlaying(): AudioBean {
        if (mQueue.isEmpty() && mQueueIndex < 0) {
            throw AudioQueueEmptyException("当前播放队列为空，请先设置播放队列")
        }
        return mQueue[mQueueIndex]
    }

    companion object {
        @JvmStatic
        var instance = SingletonHolder.holder

        @JvmStatic
        fun instance() = instance

    }

    private object SingletonHolder {
        val holder: AudioController = AudioController()
    }
}