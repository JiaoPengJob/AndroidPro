package com.tch.youmuwa.http.presenter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.tch.youmuwa.application.MyApplication;
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
import com.tch.youmuwa.bean.parameters.GetCodeParam;
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
import com.tch.youmuwa.bean.parameters.ThirdLoginParam;
import com.tch.youmuwa.bean.parameters.UpWorkerInfoParam;
import com.tch.youmuwa.bean.parameters.UpdatetProjecAddressParam;
import com.tch.youmuwa.bean.parameters.UploadAvatorParam;
import com.tch.youmuwa.bean.parameters.WithdrawalParam;
import com.tch.youmuwa.bean.parameters.WorkerInfoParam;
import com.tch.youmuwa.bean.result.BaseBean;
import com.tch.youmuwa.dao.DaoSession;
import com.tch.youmuwa.dao.UserInfoDao;
import com.tch.youmuwa.http.model.HttpClientModel;
import com.tch.youmuwa.http.subscriber.BaseSubscriberCallBack;
import com.tch.youmuwa.http.view.ClientBaseView;
import com.tch.youmuwa.ui.activity.login.LoginActivity;
import com.tch.youmuwa.ui.activity.mine.PerfectDataActivity;
import com.tch.youmuwa.util.CacheDataManager;

import org.reactivestreams.Subscription;

import java.util.Map;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * 实现主导器
 */

public class PresenterImpl<T> implements IPresenter {

    private Context context;

    private HttpClientModel model;

    private ClientBaseView view;

    private Flowable flowable;
    private DaoSession daoSession;
    private UserInfoDao userInfoDao;

    public PresenterImpl(Context context) {
        this.context = context;
    }

    @Override
    public void onCreate() {
        this.model = new HttpClientModel(context);
    }

    @Override
    public void attachView(ClientBaseView view) {
        this.view = view;
    }

    /**
     * 登录
     *
     * @param loginParam
     */
    public void login(LoginParam loginParam) {
        flowable = model.login(loginParam);
        clientYMWHttp();
    }

    /**
     * 注册
     *
     * @param registerParam
     */
    public void register(RegisterParam registerParam) {
        flowable = model.register(registerParam);
        clientYMWHttp();
    }

    /**
     * 获取验证码
     *
     * @param getCodeParam
     */
    public void getsms(GetCodeParam getCodeParam) {
        flowable = model.getsms(getCodeParam);
        clientYMWHttp();
    }

    /**
     * 忘记密码
     *
     * @param forgetParam
     */
    public void forget(ForgetParam forgetParam) {
        flowable = model.forget(forgetParam);
        clientYMWHttp();
    }

    /**
     * 获取服务条款
     */
    public void getprovision() {
        flowable = model.getprovision();
        clientYMWHttp();
    }

    /**
     * 第三方登录
     */
    public void thirdlogin(ThirdLoginParam thirdLoginParam) {
        flowable = model.thirdlogin(thirdLoginParam);
        clientYMWHttp();
    }

    /**
     * 搜索工人
     */
    public void searchworker(SearchWorkerParam searchWorkerParam) {
        flowable = model.searchworker(searchWorkerParam);
        clientYMWHttp();
    }

    /**
     * 获取工人工程经历
     */
    public void getworkerexp(GetWorkerExpParam getWorkerExpParam) {
        flowable = model.getworkerexp(getWorkerExpParam);
        clientYMWHttp();
    }


    /**
     * 获取工人评价列表
     */
    public void getworkerevaluate(GetWorkerExpParam getWorkerExpParam) {
        flowable = model.getworkerevaluate(getWorkerExpParam);
        clientYMWHttp();
    }

    /**
     * 添加施工地址
     */
    public void addprojectaddress(AddProjectAddressParam addressParam) {
        flowable = model.addprojectaddress(addressParam);
        clientYMWHttp();
    }

    /**
     * 获取施工地址列表
     */
    public void getprojectaddrs(ProjectAddrsParam projectAddrsParam) {
        flowable = model.getprojectaddrs(projectAddrsParam);
        clientYMWHttp();
    }

    /**
     * 下单需求 - 公共
     */
    public void sendrequire(SendRequireParam sendRequireParam) {
        flowable = model.sendrequire(sendRequireParam);
        clientYMWHttp();
    }

    /**
     * 下单需求 - 指定工人
     */
    public void sendrequiretoworker(SendRequireToWorkerParam sendRequireToWorkerParam) {
        flowable = model.sendrequiretoworker(sendRequireToWorkerParam);
        clientYMWHttp();
    }

    /**
     * 工人完善个人资料
     */
    public void complete(CompleteParam completeParam) {
        flowable = model.complete(completeParam);
        clientYMWHttp();
    }

    /**
     * 获取工种
     */
    public void getworkertype() {
        flowable = model.getworkertype();
        clientYMWHttp();
    }

    /**
     * 更新施工地址
     */
    public void updateprojectaddress(UpdatetProjecAddressParam updatetProjecAddressParam) {
        flowable = model.updateprojectaddress(updatetProjecAddressParam);
        clientYMWHttp();
    }

    /**
     * 设为默认施工地址
     */
    public void defaultedprojectaddress(DefaultedProjectAddressParam defaultedAddressParam) {
        flowable = model.defaultedprojectaddress(defaultedAddressParam);
        clientYMWHttp();
    }

    /**
     * 退出登录
     */
    public void logout() {
        flowable = model.logout();
        clientYMWHttp();
    }

    /**
     * 设为默认施工地址
     */
    public void switchroles(int type) {
        flowable = model.switchroles(type);
        clientYMWHttp();
    }

    /**
     * 头像上传
     */
    public void uploadavator(String path) {
        flowable = model.uploadavator(path);
        clientYMWHttp();
    }

    /**
     * 获取用户个人资料
     */
    public void getuserinfo() {
        flowable = model.getuserinfo();
        clientYMWHttp();
    }


    /**
     * 工人修改个人资料
     */
    public void upworkerinfo(UpWorkerInfoParam workerInfoParam) {
        flowable = model.upworkerinfo(workerInfoParam);
        clientYMWHttp();
    }

    /**
     * 获取已经发布的需求单列表
     */
    public void getrequireslist(GetRequiresListParam requiresListParam) {
        flowable = model.getrequireslist(requiresListParam);
        clientYMWHttp();
    }

    /**
     * 获取需求单信息
     */
    public void getrequireinfo(RequireInfoParam requireInfoParam) {
        flowable = model.getrequireinfo(requireInfoParam);
        clientYMWHttp();
    }

    /**
     * 获取已抢单工人列表
     */
    public void getworkersofrequire(RequireInfoParam requireInfoParam) {
        flowable = model.getworkersofrequire(requireInfoParam);
        clientYMWHttp();
    }

    /**
     * 获取工人信息
     */
    public void getworkerintro(WorkerInfoParam workerInfoParam) {
        flowable = model.getworkerintro(workerInfoParam);
        clientYMWHttp();
    }


    /**
     * 工人钱包
     */
    public void workerwallet() {
        flowable = model.workerwallet();
        clientYMWHttp();
    }

    /**
     * 验证提现密码是否设置
     */
    public void issetwithdrawpass() {
        flowable = model.issetwithdrawpass();
        clientYMWHttp();
    }

    /**
     * 提现密码设置
     */
    public void editwithdrawpass(EditWithdrawPassParam editWithdrawPassParam) {
        flowable = model.editwithdrawpass(editWithdrawPassParam);
        clientYMWHttp();
    }

    /**
     * 重置密码
     */
    public void repassword(RePasswordParam rePasswordParam) {
        flowable = model.repassword(rePasswordParam);
        clientYMWHttp();
    }

    /**
     * 取消需求单
     */
    public void cancelrequire(RequireInfoParam requireParam) {
        flowable = model.cancelrequire(requireParam);
        clientYMWHttp();
    }

    /**
     * 提醒工人接单
     */
    public void requirenotifyworker(RequireInfoParam requireParam) {
        flowable = model.requirenotifyworker(requireParam);
        clientYMWHttp();
    }

    /**
     * 拒绝或招用工人
     */
    public void requireresponseworker(ResponseWorkerParam responseWorkerParam) {
        flowable = model.requireresponseworker(responseWorkerParam);
        clientYMWHttp();
    }

    /**
     * 提现申请
     */
    public void withdrawal(WithdrawalParam withdrawalParam) {
        flowable = model.withdrawal(withdrawalParam);
        clientYMWHttp();
    }

    /**
     * 验证是否绑定银行卡
     */
    public void isbandbank() {
        flowable = model.isbandbank();
        clientYMWHttp();
    }

    /**
     * 工人交易明细
     */
    public void getdealdetail(String date) {
        flowable = model.getdealdetail(date);
        clientYMWHttp();
    }

    /**
     * 获取用户协议
     */
    public void getagreement() {
        flowable = model.getagreement();
        clientYMWHttp();
    }

    /**
     * 关于我们
     */
    public void about() {
        flowable = model.about();
        clientYMWHttp();
    }

    /**
     * 工人搜索列表
     */
    public void getrequire(Map<String, String> options) {
        flowable = model.getrequire(options);
        clientYMWHttp();
    }

    /**
     * 工人/获取需求详情
     */
    public void getrequiredetails(int id) {
        flowable = model.getrequiredetails(id);
        clientYMWHttp();
    }


    /**
     * 工人/抢单
     */
    public void graborder(int id) {
        flowable = model.graborder(id);
        clientYMWHttp();
    }

    /**
     * 获取已经生成的订单列表
     */
    public void getorderslist(OrdersListParam ordersListParam) {
        flowable = model.getorderslist(ordersListParam);
        clientYMWHttp();
    }

    /**
     * 删除施工地址
     */
    public void removeprojectaddress(DeleteAreaParam deleteAreaParam) {
        flowable = model.removeprojectaddress(deleteAreaParam);
        clientYMWHttp();
    }

    /**
     * 获取订单信息
     */
    public void getorderinfo(OrderInfoParam orderInfoParam) {
        flowable = model.getorderinfo(orderInfoParam);
        clientYMWHttp();
    }

    /**
     * 取消订单(包工)
     */
    public void ordercancel(OrderCancelParam orderCancelParam) {
        flowable = model.ordercancel(orderCancelParam);
        clientYMWHttp();
    }

    /**
     * 辞退订单
     */
    public void orderdismiss(OrderDismissParam orderDismissParam) {
        flowable = model.orderdismiss(orderDismissParam);
        clientYMWHttp();
    }

    /**
     * 评价已完成订单
     */
    public void orderevaluate(OrderEvaluateParam orderEvaluateParam) {
        flowable = model.orderevaluate(orderEvaluateParam);
        clientYMWHttp();
    }

    /**
     * 雇主交易明细
     */
    public void getdealdetailemployer(DealdetailEmployer dealdetailEmployer) {
        flowable = model.getdealdetailemployer(dealdetailEmployer);
        clientYMWHttp();
    }

    /**
     * 已接单列表
     */
    public void getalready(int page_index, int paged) {
        flowable = model.getalready(page_index, paged);
        clientYMWHttp();
    }

    /**
     * 工人端订单详情
     */
    public void getorderdetail(int id) {
        flowable = model.getorderdetail(id);
        clientYMWHttp();
    }

    /**
     * 点工完成接口
     */
    public void achieve1(PointFinishedParam pointFinished) {
        flowable = model.achieve1(pointFinished);
        clientYMWHttp();
    }

    /**
     * 包工完成接口
     */
    public void achieve2(ContractorFinishedParam contractorFinished) {
        flowable = model.achieve2(contractorFinished);
        clientYMWHttp();
    }

    /**
     * 取消抢单
     */
    public void cancelorder(String id) {
        flowable = model.cancelorder(id);
        clientYMWHttp();
    }

    /**
     * 工人位置更新
     */
    public void updatepoi(PoiParam poiParam) {
        flowable = model.updatepoi(poiParam);
        clientYMWHttp();
    }

    /**
     * 工人端订单列表
     */
    public void getorder(Map<String, Integer> options) {
        flowable = model.getorder(options);
        clientYMWHttp();
    }

    /**
     * 点工确认辞退
     */
    public void dismiss1(PointFinishedParam pointFinished) {
        flowable = model.dismiss1(pointFinished);
        clientYMWHttp();
    }

    /**
     * 包工确认辞退
     */
    public void dismiss2(ContractorDisParam contractorDisParam) {
        flowable = model.dismiss2(contractorDisParam);
        clientYMWHttp();
    }

    /**
     * 包工估价
     */
    public void pricing(PricingParam pricingParam) {
        flowable = model.pricing(pricingParam);
        clientYMWHttp();
    }

    /**
     * 拒单接口
     */
    public void refuseorder(RefuseOrderParam refuseOrderParam) {
        flowable = model.refuseorder(refuseOrderParam);
        clientYMWHttp();
    }

    /**
     * 工人接单
     */
    public void receive(int id) {
        flowable = model.receive(id);
        clientYMWHttp();
    }

    /**
     * 支付接口
     */
    public void beginpay(BeginPayParam beginPayParam) {
        flowable = model.beginpay(beginPayParam);
        clientYMWHttp();
    }

    /**
     * 确认订单完成
     */
    public void orderconfirm(OrderInfoParam orderInfo) {
        flowable = model.orderconfirm(orderInfo);
        clientYMWHttp();
    }

    /**
     * 消息-获取消息列表
     */
    public void getmessagelist() {
        flowable = model.getmessagelist();
        clientYMWHttp();
    }

    /**
     * 消息-清空已读消息
     */
    public void clearmessages() {
        flowable = model.clearmessages();
        clientYMWHttp();
    }

    /**
     * 消息-标记消息为已读
     */
    public void markmessageisread(MsgParam msgParam) {
        flowable = model.markmessageisread(msgParam);
        clientYMWHttp();
    }

    /**
     * 绑定手机号
     */
    public void bindmobile(BindMobileParam bindMobileParam) {
        flowable = model.bindmobile(bindMobileParam);
        clientYMWHttp();
    }

    /**
     * 地区名称获取坐标
     */
    public void citytopoi(String city) {
        flowable = model.citytopoi(city);
        clientYMWHttp();
    }

    /**
     * 银行卡绑定接口
     */
    public void bandbankcard(BindBankCardParam bindBankCardParam) {
        flowable = model.bandbankcard(bindBankCardParam);
        clientYMWHttp();
    }

    /**
     * 获取上门费信息
     */
    public void getsettings() {
        flowable = model.getsettings();
        clientYMWHttp();
    }

    /**
     * 意见反馈
     */
    public void feedback(FeedBackParam feedBackParam) {
        flowable = model.feedback(feedBackParam);
        clientYMWHttp();
    }

    /**
     * 工人端获取区域接口
     */
    public void getworkerarea() {
        flowable = model.getworkerarea();
        clientYMWHttp();
    }

    /**
     * 核对App版本信息
     */
    public void checkversion(CheckVersionParam cvp) {
        flowable = model.checkversion(cvp);
        clientYMWHttp();
    }

    /**
     * 更新雇主信息
     */
    public void upemployerinfo(EmployerInfoParam employerInfoParam) {
        flowable = model.upemployerinfo(employerInfoParam);
        clientYMWHttp();
    }

    /**
     * app分享接口
     */
    public void getshareapp() {
        flowable = model.getshareapp();
        clientYMWHttp();
    }

    /**
     * 获取工人忙碌日期区
     */
    public void getworkerbusylist(WorkerInfoParam workerInfoParam) {
        flowable = model.getworkerbusylist(workerInfoParam);
        clientYMWHttp();
    }

    /**
     * 链接服务器
     */
    private void clientYMWHttp() {
        flowable.subscribeOn(Schedulers.newThread())
                .map(new Function<BaseBean<Object>, BaseBean<Object>>() {
                    @Override
                    public BaseBean<Object> apply(BaseBean<Object> retResultBean) throws Exception {
                        return retResultBean;
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriberCallBack<BaseBean<Object>>() {
                    @Override
                    public void onSuccess(BaseBean<Object> retResultBean) {
                        if (retResultBean != null) {
                            if (retResultBean.getState() != 1) {
                                Log.e("TAG", "BasePresenter--onSuccess--Error:   " + retResultBean.getMsg().toString());
//                                Toast.makeText(context, retResultBean.getMsg().toString(), Toast.LENGTH_SHORT).show();
                            }
                            if (retResultBean.getState() == 1002) {
                                daoSession = ((MyApplication) context.getApplicationContext()).getDaoSession();
                                userInfoDao = daoSession.getUserInfoDao();
                                userInfoDao.deleteAll();
                                Intent intent = new Intent(context, LoginActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                                context.startActivity(intent);
                                Toast.makeText(context, retResultBean.getMsg().toString(), Toast.LENGTH_SHORT).show();
                            } else {
                                view.onSuccess(retResultBean);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        view.onError(t.toString());
                        Log.e("TAG", "BasePresenter--Error:   " + t.toString());
//                        Toast.makeText(context, t.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {
        model.stopClient();
    }

    @Override
    public void pause() {

    }

    @Override
    public void attachIncomingIntent(Intent intent) {

    }

}
