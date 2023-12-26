package com.chaos.mediafun.media

/**
 * @Author      : wen
 * @Email       : iwenchaos6688@163.com
 * @Date        : on 2023/12/26 09:23.
 * @Description :描述
 */
interface IDecoderProgress {

    /**
     * 视频播放进度回调
     */
    fun videoProgressChange(progress: Long)

    /**
     * 视频宽高回调,旋转角度
     */
    fun videoSizeChange(width: Int, height: Int,rotationAngle:Int)
}