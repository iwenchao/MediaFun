package com.chaos.mediafun.media.extractor

import android.media.MediaFormat
import com.chaos.mediafun.media.IExtractor
import java.nio.ByteBuffer

/**
 * @Author      : wen
 * @Email       : iwenchaos6688@163.com
 * @Date        : on 2023/12/28 10:40.
 * @Description :描述
 */
class AudioExtractor(path:String):IExtractor {

    private val mmExtractor = MMExtractor(path)

    override fun getFormat(): MediaFormat? {
        return mmExtractor.getAudioFormat()
    }

    override fun readBuffer(byteBuffer: ByteBuffer): Int {
        return mmExtractor.readBuffer(byteBuffer)
    }

    override fun getCurrentTimeStamp(): Long {
        return mmExtractor.getCurrentTimestamp()
    }

    override fun getSampleFlag(): Int {
        return mmExtractor.getSampleFlag()
    }

    override fun seek(position: Long): Long {
        return mmExtractor.seek(position)
    }

    override fun setStartPosition(position: Long) {
        return mmExtractor.setStartPosition(position)
    }

    override fun stop() {
        mmExtractor.stop()
    }
}