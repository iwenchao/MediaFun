package com.chaos.mediafun.native

import android.util.Log
import android.widget.Toast

/**
 * @Author      : wen
 * @Email       : iwenchaos6688@163.com
 * @Date        : on 2023/12/12 11:02.
 * @Description :描述
 *
 * 传入参数，返回参数；
 * 对象调用，
 * 回调返回，
 *
 */
class NormalUserInfo: IUserInfo {
    var name:String = ""
    var age:Int = 0

    constructor()

    constructor(name:String,age:Int){
        this.name = name
        this.age = age
    }




    external fun sayHello():String

    external fun add(a:Int,b:Int):Int


    override fun doWork() {
        Log.i("tag",sayHello()+"，我叫${name}， 今年${age}岁")
    }


}


interface IUserInfo {
   fun doWork()
}