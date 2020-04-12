package com.yqq.voice.model.user

import com.yqq.voice.model.BaseModel

/**
 * 用户真正实体数据
 */
class UserContent constructor() : BaseModel() {
    var userId: String? = null //用户唯一标识符
    var photoUrl: String? = null
    var name: String? = null
    var tick: String? = null
    var mobile: String? = null
    var platform: String? = null
}