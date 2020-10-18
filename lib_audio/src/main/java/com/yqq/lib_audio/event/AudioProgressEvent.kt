package com.yqq.lib_audio.event

import com.yqq.lib_audio.core.CustomMediaPlayer

class AudioProgressEvent(
    var mStatus: CustomMediaPlayer.Status,
    var progress: Int,
    var maxLength: Int
)
