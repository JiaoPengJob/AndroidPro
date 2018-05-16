package com.tch.zx.http.presenter;

import android.content.Context;
import android.content.Intent;

import com.tch.zx.bean.BaseResultBean;
import com.tch.zx.http.bean.result.RetResultBean;
import com.tch.zx.http.model.BaseModel;
import com.tch.zx.http.model.CollectCancelModel;
import com.tch.zx.http.subscriber.BaseSubscriberCallBack;
import com.tch.zx.http.view.BaseView;
import com.tch.zx.http.view.CollectCancelView;
import com.tch.zx.http.view.View;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * 取消关注
 */

public class BasePresenter<T> implements Presenter {

    private Context context;
    private BaseModel model;
    private BaseView baseView;
    private Flowable flowable;

    public BasePresenter(Context context) {
        this.context = context;
    }

    @Override
    public void onCreate() {
        this.model = new BaseModel(context);
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void attachView(View view) {
        baseView = (BaseView) view;
    }

    @Override
    public void attachIncomingIntent(Intent intent) {

    }

    /**
     * 搜索好友
     *
     * @param data
     */
    public void queryFriendByPhone(String data) {
        flowable = model.queryFriendByPhone(data);
        flowable();
    }

    /**
     * 获取用户信息
     *
     * @param data
     */
    public void getUserInfoByAppUserId(String data) {
        flowable = model.getUserInfoByAppUserId(data);
        flowable();
    }

    /**
     * 添加好友申请
     *
     * @param data
     */
    public void addFriend(String data) {
        flowable = model.addFriend(data);
        flowable();
    }

    /**
     * 获取好友请求列表
     *
     * @param data
     */
    public void queryFriendAppyList(String data) {
        flowable = model.queryFriendAppyList(data);
        flowable();
    }

    /**
     * 处理好友请求
     *
     * @param data
     */
    public void processAppyFriendRequest(String data) {
        flowable = model.processAppyFriendRequest(data);
        flowable();
    }

    /**
     * 获取关注列表
     *
     * @param data
     */
    public void getFollowUserListByAppUserId(String data) {
        flowable = model.getFollowUserListByAppUserId(data);
        flowable();
    }

    /**
     * 获取群组列表
     *
     * @param data
     */
    public void getGroupListByAppUserId(String data) {
        flowable = model.getGroupListByAppUserId(data);
        flowable();
    }

    /**
     * 创建群组
     *
     * @param data
     */
    public void createGroupByParams(String data) {
        flowable = model.createGroupByParams(data);
        flowable();
    }

    /**
     * 解散/退出群组
     *
     * @param data
     */
    public void groupQuitOrDismiss(String data) {
        flowable = model.groupQuitOrDismiss(data);
        flowable();
    }

    /**
     * 修改群名称
     *
     * @param data
     */
    public void modifyGroupName(String data) {
        flowable = model.modifyGroupName(data);
        flowable();
    }

    /**
     * 修改群昵称
     *
     * @param data
     */
    public void modifyGroupMemberNickName(String data) {
        flowable = model.modifyGroupMemberNickName(data);
        flowable();
    }

    /**
     * 获取群成员列表
     *
     * @param data
     */
    public void getGroupMemberList(String data) {
        flowable = model.getGroupMemberList(data);
        flowable();
    }

    /**
     * 拉人进群
     *
     * @param data
     */
    public void addGroupMember(String data) {
        flowable = model.addGroupMember(data);
        flowable();
    }

    /**
     * 加好友设置
     *
     * @param data
     */
    public void addFriendSet(String data) {
        flowable = model.addFriendSet(data);
        flowable();
    }

    /**
     * 动态删除
     *
     * @param data
     */
    public void deleteDyNamic(String data) {
        flowable = model.deleteDyNamic(data);
        flowable();
    }

    /**
     * 意见反馈
     *
     * @param data
     */
    public void insertFeedback(String data) {
        flowable = model.insertFeedback(data);
        flowable();
    }

    /**
     * 申请成为讲师或大咖
     *
     * @param data
     */
    public void insertApplyInfo(String data) {
        flowable = model.insertApplyInfo(data);
        flowable();
    }

    /**
     * 购买记录
     *
     * @param data
     */
    public void payRecord(String data) {
        flowable = model.payRecord(data);
        flowable();
    }

    /**
     * 修改用户公司信息
     *
     * @param data
     */
    public void updateCompanyInfo(String data) {
        flowable = model.updateCompanyInfo(data);
        flowable();
    }

    /**
     * 关注行业
     *
     * @param data
     */
    public void userFollowIndustry(String data) {
        flowable = model.userFollowIndustry(data);
        flowable();
    }

    /**
     * 提供行业
     *
     * @param data
     */
    public void userOfferIndustry(String data) {
        flowable = model.userOfferIndustry(data);
        flowable();
    }

    /**
     * 需求行业
     *
     * @param data
     */
    public void userNeedIndustry(String data) {
        flowable = model.userNeedIndustry(data);
        flowable();
    }

    /**
     * 动态列表
     *
     * @param data
     */
    public void getDynamicList(String data) {
        flowable = model.getDynamicList(data);
        flowable();
    }

    /**
     * 动态评论列表
     *
     * @param data
     */
    public void getDynamicCommentList(String data) {
        flowable = model.getDynamicCommentList(data);
        flowable();
    }

    /**
     * 动态发布
     *
     * @param data
     */
    public void addDynamic(String data) {
        flowable = model.addDynamic(data);
        flowable();
    }

    /**
     * 动态评论接口
     *
     * @param data
     */
    public void addDynamicComment(String data) {
        flowable = model.addDynamicComment(data);
        flowable();
    }

    /**
     * 动态评论点赞接口
     *
     * @param data
     */
    public void updateDynamicCommentFN(String data) {
        flowable = model.updateDynamicCommentFN(data);
        flowable();
    }

    /**
     * 支付获取订单号接口
     *
     * @param data
     */
    public void createOrder(String data) {
        flowable = model.createOrder(data);
        flowable();
    }

    /**
     * 手机验证码
     *
     * @param data
     */
    public void smsUtils(String data) {
        flowable = model.smsUtils(data);
        flowable();
    }

    /**
     * 行业分类列表
     *
     * @param data
     */
    public void getIndustryList(String data) {
        flowable = model.getIndustryList(data);
        flowable();
    }

    /**
     * 手机号注册
     *
     * @param data
     */
    public void phoneRegistered(String data) {
        flowable = model.phoneRegistered(data);
        flowable();
    }

    /**
     * 修改密码
     *
     * @param data
     */
    public void updatePassword(String data) {
        flowable = model.updatePassword(data);
        flowable();
    }

    /**
     * 微信注册
     *
     * @param data
     */
    public void weixinRegistered(String data) {
        flowable = model.weixinRegistered(data);
        flowable();
    }

    /**
     * 微信登录
     *
     * @param data
     */
    public void weixinLogin(String data) {
        flowable = model.weixinLogin(data);
        flowable();
    }

    /**
     * 验证密码
     *
     * @param data
     */
    public void validatePassword(String data) {
        flowable = model.validatePassword(data);
        flowable();
    }

    /**
     * 更换手机号
     *
     * @param data
     */
    public void updatePhone(String data) {
        flowable = model.updatePhone(data);
        flowable();
    }

    /**
     * 发布合作
     *
     * @param data
     */
    public void updateUserCooperation(String data) {
        flowable = model.updateUserCooperation(data);
        flowable();
    }

    private void flowable() {
        flowable.subscribeOn(Schedulers.newThread())
                .map(new Function<BaseResultBean<T>, BaseResultBean<T>>() {
                    @Override
                    public BaseResultBean<T> apply(BaseResultBean<T> retResultBean) throws Exception {
                        return retResultBean;
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriberCallBack<BaseResultBean<T>>() {
                    @Override
                    public void onSuccess(BaseResultBean<T> retResultBean) {
                        if (retResultBean != null) {
                            baseView.onSuccess(retResultBean);
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        baseView.onError(t.toString().substring(t.toString().indexOf(":") + 1));
                    }
                });
    }
}
