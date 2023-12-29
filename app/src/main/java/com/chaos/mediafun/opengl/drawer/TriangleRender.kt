package com.chaos.mediafun.opengl.drawer

import android.opengl.GLES20
import com.chaos.mediafun.opengl.IRender
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

/**
 * @Author      : wen
 * @Email       : iwenchaos6688@163.com
 * @Date        : on 2023/12/29 10:10.
 * @Description :描述
 */
class TriangleRender : IRender {
    // 顶点坐标
    private val mVertexCoors = floatArrayOf(
        -1f, -1f,
        1f, -1f,
        0f, 1f
    )

    // 纹理坐标
    private val mTextureCoors = floatArrayOf(
        0f, 1f,
        1f, 1f,
        0.5f, 0f
    )

    // 纹理id
    private var mTextureId = 0

    // OpenGl程序id
    private var mProgram = -1

    // 顶点坐标接收者
    private var mVertexPosHandler = -1

    // 纹理坐标接收者
    private var mTexturePosHandler: Int = -1

    private lateinit var mVertexBuffer: FloatBuffer
    private lateinit var mTextureBuffer: FloatBuffer

    init {
        //步骤1，初始化顶点坐标
        initPos()

    }

    fun initPos() {
        val bb = ByteBuffer.allocateDirect(mVertexCoors.size * 4)
        bb.order(ByteOrder.nativeOrder())
        //将坐标数据转换为FloatBuffer，用以传入给OpenGL ES程序
        mVertexBuffer = bb.asFloatBuffer()
        mVertexBuffer.put(mVertexCoors)
        mVertexBuffer.position(0)

        val cc = ByteBuffer.allocateDirect(mTextureCoors.size * 4)
        cc.order(ByteOrder.nativeOrder())
        mTextureBuffer = cc.asFloatBuffer()
        mTextureBuffer.put(mTextureCoors)
        mTextureBuffer.position(0)
    }


    override fun setVideoSize(videoW: Int, videoH: Int) {
    }

    override fun setWorldSize(worldW: Int, worldH: Int) {
    }

    override fun setAlpha(alpha: Float) {

    }

    override fun setTextureID(id: Int) {
        mTextureId = id
    }

    override fun draw() {
        if (mTextureId != -1) {
            createProgram()
            doDraw()
        }
    }

    fun createProgram() {
        if (mProgram == -1) {
            //加载顶点着色器和片段着色器
            val vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, getVertexShader())
            val fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, getFragmentShader())

            //创建OpenGL ES程序，；注意需要在OpenGL渲染线程中调用，否则无法渲染
            mProgram = GLES20.glCreateProgram()
            //将顶点着色器添加到OpenGL ES程序中
            GLES20.glAttachShader(mProgram, vertexShader)
            //将片段着色器添加到OpenGL ES程序中
            GLES20.glAttachShader(mProgram, fragmentShader)
            //链接OpenGL ES程序
            GLES20.glLinkProgram(mProgram)
            //获取顶点着色器和片段着色器的位置
            mVertexPosHandler = GLES20.glGetAttribLocation(mProgram, "aPosition")
            mTexturePosHandler = GLES20.glGetAttribLocation(mProgram, "aCoordinate")

        }
        //使用OpenGL 程序
        GLES20.glUseProgram(mProgram)
    }

    fun doDraw() {
        //启动顶点和纹理坐标
        GLES20.glEnableVertexAttribArray(mVertexPosHandler)
        GLES20.glEnableVertexAttribArray(mTexturePosHandler)
        //设置着色器参数， 第二个参数表示一个顶点包含的数据数量，这里为xy，所以为2
        GLES20.glVertexAttribPointer(mVertexPosHandler, 2, GLES20.GL_FLOAT, false, 0, mVertexBuffer)
        GLES20.glVertexAttribPointer(
            mTexturePosHandler,
            2,
            GLES20.GL_FLOAT,
            false,
            0,
            mTextureBuffer
        )
        //开始绘制
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 3)
    }

    private fun loadShader(type: Int, shaderCode: String): Int {
        //根据类型创建着色器
        val shader = GLES20.glCreateShader(type)
        //给着色器添加源代码，并编译
        GLES20.glShaderSource(shader, shaderCode)
        GLES20.glCompileShader(shader)
        return shader
    }

    /**
     * 创建顶点做色漆色
     * @return
     */
    private fun getVertexShader(): String {
        return "attribute vec4 aPosition;" +
                "void main() {" +
                "  gl_Position = aPosition;" +
                "}"
    }

    private fun getFragmentShader(): String {
        return "precision mediump float;" +
                "void main() {" +
                "  gl_FragColor = vec4(1.0, 0.0, 0.0, 1.0);" +
                "}"
    }

    override fun release() {
        GLES20.glDisableVertexAttribArray(mVertexPosHandler)
        GLES20.glDisableVertexAttribArray(mTexturePosHandler)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0)
        GLES20.glDeleteTextures(1, intArrayOf(mTextureId), 0)
        GLES20.glDeleteProgram(mProgram)

    }
}