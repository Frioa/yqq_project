package com.yqq.lib_network.okhttp.response


import android.os.Handler
import android.os.Looper
import com.yqq.lib_network.okhttp.listener.DisposeDataHandle
import com.yqq.lib_network.okhttp.listener.DisposeDataListener
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException

abstract class BaseCallback(handle: DisposeDataHandle) : Callback {

    protected val NETWORK_ERROR = -1 // the network relative error
    protected val IO_ERROR = -2 // the JSON relative error
    protected val EMPTY_MSG = ""
    /**
     * the logic layer exception, may alter in different app
     */
    protected val RESULT_CODE = "ecode" // 有返回则对于http请求来说是成功的，但还有可能是业务逻辑上的错误
    protected val RESULT_CODE_VALUE = 0
    protected val ERROR_MSG = "emsg"

    protected val JSON_ERROR = -2 // the JSON relative error
    protected val OTHER_ERROR = -3 // the unknow error

    protected var mDeliveryHandler: Handler = Handler(Looper.getMainLooper())
    protected val mListener: DisposeDataListener = handle.mListener

    override fun onFailure(call: Call, e: IOException) {
    }

    override fun onResponse(call: Call, response: Response) {
    }

}