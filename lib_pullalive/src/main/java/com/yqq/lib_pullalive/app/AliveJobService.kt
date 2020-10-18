package com.yqq.lib_pullalive.app

import android.annotation.TargetApi
import android.app.Service
import android.app.job.JobInfo
import android.app.job.JobParameters
import android.app.job.JobScheduler
import android.app.job.JobService
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log

/**
 * 一个轻量级后台 Job service，利用空闲时间执行一些小事情，提高进程利用率
 */
@TargetApi(value = Build.VERSION_CODES.LOLLIPOP)
public class AliveJobService : JobService() {

    private lateinit var mJobScheduler: JobScheduler
    private val mHandler = Handler(Looper.getMainLooper() ) {
        when (it.what) {
            PULL_ALIVE -> {
                Log.d(TAG, "pull alive")
                jobFinished(it.obj as JobParameters, true)
                true
            }
            0x02 -> {
                true
            }
            else -> {
                true
            }
        }
    }

    override fun onCreate() {
        // getSystemService 也是单例的写法 ，系统将各种单例放入 一个 Map 中通过Key获取
        mJobScheduler = getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
        Log.e(TAG, "onCreate()")
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val job = initJobInfo(startId)
        // 提交自己的 job 到 system process 中
        if (mJobScheduler.schedule(job) <= 0) {
            Log.e(TAG, "AliveJobService failed")
        } else {
            Log.d(TAG, "AliveJobService success")
        }
        return Service.START_STICKY //系统会在何时的时间重启
    }

    // 初始化 JobInfo
    private fun initJobInfo(startId: Int): JobInfo {
        val builder =
            JobInfo.Builder(startId, ComponentName(packageName, AliveJobService::javaClass.name))
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setRequiresBatteryNotLow(false)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            builder.setMinimumLatency(JobInfo.DEFAULT_INITIAL_BACKOFF_MILLIS)
                .setOverrideDeadline(JobInfo.DEFAULT_INITIAL_BACKOFF_MILLIS)
                .setBackoffCriteria(JobInfo.DEFAULT_INITIAL_BACKOFF_MILLIS, JobInfo.BACKOFF_POLICY_LINEAR) // 线性重试方案
        } else {

            builder.setPeriodic(JobInfo.DEFAULT_INITIAL_BACKOFF_MILLIS)
        }
        builder.setPersisted(false) // 是否持久化
        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_NONE) // 不需要网络
        builder.setRequiresCharging(false) // 充电执行

        return builder.build()
    }

    override fun onStopJob(params: JobParameters?): Boolean {
        mHandler.sendMessage(Message.obtain( mHandler, PULL_ALIVE, params))
        return true
    }

    override fun onStartJob(params: JobParameters?): Boolean {
        mHandler.removeCallbacksAndMessages(null)
        return true

    }

    companion object {
        final val TAG = AliveJobService::javaClass.name
        final val PULL_ALIVE = 0x01


        fun start(context: Context) {
            val intent = Intent(context, AliveJobService::javaClass.javaClass)
            context.startService(intent)
        }
    }
}