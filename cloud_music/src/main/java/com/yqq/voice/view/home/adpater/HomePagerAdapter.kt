package com.yqq.voice.view.home.adpater

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.yqq.voice.view.VideoFragment
import com.yqq.voice.model.CHANNEL
import com.yqq.voice.view.home.friend.FriendFragment
//import com.yqq.voice.view.home.discory.DiscoryFragment
//import com.yqq.voice.view.home.friend.FriendFragment
import com.yqq.voice.view.mine.MineFragment

class HomePagerAdapter(
    fm: FragmentManager,
    private val mList: Array<CHANNEL>
) : FragmentPagerAdapter(fm) {
    //这种方式，避免一次性创建所有的framgent
    override fun getItem(position: Int): Fragment? {
        val type = mList[position].value

        when (type) {
            CHANNEL.MINE_ID -> return MineFragment.newInstance()
//            CHANNEL.DISCORY_ID -> return DiscoryFragment.newInstance()
            CHANNEL.FRIEND_ID -> return FriendFragment.newInstance()
            CHANNEL.VIDEO_ID -> return VideoFragment.newInstance()
        }
        return null
    }

    override fun getCount(): Int {
        return mList.size ?: 0
    }

}