package com.tch.zx.util;

import android.graphics.Bitmap;

import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXMusicObject;
import com.tencent.mm.opensdk.modelmsg.WXTextObject;
import com.tencent.mm.opensdk.modelmsg.WXVideoObject;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;

import static android.provider.UserDictionary.Words.APP_ID;

/**
 * 微信功能帮助类
 */

public class WXUtil {

    /**
     * 微信支付
     */
    public static void pay(IWXAPI iwxapi) {
        PayReq req = new PayReq();
        req.appId = ConstantData.WX_APP_ID;
        req.partnerId = "";
        req.prepayId = "";
        req.packageValue = "";
        req.nonceStr = "";
        req.timeStamp = "";
        req.sign = "";
        iwxapi.sendReq(req);
    }

    /**
     * 发送登录请求
     */
    public static void login(IWXAPI iwxapi) {
        //发送登录请求
        SendAuth.Req req = new SendAuth.Req();
        req.scope = ConstantData.WX_LOGIN_SCOPE;
        req.state = ConstantData.WX_LOGIN_STATE;
        //发送数据到微信
        iwxapi.sendReq(req);
    }

    /**
     * 发送文字分享请求数据
     *
     * @param text 分享的文字内容
     */
    public static void shareText(IWXAPI iwxapi, String text) {
        //初始化一个WXTextObject对象
        WXTextObject wxTextObject = new WXTextObject();
        wxTextObject.text = text;
        //用wxTextObject初始化一个WXMediaMessage对象
        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = wxTextObject;
        msg.description = text;
        //构造一个Req
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = text;
        req.message = msg;
        //分享到朋友圈
        req.scene = SendMessageToWX.Req.WXSceneTimeline;
        //分享到聊天页面
        req.scene = SendMessageToWX.Req.WXSceneSession;
        //发送数据到微信
        iwxapi.sendReq(req);
    }

    /**
     * 发送图片分享请求数据
     *
     * @param bitmap 分享的图片
     */
    public static void shareImages(IWXAPI iwxapi, Bitmap bitmap, int width, int height) {
        WXImageObject imageObject = new WXImageObject(bitmap);
        WXMediaMessage mediaMessage = new WXMediaMessage();
        mediaMessage.mediaObject = imageObject;
        //设置缩略图
        Bitmap thumbBmp = Bitmap.createScaledBitmap(bitmap, width, height, true);
        bitmap.recycle();
        mediaMessage.thumbData =HelperUtil.bmpToByteArray(bitmap);
        //构造一个Req
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        //用于唯一标识一个请求
        req.transaction = "image";
        req.message = mediaMessage;
        //分享到朋友圈
        req.scene = SendMessageToWX.Req.WXSceneTimeline;
        //分享到聊天页面
        req.scene = SendMessageToWX.Req.WXSceneSession;
        //发送数据到微信
        iwxapi.sendReq(req);
    }

    /**
     * 发送音乐分享请求数据
     *
     * @param iwxapi           微信api
     * @param musicUrl         音乐地址
     * @param musicTitle       标题
     * @param musicDescription 描述
     * @param bitmapArray      音乐缩略图
     */
    public static void shareMusic(IWXAPI iwxapi, String musicUrl, String musicTitle, String musicDescription, byte[] bitmapArray) {
        WXMusicObject wxMusicObject = new WXMusicObject();
        wxMusicObject.musicUrl = musicUrl;
        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = wxMusicObject;
        msg.title = musicTitle;
        msg.description = musicDescription;
        msg.thumbData = bitmapArray;
        //构造一个Req
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        //用于唯一标识一个请求
        req.transaction = "music";
        req.message = msg;
        //分享到朋友圈
        req.scene = SendMessageToWX.Req.WXSceneTimeline;
        //分享到聊天页面
        req.scene = SendMessageToWX.Req.WXSceneSession;
        //发送数据到微信
        iwxapi.sendReq(req);
    }

    /**
     * 发送视频分享请求数据
     *
     * @param iwxapi           微信api
     * @param videoUrl         视频地址
     * @param videoTitle       标题
     * @param videoDescription 描述
     * @param bitmapArray      缩略图
     */
    public static void shareVideo(IWXAPI iwxapi, String videoUrl, String videoTitle, String videoDescription, byte[] bitmapArray) {
        WXVideoObject videoObject = new WXVideoObject();
        videoObject.videoUrl = videoUrl;
        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = videoObject;
        msg.title = videoTitle;
        msg.description = videoDescription;
        msg.thumbData = bitmapArray;
        //构造一个Req
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        //用于唯一标识一个请求
        req.transaction = "video";
        req.message = msg;
        //分享到朋友圈
        req.scene = SendMessageToWX.Req.WXSceneTimeline;
        //分享到聊天页面
        req.scene = SendMessageToWX.Req.WXSceneSession;
        //发送数据到微信
        iwxapi.sendReq(req);
    }

    /**
     * 发送网页分享请求数据
     *
     * @param iwxapi         微信api
     * @param webUrl         网页地址
     * @param webTitle       标题
     * @param webDescription 描述
     * @param bitmapArray    缩略图
     */
    public static void shareWeb(IWXAPI iwxapi, String webUrl, String webTitle, String webDescription, byte[] bitmapArray) {
        WXWebpageObject webpageObject = new WXWebpageObject();
        webpageObject.webpageUrl = webUrl;
        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = webpageObject;
        msg.title = webTitle;
        msg.description = webDescription;
        msg.thumbData = bitmapArray;
        //构造一个Req
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        //用于唯一标识一个请求
        req.transaction = "webpage";
        req.message = msg;
        //分享到朋友圈
        req.scene = SendMessageToWX.Req.WXSceneTimeline;
        //分享到聊天页面
        req.scene = SendMessageToWX.Req.WXSceneSession;
        //发送数据到微信
        iwxapi.sendReq(req);
    }
}
