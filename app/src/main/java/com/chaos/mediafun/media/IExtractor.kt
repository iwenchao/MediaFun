package com.chaos.mediafun.media

import android.media.MediaFormat
import java.nio.ByteBuffer

/**
 * @Author      : wen
 * @Email       : iwenchaos6688@163.com
 * @Date        : on 2023/12/28 10:29.
 * @Description : 音视频分离器
 */
interface IExtractor {

    fun getFormat():MediaFormat?

    /**
     * 读取一帧数据
     */
    fun readBuffer(byteBuffer: ByteBuffer):Int

    /**
     * 获取当前帧的时间戳
     */
    fun getCurrentTimeStamp():Long

    fun getSampleFlag():Int

    /**
     * seek到指定位置，并返回实际帧的时间戳
     * @param position 时间戳 timeUs
     * @return 实际帧的时间戳
     */
    fun seek(position:Long):Long

    fun setStartPosition(position:Long)
    /**
     * 停止读取数据
     */
    fun stop()

}