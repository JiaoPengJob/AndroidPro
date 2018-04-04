需要设置：
//设置Log输出日志
        val formatStrategy = PrettyFormatStrategy.newBuilder()
                .showThreadInfo(true)  // (Optional) 是否显示线程信息. Default true
                .methodCount(0)         // (Optional) 要显示多少种方法行. Default 2
                .methodOffset(3)        // (Optional) 跳过堆栈跟踪中的一些方法调用. Default 5
                .tag("Kotlin:")   // (Optional) 每个日志的自定义标签. Default PRETTY_LOGGER
                .build()
        Logger.addLogAdapter(AndroidLogAdapter(formatStrategy))

//设置Toasty
Toasty.Config.getInstance()
    .setErrorColor(@ColorInt int errorColor) // optional
    .setInfoColor(@ColorInt int infoColor) // optional
    .setSuccessColor(@ColorInt int successColor) // optional
    .setWarningColor(@ColorInt int warningColor) // optional
    .setTextColor(@ColorInt int textColor) // optional
    .tintIcon(boolean tintIcon) // optional (apply textColor also to the icon)
    .setToastTypeface(@NonNull Typeface typeface) // optional
    .setTextSize(int sizeInSp) // optional
    .apply(); // required

//设置Shape或Selector的xml的工具，摒弃xml传统配置模式，减少文件数量，便于多人员开发
具体使用见：https://github.com/LiangLuDev/DevShapeUtils

//在开发蓝牙时，可以使用FastBle
https://github.com/Jasonchenlijian/FastBle

//使用banner时，youth的Banner
https://github.com/youth5201314/banner

//底部菜单栏可以考虑使用FlycoTabLayout
https://github.com/H07000223/FlycoTabLayout

//加载WebView时可以使用AgentWeb
https://github.com/Justson/AgentWeb

//列表中需要滑动菜单时，使用SwipeMenu
https://github.com/TUBB/SwipeMenu

//需要弹出框时，可以使用CustomPopwindow
https://github.com/pinguo-zhouwei/CustomPopwindow

//相册使用 Album
https://github.com/yanzhenjie/Album/blob/master/README-CN.md

//网络访问可以使用RxEasyHttp
https://github.com/zhou-you/RxEasyHttp
