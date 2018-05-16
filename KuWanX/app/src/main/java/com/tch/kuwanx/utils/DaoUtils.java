package com.tch.kuwanx.utils;

import android.app.Activity;

import com.tch.kuwanx.application.BaseApplication;
import com.tch.kuwanx.dao.DaoSession;
import com.tch.kuwanx.dao.User;
import com.tch.kuwanx.dao.UserDao;
import com.tch.kuwanx.result.LoginResult;

/**
 * Created by jiaop on 2017/11/27.
 * 数据库帮助类
 */

public class DaoUtils {

    private static DaoSession getDaoSession(Activity activity) {
        return ((BaseApplication) activity.getApplication()).getDaoSession();
    }

    /**
     * 获取用户信息Dao
     *
     * @param activity
     * @return
     */
    public static UserDao getUserDao(Activity activity) {
        return getDaoSession(activity).getUserDao();
    }

    public static void saveUserId(Activity activity, LoginResult loginResult, String phone) {
        User user = new User();
        user.setUserId(loginResult.getResult().getId());
        user.setNickname(loginResult.getResult().getNickname());
        user.setPhone(phone);
        user.setUser_idcard(loginResult.getResult().getUser_idcard());
        user.setUser_realname(loginResult.getResult().getUser_realname());
        user.setPaypassword(loginResult.getResult().getPaypassword());
        user.setAccountnum(loginResult.getResult().getAccountnum());
        user.setYunToken(loginResult.getResult().getYunToken());
        user.setHeadpic(loginResult.getResult().getHeadpic());
        getUserDao(activity).insert(user);
    }

    public static String getUserId(Activity activity) {
        return getUserDao(activity).loadAll().get(0).getUserId();
    }

    public static User getUser(Activity activity) {
        return getUserDao(activity).loadAll().get(0);
    }

    public static void clearDao(Activity activity) {
        getDaoSession(activity).getUserDao().deleteAll();
    }

    public static String getUserpayPwd(Activity activity) {
        return getUserDao(activity).loadAll().get(0).getPaypassword();
    }

    public static String getUserAccountnum(Activity activity) {
        return getUserDao(activity).loadAll().get(0).getAccountnum();
    }

    public static String getUserToken(Activity activity) {
        return getUserDao(activity).loadAll().get(0).getYunToken();
    }

    public static void refreshUserToken(Activity activity, String token) {
        User user = getUserDao(activity).loadAll().get(0);
        user.setYunToken(token);
        getUserDao(activity).update(user);
    }

    public static void refreshUserHeadpic(Activity activity, String headpic) {
        User user = getUserDao(activity).loadAll().get(0);
        user.setHeadpic(headpic);
        getUserDao(activity).update(user);
    }

    public static void refreshUserName(Activity activity, String name) {
        User user = getUserDao(activity).loadAll().get(0);
        user.setNickname(name);
        getUserDao(activity).update(user);
    }
}
