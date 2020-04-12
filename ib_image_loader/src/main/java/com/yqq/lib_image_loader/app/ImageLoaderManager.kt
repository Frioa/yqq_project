package com.yqq.lib_image_loader.app

import android.app.Notification
import android.content.Context
import android.graphics.Bitmap
import android.widget.ImageView
import android.widget.RemoteViews
import androidx.core.graphics.drawable.RoundedBitmapDrawable
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions.withCrossFade
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.bumptech.glide.request.target.NotificationTarget
import com.yqq.lib_image_loader.R
import com.yqq.lib_image_loader.image.CustomRequestListener
import com.bumptech.glide.request.target.Target as mTarget


/**
 * 图片加载类，与外界唯一通信类，支持为各种 view ，notification， appwidget， viewgroup加载图片
 */
class ImageLoaderManager private constructor() {
    /**
     * 为 ImageView 加载图片
     */
    fun displayImageForView(imageView: ImageView, url: String) =
        Glide.with(imageView.context)
            .asBitmap()
            .load(url)
            .apply(initCommonRequestOption())
            .transition(withCrossFade())
            .into(imageView)

    /**
     * 为 ImageView 加载圆形图片
     */
    fun displayImageForCircle(imageView: ImageView, url: String) =
        Glide.with(imageView.context)
            .asBitmap()
            .load(url)
            .apply(initCommonRequestOption())
            .into(object : BitmapImageViewTarget(imageView) {
                // 将 ImageView 包装 target
                override fun setResource(resource: Bitmap?) {
                    val circularBitmapDrawable: RoundedBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(imageView.resources, resource)
                    circularBitmapDrawable.isCircular = true
                    imageView.setImageDrawable(circularBitmapDrawable)
                }
            })


    /**
     * 为notification加载图
     */
    fun displayImageForNotification(
        context: Context, rv: RemoteViews, id: Int,
        notification: Notification, notificationId: Int, url: String
    ) {
        this.displayImageForTarget(
            context,
            initNotificationTarget(context, id, rv, notification, notificationId),
            url
        )
    }

    /**
     * 为非view加载图片
     */
    private fun displayImageForTarget(context: Context, target: mTarget<Bitmap>, url: String) =
        displayImageForTarget(context, target, url, null)


    /**
     * 为非view加载图片
     */
    private fun displayImageForTarget(
        context: Context, target: mTarget<Bitmap>, url: String,
        requestListener: CustomRequestListener<Bitmap>?
    ) {
        Glide.with(context)
            .asBitmap()
            .load(url)
            .apply(initCommonRequestOption())
            .transition(withCrossFade())
            .fitCenter()
            .listener(requestListener)
            .into(target)
    }

    /*
     * 初始化Notification Target
     */
    private fun initNotificationTarget(
        context: Context, id: Int, rv: RemoteViews,
        notification: Notification, NOTIFICATION_ID: Int
    ): NotificationTarget {
        return NotificationTarget(context, id, rv, notification, NOTIFICATION_ID)
    }


    private fun initCommonRequestOption(): RequestOptions {
        return RequestOptions()
            .placeholder(R.mipmap.b4y) // 展位图
            .error(R.mipmap.b4y) // 错误图
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC) // 缓存策略
            .skipMemoryCache(true) // 启动内存缓存
            .priority(Priority.NORMAL) // 线程优先级
    }

    companion object {
        val instance = SingletonHolder.holder
    }

    private object SingletonHolder {
        val holder: ImageLoaderManager = ImageLoaderManager()
    }
}