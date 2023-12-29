package com.chaos.mediafun.opengl

import android.graphics.SurfaceTexture

/**
 * @Author      : wen
 * @Email       : iwenchaos6688@163.com
 * @Date        : on 2023/12/29 10:09.
 * @Description :描述
 */
interface IRender {
    fun setVideoSize(videoW: Int, videoH: Int)
    fun setWorldSize(worldW: Int, worldH: Int)
    fun setAlpha(alpha: Float)
    fun draw()
    fun setTextureID(id: Int)
    fun getSurfaceTexture(cb: (st: SurfaceTexture)->Unit) {}
    fun release()

}