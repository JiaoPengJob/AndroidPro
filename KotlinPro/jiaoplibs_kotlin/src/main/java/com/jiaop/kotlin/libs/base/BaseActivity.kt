package com.jiaop.kotlin.libs.base

import android.os.Bundle
import android.support.v7.app.AppCompatActivity

/**
 * Created by jiaop
 * Activity基类
 */
abstract class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //隐藏标题栏
        supportActionBar?.hide()


    }


}