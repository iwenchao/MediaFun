package com.chaos.mediafun

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.chaos.mediafun.base.BaseActivity
import com.chaos.mediafun.databinding.ActivitySimplePlayerBinding

/**
 * @Author      : wen
 * @Email       : iwenchaos6688@163.com
 * @Date        : on 2023/12/25 16:54.
 * @Description :描述
 */
class SimplePlayerActivity:BaseActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivitySimplePlayerBinding>(this,R.layout.activity_simple_player)

    }


}