package com.chaos.mediafun

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.chaos.mediafun.base.BaseActivity
import com.chaos.mediafun.databinding.ActivityC2JavaBinding
import com.chaos.mediafun.native.NormalUserInfo

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

        val userInfo = NormalUserInfo()
        val userInfo2 = getPerson()
        binding.tvText.setOnClickListener {
            binding.tvText.text = userInfo.sayHello()
        }
        binding.tvText2.setOnClickListener {
            binding.tvText2.text = userInfo.add(1,2).toString()
        }

        binding.tvText3.setOnClickListener {
            userInfo2.doWork()
        }

    }


    external fun getPerson():NormalUserInfo


}