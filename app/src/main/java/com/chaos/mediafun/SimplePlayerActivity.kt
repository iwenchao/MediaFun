package com.chaos.mediafun

import android.os.Environment
import android.view.View
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import com.chaos.mediafun.base.BaseActivity
import com.chaos.mediafun.databinding.ActivitySimplePlayerBinding
import com.chaos.mediafun.media.decoder.AudioDecoder
import com.chaos.mediafun.media.decoder.VideoDecoder
import com.chaos.mediafun.utils.LogUtils
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

    private fun initPlayer() {
        val threadPool = Executors.newFixedThreadPool(10)
        videoDecoder = VideoDecoder(filePath, binding.surfaceView, null)
        threadPool.execute(videoDecoder)

        audioDecoder = AudioDecoder(filePath)
        threadPool.execute(audioDecoder)
        videoDecoder.goOn()
        audioDecoder.goOn()
    }

    private fun initListener(){
        binding.seekBar.setOnSeekBarChangeListener(object :OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                LogUtils.i("TAG","seekbar progress:${progress}")
//                videoDecoder.seekTo()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }

        })
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

    override fun onDestroy() {
        videoDecoder.stop()
        audioDecoder.stop()
        super.onDestroy()
    }
}