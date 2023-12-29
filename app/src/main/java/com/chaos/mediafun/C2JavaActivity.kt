package com.chaos.mediafun

import com.chaos.mediafun.base.BaseActivity
import com.chaos.mediafun.databinding.ActivityC2JavaBinding
import com.chaos.mediafun.native.NormalUserInfo

/**
 * @Author      : wen
 * @Email       : iwenchaos6688@163.com
 * @Date        : on 2023/12/12 09:20.
 * @Description :描述
 */
class C2JavaActivity : BaseActivity<ActivityC2JavaBinding>() {


    companion object {
        init {
            System.loadLibrary("helloLib")

        }
    }

    override fun getLayoutId() = R.layout.activity_c_2_java

    override fun afterViews() {

        val userInfo = NormalUserInfo()
        val userInfo2 = getPerson()
        binding.tvText.setOnClickListener {
            binding.tvText.text = userInfo.sayHello()
        }
        binding.tvText2.setOnClickListener {
            binding.tvText2.text = userInfo.add(1, 2).toString()
        }

        binding.tvText3.setOnClickListener {
            userInfo2.doWork()
        }

    }


    external fun getPerson(): NormalUserInfo


}