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