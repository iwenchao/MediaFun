package com.chaos.mediafun

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import com.chaos.mediafun.base.BaseActivity
import com.chaos.mediafun.databinding.ActivityMainBinding
import com.yanzhenjie.permission.AndPermission
import com.yanzhenjie.permission.runtime.Permission

/**
 * @Author      : wen
 * @Email       : iwenchaos6688@163.com
 * @Date        : on 2023/12/11 17:20.
 * @Description :描述
 */
class MainActivity : BaseActivity<ActivityMainBinding>() {
    override fun getLayoutId() = R.layout.activity_main

    override fun afterViews() {
        requestPermission()
        binding.tvC2Java.setOnClickListener {
            startActivity(Intent(this, C2JavaActivity::class.java))
        }
        binding.tv2Douyin.setOnClickListener {
            startActivity(Intent().apply {
                this.setData(Uri.parse("snssdk1128://user/profile/93325972684"))
            })
        }
        binding.tv2SimplePlay.setOnClickListener {
            startActivity(Intent(this, SimplePlayerActivity::class.java))
        }
        binding.tv2SimpleRender.setOnClickListener {
            startActivity(Intent(this, SimpleRenderActivity::class.java).apply {
                putExtra("type", 0)
            })
        }
        binding.tv2SimpleRender2.setOnClickListener {
            startActivity(Intent(this, SimpleRenderActivity::class.java).apply {
                putExtra("type", 1)
            })
        }
        binding.tv2OpenGLPlayer.setOnClickListener {
            startActivity(Intent(this, OpenGLPlayerActivity::class.java))
        }
    }

    private fun requestPermission() {
        val permissions = Permission.Group.STORAGE
        AndPermission.with(this)
            .runtime()
            .permission(permissions)
            .onGranted {
            }
            .onDenied {
                Toast.makeText(this, "请打开权限，否则无法获取本地文件", Toast.LENGTH_SHORT).show()
            }
            .start()
    }


}