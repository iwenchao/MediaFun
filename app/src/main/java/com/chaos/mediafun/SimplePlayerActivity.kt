package com.chaos.mediafun

import android.os.Environment
import android.view.View
import com.chaos.mediafun.base.BaseActivity
import com.chaos.mediafun.databinding.ActivitySimplePlayerBinding
import com.chaos.mediafun.media.decoder.AudioDecoder
import com.chaos.mediafun.media.decoder.VideoDecoder
import java.util.concurrent.Executors

/**
 * @Author      : wen
 * @Email       : iwenchaos6688@163.com
 * @Date        : on 2023/12/25 16:54.
 * @Description :描述
 */
class SimplePlayerActivity : BaseActivity<ActivitySimplePlayerBinding>() {
    val filePath: String = Environment.getExternalStorageDirectory().absolutePath + "/funmedia.mp4"
    lateinit var videoDecoder: VideoDecoder
    lateinit var audioDecoder: AudioDecoder
    override fun getLayoutId() = R.layout.activity_simple_player

    override fun afterViews() {
        initPlayer()
    }

    fun playOrPause(view:View) {
        if(videoDecoder.isDecoding()){
            videoDecoder.pause()
            audioDecoder.pause()
        }else{
            videoDecoder.goOn()
            audioDecoder.goOn()
        }

    }


    fun clickRepack(view: View) {
        repack()
    }

    private fun repack() {
//        val repack = MP4Repack(path)
//        repack.start()
    }

    private fun initPlayer() {
        val threadPool = Executors.newFixedThreadPool(10)
        videoDecoder = VideoDecoder(filePath, binding.surfaceView, null)
        threadPool.execute(videoDecoder)

        audioDecoder = AudioDecoder(filePath)
        threadPool.execute(audioDecoder)
        videoDecoder.goOn()
        audioDecoder.goOn()
    }

    override fun onDestroy() {
        videoDecoder.stop()
        audioDecoder.stop()
        super.onDestroy()
    }
}