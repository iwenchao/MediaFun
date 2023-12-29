package com.chaos.mediafun.media

import android.media.MediaCodec
import java.nio.ByteBuffer

/**
 * @Author      : wen
 * @Email       : iwenchaos6688@163.com
 * @Date        : on 2023/12/26 09:38.
 * @Description :描述
 */
class Frame {
    var buffer: ByteBuffer? = null
    var bufferInfo = MediaCodec.BufferInfo()
        internal set


    fun setBufferInfo(bufferInfo: MediaCodec.BufferInfo) {
        this.bufferInfo.set(bufferInfo.offset, bufferInfo.size, bufferInfo.presentationTimeUs, bufferInfo.flags)
    }

}