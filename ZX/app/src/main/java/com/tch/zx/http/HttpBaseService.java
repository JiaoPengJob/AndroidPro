package com.tch.zx.http;

import com.tch.zx.bean.BaseResultBean;
import com.tch.zx.bean.SmallListBean;
import com.tch.zx.http.bean.result.FriendListByAppUserIdResult;
import com.tch.zx.http.bean.result.LiveListResultBean;
import com.tch.zx.http.bean.result.OfflineCommentResultBean;
import com.tch.zx.http.bean.result.OfflineDetailResultBean;
import com.tch.zx.http.bean.result.RetResultBean;
import com.tch.zx.http.bean.result.HomeResultBean;
import com.tch.zx.http.bean.result.LiveDetailsResultBean;
import com.tch.zx.http.bean.result.LoginResultBean;
import com.tch.zx.http.bean.result.OfflineListResultBean;
import com.tch.zx.http.bean.result.OrderListResultBean;
import com.tch.zx.http.bean.result.SIndustryListResultBean;
import com.tch.zx.http.bean.result.SearchInfoResultBean;
import com.tch.zx.http.bean.result.SmallCommentInsertResultBean;
import com.tch.zx.http.bean.result.SmallCommentResultBean;
import com.tch.zx.http.bean.result.SmallDetailsResultBean;
import com.tch.zx.http.bean.result.SpecialCommentResultBean;
import com.tch.zx.http.bean.result.SpecialDetailResultBean;
import com.tch.zx.http.bean.result.SpecialListResultBean;
import com.tch.zx.http.bean.result.SpecialSubscribeResultBean;
import com.tch.zx.http.bean.result.SpecialWhetherPayResultBean;
import com.tch.zx.http.bean.result.TypeResultBean;
import com.tch.zx.http.bean.result.UploadPicResultBean;
import com.tch.zx.http.view.FIndustryListResultBean;

import java.util.List;
import java.util.Map;

import io.reactivex.Flowable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;

public interface HttpBaseService<T> {

    @POST("appuser/userlogin.jhtml")
    Flowable<LoginResultBean> login(@Query("data") String data);

    @POST("type/type.jhtml")
    Flowable<TypeResultBean> type(@Query("data") String data);

    @POST("home/home.jhtml")
    Flowable<HomeResultBean> home(@Query("data") String data);

    @POST("small/smallList.jhtml")
    Flowable<SmallListBean> smallList(@Query("data") String data);

    @POST("special/specialList.jhtml")
    Flowable<SpecialListResultBean> specialList(@Query("data") String data);

    @POST("offline/offlineList.jhtml")
    Flowable<OfflineListResultBean> offlineList(@Query("data") String data);

    @POST("order/orderList.jhtml")
    Flowable<OrderListResultBean> orderList(@Query("data") String data);

    @POST("live/liveDetails.jhtml")
    Flowable<LiveDetailsResultBean> liveDetails(@Query("data") String data);

    @POST("small/smallDetails.jhtml")
    Flowable<SmallDetailsResultBean> smallDetails(@Query("data") String data);

    @POST("small/smallComment.jhtml")
    Flowable<SmallCommentResultBean> smallComment(@Query("data") String data);

    @POST("small/smallCommentInsert.jhtml")
    Flowable<SmallCommentInsertResultBean> smallCommentInsert(@Query("data") String data);

    @POST("public/concernInsert.jhtml")
    Flowable<RetResultBean> concernInsert(@Query("data") String data);

    @POST("public/concernCancel.jhtml")
    Flowable<RetResultBean> concernCancel(@Query("data") String data);

    @POST("public/collectInsert.jhtml")
    Flowable<RetResultBean> collectInsert(@Query("data") String data);

    @POST("public/collectCancel.jhtml")
    Flowable<RetResultBean> collectCancel(@Query("data") String data);

    @POST("special/specialSubscribe.jhtml")
    Flowable<SpecialSubscribeResultBean> specialSubscribe(@Query("data") String data);

    @POST("special/specialWhetherPay.jhtml")
    Flowable<SpecialWhetherPayResultBean> specialWhetherPay(@Query("data") String data);

    @POST("fabulous/giveFabulous.jhtml")
    Flowable<RetResultBean> giveFabulous(@Query("data") String data);

    @POST("special/specialDetail.jhtml")
    Flowable<SpecialDetailResultBean> specialDetail(@Query("data") String data);

    @POST("special/specialComment.jhtml")
    Flowable<SpecialCommentResultBean> specialComment(@Query("data") String data);

    @POST("special/specialCommentInsert.jhtml")
    Flowable<RetResultBean> specialCommentInsert(@Query("data") String data);

    @POST("offline/offlineDetail.jhtml")
    Flowable<OfflineDetailResultBean> offlineDetail(@Query("data") String data);

    @POST("offline/offlineComment.jhtml")
    Flowable<OfflineCommentResultBean> offlineComment(@Query("data") String data);

    @POST("offline/offlineCommentInsert.jhtml")
    Flowable<RetResultBean> offlineCommentInsert(@Query("data") String data);

    @POST("live/liveList.jhtml")
    Flowable<LiveListResultBean> liveList(@Query("data") String data);

    @FormUrlEncoded
    @POST("search/searchInfo.jhtml")
    Flowable<SearchInfoResultBean> searchInfo(@Field("data") String data);

    @POST("industry/fIndustryList.jhtml")
    Flowable<FIndustryListResultBean> fIndustryList(@Query("data") String data);

    @POST("industry/sIndustryList.jhtml")
    Flowable<SIndustryListResultBean> sIndustryList(@Query("data") String data);

    @Multipart
    @POST("public/uploadPic.jhtml")
    Flowable<UploadPicResultBean> uploadPic(@Query("data") String data,
                                            @PartMap() Map<String, RequestBody> partList);

    @POST("rongyun/getFriendListByAppUserId.jhtml")
    Flowable<BaseResultBean<Object>> getFriendListByAppUserId(@Query("data") String appUserId);

    @POST("rongyun/queryFriendByPhone.jhtml")
    Flowable<BaseResultBean<Object>> queryFriendByPhone(@Query("data") String userName);

    @POST("rongyun/getUserInfoByAppUserId.jhtml")
    Flowable<BaseResultBean<Object>> getUserInfoByAppUserId(@Query("data") String appUserId);

    @FormUrlEncoded
    @POST("rongyun/addFriend.jhtml")
    Flowable<BaseResultBean<Object>> addFriend(@Field("data") String data);//解决中文参数乱码问题

    @POST("rongyun/queryFriendAppyList.jhtml")
    Flowable<BaseResultBean<Object>> queryFriendAppyList(@Query("data") String appUserId);

    @POST("rongyun/processAppyFriendRequest.jhtml")
    Flowable<BaseResultBean<Object>> processAppyFriendRequest(@Query("data") String data);

    @POST("rongyun/getFollowUserListByAppUserId.jhtml")
    Flowable<BaseResultBean<Object>> getFollowUserListByAppUserId(@Query("data") String data);

    @POST("rongyun/getGroupListByAppUserId.jhtml")
    Flowable<BaseResultBean<Object>> getGroupListByAppUserId(@Query("data") String data);

    @FormUrlEncoded
    @POST("rongyun/createGroupByParams.jhtml")
    Flowable<BaseResultBean<Object>> createGroupByParams(@Field("data") String data);//解决中文参数乱码问题

    @POST("rongyun/groupQuitOrDismiss.jhtml")
    Flowable<BaseResultBean<Object>> groupQuitOrDismiss(@Query("data") String data);

    @FormUrlEncoded
    @POST("rongyun/modifyGroupName.jhtml")
    Flowable<BaseResultBean<Object>> modifyGroupName(@Field("data") String data);//解决中文参数乱码问题

    @FormUrlEncoded
    @POST("rongyun/modifyGroupMemberNickName.jhtml")
    Flowable<BaseResultBean<Object>> modifyGroupMemberNickName(@Field("data") String data);//解决中文参数乱码问题

    @POST("rongyun/getGroupMemberList.jhtml")
    Flowable<BaseResultBean<Object>> getGroupMemberList(@Query("data") String data);

    @FormUrlEncoded
    @POST("rongyun/addGroupMember.jhtml")
    Flowable<BaseResultBean<Object>> addGroupMember(@Field("data") String data);//解决中文参数乱码问题

    @POST("rongyun/addFriendSet.jhtml")
    Flowable<BaseResultBean<Object>> addFriendSet(@Query("data") String data);

    @POST("dynamic/deleteDyNamic.jhtml")
    Flowable<BaseResultBean<Object>> deleteDyNamic(@Query("data") String data);

    @FormUrlEncoded
    @POST("feedback/insertFeedback.jhtml")
    Flowable<BaseResultBean<Object>> insertFeedback(@Field("data") String data);//解决中文参数乱码问题

    @FormUrlEncoded
    @POST("appuser/insertApplyInfo.jhtml")
    Flowable<BaseResultBean<Object>> insertApplyInfo(@Field("data") String data);

    @POST("order/payRecord.jhtml")
    Flowable<BaseResultBean<Object>> payRecord(@Query("data") String data);

    @FormUrlEncoded
    @POST("appuser/updateCompanyInfo.jhtml")
    Flowable<BaseResultBean<Object>> updateCompanyInfo(@Field("data") String data);

    @POST("appuser/userFollowIndustry.jhtml")
    Flowable<BaseResultBean<Object>> userFollowIndustry(@Query("data") String data);

    @POST("appuser/userOfferIndustry.jhtml")
    Flowable<BaseResultBean<Object>> userOfferIndustry(@Query("data") String data);

    @POST("appuser/userNeedIndustry.jhtml")
    Flowable<BaseResultBean<Object>> userNeedIndustry(@Query("data") String data);

    @POST("dynamic/getDynamicList.jhtml")
    Flowable<BaseResultBean<Object>> getDynamicList(@Query("data") String data);

    @POST("dynamic/getDynamicCommentList.jhtml")
    Flowable<BaseResultBean<Object>> getDynamicCommentList(@Query("data") String data);

    @FormUrlEncoded
    @POST("dynamic/addDynamic.jhtml")
    Flowable<BaseResultBean<Object>> addDynamic(@Field("data") String data);

    @FormUrlEncoded
    @POST("dynamic/addDynamicComment.jhtml")
    Flowable<BaseResultBean<Object>> addDynamicComment(@Field("data") String data);

    @POST("dynamic/updateDynamicCommentFN.jhtml")
    Flowable<BaseResultBean<Object>> updateDynamicCommentFN(@Query("data") String data);

    @FormUrlEncoded
    @POST("order/createOrder.jhtml")
    Flowable<BaseResultBean<Object>> createOrder(@Field("data") String data);

    @POST("appuser/smsUtils.jhtml")
    Flowable<BaseResultBean<Object>> smsUtils(@Query("data") String data);

    @POST("industry/getIndustryList")
    Flowable<BaseResultBean<Object>> getIndustryList(@Query("data") String data);

    @FormUrlEncoded
    @POST("appuser/phoneRegistered.jhtml")
    Flowable<BaseResultBean<Object>> phoneRegistered(@Field("data") String data);

    @POST("appuser/updatePassword.jhtml")
    Flowable<BaseResultBean<Object>> updatePassword(@Query("data") String data);

    @FormUrlEncoded
    @POST("appuser/weixinRegistered.jhtml")
    Flowable<BaseResultBean<Object>> weixinRegistered(@Field("data") String data);

    @POST("appuser/weixinLogin.jhtml")
    Flowable<BaseResultBean<Object>> weixinLogin(@Query("data") String data);

    @POST("appuser/validatePassword.jhtml")
    Flowable<BaseResultBean<Object>> validatePassword(@Query("data") String data);

    @POST("appuser/updatePhone.jhtml")
    Flowable<BaseResultBean<Object>> updatePhone(@Query("data") String data);

    @FormUrlEncoded
    @POST("appuser/updateUserCooperation.jhtml")
    Flowable<BaseResultBean<Object>> updateUserCooperation(@Field("data") String data);

}
