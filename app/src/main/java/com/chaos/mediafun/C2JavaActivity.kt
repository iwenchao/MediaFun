package com.chaos.mediafun

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.chaos.mediafun.base.BaseActivity
import com.chaos.mediafun.databinding.ActivityC2JavaBinding

/**
 * @Author      : wen
 * @Email       : iwenchaos6688@163.com
 * @Date        : on 2023/12/12 09:20.
 * @Description :描述
 */
class C2JavaActivity: BaseActivity() {


    companion object{
        init {
            System.loadLibrary("helloLib")

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityC2JavaBinding>(this, R.layout.activity_c_2_java)
        binding.tvText.setOnClickListener {
            binding.tvText.text = sayHello()
        }


    }


  external fun sayHello():String



}