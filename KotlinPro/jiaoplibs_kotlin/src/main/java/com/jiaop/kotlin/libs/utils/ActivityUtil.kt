package com.jiaop.kotlin.libs.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import java.util.*


/**
 * Created by jiaop
 * Activity工具类
 */
object ActivityUtil {

    /**
     * Stack 类表示后进先出（LIFO）的对象堆栈
     */
    private var activityStack: Stack<Activity>? = null

    /**
     *  打开一个新的Activity并且移除之前所有的Activity
     */
    fun startActivityClearTask(context: Context?, cls: Class<*>?) {
        var intent = Intent()
        intent.setClass(context, cls)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        context?.startActivity(intent)
    }

    /**
     *  打开一个新的Activity传递参数，并且移除之前所有的Activity
     */
    fun startActivityClearTaskWithExtra(context: Context?, bundle: Bundle, cls: Class<*>?) {
        var intent = Intent(context, cls)
        intent.putExtras(bundle)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        context?.startActivity(intent)
    }

    /**
     * 获取当前所有Activity
     */
    fun getAllActivity(): List<Activity> {
        val list = ArrayList<Activity>()
        for (activity in activityStack!!) {
            list.add(activity)
        }
        return list
    }

    /**
     * 向堆栈中添加Activity
     */
    fun addActivity(activity: Activity) {
        if (activityStack == null) {
            activityStack = Stack()
        }
        activityStack?.add(activity)
    }

    /**
     * 移除指定Activity
     */
    fun removeActivity(activity: Activity) {
        if (activityStack == null) {
            activityStack = Stack()
        }
        activityStack?.remove(activity)
    }

    /**
     * 获取当前活动的Activity
     */
    fun getCurrentActivity(): Activity {
        return activityStack!!.lastElement()
    }

    /**
     * 结束当前活动的Activity
     */
    fun finishActivity() {
        val activity = activityStack?.lastElement()
        finishActivity(activity)
    }

    /**
     * 结束指定Activity
     */
    fun finishActivity(activity: Activity?) {
        activityStack?.remove(activity)
        activity?.finish()
    }

    /**
     * 结束指定Activity的类
     */
    fun finishActivity(cls: Class<*>?) {
        for (activity in activityStack!!) {
            if (activity.javaClass == cls) {
                finishActivity(activity)
            }
        }
    }

    /**
     * 结束所有Activity
     */
    fun finishAllActivity() {
        if (activityStack != null
                && activityStack?.size!! > 0) {
            var i = 0
            val size = activityStack?.size!!
            while (i < size) {
                if (null != activityStack?.get(i)) {
                    activityStack?.get(i)!!.finish()
                }
                i++
            }
            activityStack?.clear()
        }
    }

}