package com.chaos.mediafun.media.decoder

import android.media.AudioFormat
import android.media.AudioManager
import android.media.AudioTrack
import android.media.MediaCodec
import android.media.MediaFormat
import com.chaos.mediafun.media.IExtractor
import com.chaos.mediafun.media.extractor.AudioExtractor
import java.nio.ByteBuffer

/**
 * @Author      : wen
 * @Email       : iwenchaos6688@163.com
 * @Date        : on 2023/12/28 15:12.
 * @Description :描述
 */
class AudioDecoder(filePath: String) : BaseDecoder(filePath) {
    private var sampleRate = -1
    private var channels = 1
    private var pcmEncodeBit = AudioFormat.ENCODING_PCM_16BIT
    private var audioTrack: AudioTrack? = null
    private var audioOutTempBuffer: ShortArray? = null


    override fun check(): Boolean {
        return true
    }

    override fun initExtractor(path: String): IExtractor {
        return AudioExtractor(path)
    }

    override fun initSpecParams(format: MediaFormat) {
        try {
            channels = format.getInteger(MediaFormat.KEY_CHANNEL_COUNT)
            sampleRate = format.getInteger(MediaFormat.KEY_SAMPLE_RATE)
            pcmEncodeBit = if (format.containsKey(MediaFormat.KEY_PCM_ENCODING)) {
                format.getInteger(MediaFormat.KEY_PCM_ENCODING)
            } else {
                //如果没有这个参数，默认为16位采样
                AudioFormat.ENCODING_PCM_16BIT
            }
        } catch (e: Exception) {
        }
    }

    override fun configCodec(codec: MediaCodec, format: MediaFormat): Boolean {
        codec.configure(format, null, null, 0)
        return true
    }

    override fun initRender(): Boolean {
        val channel = if (channels == 1) {
            AudioFormat.CHANNEL_OUT_MONO
        } else {
            AudioFormat.CHANNEL_OUT_STEREO
        }
        val minBufferSize = AudioTrack.getMinBufferSize(sampleRate, channel, pcmEncodeBit)
        audioOutTempBuffer = ShortArray(minBufferSize / 2)
        audioTrack = AudioTrack(
            AudioManager.STREAM_MUSIC,//播放类型：音乐
            sampleRate,//采样率
            channel,//通道
            pcmEncodeBit, //采样位数
            minBufferSize,//缓冲区大小
            AudioTrack.MODE_STREAM  //播放模式：数据流动态写入，另一种是一次性写入
        )
        audioTrack?.play()
        return true
    }

    override fun render(outputBuffer: ByteBuffer, info: MediaCodec.BufferInfo) {
        if (audioOutTempBuffer!!.size < info.size / 2) {
            audioOutTempBuffer = ShortArray(info.size / 2)
        }
        outputBuffer.position(0)
        outputBuffer.asShortBuffer().get(audioOutTempBuffer!!, 0, info.size / 2)
        audioTrack?.write(audioOutTempBuffer!!, 0, info.size / 2)
    }

    override fun doneEncode() {
        audioTrack?.stop()
        audioTrack?.release()
    }
}