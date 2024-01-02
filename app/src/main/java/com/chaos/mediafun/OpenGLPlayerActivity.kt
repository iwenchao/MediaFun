package com.chaos.mediafun

import android.os.Environment
import android.view.Surface
import com.chaos.mediafun.base.BaseActivity
import com.chaos.mediafun.databinding.ActivityOpenglPlayerBinding
import com.chaos.mediafun.media.decoder.AudioDecoder
import com.chaos.mediafun.media.decoder.VideoDecoder
import com.chaos.mediafun.opengl.IRender
import com.chaos.mediafun.opengl.SimpleRender
import com.chaos.mediafun.opengl.drawer.VideoRender
import java.util.concurrent.Executors

/**
 * @Author      : wen
 * @Email       : iwenchaos6688@163.com
 * @Date        : on 2024/1/2 09:45.
 * @Description : 使用OpenGL渲染的播放器
 */
class OpenGLPlayerActivity : BaseActivity<ActivityOpenglPlayerBinding>() {
    val path = Environment.getExternalStorageDirectory().absolutePath + "/funmedia.mp4"
    lateinit var render: IRender

    override fun getLayoutId() = R.layout.activity_opengl_player

    override fun afterViews() {
        initRender()
    }

    private fun initRender() {
        render = VideoRender()
        render.setVideoSize(1080,1920)
        render.getSurfaceTexture{
            initPlayer(Surface(it))
        }
        binding.glSurfaceView.setEGLContextClientVersion(2)
        val simpleRender = SimpleRender()
        simpleRender.addRender(render)
        binding.glSurfaceView.setRenderer(simpleRender)
    }

    private fun initPlayer(surface: Surface){
        val threadPool = Executors.newFixedThreadPool(4)
        val videoDecoder = VideoDecoder(path, null,surface)
        threadPool.execute(videoDecoder)
        val audioDecoder = AudioDecoder(path)
        threadPool.execute(audioDecoder)

        videoDecoder.goOn()
        audioDecoder.goOn()
    }

    override fun onDestroy() {

        super.onDestroy()

    }
}