package com.jiaop.kotlin

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.widget.LinearLayout
import com.jiaop.libs.base.JPBaseActivity
import com.jiaop.libs.utils.JPGlideUtil
import com.jiaop.libs.utils.JPUIUtil
import com.yanzhenjie.permission.*
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.coroutines.onClick


class MainActivity : JPBaseActivity(), Rationale {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnClick.onClick {
            //            var state = JPNetWorkUtil.getNetType(this@MainActivity)
//            toast(state.toString())

//            showImg()

//            JPToastUtil.error(this@MainActivity, "Error")

//            initPermission()

            startActivity<KotlinActivity>()
        }

        initTitle()

    }

    fun initTitle() {
        //左边图片
        tbMain.setLeftImageResource(R.mipmap.title_back)
        //左边文字
        tbMain.setLeftText("返回")
        //详见https://github.com/bacy/titlebar
    }

    fun initPermission() {
        AndPermission.with(this@MainActivity)
                .permission(Permission.CAMERA
                        , Permission.RECORD_AUDIO
                )
                .rationale(this@MainActivity)
                .onGranted(Action {
                    fun onAction(permissions: List<String>) {
                        toast("onGranted")
                    }
                }).onDenied(Action {
                    fun onAction(permissions: List<String>) {
                        toast("onDenied")
                    }
                })
                .start()
    }

    override fun showRationale(context: Context?, permissions: MutableList<String>?, executor: RequestExecutor?) {
        // 这里使用一个Dialog询问用户是否继续授权。
        alert("Hi, I'm Roy", "Have you tried turning it off and on again?") {
            yesButton {}
            noButton {}
        }.show()
        // 如果用户继续：
        executor?.execute()

        // 如果用户中断：
        executor?.cancel()
    }

    var imgUri = "https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=2044350412,2130864133&fm=27&gp=0.jpg"
    var imgGif = "https://img3.duitang.com/uploads/item/201606/22/20160622003814_NicYZ.gif"

    fun showImg() {
        var imgLp = LinearLayout.LayoutParams(
                JPUIUtil.getScreenWidth(this@MainActivity) - 20,
                JPUIUtil.getScreenWidth(this@MainActivity) - 20)
        imgLp.margin = 10

        ivShow.layoutParams = imgLp

        JPGlideUtil.loadBlurImg(this@MainActivity, imgUri, ivShow, 20)
    }

    override fun initWiFiData() {
        toast("WIFI")
    }

    override fun initNetData() {
        toast("Net")
    }

    override fun initOfflineData() {
        toast("No Net")
    }

    override fun statusBarColor(): Int {
        return R.color.color_immersion_bar
    }
}
