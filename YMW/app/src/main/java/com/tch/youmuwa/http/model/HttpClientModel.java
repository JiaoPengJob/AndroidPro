package com.tch.youmuwa.http.model;


import android.content.Context;
import android.content.Intent;

import com.tch.youmuwa.bean.parameters.AddProjectAddressParam;
import com.tch.youmuwa.bean.parameters.BeginPayParam;
import com.tch.youmuwa.bean.parameters.BindBankCardParam;
import com.tch.youmuwa.bean.parameters.BindMobileParam;
import com.tch.youmuwa.bean.parameters.CheckVersionParam;
import com.tch.youmuwa.bean.parameters.CompleteParam;
import com.tch.youmuwa.bean.parameters.ContractorDisParam;
import com.tch.youmuwa.bean.parameters.ContractorFinishedParam;
import com.tch.youmuwa.bean.parameters.DealdetailEmployer;
import com.tch.youmuwa.bean.parameters.DefaultedProjectAddressParam;
import com.tch.youmuwa.bean.parameters.DeleteAreaParam;
import com.tch.youmuwa.bean.parameters.EditWithdrawPassParam;
import com.tch.youmuwa.bean.parameters.EmployerInfoParam;
import com.tch.youmuwa.bean.parameters.FeedBackParam;
import com.tch.youmuwa.bean.parameters.ForgetParam;
import com.tch.youmuwa.bean.parameters.GetRequireResult;
import com.tch.youmuwa.bean.parameters.GetRequiresListParam;
import com.tch.youmuwa.bean.parameters.GetWorkerExpParam;
import com.tch.youmuwa.bean.parameters.HasParam;
import com.tch.youmuwa.bean.parameters.LoginParam;
import com.tch.youmuwa.bean.parameters.MsgParam;
import com.tch.youmuwa.bean.parameters.OrderCancelParam;
import com.tch.youmuwa.bean.parameters.OrderDismissParam;
import com.tch.youmuwa.bean.parameters.OrderEvaluateParam;
import com.tch.youmuwa.bean.parameters.OrderInfoParam;
import com.tch.youmuwa.bean.parameters.OrdersListParam;
import com.tch.youmuwa.bean.parameters.PoiParam;
import com.tch.youmuwa.bean.parameters.PointFinishedParam;
import com.tch.youmuwa.bean.parameters.PricingParam;
import com.tch.youmuwa.bean.parameters.ProjectAddrsParam;
import com.tch.youmuwa.bean.parameters.RePasswordParam;
import com.tch.youmuwa.bean.parameters.RefuseOrderParam;
import com.tch.youmuwa.bean.parameters.RegisterParam;
import com.tch.youmuwa.bean.parameters.RequireInfoParam;
import com.tch.youmuwa.bean.parameters.ResponseWorkerParam;
import com.tch.youmuwa.bean.parameters.SearchWorkerParam;
import com.tch.youmuwa.bean.parameters.SendRequireParam;
import com.tch.youmuwa.bean.parameters.SendRequireToWorkerParam;
import com.tch.youmuwa.bean.parameters.UpWorkerInfoParam;
import com.tch.youmuwa.bean.parameters.UpdateAreaParam;
import com.tch.youmuwa.bean.parameters.ThirdLoginParam;
import com.tch.youmuwa.bean.parameters.UpdatetProjecAddressParam;
import com.tch.youmuwa.bean.parameters.UploadAvatorParam;
import com.tch.youmuwa.bean.parameters.WithdrawalParam;
import com.tch.youmuwa.bean.parameters.WorkerInfoParam;
import com.tch.youmuwa.bean.result.BaseBean;
import com.tch.youmuwa.bean.parameters.GetCodeParam;
import com.tch.youmuwa.http.api.HttpApi;
import com.tch.youmuwa.http.api.RetrofitApi;

import java.util.Map;

import io.reactivex.Flowable;
import retrofit2.http.Body;

/**
 * Model基类
 */
public class HttpClientModel {

    private HttpApi httpApi;
    private RetrofitApi api;

    public HttpClientModel(Context context) {
        api = RetrofitApi.getInstance(context);
        this.httpApi = api.getServer();
    }

    public void stopClient() {
        api.stopClient();
    }

    /**
     * 登录
     *
     * @param loginParam
     * @return
     */
    public Flowable<BaseBean<Object>> login(LoginParam loginParam) {
        return httpApi.login(loginParam);
    }

    /**
     * 注册
     *
     * @param registerParam
     * @return
     */
    public Flowable<BaseBean<Object>> register(RegisterParam registerParam) {
        return httpApi.register(registerParam);
    }

    /**
     * 获取验证码
     *
     * @param getCodeParam
     * @return
     */
    public Flowable<BaseBean<Object>> getsms(GetCodeParam getCodeParam) {
        return httpApi.getsms(getCodeParam);
    }

    /**
     * 忘记密码
     *
     * @param forgetParam
     * @return
     */
    public Flowable<BaseBean<Object>> forget(ForgetParam forgetParam) {
        return httpApi.forget(forgetParam);
    }

    /**
     * 获取服务条款
     *
     * @return
     */
    public Flowable<BaseBean<Object>> getprovision() {
        return httpApi.getprovision();
    }

    /**
     * 雇主第三方登录
     *
     * @return
     */
    public Flowable<BaseBean<Object>> thirdlogin(ThirdLoginParam thirdLoginParam) {
        return httpApi.thirdlogin(thirdLoginParam);
    }

    /**
     * 搜索工人
     *
     * @return
     */
    public Flowable<BaseBean<Object>> searchworker(SearchWorkerParam searchWorkerParam) {
        return httpApi.searchworker(searchWorkerParam);
    }

    /**
     * 获取工人工程经历
     *
     * @return
     */
    public Flowable<BaseBean<Object>> getworkerexp(GetWorkerExpParam getWorkerExpParam) {
        return httpApi.getworkerexp(getWorkerExpParam);
    }

    /**
     * 获取工人评价列表
     *
     * @return
     */
    public Flowable<BaseBean<Object>> getworkerevaluate(GetWorkerExpParam getWorkerExpParam) {
        return httpApi.getworkerevaluate(getWorkerExpParam);
    }

    /**
     * 添加施工地址
     *
     * @return
     */
    public Flowable<BaseBean<Object>> addprojectaddress(AddProjectAddressParam addressParam) {
        return httpApi.addprojectaddress(addressParam);
    }

    /**
     * 获取施工地址列表
     *
     * @return
     */
    public Flowable<BaseBean<Object>> getprojectaddrs(ProjectAddrsParam projectAddrsParam) {
        return httpApi.getprojectaddrs(projectAddrsParam);
    }

    /**
     * 下单需求 - 指定工人
     *
     * @return
     */
    public Flowable<BaseBean<Object>> sendrequiretoworker(SendRequireToWorkerParam sendRequireToWorkerParam) {
        return httpApi.sendrequiretoworker(sendRequireToWorkerParam);
    }

    /**
     * 下单需求 - 公共
     *
     * @return
     */
    public Flowable<BaseBean<Object>> sendrequire(SendRequireParam sendRequireParam) {
        return httpApi.sendrequire(sendRequireParam);
    }

    /**
     * 工人完善个人资料
     *
     * @return
     */
    public Flowable<BaseBean<Object>> complete(CompleteParam completeParam) {
        return httpApi.complete(completeParam);
    }

    /**
     * 获取工种
     *
     * @return
     */
    public Flowable<BaseBean<Object>> getworkertype() {
        return httpApi.getworkertype();
    }


    /**
     * 更新施工地址
     *
     * @return
     */
    public Flowable<BaseBean<Object>> updateprojectaddress(UpdatetProjecAddressParam updatetProjecAddressParam) {
        return httpApi.updateprojectaddress(updatetProjecAddressParam);
    }

    /**
     * 设为默认施工地址
     *
     * @return
     */
    public Flowable<BaseBean<Object>> defaultedprojectaddress(DefaultedProjectAddressParam defaultedAddressParam) {
        return httpApi.defaultedprojectaddress(defaultedAddressParam);
    }

    /**
     * 退出登录
     *
     * @return
     */
    public Flowable<BaseBean<Object>> logout() {
        return httpApi.logout("");
    }

    /**
     * 角色切换
     *
     * @return
     */
    public Flowable<BaseBean<Object>> switchroles(int type) {
        return httpApi.switchroles(type);
    }

    /**
     * 头像上传
     *
     * @return
     */
    public Flowable<BaseBean<Object>> uploadavator(String path) {
        return httpApi.uploadavator(path);
    }

    /**
     * 获取用户个人资料
     *
     * @return
     */
    public Flowable<BaseBean<Object>> getuserinfo() {
        return httpApi.getuserinfo();
    }

    /**
     * 工人修改个人资料
     *
     * @return
     */
    public Flowable<BaseBean<Object>> upworkerinfo(UpWorkerInfoParam upWorkerInfoParam) {
        return httpApi.upworkerinfo(upWorkerInfoParam);
    }

    /**
     * 获取已经发布的需求单列表
     *
     * @return
     */
    public Flowable<BaseBean<Object>> getrequireslist(GetRequiresListParam requiresListParam) {
        return httpApi.getrequireslist(requiresListParam);
    }

    /**
     * 获取需求单信息
     *
     * @return
     */
    public Flowable<BaseBean<Object>> getrequireinfo(RequireInfoParam requireInfoParam) {
        return httpApi.getrequireinfo(requireInfoParam);
    }

    /**
     * 获取已抢单工人列表
     *
     * @return
     */
    public Flowable<BaseBean<Object>> getworkersofrequire(RequireInfoParam requireInfoParam) {
        return httpApi.getworkersofrequire(requireInfoParam);
    }

    /**
     * 获取工人信息
     *
     * @return
     */
    public Flowable<BaseBean<Object>> getworkerintro(WorkerInfoParam workerInfoParam) {
        return httpApi.getworkerintro(workerInfoParam);
    }

    /**
     * 工人钱包
     *
     * @return
     */
    public Flowable<BaseBean<Object>> workerwallet() {
        return httpApi.workerwallet();
    }

    /**
     * 验证提现密码是否设置
     *
     * @return
     */
    public Flowable<BaseBean<Object>> issetwithdrawpass() {
        return httpApi.issetwithdrawpass();
    }

    /**
     * 提现密码设置
     *
     * @return
     */
    public Flowable<BaseBean<Object>> editwithdrawpass(EditWithdrawPassParam editWithdrawPassParam) {
        return httpApi.editwithdrawpass(editWithdrawPassParam);
    }

    /**
     * 重置密码
     *
     * @return
     */
    public Flowable<BaseBean<Object>> repassword(RePasswordParam rePasswordParam) {
        return httpApi.repassword(rePasswordParam);
    }

    /**
     * 取消需求单
     *
     * @return
     */
    public Flowable<BaseBean<Object>> cancelrequire(RequireInfoParam requireParam) {
        return httpApi.cancelrequire(requireParam);
    }

    /**
     * 提醒工人接单
     *
     * @return
     */
    public Flowable<BaseBean<Object>> requirenotifyworker(RequireInfoParam requireParam) {
        return httpApi.requirenotifyworker(requireParam);
    }

    /**
     * 拒绝或招用工人
     *
     * @return
     */
    public Flowable<BaseBean<Object>> requireresponseworker(ResponseWorkerParam responseWorkerParam) {
        return httpApi.requireresponseworker(responseWorkerParam);
    }

    /**
     * 提现申请
     *
     * @return
     */
    public Flowable<BaseBean<Object>> withdrawal(WithdrawalParam withdrawalParam) {
        return httpApi.withdrawal(withdrawalParam);
    }


    /**
     * 验证是否绑定银行卡
     *
     * @return
     */
    public Flowable<BaseBean<Object>> isbandbank() {
        return httpApi.isbandbank();
    }

    /**
     * 工人交易明细
     *
     * @return
     */
    public Flowable<BaseBean<Object>> getdealdetail(String date) {
        return httpApi.getdealdetail(date);
    }

    /**
     * 获取用户协议
     *
     * @return
     */
    public Flowable<BaseBean<Object>> getagreement() {
        return httpApi.getagreement();
    }

    /**
     * 关于我们
     *
     * @return
     */
    public Flowable<BaseBean<Object>> about() {
        return httpApi.about();
    }

    /**
     * 工人搜索列表
     *
     * @return
     */
    public Flowable<BaseBean<Object>> getrequire(Map<String, String> options) {
        return httpApi.getrequire(options);
    }

    /**
     * 工人/获取需求详情
     *
     * @return
     */
    public Flowable<BaseBean<Object>> getrequiredetails(int id) {
        return httpApi.getrequiredetails(id);
    }

    /**
     * 工人/抢单
     *
     * @return
     */
    public Flowable<BaseBean<Object>> graborder(int id) {
        return httpApi.graborder(id);
    }

    /**
     * 获取已经生成的订单列表
     *
     * @return
     */
    public Flowable<BaseBean<Object>> getorderslist(OrdersListParam ordersListParam) {
        return httpApi.getorderslist(ordersListParam);
    }

    /**
     * 删除施工地址
     *
     * @return
     */
    public Flowable<BaseBean<Object>> removeprojectaddress(DeleteAreaParam deleteAreaParam) {
        return httpApi.removeprojectaddress(deleteAreaParam);
    }

    /**
     * 获取订单信息
     *
     * @return
     */
    public Flowable<BaseBean<Object>> getorderinfo(OrderInfoParam orderInfoParam) {
        return httpApi.getorderinfo(orderInfoParam);
    }

    /**
     * 取消订单(包工)
     *
     * @return
     */
    public Flowable<BaseBean<Object>> ordercancel(OrderCancelParam orderCancelParam) {
        return httpApi.ordercancel(orderCancelParam);
    }

    /**
     * 辞退订单
     *
     * @return
     */
    public Flowable<BaseBean<Object>> orderdismiss(OrderDismissParam orderDismissParam) {
        return httpApi.orderdismiss(orderDismissParam);
    }

    /**
     * 评价已完成订单
     *
     * @return
     */
    public Flowable<BaseBean<Object>> orderevaluate(OrderEvaluateParam orderEvaluateParam) {
        return httpApi.orderevaluate(orderEvaluateParam);
    }

    /**
     * 雇主交易明细
     *
     * @return
     */
    public Flowable<BaseBean<Object>> getdealdetailemployer(DealdetailEmployer dealdetailEmployer) {
        return httpApi.getdealdetailemployer(dealdetailEmployer);
    }

    /**
     * 已接单列表
     *
     * @return
     */
    public Flowable<BaseBean<Object>> getalready(int page_index, int paged) {
        return httpApi.getalready(page_index, paged);
    }

    /**
     * 工人端订单详情
     *
     * @return
     */
    public Flowable<BaseBean<Object>> getorderdetail(int id) {
        return httpApi.getorderdetail(id);
    }

    /**
     * 点工完成接口
     *
     * @return
     */
    public Flowable<BaseBean<Object>> achieve1(PointFinishedParam pointFinished) {
        return httpApi.achieve1(pointFinished);
    }

    /**
     * 包工完成接口
     *
     * @return
     */
    public Flowable<BaseBean<Object>> achieve2(ContractorFinishedParam contractorFinished) {
        return httpApi.achieve2(contractorFinished);
    }

    /**
     * 取消抢单
     *
     * @return
     */
    public Flowable<BaseBean<Object>> cancelorder(String id) {
        return httpApi.cancelorder(id);
    }

    /**
     * 工人位置更新
     *
     * @return
     */
    public Flowable<BaseBean<Object>> updatepoi(PoiParam poiParam) {
        return httpApi.updatepoi(poiParam);
    }

    /**
     * 工人端订单列表
     *
     * @return
     */
    public Flowable<BaseBean<Object>> getorder(Map<String, Integer> options) {
        return httpApi.getorder(options);
    }

    /**
     * 点工确认辞退
     *
     * @return
     */
    public Flowable<BaseBean<Object>> dismiss1(PointFinishedParam pointFinished) {
        return httpApi.dismiss1(pointFinished);
    }

    /**
     * 包工确认辞退
     *
     * @return
     */
    public Flowable<BaseBean<Object>> dismiss2(ContractorDisParam contractorDisParam) {
        return httpApi.dismiss2(contractorDisParam);
    }

    /**
     * 包工估价
     *
     * @return
     */
    public Flowable<BaseBean<Object>> pricing(PricingParam pricingParam) {
        return httpApi.pricing(pricingParam);
    }

    /**
     * 拒单接口
     *
     * @return
     */
    public Flowable<BaseBean<Object>> refuseorder(RefuseOrderParam refuseOrderParam) {
        return httpApi.refuseorder(refuseOrderParam);
    }

    /**
     * 工人接单
     *
     * @return
     */
    public Flowable<BaseBean<Object>> receive(int id) {
        return httpApi.receive(id);
    }

    /**
     * 支付接口
     *
     * @return
     */
    public Flowable<BaseBean<Object>> beginpay(BeginPayParam beginPayParam) {
        return httpApi.beginpay(beginPayParam);
    }

    /**
     * 确认订单完成
     *
     * @return
     */
    public Flowable<BaseBean<Object>> orderconfirm(OrderInfoParam orderInfo) {
        return httpApi.orderconfirm(orderInfo);
    }

    /**
     * 消息-获取消息列表
     *
     * @return
     */
    public Flowable<BaseBean<Object>> getmessagelist() {
        return httpApi.getmessagelist();
    }

    /**
     * 消息-清空已读消息
     *
     * @return
     */
    public Flowable<BaseBean<Object>> clearmessages() {
        return httpApi.clearmessages("");
    }

    /**
     * 消息-标记消息为已读
     *
     * @return
     */
    public Flowable<BaseBean<Object>> markmessageisread(MsgParam msgParam) {
        return httpApi.markmessageisread(msgParam);
    }

    /**
     * 绑定手机号
     *
     * @return
     */
    public Flowable<BaseBean<Object>> bindmobile(BindMobileParam bindMobileParam) {
        return httpApi.bindmobile(bindMobileParam);
    }

    /**
     * 地区名称获取坐标
     *
     * @return
     */
    public Flowable<BaseBean<Object>> citytopoi(String city) {
        return httpApi.citytopoi(city);
    }

    /**
     * 银行卡绑定接口
     *
     * @return
     */
    public Flowable<BaseBean<Object>> bandbankcard(BindBankCardParam bindBankCardParam) {
        return httpApi.bandbankcard(bindBankCardParam);
    }

    /**
     * 获取配置信息
     *
     * @return
     */
    public Flowable<BaseBean<Object>> getsettings() {
        return httpApi.getsettings("");
    }

    /**
     * 意见反馈
     *
     * @return
     */
    public Flowable<BaseBean<Object>> feedback(FeedBackParam feedBackParam) {
        return httpApi.feedback(feedBackParam);
    }

    /**
     * 工人端获取区域接口
     *
     * @return
     */
    public Flowable<BaseBean<Object>> getworkerarea() {
        return httpApi.getworkerarea();
    }

    /**
     * 核对App版本信息
     *
     * @return
     */
    public Flowable<BaseBean<Object>> checkversion(CheckVersionParam cvp) {
        return httpApi.checkversion(cvp);
    }

    /**
     * 更新雇主信息
     *
     * @return
     */
    public Flowable<BaseBean<Object>> upemployerinfo(EmployerInfoParam employerInfoParam) {
        return httpApi.upemployerinfo(employerInfoParam);
    }

    /**
     * app分享接口
     *
     * @return
     */
    public Flowable<BaseBean<Object>> getshareapp() {
        return httpApi.getshareapp();
    }

    /**
     * 获取工人忙碌日期区
     *
     * @return
     */
    public Flowable<BaseBean<Object>> getworkerbusylist(WorkerInfoParam workerInfoParam) {
        return httpApi.getworkerbusylist(workerInfoParam);
    }

}
