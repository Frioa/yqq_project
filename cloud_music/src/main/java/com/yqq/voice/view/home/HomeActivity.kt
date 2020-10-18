package com.yqq.voice.view.home

import android.content.Context
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.View
import com.yqq.lib_audio.app.AudioHelper
import com.yqq.lib_audio.core.AudioController
import com.yqq.lib_audio.model.AudioBean
import com.yqq.lib_commin_ui.base.BaseActivity
import com.yqq.lib_commin_ui.pager_indictor.ScaleTransitionPagerTitleView
import com.yqq.lib_image_loader.app.ImageLoaderManager
import com.yqq.voice.R
import com.yqq.voice.enent.LoginEvent
import com.yqq.voice.model.CHANNEL
import com.yqq.voice.view.home.adpater.HomePagerAdapter
import com.yqq.voice.view.login.LoginActivity
import com.yqq.voice.view.login.manager.UserManager
import kotlinx.android.synthetic.main.activity_home.*
import net.lucode.hackware.magicindicator.ViewPagerHelper
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


class HomeActivity : BaseActivity() {

    private lateinit var homePagerAdapter: HomePagerAdapter
    private val mLists = ArrayList<AudioBean>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        EventBus.getDefault().register(this)
        setContentView(R.layout.activity_home)
        initView()
        initData()
    }

    private fun initData() {
        mLists.add(
            AudioBean(
                "100001", "http://sp-sycdn.kuwo.cn/resource/n2/85/58/433900159.mp3",
                "以你的名字喊我", "周杰伦", "七里香", "电影《不能说的秘密》主题曲,尤其以最美的不是下雨天,是与你一起躲过雨的屋檐最为经典",
                "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1559698076304&di=e6e99aa943b72ef57b97f0be3e0d2446&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fblog%2F201401%2F04%2F20140104170315_XdG38.jpeg",
                "4:30"
            )
        )
        mLists.add(
            AudioBean(
                "100002", "http://sq-sycdn.kuwo.cn/resource/n1/98/51/3777061809.mp3", "勇气",
                "梁静茹", "勇气", "电影《不能说的秘密》主题曲,尤其以最美的不是下雨天,是与你一起躲过雨的屋檐最为经典",
                "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1559698193627&di=711751f16fefddbf4cbf71da7d8e6d66&imgtype=jpg&src=http%3A%2F%2Fimg0.imgtn.bdimg.com%2Fit%2Fu%3D213168965%2C1040740194%26fm%3D214%26gp%3D0.jpg",
                "4:40"
            )
        )
        mLists.add(
            AudioBean(
                "100003", "http://sp-sycdn.kuwo.cn/resource/n2/52/80/2933081485.mp3", "灿烂如你",
                "汪峰", "春天里", "电影《不能说的秘密》主题曲,尤其以最美的不是下雨天,是与你一起躲过雨的屋檐最为经典",
                "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1559698239736&di=3433a1d95c589e31a36dd7b4c176d13a&imgtype=0&src=http%3A%2F%2Fpic.zdface.com%2Fupload%2F201051814737725.jpg",
                "3:20"
            )
        )
        mLists.add(
            AudioBean(
                "100004", "http://sr-sycdn.kuwo.cn/resource/n2/33/25/2629654819.mp3", "小情歌",
                "五月天", "小幸运", "电影《不能说的秘密》主题曲,尤其以最美的不是下雨天,是与你一起躲过雨的屋檐最为经典",
                "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1559698289780&di=5146d48002250bf38acfb4c9b4bb6e4e&imgtype=0&src=http%3A%2F%2Fpic.baike.soso.com%2Fp%2F20131220%2Fbki-20131220170401-1254350944.jpg",
                "2:45"
            )
        )
        AudioController.instance.setQueue(mLists)
        AudioHelper.startMusicService(mLists);
    }

    private fun initView() {
        homePagerAdapter = HomePagerAdapter(supportFragmentManager, CHANNELS)
        view_pager.adapter = homePagerAdapter

        initMagicIndicator()
        unloggin_layout.setOnClickListener {
            if (!UserManager.instance.hasLogin()) {
                LoginActivity.start(this)
            } else {
                drawer_layout.closeDrawer(Gravity.LEFT)
            }
        }
    }

    // 初始化指示器
    private fun initMagicIndicator() {
        magic_indicator.background = ColorDrawable(resources.getColor(R.color.color_white))
        val commonNavigator = CommonNavigator(this)
        commonNavigator.isAdjustMode = true
        commonNavigator.adapter = object : CommonNavigatorAdapter() {
            override fun getTitleView(context: Context, index: Int): IPagerTitleView {
                val simplePagerTitleView = ScaleTransitionPagerTitleView(context)
                simplePagerTitleView.text = CHANNELS[index].key
                simplePagerTitleView.textSize = 19.0f
                simplePagerTitleView.typeface = Typeface.defaultFromStyle(Typeface.BOLD)
                simplePagerTitleView.normalColor = resources.getColor(R.color.color_999999)
                simplePagerTitleView.selectedColor = resources.getColor(R.color.color_333333)

                simplePagerTitleView.setOnClickListener {
                    view_pager.currentItem = index
                }
                return simplePagerTitleView
            }

            override fun getCount(): Int = CHANNELS?.size ?: 0

            override fun getIndicator(context: Context?): IPagerIndicator? {
                return null
            }

            override fun getTitleWeight(context: Context?, index: Int) = 1.0f
        }
        magic_indicator.navigator = commonNavigator
        ViewPagerHelper.bind(magic_indicator, view_pager)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onLoginEvent(event: LoginEvent) {
        unloggin_layout.visibility = View.GONE
        photo_view.visibility = View.VISIBLE
        ImageLoaderManager.getInstance()
            .displayImageForCircle(photo_view, UserManager.instance.getUser()!!.data!!.photoUrl)
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    companion object {
        // 指定首页要出现的卡片
        val CHANNELS: Array<CHANNEL> = arrayOf(CHANNEL.MY, CHANNEL.FRIEND, CHANNEL.VIDEO)
    }
}