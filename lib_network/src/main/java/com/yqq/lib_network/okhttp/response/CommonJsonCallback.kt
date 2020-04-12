package com.yqq.lib_network.okhttp.response

import android.os.Handler
import android.os.Looper
import com.google.gson.Gson
import com.yqq.lib_network.exception.OkHttpException
import com.yqq.lib_network.okhttp.listener.DisposeDataHandle
import com.yqq.lib_network.okhttp.listener.DisposeDataListener
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

/**
 * @author vision
 * @function 专门处理JSON的回调
 */
class CommonJsonCallback(handle: DisposeDataHandle): BaseCallback(handle) {
    private val mClass: Class<*>? = handle.mClass

    override fun onFailure(
        call: Call,
        e: IOException
    ) {
        /**
         * 此时还在非UI线程，因此要转发
         */
        mDeliveryHandler.post { mListener.onFailure(OkHttpException(NETWORK_ERROR, e)) }
    }

    @Throws(IOException::class)
    override fun onResponse(call: Call, response: Response) {
        val result = response.body().string()
        mDeliveryHandler.post { handleResponse(result) }
    }

    private fun handleResponse(responseObj: Any?) {
        if (responseObj == null || responseObj.toString().trim { it <= ' ' } == "") {
            mListener.onFailure(OkHttpException(NETWORK_ERROR, EMPTY_MSG))
            return
        }
        try {
            /**
             * 协议确定后看这里如何修改
             */
            val result = JSONObject(responseObj.toString())
            if (mClass == null) {
                mListener.onSuccess(result)
            } else {
                val obj = Gson().fromJson(responseObj.toString(), mClass)
                if (obj != null) {
                    mListener.onSuccess(obj)
                } else {
                    mListener.onFailure(OkHttpException(JSON_ERROR, EMPTY_MSG))
                }
            }
        } catch (e: Exception) {
            mListener.onFailure(OkHttpException(OTHER_ERROR, e.message))
            e.printStackTrace()
        }
    }

}