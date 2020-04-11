package com.yqq.voice.view.home

import android.content.Context
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import com.yqq.lib_commin_ui.ScaleTransitionPagerTitleView
import com.yqq.lib_commin_ui.base.BaseActivity
import com.yqq.voice.R
import com.yqq.voice.model.CHANNEL
import com.yqq.voice.view.home.adpater.HomePagerAdapter
import kotlinx.android.synthetic.main.activity_home.*
import net.lucode.hackware.magicindicator.MagicIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView

class HomeActivity : BaseActivity() {

    private lateinit var homePagerAdapter: HomePagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        initView()
    }

    private fun initView() {
        homePagerAdapter = HomePagerAdapter(supportFragmentManager, CHANNELS)
        Log.d("homePagerAdapter","${homePagerAdapter.getItem(0)}")

        view_pager.adapter = homePagerAdapter
        // initMagicIndicator()
    }

    // 初始化指示器
    private fun initMagicIndicator() {
        val magicIndicator = MagicIndicator(this)
        magicIndicator.background = ColorDrawable(resources.getColor(R.color.color_white))
        val commonNavigator = CommonNavigator(this)
        commonNavigator.adapter = object : CommonNavigatorAdapter() {
            override fun getTitleView(context: Context, index: Int): IPagerTitleView {
                val simplePagerTitleView = ScaleTransitionPagerTitleView(context)
                simplePagerTitleView.text = CHANNELS[index].key
                simplePagerTitleView.textSize = 19.0f
                simplePagerTitleView.typeface = Typeface.defaultFromStyle(Typeface.BOLD)
                simplePagerTitleView.normalColor = resources.getColor(R.color.color_selected)
                simplePagerTitleView.setOnClickListener {
                    view_pager.currentItem = index
                }
                return simplePagerTitleView
            }

            override fun getCount(): Int = CHANNELS.size

            override fun getIndicator(context: Context?): IPagerIndicator? {
                return null
            }

            override fun getTitleWeight(context: Context?, index: Int): Float {
                return 1.0f
            }

        }
        magicIndicator.navigator = commonNavigator
//        ViewPagerHelper.bind(magicIndicator, view_pager)
    }

    companion object {
        // 指定首页要出现的卡片
        val CHANNELS: Array<CHANNEL> = arrayOf(CHANNEL.MY, CHANNEL.VIDEO)
    }
}