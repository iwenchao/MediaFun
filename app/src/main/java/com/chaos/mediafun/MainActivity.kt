package com.chaos.mediafun

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.chaos.mediafun.base.BaseActivity
import com.chaos.mediafun.databinding.ActivityMainBinding

/**
 * @Author      : wen
 * @Email       : iwenchaos6688@163.com
 * @Date        : on 2023/12/11 17:20.
 * @Description :描述
 */
class MainActivity: BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        binding.tvC2Java.setOnClickListener {
            startActivity(Intent(this,C2JavaActivity::class.java))
        }
        binding.tv2Douyin.setOnClickListener {
            startActivity(Intent().apply {
                this.setData(Uri.parse("snssdk1128://user/profile/93325972684"))
            })
        }
    }



    fun starDouyin(){

    }

}