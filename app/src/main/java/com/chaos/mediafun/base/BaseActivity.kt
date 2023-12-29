package com.chaos.mediafun.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

/**
 * @Author      : wen
 * @Email       : iwenchaos6688@163.com
 * @Date        : on 2023/12/12 09:21.
 * @Description :描述
 */
abstract class BaseActivity<VB: ViewDataBinding> : AppCompatActivity() {

    lateinit var binding: VB

    abstract fun getLayoutId(): Int

    abstract fun afterViews()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, getLayoutId())
        afterViews()
    }

}