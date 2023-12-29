package com.chaos.mediafun.opengl

import android.opengl.GLES20
import android.opengl.GLSurfaceView
import com.chaos.mediafun.utils.LogUtils
import com.chaos.mediafun.utils.OpenGLUtil
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

/**
 * @Author      : wen
 * @Email       : iwenchaos6688@163.com
 * @Date        : on 2023/12/29 10:09.
 * @Description :描述
 */
class SimpleRender : GLSurfaceView.Renderer {
    private val renderList = mutableListOf<IRender>()
    fun addRender(render: IRender) {
        renderList.add(render)
    }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        GLES20.glClearColor(0f, 0f, 0f, 0f)
        //开启混合，即半透明
        GLES20.glEnable(GLES20.GL_BLEND)
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA)

        val textureIdList = OpenGLUtil.createTextureIds(renderList.size)
        for ((index, render) in renderList.withIndex()) {
            render.setTextureID(textureIdList[index])
            LogUtils.i("TAG", "纹理id${textureIdList[index]}")
        }

    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)
        for (render in renderList) {
            render.setWorldSize(width, height)
        }
    }

    override fun onDrawFrame(gl: GL10?) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)
        renderList.forEach {
            it.draw()
        }
    }
}