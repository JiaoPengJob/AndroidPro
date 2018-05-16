package com.tch.youmuwa.http.api;

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
import com.tch.youmuwa.bean.parameters.ThirdLoginParam;
import com.tch.youmuwa.bean.parameters.UpdatetProjecAddressParam;
import com.tch.youmuwa.bean.parameters.UploadAvatorParam;
import com.tch.youmuwa.bean.parameters.WithdrawalParam;
import com.tch.youmuwa.bean.parameters.WorkerInfoParam;
import com.tch.youmuwa.bean.result.BaseBean;
import com.tch.youmuwa.bean.parameters.GetCodeParam;

import java.util.Map;

import io.reactivex.Flowable;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * 设置请求接口链接
 */

public interface HttpApi {

    @POST("login")
    Flowable<BaseBean<Object>> login(@Body LoginParam loginParam);

    @POST("register")
    Flowable<BaseBean<Object>> register(@Body RegisterParam registerParam);

    @POST("getsms")
    Flowable<BaseBean<Object>> getsms(@Body GetCodeParam getCodeParam);

    @POST("forget")
    Flowable<BaseBean<Object>> forget(@Body ForgetParam forgetParam);

    @GET("getprovision")
    Flowable<BaseBean<Object>> getprovision();

    @POST("thirdlogin")
    Flowable<BaseBean<Object>> thirdlogin(@Body ThirdLoginParam thirdLoginParam);

    @POST("searchworker")
    Flowable<BaseBean<Object>> searchworker(@Body SearchWorkerParam searchWorkerParam);

    @POST("getworkerexp")
    Flowable<BaseBean<Object>> getworkerexp(@Body GetWorkerExpParam getWorkerExpParam);

    @POST("getworkerevaluate")
    Flowable<BaseBean<Object>> getworkerevaluate(@Body GetWorkerExpParam getWorkerExpParam);

    @POST("addprojectaddress")
    Flowable<BaseBean<Object>> addprojectaddress(@Body AddProjectAddressParam addProjectAddressParam);

    @POST("getprojectaddrs")
    Flowable<BaseBean<Object>> getprojectaddrs(@Body ProjectAddrsParam projectAddrsParam);

    @POST("sendrequiretoworker")
    Flowable<BaseBean<Object>> sendrequiretoworker(@Body SendRequireToWorkerParam sendRequireToWorkerParam);

    @POST("sendrequire")
    Flowable<BaseBean<Object>> sendrequire(@Body SendRequireParam sendRequireParam);

    @POST("complete")
    Flowable<BaseBean<Object>> complete(@Body CompleteParam completeParam);

    @GET("getworkertype")
    Flowable<BaseBean<Object>> getworkertype();

    @POST("updateprojectaddress")
    Flowable<BaseBean<Object>> updateprojectaddress(@Body UpdatetProjecAddressParam updatetProjecAddressParam);

    @POST("defaultedprojectaddress")
    Flowable<BaseBean<Object>> defaultedprojectaddress(@Body DefaultedProjectAddressParam defaultedAddressParam);

    @POST("logout")
    Flowable<BaseBean<Object>> logout(@Body String str);

    @GET("switchroles")
    Flowable<BaseBean<Object>> switchroles(@Query("type") int type);

    @FormUrlEncoded
    @POST("uploadavator")
    Flowable<BaseBean<Object>> uploadavator(@Field("imgpath") String mobile);

    @GET("getuserinfo")
    Flowable<BaseBean<Object>> getuserinfo();

    @POST("upworkerinfo")
    Flowable<BaseBean<Object>> upworkerinfo(@Body UpWorkerInfoParam upWorkerInfoParam);

    @POST("getrequireslist")
    Flowable<BaseBean<Object>> getrequireslist(@Body GetRequiresListParam requiresListParam);

    @POST("getrequireinfo")
    Flowable<BaseBean<Object>> getrequireinfo(@Body RequireInfoParam requireInfoParam);

    @POST("getworkersofrequire")
    Flowable<BaseBean<Object>> getworkersofrequire(@Body RequireInfoParam requireInfoParam);

    @POST("getworkerintro")
    Flowable<BaseBean<Object>> getworkerintro(@Body WorkerInfoParam workerInfoParam);

    @GET("workerwallet")
    Flowable<BaseBean<Object>> workerwallet();

    @GET("issetwithdrawpass")
    Flowable<BaseBean<Object>> issetwithdrawpass();

    @POST("editwithdrawpass")
    Flowable<BaseBean<Object>> editwithdrawpass(@Body EditWithdrawPassParam editWithdrawPassParam);

    @POST("repassword")
    Flowable<BaseBean<Object>> repassword(@Body RePasswordParam rePasswordParam);

    @POST("cancelrequire")
    Flowable<BaseBean<Object>> cancelrequire(@Body RequireInfoParam requireParam);

    @POST("requirenotifyworker")
    Flowable<BaseBean<Object>> requirenotifyworker(@Body RequireInfoParam requireParam);

    @POST("requireresponseworker")
    Flowable<BaseBean<Object>> requireresponseworker(@Body ResponseWorkerParam responseWorkerParam);

    @POST("withdrawal")
    Flowable<BaseBean<Object>> withdrawal(@Body WithdrawalParam withdrawalParam);

    @GET("isbandbank")
    Flowable<BaseBean<Object>> isbandbank();

    @GET("getdealdetail")
    Flowable<BaseBean<Object>> getdealdetail(@Query("date") String date);

    @GET("getagreement")
    Flowable<BaseBean<Object>> getagreement();

    @GET("about")
    Flowable<BaseBean<Object>> about();

    @GET("getrequire")
    Flowable<BaseBean<Object>> getrequire(@QueryMap Map<String, String> options);

    @GET("getrequiredetails")
    Flowable<BaseBean<Object>> getrequiredetails(@Query("id") int id);

    @GET("graborder")
    Flowable<BaseBean<Object>> graborder(@Query("id") int id);

    @POST("getorderslist")
    Flowable<BaseBean<Object>> getorderslist(@Body OrdersListParam ordersListParam);

    @POST("removeprojectaddress")
    Flowable<BaseBean<Object>> removeprojectaddress(@Body DeleteAreaParam deleteAreaParam);

    @POST("getorderinfo")
    Flowable<BaseBean<Object>> getorderinfo(@Body OrderInfoParam orderInfoParam);

    @POST("ordercancel")
    Flowable<BaseBean<Object>> ordercancel(@Body OrderCancelParam orderCancelParam);

    @POST("orderdismiss")
    Flowable<BaseBean<Object>> orderdismiss(@Body OrderDismissParam orderDismissParam);

    @POST("orderevaluate")
    Flowable<BaseBean<Object>> orderevaluate(@Body OrderEvaluateParam orderEvaluateParam);

    @POST("getdealdetailemployer")
    Flowable<BaseBean<Object>> getdealdetailemployer(@Body DealdetailEmployer dealdetailEmployer);

    @GET("getalready")
    Flowable<BaseBean<Object>> getalready(@Query("page_index") int page_index, @Query("paged") int paged);

    @GET("getorderdetail")
    Flowable<BaseBean<Object>> getorderdetail(@Query("id") int id);

    @POST("achieve1")
    Flowable<BaseBean<Object>> achieve1(@Body PointFinishedParam pointFinished);

    @POST("achieve2")
    Flowable<BaseBean<Object>> achieve2(@Body ContractorFinishedParam contractorFinished);

    @GET("cancelorder")
    Flowable<BaseBean<Object>> cancelorder(@Query("id") String id);

    @POST("updatepoi")
    Flowable<BaseBean<Object>> updatepoi(@Body PoiParam poiParam);

    @GET("getorder")
    Flowable<BaseBean<Object>> getorder(@QueryMap Map<String, Integer> options);

    @POST("dismiss1")
    Flowable<BaseBean<Object>> dismiss1(@Body PointFinishedParam pointFinished);

    @POST("dismiss2")
    Flowable<BaseBean<Object>> dismiss2(@Body ContractorDisParam contractorDisParam);

    @POST("pricing")
    Flowable<BaseBean<Object>> pricing(@Body PricingParam pricingParam);

    @POST("refuseorder")
    Flowable<BaseBean<Object>> refuseorder(@Body RefuseOrderParam refuseOrderParam);

    @GET("receive")
    Flowable<BaseBean<Object>> receive(@Query("id") int id);

    @POST("beginpay")
    Flowable<BaseBean<Object>> beginpay(@Body BeginPayParam beginPayParam);

    @POST("orderconfirm")
    Flowable<BaseBean<Object>> orderconfirm(@Body OrderInfoParam orderInfo);

    @GET("getmessagelist")
    Flowable<BaseBean<Object>> getmessagelist();

    @POST("clearmessages")
    Flowable<BaseBean<Object>> clearmessages(@Body String str);

    @POST("markmessageisread")
    Flowable<BaseBean<Object>> markmessageisread(@Body MsgParam msgParam);

    @POST("bindmobile")
    Flowable<BaseBean<Object>> bindmobile(@Body BindMobileParam bindMobileParam);

    @GET("citytopoi")
    Flowable<BaseBean<Object>> citytopoi(@Query("city") String city);

    @POST("bandbankcard")
    Flowable<BaseBean<Object>> bandbankcard(@Body BindBankCardParam bindBankCardParam);

    @POST("getsettings")
    Flowable<BaseBean<Object>> getsettings(@Body String param);

    @POST("feedback")
    Flowable<BaseBean<Object>> feedback(@Body FeedBackParam feedBackParam);

    @GET("getworkerarea")
    Flowable<BaseBean<Object>> getworkerarea();

    @POST("checkversion")
    Flowable<BaseBean<Object>> checkversion(@Body CheckVersionParam cvp);

    @POST("upemployerinfo")
    Flowable<BaseBean<Object>> upemployerinfo(@Body EmployerInfoParam employerInfoParam);

    @GET("getshareapp")
    Flowable<BaseBean<Object>> getshareapp();

    @POST("getworkerbusylist")
    Flowable<BaseBean<Object>> getworkerbusylist(@Body WorkerInfoParam workerInfoParam);

}
