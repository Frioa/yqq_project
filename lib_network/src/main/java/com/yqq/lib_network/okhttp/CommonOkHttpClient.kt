package com.yqq.lib_network.okhttp

import com.yqq.lib_network.okhttp.listener.DisposeDataHandle
import com.yqq.lib_network.okhttp.response.CommonFileCallback
import com.yqq.lib_network.okhttp.response.CommonJsonCallback
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.concurrent.TimeUnit

private class CommonOkHttpClient {
    companion object {
        private const val TIME_OUT = 30L

        private val okHttpClient =
            OkHttpClient.Builder().hostnameVerifier { hostname, session ->
                // 默认信任域名
                true
            }.addInterceptor { chain ->
                val request =
                    chain.request().newBuilder().addHeader("User-Agent", "Imooc-Mobile").build()
                chain.proceed(request)
            }.connectTimeout(TIME_OUT, TimeUnit.SECONDS).readTimeout(TIME_OUT, TimeUnit.SECONDS)
                .writeTimeout(TIME_OUT, TimeUnit.SECONDS).followSslRedirects(true).build()

        fun get(request: Request, handler: DisposeDataHandle): Call {
            val call = okHttpClient.newCall(request)
            call.enqueue(CommonJsonCallback(handler))
            return call
        }

        fun post(request: Request, handler: DisposeDataHandle): Call {
            val call = okHttpClient.newCall(request)
            call.enqueue(CommonJsonCallback(handler))
            return call
        }

        fun downloadFile(request: Request, handler: DisposeDataHandle): Call {
            val call = okHttpClient.newCall(request)
            call.enqueue(CommonFileCallback(handler))
            return call
        }
    }
}
