package com.chaos.mediafun.opengl.egl

import android.content.Context
import android.graphics.PointF
import android.opengl.GLSurfaceView
import android.util.AttributeSet
import android.view.MotionEvent
import com.chaos.mediafun.opengl.drawer.VideoRender

/**
 * @Author      : wen
 * @Email       : iwenchaos6688@163.com
 * @Date        : on 2024/1/2 11:22.
 * @Description :描述
 */
class DefGLSurfaceView : GLSurfaceView {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    private var render: VideoRender? = null
    private var mPrePoint = PointF()
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                mPrePoint.x = event.x
                mPrePoint.y = event.y
            }

            MotionEvent.ACTION_MOVE -> {
                val dx = (event.x - mPrePoint.x) / width
                val dy = (event.y - mPrePoint.y) / height
                render?.translate(dx, dy)
                mPrePoint.x = event.x
                mPrePoint.y = event.y
            }
        }
        return true
    }

    fun addRender(drawer: VideoRender) {
        render = drawer
    }
}