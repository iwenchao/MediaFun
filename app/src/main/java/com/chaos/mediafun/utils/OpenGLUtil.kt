package com.chaos.mediafun.utils

import android.opengl.GLES20

/**
 * @Author      : wen
 * @Email       : iwenchaos6688@163.com
 * @Date        : on 2023/12/29 10:55.
 * @Description : 常用方法集合
 */
object OpenGLUtil {


    fun createTextureIds(count :Int):IntArray{
        val texture = IntArray(count)
        //生成纹理
        GLES20.glGenTextures(count,texture,0)
        return texture
    }

}