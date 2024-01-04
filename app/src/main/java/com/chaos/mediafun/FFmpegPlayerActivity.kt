package com.chaos.mediafun

import android.os.Environment
import android.view.Surface
import android.view.SurfaceHolder
import android.widget.Toast
import com.chaos.mediafun.base.BaseActivity
import com.chaos.mediafun.databinding.ActivityFfmpegPlayerBinding
import java.io.File

/**
 * @Author      : wen
 * @Email       : iwenchaos6688@163.com
 * @Date        : on 2024/1/2 17:01.
 * @Description :描述
 */
class FFmpegPlayerActivity:BaseActivity<ActivityFfmpegPlayerBinding>() {

    val path = Environment.getExternalStorageDirectory().absolutePath + "/funmedia.mp4"

    /**
     * native 播放器引用指针
     */
    private var player: Int? = null

    override fun getLayoutId() = R.layout.activity_ffmpeg_player

    override fun afterViews() {


    }

    private fun initSurface(){
        if (File(path).exists()) {
            binding.glSurfaceView.holder.addCallback(object : SurfaceHolder.Callback {
                override fun surfaceChanged(holder: SurfaceHolder, format: Int,
                                            width: Int, height: Int) {}
                override fun surfaceDestroyed(holder: SurfaceHolder) {}

                override fun surfaceCreated(holder: SurfaceHolder) {
                    if (player == null) {
                        player = createPlayer(path, holder.surface)
                        play(player!!)
                    }
                }
            })
        } else {
            Toast.makeText(this, "视频文件不存在，请在手机根目录下放置 funmedia.mp4", Toast.LENGTH_SHORT).show()
        }
    }

    external fun createPlayer(filePath:String, surface: Surface):Int?

    external fun play(player:Int)

}