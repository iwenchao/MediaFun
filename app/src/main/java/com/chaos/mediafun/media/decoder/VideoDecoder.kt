package com.chaos.mediafun.media.decoder

import android.media.MediaCodec
import android.media.MediaFormat
import android.view.Surface
import android.view.SurfaceHolder
import android.view.SurfaceView
import com.chaos.mediafun.media.IExtractor
import com.chaos.mediafun.media.extractor.VideoExtractor
import java.nio.ByteBuffer

/**
 * @Author      : wen
 * @Email       : iwenchaos6688@163.com
 * @Date        : on 2023/12/26 09:45.
 * @Description :描述
 */
class VideoDecoder(filePath: String, surfaceView: SurfaceView?, surface: Surface?) :
    BaseDecoder(filePath) {
    private val TAG = "VideoDecoder"
    private var surfaceView: SurfaceView? = surfaceView
    private var surface: Surface? = surface

    override fun check(): Boolean {
        if (surfaceView == null && surface == null) {
            decoderStateListener?.decoderError(this, "显示器为空")
            return false
        }
        return true
    }

    override fun initExtractor(path: String): IExtractor {
        return VideoExtractor(path)
    }

    override fun initSpecParams(format: MediaFormat) {
    }

    override fun configCodec(codec: MediaCodec, format: MediaFormat): Boolean {
        if (surface != null) {
            codec.configure(format, surface, null, 0)
            notifyDecode()
        } else if (surfaceView?.holder?.surface != null) {
            surface = surfaceView?.holder?.surface
            configCodec(codec, format)
        } else {
            surfaceView?.holder?.addCallback(object : SurfaceHolder.Callback2 {
                override fun surfaceCreated(holder: SurfaceHolder) {
                    surface = holder.surface
                    configCodec(codec, format)
                }

                override fun surfaceChanged(
                    holder: SurfaceHolder,
                    format: Int,
                    width: Int,
                    height: Int
                ) {
                }

                override fun surfaceDestroyed(holder: SurfaceHolder) {
                }

                override fun surfaceRedrawNeeded(holder: SurfaceHolder) {
                }

            })
            return false
        }
        return true
    }

    override fun initRender(): Boolean {
        return true
    }

    override fun render(outputBuffer: ByteBuffer, info: MediaCodec.BufferInfo) {
    }

    override fun doneEncode() {
    }
}