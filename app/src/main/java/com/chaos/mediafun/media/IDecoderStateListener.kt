package com.chaos.mediafun.media

import com.chaos.mediafun.media.decoder.BaseDecoder

/**
 * @Author      : wen
 * @Email       : iwenchaos6688@163.com
 * @Date        : on 2023/12/26 09:25.
 * @Description :描述
 */
interface IDecoderStateListener {
    fun decoderPrepare(decodeJob: BaseDecoder?)
    fun decoderReady(decodeJob: BaseDecoder?)
    fun decoderRunning(decodeJob: BaseDecoder?)
    fun decoderPause(decodeJob: BaseDecoder?)
    fun decodeOneFrame(decodeJob: BaseDecoder?, frame: Frame)
    fun decoderFinish(decodeJob: BaseDecoder?)
    fun decoderDestroy(decodeJob: BaseDecoder?)
    fun decoderError(decodeJob: BaseDecoder?, msg: String)


}