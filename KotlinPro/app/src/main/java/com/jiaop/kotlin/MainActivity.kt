package com.jiaop.kotlin

import android.content.Context
import android.os.Bundle
import android.widget.LinearLayout
import com.jiaop.libs.base.JPBaseActivity
import com.jiaop.libs.utils.JPGlideUtil
import com.jiaop.libs.utils.JPUIUtil
import com.luliang.shapeutils.DevShapeUtils
import com.luliang.shapeutils.shape.DevShape
import com.yanzhenjie.permission.*
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.coroutines.onClick
import com.youth.banner.BannerConfig
import com.youth.banner.Transformer
import com.youth.banner.Transformer.DepthPage
import kotlinx.android.synthetic.main.activity_kotlin.*


class MainActivity : JPBaseActivity(), Rationale {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        showOval()

        btnClick.onClick {
            //            var state = JPNetWorkUtil.getNetType(this@MainActivity)
//            toast(state.toString())

//            showImg()

//            JPToastUtil.error(this@MainActivity, "Error")

//            initPermission()

//            startActivity<KotlinActivity>()

            startActivity<BlueToothSppActivity>()
        }

        initTitle()

    }

    override fun layoutId(): Int {
        return R.layout.activity_main
    }

    override fun initView() {
        toast("进入加载布局样式")
        showBanner()
    }

    fun showBanner() {
        var arrs = ArrayList<String>()
        var titles = ArrayList<String>()
        for (i in 1..10) {
            arrs.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1522752805514&di=f763bcd3b7f64d1b4660b6d86ed013ef&imgtype=0&src=http%3A%2F%2Fimg5.duitang.com%2Fuploads%2Fitem%2F201508%2F25%2F20150825092205_yvWLt.gif")
            titles.add("标题--" + i)
        }

        //设置banner样式
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR)
        //设置图片加载器
        .setImageLoader(GlideImageLoader())
        //设置图片集合
        .setImages(arrs)
        //设置banner动画效果
        .setBannerAnimation(Transformer.Accordion)
        //设置标题集合（当banner样式有显示title时）
        .setBannerTitles(titles)
        //设置自动轮播，默认为true
        .isAutoPlay(true)
        //设置轮播时间
        .setDelayTime(2000)
        //设置指示器位置（当banner模式中有指示器时）
        .setIndicatorGravity(BannerConfig.CENTER)
        //开始
        .start()
    }

    /**
     * 显示shape和selector样式
     */
    fun showOval() {
        //椭圆
        //DevShapeUtils.shape(DevShape.OVAL).solid(R.color.btColor).into(btnClick)
        //矩形
        //DevShapeUtils.shape(DevShape.RECTANGLE).solid(R.color.colorAccent).into(btnClick)
        //圆角,即设置圆角半径
        //当radios为999时，设置的为半圆角
        //DevShapeUtils.shape(DevShape.RECTANGLE).solid(R.color.colorAccent).radius(999f).into(btnClick)
        //实线圆角边框（line 参数1：边框宽度 参数2：边框颜色）
        //solid设置的为背景色，line设置的是线的样式
        //DevShapeUtils.shape(DevShape.RECTANGLE).solid(R.color.btColor).line(1, R.color.colorPrimaryDark).radius(10f).into(btnClick)

        //触摸背景颜色变化(selectorBackground 参数1：触摸颜色 参数2 正常颜色)
        //DevShapeUtils.selectorBackground(R.color.colorAccent, R.color.colorPrimary)
        //        .selectorColor(R.color.colorPrimaryDark, R.color.btColor).into(btnClick)
        //圆角触摸变化
        DevShapeUtils
                .selectorBackground(DevShapeUtils.shape(DevShape.RECTANGLE).solid(R.color.colorPrimaryDark).radius(999f).build(),
                        DevShapeUtils.shape(DevShape.RECTANGLE).solid(R.color.btColor).radius(999f).build())
                .selectorColor("#ffffff", "#000000")
                .into(btnClick)
    }

    fun initTitle() {
        //左边图片
        tbMain.setLeftImageResource(R.mipmap.title_back)
        //左边文字
        tbMain.setLeftText("返回")
        tbMain.setTitle("标题栏")
        tbMain.setLeftClickListener {
            toast("click left back button")
        }
        //详见https://github.com/bacy/titlebar
    }

    /**
     * 设置权限
     */
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
        return R.color.btColor
    }
}
