package com.yqq.voice.view.home

import android.content.Context
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.View
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        EventBus.getDefault().register(this)
        setContentView(R.layout.activity_home)
        initView()
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
                simplePagerTitleView.selectedColor =  resources.getColor(R.color.color_333333)

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
        val CHANNELS: Array<CHANNEL> = arrayOf(CHANNEL.MY, CHANNEL.VIDEO)
    }
}