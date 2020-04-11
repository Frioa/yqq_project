package com.yqq.lib_network.okhttp.request

import okhttp3.FormBody
import okhttp3.Headers
import okhttp3.Request

/**
 * 对外提供 get/post 文件上传请求
 */
class CommonRequest {
    companion object {
        /**
         * 对外创建 Post 请求对象
         * @param url
         * @param params
         * @return
         */
        fun createPostRequest(url: String, params: RequestParams?): Request =
            createPostRequest(url, params, null)


        /**
         * 对外创建 Post 请求对象
         * @param url
         * @param params
         * @param headers
         * @return
         */
        fun createPostRequest(
            url: String,
            params: RequestParams?,
            headers: RequestParams?
        ): Request {
            val mFormBodyBuilder = FormBody.Builder()
            val mHeaderBuilder = Headers.Builder();

            if (params != null) {

                for ((key, value) in params.urlParams.entries) {
                    // 参数遍历
                    mFormBodyBuilder.add(key, value)
                }
            }

            if (headers != null) {
                for ((key, value) in headers.urlParams.entries) {
                    mHeaderBuilder.add(key, value)
                }
            }
            return Request.Builder()
                .url(url)
                .headers(mHeaderBuilder.build())
                .post(mFormBodyBuilder.build()).build()
        }

        fun createGetRequest(url: String, params: RequestParams?): Request? =
            createGetRequest(url, params, null)


        /**
         * 可以带请求头的Get请求
         *
         * @param url
         * @param params
         * @param headers
         * @return
         */
        fun createGetRequest(
            url: String,
            params: RequestParams?,
            headers: RequestParams?
        ): Request? {
            // 遍历参数
            val urlBuilder = StringBuilder(url).append("?")
            if (params != null) {
                for ((key, value) in params.urlParams) {
                    urlBuilder.append(key).append("=").append(value).append("&")
                }
            }
            //添加请求头
            val mHeaderBuild = Headers.Builder()
            if (headers != null) {
                for ((key, value) in headers.urlParams) {
                    mHeaderBuild.add(key, value)
                }
            }

            return Request.Builder().url(urlBuilder.substring(0, urlBuilder.length - 1))
                .get()
                .headers(mHeaderBuild.build())
                .build()
        }
    }
}