package com.yqq.lib_network.okhttp.response

import android.os.Handler
import android.os.Looper
import android.os.Message
import com.yqq.lib_network.exception.OkHttpException
import com.yqq.lib_network.okhttp.listener.DisposeDataHandle
import com.yqq.lib_network.okhttp.listener.DisposeDownloadListener
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream

/**
 * @文件描述：专门处理文件下载回调
 */
class CommonFileCallback(handle: DisposeDataHandle) : Callback {
    /**
     * the java layer exception, do not same to the logic error
     */
    protected val NETWORK_ERROR = -1 // the network relative error
    protected val IO_ERROR = -2 // the JSON relative error
    protected val EMPTY_MSG = ""
    private val mDeliveryHandler: Handler
    private val mListener: DisposeDownloadListener = handle.mListener as DisposeDownloadListener
    private val mFilePath: String = handle.mSource
    private var mProgress = 0
    override fun onFailure(
        call: Call,
        ioexception: IOException
    ) {
        mDeliveryHandler.post { mListener.onFailure(OkHttpException(NETWORK_ERROR, ioexception)) }
    }

    @Throws(IOException::class)
    override fun onResponse(call: Call, response: Response) {
        val file = handleResponse(response)
        mDeliveryHandler.post {
            if (file != null) {
                mListener.onSuccess(file)
            } else {
                mListener.onFailure(OkHttpException(IO_ERROR, EMPTY_MSG))
            }
        }
    }

    /**
     * 此时还在子线程中，不则调用回调接口
     *
     * @param response
     * @return
     */
    private fun handleResponse(response: Response?): File? {
        if (response == null) {
            return null
        }
        var inputStream: InputStream? = null
        var file: File? = null
        var fos: FileOutputStream? = null
        val buffer = ByteArray(2048)
        var length: Int
        var currentLength = 0
        val sumLength: Double
        try {
            checkLocalFilePath(mFilePath)
            file = File(mFilePath)
            fos = FileOutputStream(file)
            inputStream = response.body().byteStream()
            sumLength = response.body().contentLength().toDouble()
            while (inputStream.read(buffer).also { length = it } != -1) {
                fos.write(buffer, 0, length)
                currentLength += length
                mProgress = (currentLength / sumLength * 100).toInt()
                mDeliveryHandler.obtainMessage(
                    PROGRESS_MESSAGE,
                    mProgress
                ).sendToTarget()
            }
            fos.flush()
        } catch (e: Exception) {
            file = null
        } finally {
            try {
                fos?.close()
                inputStream?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return file
    }

    private fun checkLocalFilePath(localFilePath: String) {
        val path = File(
            localFilePath.substring(
                0,
                localFilePath.lastIndexOf("/") + 1
            )
        )
        val file = File(localFilePath)
        if (!path.exists()) {
            path.mkdirs()
        }
        if (!file.exists()) {
            try {
                file.createNewFile()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    companion object {
        /**
         * 将其它线程的数据转发到UI线程
         */
        private const val PROGRESS_MESSAGE = 0x01
    }

    init {
        mDeliveryHandler = object : Handler(Looper.getMainLooper()) {
            override fun handleMessage(msg: Message) {
                when (msg.what) {
                    PROGRESS_MESSAGE -> mListener.onProgress(msg.obj as Int)
                }
            }
        }
    }
}