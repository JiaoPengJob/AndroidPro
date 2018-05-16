package com.tch.zx.util;

/**
 * 用于存放静态常量
 */

public class ConstantData {

    /**
     * 随便看看的登录状态
     */
    public static final String LOGIN_TYPE_CASUALSEE = "00";
    /**
     * 正常登录状态
     */
    public static final String LOGIN_TYPE_LOGINED = "01";
    /**
     * 获取验证码handler的message
     */
    public static final int HANDLER_MESSAGE_CODE_REGISTER = 100;
    /**
     * 获取验证码计时器停止handler的message
     */
    public static final int HANDLER_MESSAGE_CODE_REGISTER_STOP = 101;
    /**
     * 微信申请的APP_Id
     */
    public static final String WX_APP_ID = "";
    /**
     * 微信登录应用授权作用域
     */
    public static final String WX_LOGIN_SCOPE = "snsapi_userinfo";
    /**
     * 用于保持请求和回调的状态，授权请求后原样带回给第三方。
     * 该参数可用于防止csrf攻击（跨站请求伪造攻击），建议第三方带上该参数，可设置为简单的随机数加session进行校验
     */
    public static final String WX_LOGIN_STATE = "wechat_sdk_demo_test";
    /**
     * 应用密钥AppSecret，在微信开放平台提交应用审核通过后获得
     */
    public static final String WX_APP_SECRET = "";

    /**
     * FineLittleClassAdapter适配器参数/热门榜单的调用标识
     */
    public static final int TOP_LIST_TYPE = 200;
    /**
     * 支付宝的参数
     */
    public static final int SDK_PAY_FLAG = 1;
    public static final int SDK_AUTH_FLAG = 2;

    /**
     * 链接服务器访问的私钥
     */
    public static final String PRIVATE_HTTP_KEY = "zx&54fch1@api";

}
