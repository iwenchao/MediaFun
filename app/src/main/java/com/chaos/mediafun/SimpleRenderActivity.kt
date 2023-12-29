package com.chaos.mediafun

import android.graphics.BitmapFactory
import com.chaos.mediafun.base.BaseActivity
import com.chaos.mediafun.databinding.ActivitySimpleRenderBinding
import com.chaos.mediafun.opengl.IRender
import com.chaos.mediafun.opengl.SimpleRender
import com.chaos.mediafun.opengl.drawer.BitmapRender
import com.chaos.mediafun.opengl.drawer.TriangleRender

/**
 * @Author      : wen
 * @Email       : iwenchaos6688@163.com
 * @Date        : on 2023/12/29 10:05.
 * @Description :描述
 */
class SimpleRenderActivity : BaseActivity<ActivitySimpleRenderBinding>() {
    private lateinit var render: IRender

    override fun getLayoutId() = R.layout.activity_simple_render

    override fun afterViews() {
        render = if (intent.getIntExtra("type", 0) == 0) {
            TriangleRender()
        } else {
            BitmapRender(BitmapFactory.decodeResource(this.resources, R.mipmap.ic_funmedia))
        }
        initRender(render)
    }

    private fun initRender(render: IRender) {
        binding.glSurfaceView.setEGLContextClientVersion(2)
        val simpleRender = SimpleRender()
        simpleRender.addRender(render)
        binding.glSurfaceView.setRenderer(simpleRender)
    }


    override fun onDestroy() {
        render.release()
        super.onDestroy()
    }
}