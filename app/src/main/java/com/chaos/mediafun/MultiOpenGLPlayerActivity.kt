package com.chaos.mediafun

import android.os.Environment
import android.view.Surface
import com.chaos.mediafun.base.BaseActivity
import com.chaos.mediafun.databinding.ActivityMultiOpenglPlayerBinding
import com.chaos.mediafun.media.decoder.AudioDecoder
import com.chaos.mediafun.media.decoder.VideoDecoder
import com.chaos.mediafun.opengl.SimpleRender
import com.chaos.mediafun.opengl.drawer.VideoRender
import java.util.concurrent.Executors

/**
 * @Author      : wen
 * @Email       : iwenchaos6688@163.com
 * @Date        : on 2024/1/2 14:37.
 * @Description :描述
 */
class MultiOpenGLPlayerActivity : BaseActivity<ActivityMultiOpenglPlayerBinding>() {
    private val path2 = Environment.getExternalStorageDirectory().absolutePath + "/funmedia_2.mp4"
    private val path = Environment.getExternalStorageDirectory().absolutePath + "/funmedia.mp4"

    private val threadPool = Executors.newFixedThreadPool(10)

    private val simpleRender = SimpleRender()

    override fun getLayoutId() = R.layout.activity_multi_opengl_player

    override fun afterViews() {
        initVideo1()
        initVideo2()
        initRender()
    }

    private fun initVideo1() {
        val render = VideoRender()
        render.setVideoSize(1080, 1920)
        render.getSurfaceTexture {
            initPlayer(path, Surface(it), true)
        }
        simpleRender.addRender(render)
    }

    private fun initVideo2() {
        val render = VideoRender()
        render.setAlpha(0.6f)
        render.setVideoSize(1080, 1920)
        render.getSurfaceTexture {
            initPlayer(path2, Surface(it), false)
        }
        simpleRender.addRender(render)
        binding.glSurfaceView.addRender(render)
        binding.glSurfaceView.postDelayed({
            render.scale(0.8f, 0.8f)
        }, 1000)
    }

    private fun initRender() {
        binding.glSurfaceView.setEGLContextClientVersion(2)
        binding.glSurfaceView.setRenderer(simpleRender)
    }

    private fun initPlayer(path: String, sf: Surface, withSound: Boolean) {
        val videoDecoder = VideoDecoder(path, null, sf)
        threadPool.execute(videoDecoder)
        videoDecoder.goOn()

        if (withSound) {
            val audioDecoder = AudioDecoder(path)
            threadPool.execute(audioDecoder)
            audioDecoder.goOn()
        }
    }
}