package com.jiaop.kotlin

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import kotlinx.android.synthetic.main.activity_kotlin.*
import org.jetbrains.anko.sdk25.coroutines.onClick

class KotlinActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kotlin)

        btnTest.onClick {
            //            vars(1, 2, 3, 4, 5, 6, 7, 8)

//            Log.e("Kotlin", "匿名函数表达式 == " + sumLambda(2, 4))

//            a = 4
//            Log.e("Kotlin", "字符串模板 == " + s2)

            areaShow()
        }

    }

    /**
     * 函数定义使用关键字fun，参数格式为 参数：类型
     */
    fun show(a: Int, b: String) {

    }

    /**
     * 表达式作为函数体，返回类型自动推断
     */
    fun sum(a: Int, b: Int) = a + b

    /**
     * public 函数，则必须明确写出返回类型
     */
    public fun sub(a: Int, b: Int): Int = a - b

    /**
     * 无返回值的函数（类似Java中的void），Unit可以省略
     */
    fun printSum(a: Int, b: Int): Unit {
        print(a + b)
    }

    /**
     * 可变长参数函数
     * 参数可以使用关键字vararg进行标识
     */
    fun vars(vararg v: Int) {
        for (vt in v) {
            print(vt)
            Log.e("Kotlin", "vars == " + vt)
        }
    }

    /**
     * lambda(匿名函数)
     * 使用实例
     */
    val sumLambda: (Int, Int) -> Int = { x, y -> x + y }

    /**
     * 定义可变变量 ，关键字 var
     */
    var a: Int = 0

    /**
     * 定义不可变变量， 关键字 val ，只能赋值一次的变量（类似Java中的final）
     */
    val b: Int = 0

    /**
     * 注意：
     * 常量与变量都可以没有初始值，但是在引用前必须初始化
     * 编译器支持自动类型判断，即声明时可以不指定类型，由编译器判断
     * 例如： val c : Int （只能写在某个函数中）
     */
    var x = 6

    /**
     * 字符串模板
     * $ 表示一个变量名或者变量值
     * $varName 表示变量值
     * ${varName.fun()} 表示变量的方法返回值
     */
    var s1 = "a == $a"

    var s2 = "${s1.replace("==", "is")},a == $a"

    /**
     * Null 安全检查机制
     * 空安全设计对于声明可为空的参数，在使用时要进行空判断处理
     * 字段后面加 !! ,像Java一样抛出异常
     * 字段后面加 ? ，可不做处理返回值为null，或配合?:做空判断处理
     */
    var age: Int? = 2

    var age1: Int = age!!

    var age2: Int = age ?: 0

    /**
     * 当一个引用可能为null时，对应的声明类型必须明确的标记为可为null
     */

    /**
     * 类型检测及自动类型转换
     * 可以使用 is 运算符检测一个表达式是否某类型的一个实例
     */

    fun getStringLength(str: String): Int? {
        if (str is String) {
            return str.length
        }
        return null
    }

    /**
     * 区间
     * 区间表达式由具有操作符形式 .. 的 rangeTo 函数辅以 in 和 !in 形成
     * 区间是为任何可比较类型定义的，但是对于整形原生类型，有一个优化的实现
     * 当区间从大到小，并不显示任何值 如： 10..1
     */

    fun areaShow() {
        for (i in 10..1) {
            Log.e("TAG", "area == " + i)
        }

        /**
         * 关键字 step 指定步长
         */
        for (i in 1..30 step 2) {
            Log.e("TAG", "step == " + i)
        }

        /**
         * 降序循环
         */
        for (i in 10 downTo 1 step 2) {
            Log.e("TAG", "downTo == " + i)
        }

        /**
         * 使用关键字 until 排除结束元素
         * 即循环1 到 9
         */
        for (i in 1 until 10) {
            Log.e("TAG", "until == " + i)
        }

    }

    /**
     * 没有基础数据类型，只有封装的数字类型
     * 每定义一个变量，即封装了一个对象，可以保证不会出现空指针
     * 数字类型也是一样，所有在比较两个数字的时候，就有比较数据大小和比较两个对象是否相同的区别了
     */

    /**
     *  三个 === 表示比较对象地址，两个 == 表示比较两个值大小
     */

    /**
     * 可以使用 _ 使数字常量更易读
     */
    val number = 1_000_000

    /**
     * 类型转换
     * 由于不同的表示方式，较小类型并不是较大类型的子类型，较小类型不能隐式转换为较大类型
     * 有些情况下也是可以使用自动类型转换的，前提是可以根据上下文环境推断出正确的数据类型，
     * 而且数学操作符会做相应的重载
     */

    /**
     * 可以使用以下函数进行转换
     */
    fun convertObj() {
        number.toByte()
        number.toChar()
        number.toDouble()
        number.toFloat()
        number.toInt()
        number.toLong()
        number.toShort()
        number.toString()
    }

    var bits = 0

    /**
     * 位操作符
     * 对于Int 和 Long 类型，有一系列的位操作符可以使用
     */

    fun bitOperator() {
        bits.shl(1) // 左移位
        bits.shr(1) //右移位
        bits.ushr(1)//无符号右移位
        bits.and(1)//与
        bits.or(1)//或
        bits.xor(1)//异或
        bits.inv()//反向
    }

    /**
     * 字符
     * Char不能直接和数字操作，Char必须是单引号 ' ' 包含起来的，如 '字'
     * 特殊字符可以用反斜杠转义，支持以下转义序列：
     * \t, \b, \n, \r, \', \", \\, \$
     * 编码及其他字符要用Unicode转义序列语法： '\uFF00'
     * 当需要可空引用时，像数字，字符会被装箱，装箱操作不会保留同一性
     */

    /**
     * 布尔
     * Boolean ： true or false
     * 若需要可空布尔值会被装箱
     * || 短路逻辑或
     * && 短路逻辑与
     * ! 逻辑非
     */

    /**
     * 数组
     * 使用类 Array 实现，并且还有一个 size 属性及 get 和 set 方法
     * 由于使用了 [] 重载了 get 和 set 方法，可以通过下标很方便的获取或设置数组对应位置的值
     * 数组的创建的两种方式：
     * 1. 使用函数 arrayOf()
     * 2. 使用工厂函数
     *
     * 在这里数组是不型变的（invariant）
     *
     *  除了 Array 外，还有以下类型：
     *  ByteArray、 ShortArray、IntArray等各个类型的数组，它们省去了装箱操作，因此效率更高
     */

    fun arrayBuild() {
        var arrs = arrayOf(1, 2, 3, 4, 5, 6)

        /**
         * 创建1-10的数组，i代表元素的索引值从0开始
         */
        var arrs1 = Array(10, { i ->
            (i + 1)
        })
    }

    /**
     * 字符串
     * String 是可不变的
     * [] 可以很方便的获取字符串的某个字符，也可以通过 for 循环获取
     * 支持 """ ... """ ,多行字符串
     * 可以通过 trimMargin() 来删除多余的空白
     * 默认 | 用作边界前缀，也可以选择其他字符
     */

    fun stringShow() {
        var str = """123
            |4
            |5678""".trimMargin()
        str.trimMargin()
    }

    /**
     * 字符串模板
     * 字符串可以包含模板表达式，即一些小段代码，会求值并把结果合并到字符串中
     * 模板表达式以 $ 开头，由一个简单的名字构成，或者用 {} 起来的任意表达式
     * 原生字符串和转义字符内部都支持模板，表示字面值 $ 字符，使用
     * ${'$'}88.88
     */

    fun stringMoudel() {
        var str = "123"
        var str1 = "this is str == $str"
        var str2 = "we has ${'$'}$str"
    }

}
