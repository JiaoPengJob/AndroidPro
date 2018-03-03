package com.jiaop.kotlin

import android.content.Intent
import android.os.Bundle
import com.jiaop.kotlin.libs.base.BaseActivity
import com.jiaop.kotlin.libs.utils.ActivityUtil
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
    }

    fun initView() {
        btnClick.setOnClickListener {
            toast("点击之后")

           startActivity<NextActivity>()
        }
    }


}
