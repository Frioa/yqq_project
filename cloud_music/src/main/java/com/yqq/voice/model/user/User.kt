package com.yqq.voice.model.user

import com.yqq.voice.model.BaseModel

/**
 * 用户数据协议
 */
class User : BaseModel() {
    var ecode = 0
    var emsg: String? = null
    var data: UserContent? = null
}