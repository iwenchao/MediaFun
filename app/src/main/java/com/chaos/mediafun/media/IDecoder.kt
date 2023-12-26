package com.chaos.mediafun.media

import android.media.MediaFormat

/**
 * @Author      : wen
 * @Email       : iwenchaos6688@163.com
 * @Date        : on 2023/12/26 09:15.
 * @Description :描述
 */
interface IDecoder : Runnable {

    /**
     * 继续解码
     */
    fun goOn()

    /**
     * 暂停解码
     */
    fun pause()

    /**
     * 停止解码
     */
    fun stop()


    /**
     * 跳转到指定位置
     * 并返回实际帧的时间
     * 单位毫秒
     */
    fun seekTo(position: Long): Long

    /**
     * 跳转到指定位置
     * 并返回实际帧的时间
     * 单位毫秒
     */
    fun seekAndPlay(position: Long): Long


    fun isDecoding(): Boolean

    fun isSeeking(): Boolean

    fun isStop(): Boolean

    /**
     * 无需音视频同步
     */
    fun withoutSync(): IDecoder

    fun getWidth(): Int
    fun getHeight(): Int
    fun getDuration(): Long
    fun getCurTimeStamp(): Long
    fun getRotationAngle(): Int
    fun getMediaFormat(): MediaFormat?
    fun getTrack(): Int
    fun getFilePath(): String
    fun setSizeListener(listener: IDecoderProgress)
    fun setStateListener(listener: IDecoderProgress)


}