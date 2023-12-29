package com.chaos.mediafun.media.extractor

import android.media.MediaExtractor
import android.media.MediaFormat
import java.nio.ByteBuffer

/**
 * @Author      : wen
 * @Email       : iwenchaos6688@163.com
 * @Date        : on 2023/12/28 10:41.
 * @Description :描述
 */
class MMExtractor(path: String) {

    private var extractor: MediaExtractor = MediaExtractor()
    private var audioTrack = -1
    private var videoTrack = -1
    private var curSampleTime: Long = 0
    private var curSampleFlag: Int = 0
    private var startPos: Long = 0

    init {
        extractor.setDataSource(path)
    }


    /**
     * 获取视频格式
     */
    fun getVideoFormat(): MediaFormat? {
        for (i in 0 until extractor?.trackCount!!) {
            val mediaFormat = extractor?.getTrackFormat(i)
            val mime = mediaFormat?.getString(MediaFormat.KEY_MIME)
            if (mime?.startsWith("video/") == true) {
                videoTrack = i
                break
            }
        }
        return if (videoTrack >= 0) extractor?.getTrackFormat(videoTrack) else null
    }

    /**
     * 获取音频格式参数
     */
    fun getAudioFormat(): MediaFormat? {
        for (i in 0 until extractor!!.trackCount) {
            val mediaFormat = extractor!!.getTrackFormat(i)
            val mime = mediaFormat.getString(MediaFormat.KEY_MIME)
            if (mime?.startsWith("audio/") == true) {
                audioTrack = i
                break
            }
        }
        return if (audioTrack >= 0) {
            extractor!!.getTrackFormat(audioTrack)
        } else null
    }


    fun readBuffer(byteBuffer: ByteBuffer): Int {
        byteBuffer.clear()
        selectSourceTrack()
        val readSampleCount = extractor.readSampleData(byteBuffer, 0)
        if (readSampleCount < 0) {
            return -1
        }
        //记录当前帧的时间戳
        curSampleTime = extractor.sampleTime
        curSampleFlag = extractor.sampleFlags
        //进入下一帧
        extractor.advance()
        return readSampleCount
    }

    private fun selectSourceTrack() {
        if (videoTrack >= 0) {
            extractor.selectTrack(videoTrack)
        } else if (audioTrack >= 0) {
            extractor.selectTrack(audioTrack)
        }

    }

    /**
     * seek到指定位置，返回实际帧的时间戳
     * @param position timeUs
     */
    fun seek(position: Long): Long {
        extractor.seekTo(position, MediaExtractor.SEEK_TO_PREVIOUS_SYNC)
        return extractor.sampleTime
    }

    fun stop() {
        extractor.release()
    }

    fun getVideoTrack(): Int {
        return videoTrack
    }

    fun getAudioTrack(): Int {
        return audioTrack
    }

    fun setStartPosition(timeUs: Long) {
        startPos = timeUs
    }

    /**
     * 获取当前帧时间
     */
    fun getCurrentTimestamp(): Long {
        return curSampleTime
    }

    fun getSampleFlag(): Int {
        return curSampleFlag
    }
}