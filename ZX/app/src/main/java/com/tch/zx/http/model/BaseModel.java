package com.tch.zx.http.model;

import android.content.Context;

import com.tch.zx.bean.BaseResultBean;
import com.tch.zx.http.HttpBaseService;
import com.tch.zx.http.RetrofitHelper;
import com.tch.zx.http.bean.result.FriendListByAppUserIdResult;
import com.tch.zx.http.bean.result.RetResultBean;

import io.reactivex.Flowable;

public class BaseModel<T> {

    private HttpBaseService service;
    private Context context;

    public BaseModel(Context context) {
        this.context = context;
    }

    /**
     * 获取好友列表
     *
     * @param appUserId
     * @return
     */
    public Flowable<BaseResultBean<Object>> getFriendListByAppUserId(String appUserId) {
        return RetrofitHelper.getInstance(context).getServer().getFriendListByAppUserId(appUserId);
    }

    /**
     * 搜索好友
     *
     * @param userName
     * @return
     */
    public Flowable<BaseResultBean<Object>> queryFriendByPhone(String userName) {
        return RetrofitHelper.getInstance(context).getServer().queryFriendByPhone(userName);
    }

    /**
     * 获取用户信息
     *
     * @param appUserId
     * @return
     */
    public Flowable<BaseResultBean<Object>> getUserInfoByAppUserId(String appUserId) {
        return RetrofitHelper.getInstance(context).getServer().getUserInfoByAppUserId(appUserId);
    }

    /**
     * 添加好友申请
     *
     * @param data
     * @return
     */
    public Flowable<BaseResultBean<Object>> addFriend(String data) {
        return RetrofitHelper.getInstance(context).getServer().addFriend(data);
    }

    /**
     * 获取好友请求列表
     *
     * @param data
     * @return
     */
    public Flowable<BaseResultBean<Object>> queryFriendAppyList(String data) {
        return RetrofitHelper.getInstance(context).getServer().queryFriendAppyList(data);
    }

    /**
     * 处理好友请求
     *
     * @param data
     * @return
     */
    public Flowable<BaseResultBean<Object>> processAppyFriendRequest(String data) {
        return RetrofitHelper.getInstance(context).getServer().processAppyFriendRequest(data);
    }

    /**
     * 获取关注列表
     *
     * @param data
     * @return
     */
    public Flowable<BaseResultBean<Object>> getFollowUserListByAppUserId(String data) {
        return RetrofitHelper.getInstance(context).getServer().getFollowUserListByAppUserId(data);
    }

    /**
     * 获取群组列表
     *
     * @param data
     * @return
     */
    public Flowable<BaseResultBean<Object>> getGroupListByAppUserId(String data) {
        return RetrofitHelper.getInstance(context).getServer().getGroupListByAppUserId(data);
    }

    /**
     * 创建群组
     *
     * @param data
     * @return
     */
    public Flowable<BaseResultBean<Object>> createGroupByParams(String data) {
        return RetrofitHelper.getInstance(context).getServer().createGroupByParams(data);
    }

    /**
     * 解散/退出群组
     *
     * @param data
     * @return
     */
    public Flowable<BaseResultBean<Object>> groupQuitOrDismiss(String data) {
        return RetrofitHelper.getInstance(context).getServer().groupQuitOrDismiss(data);
    }

    /**
     * 修改群名称
     *
     * @param data
     * @return
     */
    public Flowable<BaseResultBean<Object>> modifyGroupName(String data) {
        return RetrofitHelper.getInstance(context).getServer().modifyGroupName(data);
    }

    /**
     * 修改群昵称
     *
     * @param data
     * @return
     */
    public Flowable<BaseResultBean<Object>> modifyGroupMemberNickName(String data) {
        return RetrofitHelper.getInstance(context).getServer().modifyGroupMemberNickName(data);
    }

    /**
     * 获取群成员列表
     *
     * @param data
     * @return
     */
    public Flowable<BaseResultBean<Object>> getGroupMemberList(String data) {
        return RetrofitHelper.getInstance(context).getServer().getGroupMemberList(data);
    }

    /**
     * 拉人进群
     *
     * @param data
     * @return
     */
    public Flowable<BaseResultBean<Object>> addGroupMember(String data) {
        return RetrofitHelper.getInstance(context).getServer().addGroupMember(data);
    }

    /**
     * 加好友设置
     *
     * @param data
     * @return
     */
    public Flowable<BaseResultBean<Object>> addFriendSet(String data) {
        return RetrofitHelper.getInstance(context).getServer().addFriendSet(data);
    }

    /**
     * 动态删除
     *
     * @param data
     * @return
     */
    public Flowable<BaseResultBean<Object>> deleteDyNamic(String data) {
        return RetrofitHelper.getInstance(context).getServer().deleteDyNamic(data);
    }

    /**
     * 意见反馈
     *
     * @param data
     * @return
     */
    public Flowable<BaseResultBean<Object>> insertFeedback(String data) {
        return RetrofitHelper.getInstance(context).getServer().insertFeedback(data);
    }

    /**
     * 申请成为讲师或大咖
     *
     * @param data
     * @return
     */
    public Flowable<BaseResultBean<Object>> insertApplyInfo(String data) {
        return RetrofitHelper.getInstance(context).getServer().insertApplyInfo(data);
    }

    /**
     * 购买记录
     *
     * @param data
     * @return
     */
    public Flowable<BaseResultBean<Object>> payRecord(String data) {
        return RetrofitHelper.getInstance(context).getServer().payRecord(data);
    }

    /**
     * 修改用户公司信息
     *
     * @param data
     * @return
     */
    public Flowable<BaseResultBean<Object>> updateCompanyInfo(String data) {
        return RetrofitHelper.getInstance(context).getServer().updateCompanyInfo(data);
    }

    /**
     * 关注行业
     *
     * @param data
     * @return
     */
    public Flowable<BaseResultBean<Object>> userFollowIndustry(String data) {
        return RetrofitHelper.getInstance(context).getServer().userFollowIndustry(data);
    }

    /**
     * 提供行业
     *
     * @param data
     * @return
     */
    public Flowable<BaseResultBean<Object>> userOfferIndustry(String data) {
        return RetrofitHelper.getInstance(context).getServer().userOfferIndustry(data);
    }

    /**
     * 需求行业
     *
     * @param data
     * @return
     */
    public Flowable<BaseResultBean<Object>> userNeedIndustry(String data) {
        return RetrofitHelper.getInstance(context).getServer().userNeedIndustry(data);
    }

    /**
     * 动态列表
     *
     * @param data
     * @return
     */
    public Flowable<BaseResultBean<Object>> getDynamicList(String data) {
        return RetrofitHelper.getInstance(context).getServer().getDynamicList(data);
    }

    /**
     * 动态评论列表
     *
     * @param data
     * @return
     */
    public Flowable<BaseResultBean<Object>> getDynamicCommentList(String data) {
        return RetrofitHelper.getInstance(context).getServer().getDynamicCommentList(data);
    }

    /**
     * 动态发布
     *
     * @param data
     * @return
     */
    public Flowable<BaseResultBean<Object>> addDynamic(String data) {
        return RetrofitHelper.getInstance(context).getServer().addDynamic(data);
    }

    /**
     * 动态评论接口
     *
     * @param data
     * @return
     */
    public Flowable<BaseResultBean<Object>> addDynamicComment(String data) {
        return RetrofitHelper.getInstance(context).getServer().addDynamicComment(data);
    }

    /**
     * 动态评论点赞接口
     *
     * @param data
     * @return
     */
    public Flowable<BaseResultBean<Object>> updateDynamicCommentFN(String data) {
        return RetrofitHelper.getInstance(context).getServer().updateDynamicCommentFN(data);
    }

    /**
     * 支付获取订单号接口
     *
     * @param data
     * @return
     */
    public Flowable<BaseResultBean<Object>> createOrder(String data) {
        return RetrofitHelper.getInstance(context).getServer().createOrder(data);
    }

    /**
     * 手机验证码
     *
     * @param data
     * @return
     */
    public Flowable<BaseResultBean<Object>> smsUtils(String data) {
        return RetrofitHelper.getInstance(context).getServer().smsUtils(data);
    }

    /**
     * 行业分类列表
     *
     * @param data
     * @return
     */
    public Flowable<BaseResultBean<Object>> getIndustryList(String data) {
        return RetrofitHelper.getInstance(context).getServer().getIndustryList(data);
    }

    /**
     * 手机号注册
     *
     * @param data
     * @return
     */
    public Flowable<BaseResultBean<Object>> phoneRegistered(String data) {
        return RetrofitHelper.getInstance(context).getServer().phoneRegistered(data);
    }

    /**
     * 修改密码
     *
     * @param data
     * @return
     */
    public Flowable<BaseResultBean<Object>> updatePassword(String data) {
        return RetrofitHelper.getInstance(context).getServer().updatePassword(data);
    }

    /**
     * 微信注册
     *
     * @param data
     * @return
     */
    public Flowable<BaseResultBean<Object>> weixinRegistered(String data) {
        return RetrofitHelper.getInstance(context).getServer().weixinRegistered(data);
    }

    /**
     * 微信登录
     *
     * @param data
     * @return
     */
    public Flowable<BaseResultBean<Object>> weixinLogin(String data) {
        return RetrofitHelper.getInstance(context).getServer().weixinLogin(data);
    }

    /**
     * 验证密码
     *
     * @param data
     * @return
     */
    public Flowable<BaseResultBean<Object>> validatePassword(String data) {
        return RetrofitHelper.getInstance(context).getServer().validatePassword(data);
    }

    /**
     * 更换手机号
     *
     * @param data
     * @return
     */
    public Flowable<BaseResultBean<Object>> updatePhone(String data) {
        return RetrofitHelper.getInstance(context).getServer().updatePhone(data);
    }

    /**
     * 发布合作
     *
     * @param data
     * @return
     */
    public Flowable<BaseResultBean<Object>> updateUserCooperation(String data) {
        return RetrofitHelper.getInstance(context).getServer().updateUserCooperation(data);
    }

}
